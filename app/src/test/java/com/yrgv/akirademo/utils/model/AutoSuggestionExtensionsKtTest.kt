package com.yrgv.akirademo.utils.model

import com.yrgv.akirademo.data.network.placesapi.model.AutoCompleteResponse.Prediction
import com.yrgv.akirademo.data.network.placesapi.model.AutoCompleteResponse.Prediction.StructuredFormatting
import com.yrgv.akirademo.data.network.placesapi.model.PREDICTION_TYPE_LIQUOR_STORE
import com.yrgv.akirademo.data.network.placesapi.model.PREDICTION_TYPE_RESTAURANT
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for model transformer functions
 */
class AutoSuggestionExtensionsKtTest {

    @Test
    fun `Prediction-toPlaceUiModel() maps values correctly`() {
        val prediction = Prediction(
            place_id = "myPlaceId",
            structured_formatting = StructuredFormatting("name", "address"),
            types = arrayListOf()
        )
        val uiModel = prediction.toPlaceUiModel()
        assertTrue(
            uiModel.id == prediction.place_id &&
                    uiModel.name == prediction.structured_formatting.main_text &&
                    uiModel.address == prediction.structured_formatting.secondary_text
        )
    }

    @Test
    fun `filterByRestaurantAndLiquorStore returns only types restaurant&liquor_store`() {
        val prediction1 = Prediction(
            "1",
            StructuredFormatting("a", "b"),
            arrayListOf("kindergarden", PREDICTION_TYPE_RESTAURANT)
        )
        val prediction2 = prediction1.copy(place_id = "2", types = arrayListOf("food"))
        val prediction3 = prediction1.copy(place_id = "3", types = arrayListOf("bar"))
        val prediction4 =
            prediction1.copy(place_id = "4", types = arrayListOf(PREDICTION_TYPE_LIQUOR_STORE))
        val prediction5 = prediction1.copy(place_id = "5", types = arrayListOf(""))
        val prediction6 = prediction1.copy(
            place_id = "6",
            types = arrayListOf(PREDICTION_TYPE_RESTAURANT, PREDICTION_TYPE_LIQUOR_STORE)
        )
        val unfilteredList = arrayListOf(
            prediction1,
            prediction2,
            prediction3,
            prediction4,
            prediction5,
            prediction6
        )

        val filteredList = runBlocking {
            unfilteredList.filterByRestaurantAndLiquorStore()
        }
        assertTrue(filteredList.size == 3)
        assertTrue(filteredList[0].place_id == "1")
        assertTrue(filteredList[1].place_id == "4")
        assertTrue(filteredList[2].place_id == "6")
    }

}