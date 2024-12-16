package com.demo.gidermanagement.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.common.utils.ResourcesProvider
import com.demo.gidermanagement.common.utils.logEvent
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.data.entity.TransactionEntity
import com.demo.gidermanagement.navigation.Screen
import com.demo.gidermanagement.ui.settings.model.SettingsContent
import com.demo.gidermanagement.ui.settings.model.SettingsItem
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


data class SettingsuiState(
    var transactionList: List<TransactionEntity> = emptyList(),
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val transactionRepository: TransactionRepository,
    private val firebaseAnalytics: FirebaseAnalytics
    ): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsuiState())
    val uiState: StateFlow<SettingsuiState> = _uiState.asStateFlow()

    private var _exportExcel= MutableStateFlow(false)
    var exportExcel : StateFlow<Boolean> = _exportExcel.asStateFlow()


    private val _settingsList = mutableStateOf<List<SettingsItem>>(emptyList())
    val settingsList: State<List<SettingsItem>> get() = _settingsList

    init {
        setSettingsList()
        getTransactions()
        firebaseAnalytics.logEvent(Constants.AnalyticsParameter.SETTINGS_VISIT,viewModelScope)
    }

    private fun setSettingsList() {
        val newSettingsList: MutableList<SettingsItem> = mutableListOf()
        val settingsItem = resourcesProvider.getStringArray(R.array.settings_list)
        val settingItemIcon: List<SettingsContent> = listOf(
            SettingsContent(R.drawable.preferences_icon, Screen.PreferencesScreen),
            SettingsContent(R.drawable.notification_icon, Screen.NotificationsScreen),
            SettingsContent(R.drawable.contact_us_icon, Screen.SettingsScreen),
            SettingsContent(R.drawable.category_icon, Screen.CategoriesScreen),
            SettingsContent(R.drawable.excel_icon, Screen.SettingsScreen),
            SettingsContent(R.drawable.evaluate, Screen.SettingsScreen),
            //SettingsContent(R.drawable.password_icon, Screen.SettingsScreen),
            //SettingsContent(R.drawable.backup_icon, Screen.SettingsScreen),
            //SettingsContent(R.drawable.about_us, Screen.SettingsScreen),
        )

        settingsItem.take(
            Constants.SettingsScreen.SettingsItemCount
        ).forEachIndexed { index, language ->
            newSettingsList.add(
                SettingsItem(
                    language,
                    settingItemIcon[index].icon,
                    settingItemIcon[index].navigationScreen
                )
            )
        }
        _settingsList.value = newSettingsList
    }

    private fun getTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.getAllTransaction().collectLatest {
                _uiState.update { currentState ->
                    currentState.copy(
                        transactionList = it
                    )
                }
            }
        }
    }

    fun exportDataToExcel(context: Context) {
        val transactions = uiState.value.transactionList
        Toast.makeText(
            context,
            resourcesProvider.getString(R.string.processing_started),
            Toast.LENGTH_SHORT
        ).show()
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val workbook = XSSFWorkbook()
                    val sheet = workbook.createSheet("Transactions")

                    val headerRow = sheet.createRow(0)
                    headerRow.createCell(Constants.NumberConstants.ZERO)
                        .setCellValue("ID")
                    headerRow.createCell(Constants.NumberConstants.ONE)
                        .setCellValue("Date")
                    headerRow.createCell(Constants.NumberConstants.TWO)
                        .setCellValue("Description")
                    headerRow.createCell(Constants.NumberConstants.THREE)
                        .setCellValue("Category")
                    headerRow.createCell(Constants.NumberConstants.FOUR)
                        .setCellValue("Amount")

                    transactions.forEachIndexed { index, transaction ->
                        val row = sheet.createRow(index + 1)
                        row.createCell(Constants.NumberConstants.ZERO)
                            .setCellValue(transaction.id.toDouble())
                        row.createCell(Constants.NumberConstants.ONE)
                            .setCellValue(transaction.transactionDate)
                        row.createCell(Constants.NumberConstants.TWO)
                            .setCellValue(transaction.transactionDesc.orEmpty())
                        row.createCell(Constants.NumberConstants.THREE)
                            .setCellValue(transaction.transactionCategory.orEmpty())
                        row.createCell(Constants.NumberConstants.FOUR)
                            .setCellValue(transaction.transactionAmount)
                    }

                    // Dosya yolunu ayarla
                    val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                    if (filePath == null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Dosya kaydedilemiyor", Toast.LENGTH_SHORT)
                                .show()
                        }
                        return@launch
                    }
                    val excelFile = File(filePath, "TransactionData.xlsx")

                    // Dosyayı cihazda kaydet
                    FileOutputStream(excelFile).use { fileOut ->
                        workbook.write(fileOut)
                    }
                    workbook.close()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "${resourcesProvider.getString(R.string.excel_file_created)} ${excelFile.absolutePath}",
                            Toast.LENGTH_SHORT
                        ).show()

                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            excelFile
                        )
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(
                                uri,
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            )
                            flags =
                                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    }
                } catch (excelError: SQLiteException) {
                    excelError.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Excel dosyası oluşturulurken bir hata meydana geldi.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }


    fun showInAppReview(context: Context) {
        val reviewManager = ReviewManagerFactory.create(context as Activity)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = reviewManager.launchReviewFlow(context, reviewInfo)
                flow.addOnCompleteListener {
                    // Kullanıcı değerlendirme yaptıktan sonra buraya gelir
                }
            } else {
                // Hata durumunda market sayfasına yönlendirme yapabilirsiniz
            }
        }
    }
}
