package com.yrgv.akirademo.data.network

import android.util.Log
import com.yrgv.akirademo.utils.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

/**
 * Parent class for all endpoints.
 * Handles network response and exceptions and returns localized response.
 */

abstract class BaseEndpoint<R> {

    /**
     * The current call instance
     * */
    private lateinit var currentCall: Call<R>


    /**
     * Returns a new Retrofit Call
     * */
    protected abstract fun getCall(): Call<R>

    /**
     * Invoke to perform the network call.
     * @return If network call fails, returns appropriate [EndpointError].
     * If successful, returns Response body <R>
     * */
    suspend fun execute(): Either<EndpointError, R> {
        cancelCurrentCall()
        currentCall = getCall()
        return performNetworkCall()
    }


    //ok to ignore warning https://discuss.kotlinlang.org/t/warning-inappropriate-blocking-method-call-with-coroutines-how-to-fix/16903
    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun performNetworkCall(): Either<EndpointError, R> {
        return withContext(Dispatchers.IO) {
            try {
                currentCall.execute().localise()
            } catch (throwable: Throwable) {
                throwable.toLocalError()
            }
        }
    }

    private fun cancelCurrentCall() {
        if (::currentCall.isInitialized) {
            try {
                currentCall.cancel()
            } catch (exception: IllegalStateException) {
                Log.e("BaseEndpoint", exception.message ?: "exception cancelling currentCall")
            }
        }
    }

    private fun Response<R>.localise(): Either<EndpointError, R> {
        return body()?.let {
            Either.value(it)
        } ?: Either.error(getEndpointError(code(), message()))
    }

}