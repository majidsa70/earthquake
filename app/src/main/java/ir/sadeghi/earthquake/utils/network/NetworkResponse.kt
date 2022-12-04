package ir.sadeghi.earthquake.utils.network

import java.io.IOException
import java.net.ProtocolException

sealed class NetworkResponse<out T: Any, out E: APIErrorResponse<ErrorBody>> {

    /**
     * Success response with body
     */
    data class Success<T: Any>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * Success response with body
     */
    data class Empty<T: Any>(val body: T?) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with body
     */
    data class APIError<E: APIErrorResponse<ErrorBody>>(val apiErrorResponse: E) : NetworkResponse<Nothing, E>()

    /**
     * Network error
     */
    data class NetworkError(val exception: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * Protocol error
     */
    data class ProtocolError(val exception: ProtocolException) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val throwable: Throwable?) : NetworkResponse<Nothing, Nothing>()
}
