package com.yrgv.akirademo.mainscreen

import androidx.annotation.VisibleForTesting
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

/**
 * THe ViewModel for main screen. This class defines the state of the view,
 * which are exposed to the activity or fragments through live data observables.
 * */
private const val MIN_QUERY_LENGTH = 2
class MainScreenViewModel(
    private val autoCompleteEndpoint: AutoCompleteEndpoint
) : ViewModel() {

    private val autocompleteViewState = MutableLiveData<AutocompleteViewState>()

    @VisibleForTesting
    var searchJob: Job? = null

    fun getAutocompleteViewState(): LiveData<AutocompleteViewState> = autocompleteViewState

    fun onPlaceClicked(place: PlaceUiModel) {
        //todo: launch detail screen with place.placeId
    }

    fun onQueryChanged(query: String?) {
        searchJob?.cancel()
        if (!query.isAcceptableQuery()) return
        performSearch(query!!) // !! is safe here due to isAcceptableQuery()
    }

    @VisibleForTesting
    fun String?.isAcceptableQuery(): Boolean {
        if (this.isNullOrBlank()) return false
        return length >= MIN_QUERY_LENGTH
    }

    @VisibleForTesting
    fun performSearch(query: String) {
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

    @VisibleForTesting
    fun onApiSearchFailure(endpointError: EndpointError) {
        val errorType = getSearchResultErrorType(endpointError)
        autocompleteViewState.postValue(AutocompleteViewState.Error(errorType))
    }

    @VisibleForTesting
    fun getSearchResultErrorType(endpointError: EndpointError): AutocompleteViewState.Error.ErrorType {
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

