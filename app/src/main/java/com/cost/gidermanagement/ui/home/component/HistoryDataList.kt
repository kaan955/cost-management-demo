package com.demo.gidermanagement.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.gidermanagement.R
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.ui.home.model.TransactionInfoItem
import com.demo.gidermanagement.ui.theme.Color.Blue1100
import com.demo.gidermanagement.ui.theme.Color.Blue200
import com.demo.gidermanagement.ui.theme.Color.Blue600

@Composable
fun HistoryDataList(
    navigateToTransaction: (Int) -> Unit,
    financialList: ArrayList<TransactionInfoItem>,
    currency: String? = ""
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Blue200),
    ){
        if(financialList.isEmpty()) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomEnd = 12.dp))
                    .background(Blue600),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    text = stringResource(id = R.string.transactions_title),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }
            Text(modifier = Modifier.align(Alignment.Center)
                .padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(id = R.string.no_data_transactions),
                color = Blue1100,
                fontWeight = FontWeight.Medium)
        } else {
            Column {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = 12.dp))
                        .background(Blue600),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                    ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp,vertical = 4.dp),
                        text = stringResource(id = R.string.transactions_title),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                val titleFinancialList = TransactionInfoItem(
                    -1,
                    stringResource(id = R.string.table_date),
                    stringResource(id = R.string.table_title),
                    stringResource(id = R.string.table_category),
                    stringResource(id = R.string.table_amount)
                )

                LazyColumn(
                    modifier = Modifier.padding(bottom = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    item {
                        TransactionItem(transactionItem = titleFinancialList,true,{})
                        }
                    items(financialList) { item ->
                        TransactionItem(item,false, onItemClick = navigateToTransaction,currency)
                    }
                }
            }
        }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(42.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp),
                content = {
                    Image(
                        modifier = Modifier.clickable {
                            navigateToTransaction.invoke(
                                Constants.TransactionItemStatus.newTransactionItem
                            )
                                                      },
                        painter = painterResource(id = R.drawable.add_item),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                })
    }
}




@Preview(showBackground = true)
@Composable
fun HistoryDataListPreview() {
    HistoryDataList(
        {},
        ArrayList()
    )
}
