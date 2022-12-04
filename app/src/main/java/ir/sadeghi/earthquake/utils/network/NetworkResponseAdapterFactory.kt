package ir.sadeghi.earthquake.utils.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not NetworkResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != NetworkResponse::class.java) {
            return null
        }

        // the response type is NetworkResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>" }

        var successBodyType = getParameterUpperBound(0, responseType)

        //I think the proper way for getting response of type GenericResponseModel is adding an adapter like GenericResponseAdapterFactory,
        //but the way of getting response of generic types is implementing
        //Types.newParameterizedType in this class.
        //So changing the model to generic caused this class to have a reference of Moshi.
        //So this violates dependency inversion principle. That's how moshi is implemented.


        /*if(successBodyType.rawType == GenericResponseModel::class.java && successBodyType is ParameterizedType){
            successBodyType = getParameterUpperBound(0, successBodyType)
            successBodyType = Types.newParameterizedType(GenericResponseModel::class.java, successBodyType)
        }*/

        // Look up a converter for the Error type on the Retrofit instance.
        val errorConverter: Converter<ResponseBody, ErrorBody> =
            retrofit.responseBodyConverter(ErrorBody::class.java, arrayOfNulls(0))


        return NetworkResponseAdapter<Any, APIErrorResponse<ErrorBody>>(
            successBodyType,
            errorConverter
        )
    }
}