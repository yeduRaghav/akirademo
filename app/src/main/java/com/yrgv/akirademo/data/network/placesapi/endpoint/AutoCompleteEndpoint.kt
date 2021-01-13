package com.yrgv.akirademo.data.network.placesapi.endpoint

import com.yrgv.akirademo.data.network.BaseEndpoint
import com.yrgv.akirademo.data.network.placesapi.PlacesApi
import com.yrgv.akirademo.data.network.placesapi.model.AutoCompleteResponse
import retrofit2.Call

private const val TYPE_ESTABLISHMENT = "establishment"
/**
 * Abstraction for `place/autocomplete` Endpoint.
 */
class AutoCompleteEndpoint(
    private val placesApi: PlacesApi,
    private val apiKey: String
) : BaseEndpoint<AutoCompleteResponse>() {

    private lateinit var input: String
    private lateinit var types: String

    fun setData(input: String, types: String = TYPE_ESTABLISHMENT) {
        this.input = input
        this.types = types
    }

    override fun getCall(): Call<AutoCompleteResponse> {
        return placesApi.getAutoComplete(
            input = input,
            types = types,
            key = apiKey
        )
    }

}