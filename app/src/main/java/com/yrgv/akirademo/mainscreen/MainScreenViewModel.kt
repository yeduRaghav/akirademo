package com.yrgv.akirademo.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yrgv.akirademo.data.network.EndpointError
import com.yrgv.akirademo.data.network.placesapi.endpoint.AutoCompleteEndpoint
import com.yrgv.akirademo.data.network.placesapi.model.AutoCompleteResponse.Prediction
import com.yrgv.akirademo.utils.Either
import com.yrgv.akirademo.utils.model.toSearchResultUiModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
private const val MIN_QUERY_LENGTH = 2
class MainScreenViewModel(
    private val autoCompleteEndpoint: AutoCompleteEndpoint
) : ViewModel() {

    private val searchResults = MutableLiveData<List<SearchResultUiModel>>()
    private var searchJob: Job? = null

    fun getSearchResults(): LiveData<List<SearchResultUiModel>> = searchResults

    fun onSearchResultClicked(result: SearchResultUiModel) {
        //todo:
    }

    fun onQueryChanged(query: String?) {
        searchJob?.cancel()
        if(query== null || query.length < MIN_QUERY_LENGTH) {
            return
        }
        if (!query.isNullOrBlank()) {
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        searchJob = viewModelScope.launch {
            val apiResponse = autoCompleteEndpoint.apply { setData(query) }.execute()
            when (apiResponse) {
                is Either.Error -> onApiSearchFailure(apiResponse.error)
                is Either.Value -> onApiSearchSuccess(apiResponse.value.predictions)
            }
        }
    }

    private suspend fun onApiSearchSuccess(predictionsFromApi: List<Prediction>) {
        searchResults.postValue(predictionsFromApi.toSearchResultUiModels())
    }

    private fun onApiSearchFailure(error:EndpointError) {
        when(error) {
            //todo: deal with me
        }
    }

}