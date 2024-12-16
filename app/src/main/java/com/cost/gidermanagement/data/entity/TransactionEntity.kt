package com.demo.gidermanagement.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = TABLENAME)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    var id: Int = 0,
    @SerialName("transactionDate")
    val transactionDate: String? = null,
    @SerialName("transactionDesc")
    val transactionDesc: String? = null,
    @SerialName("transactionCategory")
    val transactionCategory: String? = null,
    @SerialName("transactionAmount")
    val transactionAmount: Double = 0.0,
    @SerialName("isOutgoings")
    val isExpenses: Boolean = false
)

const val TABLENAME = "TransactionEntity"
