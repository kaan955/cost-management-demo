package com.demo.gidermanagement.common.model

data class BottomNavigationItem(
    val title: String?,
    val icon: Int,
    val onClick: () -> Unit,
)
