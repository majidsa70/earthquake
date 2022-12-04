package ir.sadeghi.earthquake.utils.network


import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.lang.reflect.Type
import java.net.ProtocolException
import java.util.concurrent.TimeUnit


class NetworkResponseAdapter<S : Any, E : APIErrorResponse<ErrorBody>>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, ErrorBody>
) : CallAdapter<S, Call<NetworkResponse<S, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<NetworkResponse<S, E>> {
        return NetworkResponseCall(call, errorBodyConverter)
    }

    internal class NetworkResponseCall<S : Any, E : APIErrorResponse<ErrorBody>>(
        private val delegate: Call<S>,
        private val errorConverter: Converter<ResponseBody, ErrorBody>
    ) : Call<NetworkResponse<S, E>> {

        override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
            return delegate.enqueue(object : Callback<S> {
                override fun onResponse(call: Call<S>, response: Response<S>) {
                    val body = response.body()
                    val code = response.code()
                    val error = response.errorBody()

                    if (response.isSuccessful) {
                        //   Timber.d("API response successful.")
                        if (body != null) {
                            // API success
                            // No need to check 204, because we checked nullability of the body
                            if (code in 200..299) {
                                //   Timber.d("API success")
                                callback.onResponse(
                                    this@NetworkResponseCall,
                                    Response.success(NetworkResponse.Success(body))
                                )
                            } else {
                                /* Timber.d(
                                     "API Unexpected Error, this should not happened," +
                                             "but we count it for sure."
                                 )*/
                                // API error
                                //          Timber.d("API Error body is not null, the code is $code")
                                val apiErrorResponse = createApiErrorResponse(
                                    -1,
                                    ErrorBody(
                                        null,
                                        SUCCESSFUL_RESPONSE_BUT_CODE_IS_NOT_IN_200_TO_299
                                    )
                                )
                                val apiError = NetworkResponse.APIError(apiErrorResponse)
                                //error response is not failed! so we pass success
                                val errorResponse =
                                    Response.success(apiError as NetworkResponse<S, E>)

                                callback.onResponse(
                                    this@NetworkResponseCall,
                                    errorResponse
                                )

                            }
                        } else {
                            //        Timber.d("API body is null")
                            // Response is successful but the body is null
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(NetworkResponse.Empty(null))
                            )
                        }
                    } else {
                        //    Timber.d("API response is not successful.")
                        val errorBody = convertToErrorBody(error)


                        val apiErrorResponse = createApiErrorResponse(
                            code,
                            errorBody ?: ErrorBody(null, ERROR_MESSAGE_IS_NULL)
                        )
                        //   Timber.d("Code: $code Error body: $errorBody")
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(
                                NetworkResponse.APIError(apiErrorResponse) as NetworkResponse<S, E>
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<S>, throwable: Throwable) {
                    //  Timber.d("API onFailure.")
                    val networkResponse = when (throwable) {
                        is ProtocolException -> NetworkResponse.ProtocolError(throwable)
                        is IOException -> NetworkResponse.NetworkError(throwable)
                        else -> NetworkResponse.UnknownError(throwable)
                    }
                    callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
                }
            })
        }

        private fun createApiErrorResponse(
            code: Int,
            errorBody: ErrorBody
        ): APIErrorResponse<ErrorBody> {
            return when (code) {
                401 -> APIErrorResponse.Unauthenticated(errorBody)
                404 -> APIErrorResponse.NotFoundResponse(errorBody)
                in 400..499 -> APIErrorResponse.ClientErrorResponse(errorBody)
                in 500..599 -> APIErrorResponse.ServerErrorResponse(errorBody)
                else -> APIErrorResponse.UnexpectedErrorResponse(errorBody)
            }
        }


        /**
         * We use Kotlin reflection for converting ResponseBody to ErrorBody.
         */
        private fun convertToErrorBody(error: ResponseBody?): ErrorBody? {
            return when {
                error == null -> null
                error.contentLength() == 0L -> null
                else -> try {
                    //we use kotlin reflection for converting the body to ErrorBody Class
                    errorConverter.convert(error)
                } catch (ex: Exception) {
                    null
                }
            }
        }

        override fun isExecuted() = delegate.isExecuted

        override fun clone() = NetworkResponseCall<S, E>(delegate.clone(), errorConverter)

        override fun isCanceled() = delegate.isCanceled

        override fun cancel() = delegate.cancel()

        override fun execute(): Response<NetworkResponse<S, E>> {
            throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
        }

        override fun request(): Request = delegate.request()

        override fun timeout(): Timeout {
            return Timeout().timeout(60L, TimeUnit.SECONDS)
        }
    }

    companion object {
        const val SUCCESSFUL_RESPONSE_BUT_CODE_IS_NOT_IN_200_TO_299 =
            "Successful response, but the code is not is the range 200 to 299."
        const val ERROR_MESSAGE_IS_NULL = "Error body is null."


    }
}
