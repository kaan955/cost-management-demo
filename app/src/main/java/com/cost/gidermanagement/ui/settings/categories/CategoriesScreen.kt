package com.demo.gidermanagement.ui.settings.categories


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.model.CategoryItem
import com.demo.gidermanagement.ui.settings.categories.component.CategoryList
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.Blue600

@Composable
fun CategoriesScreen(
    onbackClicked: ( ) -> Unit
){
    val categoriesViewModel = hiltViewModel<CategoriesViewModel>()
    val categoriesuiState by categoriesViewModel.uiState.collectAsState()

    CategoriesScreenContent(
        uiState = categoriesuiState,
        onbackClicked = onbackClicked,
        deleteTransactionData = {categoriesViewModel.deleteCategory(it)},
        onConfirm = { categoriesViewModel.saveCategory(it)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreenContent(
    uiState: CategoriesuiState,
    onbackClicked: () -> Unit,
    deleteTransactionData: (CategoryItem) -> Unit,
    onConfirm: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue200)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.category_title),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Blue600
                    ),
                    navigationIcon = {
                        IconButton(onClick = { onbackClicked.invoke() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, null,
                                tint = Color.White
                            )
                        }
                    },
                )
            },
        ) { paddingValues->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp
                    )

            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Blue200, RoundedCornerShape(16.dp))
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        CategoryList(
                            uiState.categoryList,
                            deleteTransactionData
                        )
                    }
                }
                IconButton(
                    onClick = {showDialog = true},
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp),
                    content = {
                        Image(
                            modifier = Modifier.clickable {
                                showDialog = true
                            },
                            painter = painterResource(id = R.drawable.add_item),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    })
            }
            if (showDialog) {
                CategoryDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { categoryName ->
                        onConfirm(categoryName)
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var isTitleError by remember { mutableStateOf(false) }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.category))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                        isTitleError = it.isEmpty()
                    },
                    label = { Text(text = stringResource(id = R.string.category_name)) },
                    placeholder = { Text("") },
                    isError = isTitleError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isTitleError) {
                    Text(
                        text = "Kategori adı boş bırakılamaz.",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            val isConfirmEnabled = !isTitleError && categoryName.isNotEmpty()
            Button(
                onClick = { onConfirm(categoryName) },
                enabled = isConfirmEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue600,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier.clip(RoundedCornerShape(2.dp))
            ) {
                Text(text = stringResource(id = R.string.notification_confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue600,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.LightGray
                ),
                modifier = Modifier.clip(RoundedCornerShape(2.dp))
            ) {
                Text(text = stringResource(id = R.string.notification_cancel))
            }
        }
    )
}



