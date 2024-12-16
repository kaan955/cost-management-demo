package com.demo.gidermanagement.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.ui.home.model.TransactionInfoItem
import com.demo.gidermanagement.ui.theme.Color.Blue1100


@Composable
fun FutureTransactionItem(
    transactionItem: TransactionInfoItem,
    isTitle: Boolean,
    onItemClick: (Int) -> Unit,
    currency: String? = ""
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .clickable {
            onItemClick.invoke(
                transactionItem.id ?:
                Constants.TransactionItemStatus.newTransactionItem
            )
        }
        .padding(vertical = 2.dp, horizontal = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (!isTitle) Color.White else Blue1100)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(100.dp),
                color = if(!isTitle)Color.Black else Color.White,
                text =  if(!isTitle) transactionItem.remainingDays.toString()
                else transactionItem.transactionDate ?: "" ,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                thickness = 2.dp,
                color = Color.DarkGray
            )
            Text(
                modifier = Modifier.width(72.dp),
                color = if(!isTitle)Color.Black else Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp,
                text = transactionItem.transactionDesc?: "-", maxLines = 1,
                textAlign = TextAlign.Center
            )
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                thickness = 2.dp,
                color = Color.DarkGray
            )
            Text(
                modifier = Modifier.width(72.dp),
                color = if(!isTitle)Color.Black else Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp,
                text = transactionItem.transactionCategory?:"-", maxLines = 1,
                textAlign = TextAlign.Center
            )
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                thickness = 2.dp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = if(transactionItem.isExpenses) { "-" } else {""} + transactionItem.transactionAmount + currency,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp,
                color = if(isTitle)Color.White else if(transactionItem.isExpenses) Color.Red else Color.Black,
                textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FutureTransactionItemPreview() {
    FutureTransactionItem(
        TransactionInfoItem(
            0,
            "22/12/2024",
            "Okul ödemesi",
            "eğitim",
            "1000.5",
            1
        ),
        false,
        {}
    )
}
