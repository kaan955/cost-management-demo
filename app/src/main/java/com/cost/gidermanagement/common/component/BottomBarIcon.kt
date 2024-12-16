package com.demo.gidermanagement.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.demo.gidermanagement.common.model.BottomNavigationItem


@Composable
fun BottomBarIcon(item: BottomNavigationItem) {
    Column(
        modifier = Modifier.size(BottomNavigationBarItemDefaults.IconSize),
        verticalArrangement = Arrangement.spacedBy(BottomNavigationBarItemDefaults.Spacing),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painterResource(id = item.icon),
            contentDescription = null
        )
        Text(
            text = item.title ?: "",
            textAlign = TextAlign.Center
        )
    }
}
    object BottomNavigationBarItemDefaults{
        val Spacing = 5.dp
        val IconSize = 24.dp
    }
