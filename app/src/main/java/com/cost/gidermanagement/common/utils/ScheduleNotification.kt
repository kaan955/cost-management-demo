package com.demo.gidermanagement.common.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.com.gidermanagement.data.entity.NotificationEntity
import com.com.gidermanagement.ui.settings.notifications.receiver.NotificationReceiver
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun scheduleNotification(context: Context, notification: NotificationEntity) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Tarih ve saat bilgilerini DateTime formatında işleyin
    val dateTime = "${notification.date} ${notification.time}"
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val dateTimeMillis = LocalDateTime.parse(dateTime, formatter)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    // Intent ve PendingIntent ayarları
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("title", notification.title)
        putExtra("notificationId", notification.id)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        notification.id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Belirli bir zaman için AlarmManager ayarla
    try {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            dateTimeMillis,
            pendingIntent
        )
    } catch (e: SecurityException) {
        Log.e("Alarm", "Exact alarm permission denied", e)
        // Inform the user to enable exact alarms in settings
    }
    }
