package com.demo.gidermanagement.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = TABLE_CATEGORY)
data class TransactionCategoriesEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    var id: Int = 0,
    @SerialName("transactionCategory")
    var transactionCategory: String? = null,

)

const val TABLE_CATEGORY = "TransactionCategoriesEntity"
