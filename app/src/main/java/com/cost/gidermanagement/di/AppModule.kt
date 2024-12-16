package com.demo.gidermanagement.di

import android.app.Application
import androidx.room.Room
import com.demo.gidermanagement.data.TransactionRepository
import com.demo.gidermanagement.data.datasource.TransactionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideMyDataBase(app: Application): TransactionDatabase {
        return Room.databaseBuilder(
            app,
            TransactionDatabase::class.java,
            "TransactionDatabase"
        )
            .fallbackToDestructiveMigration() //later add migrations if u change the table
            .build()
    }



    @Provides
    @Singleton
    fun provideMyRepository(transactionDatabase:TransactionDatabase) :TransactionRepository {
        return TransactionRepository(transactionDatabase.dao)
    }
}
