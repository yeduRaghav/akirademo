package com.yrgv.akirademo.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yrgv.akirademo.data.network.placesapi.PlacesApi
import com.yrgv.akirademo.data.network.placesapi.endpoint.AutoCompleteEndpoint
import com.yrgv.akirademo.session.SessionRepository

class MainScreenViewModelFactory(
    private val placesApi: PlacesApi
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
            return MainScreenViewModel(
                AutoCompleteEndpoint(placesApi, SessionRepository.getPlacesApiKey())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class :${modelClass.canonicalName}")
    }

}