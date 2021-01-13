package com.yrgv.akirademo.utils

import android.util.Log
import com.yrgv.akirademo.BuildConfig

/**
 * Functions to help debug
 */
fun logThread(title: String) {
    if (!BuildConfig.DEBUG) return
    Log.d("appan", "$title ==> ${Thread.currentThread().name}")
}