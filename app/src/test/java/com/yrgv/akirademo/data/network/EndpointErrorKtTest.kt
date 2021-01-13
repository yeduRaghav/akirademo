package com.yrgv.akirademo.data.network

import com.yrgv.akirademo.utils.Either
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.IllegalStateException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 *
 */
class EndpointErrorKtTest {

    @Test
    fun `getEndpointError StatusCode_BADREQUEST returns BadRequest`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.BAD_REQUEST, message)
        assertTrue(error is EndpointError.ClientError.BadRequest)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getEndpointError StatusCode_UNAUTHORISED returns Unauthorised`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.UNAUTHORISED, message)
        assertTrue(error is EndpointError.ClientError.Unauthorised)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getEndpointError StatusCode_FORBIDDEN returns Forbidden`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.FORBIDDEN, message)
        assertTrue(error is EndpointError.ClientError.Forbidden)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getEndpointError StatusCode_NOT_FOUND returns NotFound`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.NOT_FOUND, message)
        assertTrue(error is EndpointError.ClientError.NotFound)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getEndpointError StatusCode_TIMEOUT returns Timeout`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.TIMEOUT, message)
        assertTrue(error is EndpointError.ClientError.Timeout)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getEndpointError StatusCode_I_AM_A_TEAPOT returns IAmATeapot`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.I_AM_A_TEAPOT, message)
        assertTrue(error is EndpointError.ClientError.IAmATeapot)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getEndpointError StatusCode_INTERNAL_SERVER_ERROR returns Unauthorised`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.INTERNAL_SERVER_ERROR, message)
        assertTrue(error is EndpointError.ServerError.InternalServerError)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getEndpointError StatusCode_SERVICE_UNAVAILABLE returns ServiceUnavailable`() {
        val message = "ABC"
        val error = getEndpointError(StatusCode.SERVICE_UNAVAILABLE, message)
        assertTrue(error is EndpointError.ServerError.ServiceUnavailable)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getEndpointError undefined StatusCode returns UnhandledError`() {
        val message = "ABC"
        val error1 = getEndpointError(444, message)
        assertTrue(error1 is EndpointError.UnhandledError && error1.responseCode == 444)
        assertTrue(error1.message?.equals(message) == true)

        val error2 = getEndpointError(450, null)
        assertTrue(error2 is EndpointError.UnhandledError && error2.responseCode == 450)
        assertTrue(error2.message == null)
    }

    @Test
    fun `throwable-toLocalError returns Unreachable for ConnectException with null responseCode`() {
        val message = "A deep message"
        val throwable = ConnectException(message)
        val localError = throwable.toLocalError(null)
        assertTrue(localError is Either.Error && localError.error is EndpointError.Unreachable && localError.error.message == message)
    }

    @Test
    fun `throwable-toLocalError returns Unreachable for ConnectException with responseCode`() {
        val message = "A deep message"
        val throwable = ConnectException(message)
        val localError = throwable.toLocalError(400)
        assertTrue(localError is Either.Error && localError.error is EndpointError.Unreachable && localError.error.message == message)
    }

    @Test
    fun `throwable-toLocalError returns Unreachable for UnknownHostException with null responseCode`() {
        val message = "A deep message"
        val localError = UnknownHostException(message).toLocalError(null)
        assertTrue(
            localError is Either.Error
                    && localError.error is EndpointError.Unreachable
                    && localError.error.message == message
        )
    }

    @Test
    fun `throwable-toLocalError returns Unreachable for UnknownHostException with responseCode`() {
        val message = "A deep message"
        val localError = UnknownHostException(message).toLocalError(400)
        assertTrue(
            localError is Either.Error
                    && localError.error is EndpointError.Unreachable
                    && localError.error.message == message
        )
    }

    @Test
    fun `throwable-toLocalError returns UnhandledError except for ConnectException and UnknownHostException`() {
        val local = Throwable().toLocalError()
        assertTrue(local is Either.Error && local.error is EndpointError.UnhandledError)
    }

}