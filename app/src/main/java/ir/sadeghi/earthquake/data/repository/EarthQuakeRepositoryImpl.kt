package ir.sadeghi.earthquake.data.repository

import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.data.entitiy.ListOrder
import ir.sadeghi.earthquake.utils.network.APIErrorResponse
import ir.sadeghi.earthquake.utils.network.ErrorBody
import ir.sadeghi.earthquake.utils.network.NetworkResponse
import javax.inject.Inject

class EarthQuakeRepositoryImpl @Inject constructor(private val dataSource: EarthquakeApiService) :
    EarthquakeRepository {


    override suspend fun getNearByMeList(
        startData: String,
        minMagnitude: Double,
        lat: Double,
        long: Double,
        maxRadius: Int,
        limit: Int,
        offset: Int
    ): NetworkResponse<EQResponse, APIErrorResponse<ErrorBody>> {
        return dataSource.getNearByMeList(startData, minMagnitude, lat, long, maxRadius,limit,offset)
    }

    override suspend fun getMostRecentList(
        startData: String,
        minMagnitude: Double,
        limit: Int,
        order: ListOrder,
        offset: Int
    ): NetworkResponse<EQResponse, APIErrorResponse<ErrorBody>> {
        return dataSource.getListByTimeAndScale(startData, minMagnitude,limit,order.value,offset)
    }

}