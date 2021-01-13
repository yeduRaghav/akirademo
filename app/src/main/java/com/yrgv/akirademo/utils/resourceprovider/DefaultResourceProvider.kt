package com.yrgv.akirademo.utils.resourceprovider

import android.app.Application
import android.content.Context


/**
 * Implementation for Resource Provider
 */
class DefaultResourceProvider private constructor(private val context: Context) :
    ResourceProvider {

    companion object {
        private lateinit var resourceProvider: ResourceProvider

        fun getInstance(application: Application): ResourceProvider {
            if (!::resourceProvider.isInitialized) {
                resourceProvider = DefaultResourceProvider(application)
            }
            return resourceProvider
        }
    }

    override fun getString(resId: Int): String? {
        return context.getString(resId)
    }

}