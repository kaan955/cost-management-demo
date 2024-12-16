package com.demo.gidermanagement.common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.google.firebase.analytics.FirebaseAnalytics

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

