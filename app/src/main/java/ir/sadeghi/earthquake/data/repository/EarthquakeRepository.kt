package ir.sadeghi.earthquake.data.repository

import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.data.entitiy.ListOrder
import ir.sadeghi.earthquake.utils.Constants.MIN_MAGNITUDE_IN_RECENT
import ir.sadeghi.earthquake.utils.Constants.START_DATE_DEFAULT
import ir.sadeghi.earthquake.utils.network.APIErrorResponse
import ir.sadeghi.earthquake.utils.network.ErrorBody
import ir.sadeghi.earthquake.utils.network.NetworkResponse

interface EarthquakeRepository {

    suspend fun getNearByMeList(
        startData: String,
        minMagnitude: Double,
        lat: Double,
        long: Double,
        maxRadius: Int,
        limit: Int,
        offset: Int = 1
    ): NetworkResponse<EQResponse, APIErrorResponse<ErrorBody>>

    suspend fun getMostRecentList(
        startData: String = START_DATE_DEFAULT,
        minMagnitude: Double = MIN_MAGNITUDE_IN_RECENT,
        limit: Int,
        order: ListOrder,
        offset: Int = 1
    ): NetworkResponse<EQResponse, APIErrorResponse<ErrorBody>>
}