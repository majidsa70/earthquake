package ir.sadeghi.earthquake.data.repository

import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.data.entitiy.ListOrder
import ir.sadeghi.earthquake.utils.network.APIErrorResponse
import ir.sadeghi.earthquake.utils.network.ErrorBody
import ir.sadeghi.earthquake.utils.network.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface EarthquakeApiService {


    @GET("fdsnws/event/1/query?format=geojson")
    suspend fun getNearByMeList(
        @Query("starttime") startData: String,
        @Query("minmagnitude") minMagnitude: Double,
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("maxradiuskm") maxRadius: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 1,
        @Query("orderby") orderBy: String = ListOrder.TIME.value,
    ): NetworkResponse<EQResponse, APIErrorResponse<ErrorBody>>

    @GET("fdsnws/event/1/query?format=geojson")
    suspend fun getListByTimeAndScale(
        @Query("starttime") startData: String,
        @Query("minmagnitude") minMagnitude: Double,
        @Query("limit") limit: Int,
        @Query("orderby") orderBy: String,
        @Query("offset") offset: Int = 1
    ): NetworkResponse<EQResponse, APIErrorResponse<ErrorBody>>
}