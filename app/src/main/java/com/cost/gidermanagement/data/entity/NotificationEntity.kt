package com.demo.gidermanagement.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = TABLE_NOTIFICATIONS)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id") val id: Int = 0,
    @SerialName("title")val title: String? = null,
    @SerialName("date")val date: String? = null,
    @SerialName("time")val time: String? = null,
    @SerialName("repeatCount")val repeatCount: Int? = 1
)

const val TABLE_NOTIFICATIONS = "NotificationEntity"
