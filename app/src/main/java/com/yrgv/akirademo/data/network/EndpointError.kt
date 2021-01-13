package com.yrgv.akirademo.data.network

import com.yrgv.akirademo.utils.Either
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Defines localized error responses and their associated data.
 */
sealed class EndpointError(open val message: String?) {

    data class UnhandledError(
        val responseCode: Int? = null,
        val originalThrowable: Throwable? = null,
        override val message: String?
    ) : EndpointError(message)

    sealed class ClientError(override val message: String?) : EndpointError(message) {
        data class BadRequest(override val message: String?) : ClientError(message)
        data class Unauthorised(override val message: String?) : ClientError(message)
        data class Forbidden(override val message: String?) : ClientError(message)
        data class NotFound(override val message: String?) : ClientError(message)
        data class Timeout(override val message: String?) : ClientError(message)
        data class IAmATeapot(override val message: String?) : ClientError(message)
    }

    sealed class ServerError(override val message: String?) : EndpointError(message) {
        data class InternalServerError(override val message: String?) : ServerError(message)
        data class ServiceUnavailable(override val message: String?) : ServerError(message)
    }

    data class Unreachable(override val message: String?) : EndpointError(message)

}

object StatusCode {
    const val BAD_REQUEST = 400
    const val UNAUTHORISED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val TIMEOUT = 408
    const val I_AM_A_TEAPOT = 418
    const val INTERNAL_SERVER_ERROR = 500
    const val SERVICE_UNAVAILABLE = 503
}

/**
 * Return appropriate EndpointError based on Http response code
 * */

fun getEndpointError(code:Int, message:String?) : EndpointError {
    return when (code) {
        StatusCode.BAD_REQUEST -> EndpointError.ClientError.BadRequest(message)
        StatusCode.UNAUTHORISED -> EndpointError.ClientError.Unauthorised(message)
        StatusCode.FORBIDDEN -> EndpointError.ClientError.Forbidden(message)
        StatusCode.NOT_FOUND -> EndpointError.ClientError.NotFound(message)
        StatusCode.TIMEOUT -> EndpointError.ClientError.Timeout(message)
        StatusCode.I_AM_A_TEAPOT -> EndpointError.ClientError.IAmATeapot(message)
        StatusCode.INTERNAL_SERVER_ERROR -> EndpointError.ServerError.InternalServerError(message)
        StatusCode.SERVICE_UNAVAILABLE -> EndpointError.ServerError.ServiceUnavailable(message)
        else -> EndpointError.UnhandledError(code, null, message)
    }
}


/**
 * Return appropriate EndpointError based on Throwable
 * */
//TODO: ADD TESTS FOR ME
fun Throwable.toLocalError(responseCode: Int? = null): Either<EndpointError, Nothing> {
    val endpointError = when (this) {
        is ConnectException,
        is UnknownHostException -> EndpointError.Unreachable(message)
        else -> EndpointError.UnhandledError(responseCode, this, message)
    }
    return Either.error(endpointError)
}