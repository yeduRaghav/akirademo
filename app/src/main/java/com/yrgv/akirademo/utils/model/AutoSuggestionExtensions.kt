package com.yrgv.akirademo.utils.model

import com.yrgv.akirademo.data.network.placesapi.model.AutoCompleteResponse.Prediction
import com.yrgv.akirademo.mainscreen.SearchResultUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Holds extension function to help with AutoSuggestion models
 */

private fun Prediction.toSearchResultUiModel() : SearchResultUiModel {
    return SearchResultUiModel(
        id = place_id,
        name = structured_formatting.main_text,
        address = structured_formatting.secondary_text
    )
}

suspend fun List<Prediction>.toSearchResultUiModels():List<SearchResultUiModel> {
    return withContext(Dispatchers.Default) {
        mapTo(mutableListOf(), { it.toSearchResultUiModel()})
    }
}
