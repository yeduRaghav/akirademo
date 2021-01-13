package com.yrgv.akirademo.data.network.placesapi

import android.util.Log
import com.yrgv.akirademo.data.network.placesapi.model.AutoCompleteResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the actual endpoints for Places Api
 */
interface PlacesApi {

    companion object {
        private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"
        private const val RESPONSE_TYPE_JSON = "/json"

        private lateinit var apiInstance: PlacesApi

        fun getInstance(): PlacesApi {
            if (!::apiInstance.isInitialized) {
                apiInstance = build()
            }
            return apiInstance
        }

        private fun build(): PlacesApi {
            val okhttpClient = OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .build()
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okhttpClient)
                .build()
                .create(PlacesApi::class.java)
        }

        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("Guruve", message)
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    }


    @GET("autocomplete$RESPONSE_TYPE_JSON")
    fun getAutoComplete(
        @Query("input") input: String,
        @Query("types") types: String,
        @Query("key") key: String
    ): Call<AutoCompleteResponse>



}