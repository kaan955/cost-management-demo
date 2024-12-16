package com.demo.gidermanagement.ui.settings.categories.component


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
import com.demo.gidermanagement.common.model.CategoryItem
import com.demo.gidermanagement.ui.detailList.component.DismissBackground
import com.demo.gidermanagement.ui.theme.Color.Blue1100


@Composable
fun CategoryItems(
    categoryItem: CategoryItem,
    isTitle: Boolean,
    onItemClick: (Int) -> Unit,
    deleteTransactionData: (CategoryItem) -> Unit,
) {
    val context = LocalContext.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    deleteTransactionData(categoryItem)
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
        enableDismissFromEndToStart = (categoryItem.categoryID ?: 0) > 5,
        modifier = Modifier,
        backgroundContent = { DismissBackground(dismissState)},
        content = {
            CategoryContent(
                categoryItem = categoryItem,
                isTitle = isTitle,
                onItemClick = onItemClick
            )
        })
}

@Composable
fun CategoryContent(
    categoryItem: CategoryItem,
    isTitle: Boolean,
    onItemClick: (Int) -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .clickable {
            onItemClick.invoke(
                categoryItem.categoryID ?:
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
                modifier = Modifier.fillMaxWidth(),
                color = if (!isTitle) Color.Black else Color.White,
                text = categoryItem.categoryName ?: "",
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
    CategoryItems(
        CategoryItem(
            0,
            "Okul",
        ),
        false,
        {},{}
    )
}
