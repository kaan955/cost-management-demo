package com.demo.gidermanagement.ui.settings.notifications.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.gidermanagement.common.constant.Constants
import com.demo.gidermanagement.data.entity.NotificationEntity
import com.demo.gidermanagement.ui.detailList.component.DismissBackground
import com.demo.gidermanagement.ui.theme.Color.Blue1100


@Composable
fun NotificationItem(
    notificationData: NotificationEntity,
    isTitle: Boolean,
    onItemClick: (Int) -> Unit,
    deleteTransactionData: (NotificationEntity) -> Unit,
) {
    val context = LocalContext.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    deleteTransactionData(notificationData)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
            return@rememberSwipeToDismissBoxState true
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = notificationData.id != -1,
        modifier = Modifier,
        backgroundContent = { DismissBackground(dismissState)},
        content = {
            NotificationContent(
                notificationData = notificationData,
                isTitle = isTitle,
                onItemClick = onItemClick
            )
        })
}

@Composable
fun NotificationContent(
    notificationData: NotificationEntity,
    isTitle: Boolean,
    onItemClick: (Int) -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .clickable {
            onItemClick.invoke(
                notificationData.id ?:
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(
                    NotificationItemDefaults.startNotificationItemWidth
                ),
                color = if (!isTitle) Color.Black else Color.White,
                text = notificationData.title ?: "",
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                thickness = 2.dp,
                color = Color.DarkGray
            )
            Text(
                modifier = Modifier.fillMaxWidth(
                    NotificationItemDefaults.endNotificationItemWidth)
                    .padding(start = 2.dp),
                color = if (!isTitle) Color.Black else Color.White,
                fontWeight = FontWeight.SemiBold,
                text = "${notificationData.date} ${notificationData.time}",
                maxLines = 1,overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }
}

object NotificationItemDefaults {
    const val startNotificationItemWidth = .3f
    const val endNotificationItemWidth = .7f
}
@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
    NotificationItem(
        NotificationEntity(
            0,
            "22/12/2024",
            "Okul ödemesi",
        ),
        false,
        {},{}
    )
}
