package com.yrgv.akirademo.utils.resourceprovider

import androidx.annotation.StringRes

/**
 * Helps ViewModels still access the resources but not have to deal with context.
 */
interface ResourceProvider {
    fun getString(@StringRes resId: Int): String?
}