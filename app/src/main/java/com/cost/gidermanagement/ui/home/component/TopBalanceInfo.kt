package com.demo.gidermanagement.ui.home.component

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.demo.gidermanagement.R
import com.demo.gidermanagement.ui.home.HomeUiState
import com.demo.gidermanagement.ui.home.model.FilterData
import com.demo.gidermanagement.ui.home.model.getPieChartData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.ui.theme.Color.Blue1100
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.FirebrickRed
import com.demo.gidermanagement.ui.theme.Color.Gray500
import com.demo.gidermanagement.ui.theme.Color.Gray700
import com.demo.gidermanagement.ui.theme.Color.Turkuaz
import java.util.*

@Composable
fun TopBalanceInfo(
    filterArray: ArrayList<FilterData>,
    selectedFilter: Int,
    filterChange: (Int) -> Unit,
    transactionGraphList: ArrayList<TransactionGraphItem>,
    uistate: HomeUiState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Blue200),
    ) {
        if (transactionGraphList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(160.dp)
                    .clip(RoundedCornerShape(bottomEnd = 16.dp, topEnd = 16.dp)),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // PieChart(transactionGraphList = transactionGraphList)
                TransactionPieChart(transactionGraphList = transactionGraphList)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .size(36.dp)
                        .weight(TopBalanceInfoDefaults.iconfilterWidth)
                        .clickable(enabled = selectedFilter > 0) { filterChange(-1) },
                    painter = painterResource(id = R.drawable.backward),
                    contentDescription = "",
                    colorFilter = if (selectedFilter > 0) ColorFilter.tint(Blue1100) else {
                        ColorFilter.tint(Gray700)
                    }
                )
                Text(
                    modifier = Modifier
                        .weight(TopBalanceInfoDefaults.textfilterWidth),
                    text = filterArray[selectedFilter].filterDesc ?: "",
                    color = Blue1100,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )

                 Image(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .weight(TopBalanceInfoDefaults.iconfilterWidth)
                        .size(36.dp)
                        .clickable(enabled = selectedFilter < Constants.NumberConstants.FIVE) { filterChange(1) },
                    painter = painterResource(id = R.drawable.forward),
                    contentDescription = "",
                    colorFilter = if (selectedFilter < Constants.NumberConstants.FIVE) ColorFilter.tint(Blue1100) else {
                        ColorFilter.tint(Gray700)
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Gray500),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Spacer(modifier = Modifier.width(4.dp))
                Canvas(modifier = Modifier.size(16.dp), onDraw = {
                    drawCircle(color = Turkuaz)
                })
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(TopBalanceInfoDefaults.typeMaxWidth),
                    text = stringResource(id = R.string.income) + ":",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.width(4.dp))
                Text(text = uistate.incomeTotalStr,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Gray500),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Spacer(modifier = Modifier.width(4.dp))
                Canvas(modifier = Modifier.size(16.dp), onDraw = {
                    drawCircle(color = FirebrickRed)
                })
                Spacer(modifier = Modifier.width(4.dp))
                Text(modifier = Modifier.fillMaxWidth(TopBalanceInfoDefaults.typeMaxWidth),
                    text = stringResource(id = R.string.outgoings) + ":",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = uistate.expensesTotalStr,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBalanceInfoPreview() {
    TopBalanceInfo(
        filterArray = arrayListOf(
            FilterData(Constants.NumberConstants.THREE,"Bugün")
        ),
        0,
        {},ArrayList(),
        uistate = HomeUiState()
    )
}

@Composable
fun TransactionPieChart(transactionGraphList: ArrayList<TransactionGraphItem>) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Crossfade(targetState = getPieChartData, label = "") { pieChartData ->
                    AndroidView(factory = { context ->
                        PieChart(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            this.description.isEnabled = false
                            this.isDrawHoleEnabled = true

                            setHoleColor(Blue200.toArgb())

                            this.legend.isEnabled = false
                            this.legend.textSize = TopBalanceInfoDefaults.legendTextSize
                            this.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.CENTER
                            // on below line we are specifying entry label color as white.
                            ContextCompat.getColor(context, R.color.white)
                            //this.setEntryLabelColor(resources.getColor(R.color.white))
                        }
                    },
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(1.dp), update = {
                            updatePieChartWithData(it,pieChartData,
                                context = context, transactionGraphList = transactionGraphList)
                        })
                }
            }
        }
    }
}
fun updatePieChartWithData(
    chart: PieChart,
    data: List<com.demo.gidermanagement.ui.home.model.PieChartData>,
    transactionGraphList: ArrayList<TransactionGraphItem>,
    context: Context
) {
    val entries = ArrayList<PieEntry>()

    for (i in transactionGraphList.indices) {
        val item = transactionGraphList[i]
        entries.add(
            PieEntry(
                item.categoryPercentage?.toFloat() ?: 0.toFloat(),
                item.categoryName ?: "")
        )
    }
    val ds = PieDataSet(entries, "")
    ds.colors = arrayListOf(
        Turkuaz.toArgb(),
        FirebrickRed.toArgb(),
    )
    ds.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.sliceSpace = TopBalanceInfoDefaults.sliceSpace

    ContextCompat.getColor(context, R.color.blue_600)
    ds.valueTextSize = TopBalanceInfoDefaults.chartValueTextSize
    ds.formSize = TopBalanceInfoDefaults.formSize
    ds.valueTypeface = Typeface.SERIF

    val d = PieData(ds)
    chart.data = d
    chart.setCenterTextSize(TopBalanceInfoDefaults.chartTextSize)
    chart.setEntryLabelTextSize(TopBalanceInfoDefaults.chartLabelTextSize)
    chart.setUsePercentValues(true)
    chart.invalidate()
}

object TopBalanceInfoDefaults{
    const val chartTextSize = 6f
    const val chartLabelTextSize = 10f
    const val chartValueTextSize = 10f
    const val sliceSpace = 2f
    const val formSize = 100f
    const val legendTextSize = 12f
    const val typeMaxWidth = .45f
    const val iconfilterWidth = .2f
    const val textfilterWidth = .6f
}

