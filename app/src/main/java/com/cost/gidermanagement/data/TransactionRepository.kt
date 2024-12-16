package com.demo.gidermanagement.data

import com.demo.gidermanagement.data.datasource.TransactionDao
import com.demo.gidermanagement.data.entity.NotificationEntity
import com.demo.gidermanagement.data.entity.TransactionCategoriesEntity
import com.demo.gidermanagement.data.entity.TransactionEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface Repository {

    suspend fun insert(transactionEntity: TransactionEntity)

    suspend fun delete(transactionEntity: TransactionEntity)

    suspend fun update(transactionEntity: TransactionEntity)

    suspend fun getAllTransaction(): Flow<List<TransactionEntity>>

    suspend fun insertCategory(transactionCategoriesEntity: TransactionCategoriesEntity)
    suspend fun deleteCategory(transactionCategoriesEntity: TransactionCategoriesEntity)
    suspend fun getAllCategory(): Flow<List<TransactionCategoriesEntity>>
    suspend fun clearTransactionTable()
    suspend fun clearTransactionCategoriesTable()
    suspend fun insertNotification(notification: NotificationEntity): Long
    suspend fun getAllNotifications(): List<NotificationEntity>
    suspend fun deleteNotification(notification: NotificationEntity)

}

class TransactionRepository @Inject constructor(
    private val dao: TransactionDao,
) : Repository {
    override suspend fun insert(transactionEntity: TransactionEntity) {
        withContext(IO) {
            dao.insert(transactionEntity)
        }
    }

    override suspend fun delete(transactionEntity: TransactionEntity) {
        withContext(IO) {
            dao.delete(transactionEntity)
        }
    }

    override suspend fun update(transactionEntity: TransactionEntity) {
        withContext(IO) {
            dao.update(transactionEntity)
        }
    }

    override suspend fun getAllTransaction(): Flow<List<TransactionEntity>> {
        return withContext(IO) {
            dao.getAllTransaction()
        }
    }

    suspend fun getSingleTransaction(itemID: Int): TransactionEntity {
        return withContext(IO) {
            dao.getSpecificTransaction(itemID)
        }
    }

    override suspend fun insertCategory(transactionCategoriesEntity: TransactionCategoriesEntity) {
        withContext(IO) {
            dao.insertCategory(transactionCategoriesEntity)
        }
    }

    suspend fun insertCategory(transactionCategoriesEntityList: List<TransactionCategoriesEntity>) {
        withContext(IO) {
            dao.insertCategories(transactionCategoriesEntityList)
        }
    }

    suspend fun updateCategory(category: List<TransactionCategoriesEntity>) {
        dao.updateCategory(category) // DAO üzerinden güncelleme işlemi
    }

    override suspend fun deleteCategory(transactionCategoriesEntity: TransactionCategoriesEntity) {
        withContext(IO) {
            dao.deleteCategory(transactionCategoriesEntity)
        }
    }

    override suspend fun getAllCategory(): Flow<List<TransactionCategoriesEntity>> {
        return withContext(IO) {
            dao.getAllCategory()
        }
    }

    override suspend fun clearTransactionTable() {
        return withContext(IO) {
            dao.clearTransactionTable()
        }
    }

    override suspend fun clearTransactionCategoriesTable() {
        return withContext(IO) {
            dao.clearTransactionCategoriesTable()
        }
    }

    override suspend fun insertNotification(notification: NotificationEntity): Long {
        return withContext(IO) {
            dao.insertNotification(notification)
        }
    }

    override suspend fun deleteNotification(notification: NotificationEntity) {
        return withContext(IO) {
            dao.deleteNotification(notification)
        }
    }

    override suspend fun getAllNotifications(): List<NotificationEntity> {
        return withContext(IO) {
            dao.getAllNotifications()
        }
    }
}
