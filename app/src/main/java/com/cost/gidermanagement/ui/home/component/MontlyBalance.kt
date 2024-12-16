package com.demo.gidermanagement.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.gidermanagement.R
import com.demo.gidermanagement.ui.theme.Color.Gray200
import com.demo.gidermanagement.ui.theme.Color.Gray300
import com.demo.gidermanagement.ui.theme.Color.Gray800


@Composable
fun MonthlyBalance(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(color = Gray800),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier.width(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.profit),
                    contentDescription = ""
                )
                Text(text = stringResource(id = R.string.income),
                    color = Gray300,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium)
            }
            Row(
                modifier = Modifier.width(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = "0,00 TL",
                    color = Gray200,
                    fontWeight = FontWeight.Medium)
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.expense),
                    contentDescription = ""
                )
                Text(text = stringResource(id = R.string.outgoings),
                    fontSize = 16.sp,
                    color = Gray300,
                    fontWeight = FontWeight.Medium)
            }
            Row(
                modifier = Modifier.width(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = "0,00 TL",
                    color = Gray200,
                    fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMonthlyBalance() {
    MonthlyBalance(
        modifier = Modifier
    )
}
