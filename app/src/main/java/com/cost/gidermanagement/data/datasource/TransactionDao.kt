package com.com.gidermanagement.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.demo.gidermanagement.data.entity.NotificationEntity
import com.demo.gidermanagement.data.entity.TransactionCategoriesEntity
import com.demo.gidermanagement.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactionEntity: TransactionEntity)

    @Delete
    suspend fun delete(transactionEntity: TransactionEntity)

    @Update
    suspend fun update(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM TransactionEntity")
    fun getAllTransaction(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM TransactionEntity WHERE id = :id")
    suspend fun getSpecificTransaction(id: Int): TransactionEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: TransactionCategoriesEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<TransactionCategoriesEntity>)

    @Update
    suspend fun updateCategory(category: List<TransactionCategoriesEntity>)

    @Delete
    suspend fun deleteCategory(categoryEntity: TransactionCategoriesEntity)

    @Query("SELECT * FROM TransactionCategoriesEntity")
    fun getAllCategory(): Flow<List<TransactionCategoriesEntity>>


    @Query("DELETE FROM TransactionEntity")
    suspend fun clearTransactionTable()

    @Query("DELETE FROM TransactionCategoriesEntity")
    suspend fun clearTransactionCategoriesTable()

    @Insert
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("SELECT * FROM NotificationEntity")
    suspend fun getAllNotifications(): List<NotificationEntity>

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
}
