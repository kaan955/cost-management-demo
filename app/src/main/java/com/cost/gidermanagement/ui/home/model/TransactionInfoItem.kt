package com.demo.gidermanagement.ui.home.model

data class TransactionInfoItem(
    val id: Int? = 0,
    val transactionDate: String? = null,
    val transactionDesc: String? = null,
    val transactionCategory: String? = null,
    val transactionAmount: String = "0.0",
    var remainingDays: Int? = null,
    val isExpenses: Boolean = false
)
