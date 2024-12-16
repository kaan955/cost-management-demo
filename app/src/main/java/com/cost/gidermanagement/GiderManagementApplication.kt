package com.demo.gidermanagement

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.demo.gidermanagement.common.constant.Constants
import timber.log.Timber
import java.util.Locale
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

@HiltAndroidApp
class GiderManagementApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
