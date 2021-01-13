package com.yrgv.akirademo.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yrgv.akirademo.data.network.EndpointError
import com.yrgv.akirademo.data.network.placesapi.endpoint.AutoCompleteEndpoint
import com.yrgv.akirademo.data.network.placesapi.model.AutoCompleteResponse.Prediction
import com.yrgv.akirademo.utils.Either
import com.yrgv.akirademo.utils.model.toFilteredPlaceUiModels
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val MIN_QUERY_LENGTH = 2
class MainScreenViewModel(
    private val autoCompleteEndpoint: AutoCompleteEndpoint
) : ViewModel() {

    private val autocompleteViewState = MutableLiveData<AutocompleteViewState>()
    private var searchJob: Job? = null

    fun getAutocompleteViewState(): LiveData<AutocompleteViewState> = autocompleteViewState

    fun onPlaceClicked(result: PlaceUiModel) {
        //todo:
    }

    fun onQueryChanged(query: String?) {
        searchJob?.cancel()
        if (query == null || query.length < MIN_QUERY_LENGTH) {
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
        val placeUIModels = predictionsFromApi.toFilteredPlaceUiModels()
        autocompleteViewState.postValue(AutocompleteViewState.Success(placeUIModels))
    }

    private fun onApiSearchFailure(endpointError: EndpointError) {
        val errorType = getSearchResultErrorType(endpointError)
        autocompleteViewState.postValue(AutocompleteViewState.Error(errorType))
    }

    private fun getSearchResultErrorType(endpointError: EndpointError): AutocompleteViewState.Error.ErrorType {
        return when (endpointError) {
            is EndpointError.Unreachable -> AutocompleteViewState.Error.ErrorType.NETWORK_ISSUE
            else -> AutocompleteViewState.Error.ErrorType.OTHER
        }
    }
}

/**
 * Defines the state of the Autocomplete view
 * */
sealed class AutocompleteViewState {
    data class Success(val places: List<PlaceUiModel>) : AutocompleteViewState()
    data class Error(val type: ErrorType) : AutocompleteViewState() {
        enum class ErrorType { NETWORK_ISSUE, OTHER }
    }
}

