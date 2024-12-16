package com.demo.gidermanagement.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.gidermanagement.R
import com.demo.gidermanagement.ui.home.model.FilterData
import com.demo.gidermanagement.ui.theme.Color.Gray300
import com.demo.gidermanagement.ui.theme.Color.Gray700
import com.demo.gidermanagement.ui.theme.Color.Gray800

@Composable
fun MonthlyBalanceNavigator(
    filterArray: ArrayList<FilterData>,
    selectedFilter: Int,
    filterChange: (Int) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = Gray800),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .size(36.dp)
                .clickable(enabled = selectedFilter > 0) { filterChange(-1) },
            painter = painterResource(id = R.drawable.backward),
            contentDescription = "",
            colorFilter = if (selectedFilter > 0) ColorFilter.tint(Gray300) else {
                ColorFilter.tint(Gray700)
            }
        )

        Text(
            text = filterArray[selectedFilter].filterDesc?: "",
            color = Gray300,
            fontWeight = FontWeight.SemiBold)

        Image(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(36.dp)
                .clickable(enabled = selectedFilter < 5) { filterChange(1) },
            painter = painterResource(id = R.drawable.forward),
            contentDescription = "",
            colorFilter = if (selectedFilter < 5) ColorFilter.tint(Gray300) else {
                ColorFilter.tint(Gray700)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMonthlyBalanceNavigator() {
    MonthlyBalanceNavigator(
        ArrayList(),
        0,
        {}
    )
}
