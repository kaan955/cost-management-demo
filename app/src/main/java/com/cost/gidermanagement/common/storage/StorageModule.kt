package com.demo.gidermanagement.common.storage

import android.content.Context
import android.content.SharedPreferences
import com.demo.gidermanagement.common.constant.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SharedPreferences.shared_pref_name, Context.MODE_PRIVATE)
    }
}
