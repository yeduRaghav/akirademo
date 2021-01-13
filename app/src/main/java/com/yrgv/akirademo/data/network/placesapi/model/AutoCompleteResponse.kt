package com.yrgv.akirademo.data.network.placesapi.model

import com.google.gson.annotations.SerializedName

/**
 * Response returned for Autocomplete endpoint
 */
data class AutoCompleteResponse(
    @SerializedName("predictions") val predictions: ArrayList<Prediction>,
) {

    data class Prediction(
        @SerializedName("place_id") val place_id: String,
        @SerializedName("structured_formatting") val structured_formatting: StructuredFormatting,
        @SerializedName("types") val types: ArrayList<String>
    ) {
        data class StructuredFormatting(
            @SerializedName("main_text") val main_text: String,
            @SerializedName("secondary_text") val secondary_text: String
        )
    }

}

const val PREDICTION_TYPE_RESTAURANT = "restaurant"
const val PREDICTION_TYPE_LIQUOR_STORE = "liquor_store"