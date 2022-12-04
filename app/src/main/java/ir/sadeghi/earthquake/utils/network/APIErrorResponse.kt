package ir.sadeghi.earthquake.utils.network

sealed class APIErrorResponse<E> {

    /**
     * Unauthenticated User (error 401)
     */
    data class Unauthenticated<E>(val error: E) : APIErrorResponse<E>()


    data class NotFoundResponse<E>(val error: E) : APIErrorResponse<E>()

    /**
     * Called for [400, 500) responses, except 401., 404
     **/
    data class ClientErrorResponse<E>(val error: E) : APIErrorResponse<E>()

    /**
     * Called for [500, 600) responses.
     **/
    data class ServerErrorResponse<E>(val error: E) : APIErrorResponse<E>()

    /**
     *  Unexpected
     **/
    data class UnexpectedErrorResponse<E>(val error: E) : APIErrorResponse<E>()
}