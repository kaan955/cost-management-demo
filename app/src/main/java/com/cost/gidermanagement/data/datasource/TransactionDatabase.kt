package com.demo.gidermanagement.data.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.demo.gidermanagement.data.entity.NotificationEntity
import com.demo.gidermanagement.data.entity.TransactionCategoriesEntity
import com.demo.gidermanagement.data.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class,
        TransactionCategoriesEntity::class,
        NotificationEntity::class],
    version = 4
)
abstract class TransactionDatabase : RoomDatabase(){
    abstract val dao: TransactionDao
}
