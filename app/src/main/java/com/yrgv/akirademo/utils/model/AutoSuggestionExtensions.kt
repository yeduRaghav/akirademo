package com.yrgv.akirademo.utils.model

import androidx.annotation.VisibleForTesting
import com.yrgv.akirademo.data.network.placesapi.model.AutoCompleteResponse.Prediction
import com.yrgv.akirademo.data.network.placesapi.model.PREDICTION_TYPE_LIQUOR_STORE
import com.yrgv.akirademo.data.network.placesapi.model.PREDICTION_TYPE_RESTAURANT
import com.yrgv.akirademo.mainscreen.PlaceUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Holds extension function to help with AutoSuggestion models
 */

@VisibleForTesting
fun Prediction.toPlaceUiModel(): PlaceUiModel {
    return PlaceUiModel(
        id = place_id,
        name = structured_formatting.main_text,
        address = structured_formatting.secondary_text
    )
}

@VisibleForTesting
suspend fun List<Prediction>.filterByRestaurantAndLiquorStore(): List<Prediction> {
    return withContext(Dispatchers.Default) {
        filter { prediction ->
            prediction.types.contains(PREDICTION_TYPE_RESTAURANT) or
                    prediction.types.contains(PREDICTION_TYPE_LIQUOR_STORE)
        }
    }
}

suspend fun List<Prediction>.toFilteredPlaceUiModels(): List<PlaceUiModel> {
    return withContext(Dispatchers.Default) {
        filterByRestaurantAndLiquorStore().mapTo(mutableListOf(), {
            it.toPlaceUiModel()
        })
    }
}
