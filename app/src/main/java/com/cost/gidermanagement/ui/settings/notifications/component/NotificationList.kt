package com.demo.gidermanagement.ui.settings.notifications.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.gidermanagement.R
import com.demo.gidermanagement.data.entity.NotificationEntity
import com.demo.gidermanagement.ui.theme.Color.Blue1100
import com.demo.gidermanagement.ui.theme.Color.Blue200

@Composable
fun NotificationList(
    notificationList: List<NotificationEntity>,
    deleteTransactionData: (NotificationEntity) -> Unit,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Blue200),
    ){
        if(notificationList.isEmpty()) {
            Text(modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.no_data_notifications),
                color = Blue1100,
                fontWeight = FontWeight.Medium)
        } else {
            Column {
                val titleNotificationList = NotificationEntity(-1,"Başlık","Tarih","")

                LazyColumn(
                    modifier = Modifier.padding(bottom = 50.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    item {
                        NotificationItem(notificationData = titleNotificationList,true,{},{})
                    }
                    items(notificationList) { item ->
                        NotificationItem(item,false, onItemClick = {},deleteTransactionData)
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun NotificationListPreview() {
    NotificationList(
        emptyList(),{}
    )
}
