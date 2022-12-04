package ir.sadeghi.earthquake.domain

import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.data.repository.DateRepository
import ir.sadeghi.earthquake.data.repository.EarthquakeRepository
import ir.sadeghi.earthquake.di.IoDispatcher
import ir.sadeghi.earthquake.ext.getPlaceFromEQ
import ir.sadeghi.earthquake.utils.Constants
import ir.sadeghi.earthquake.utils.State
import ir.sadeghi.earthquake.utils.network.convertToState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetEarthquakeListByMeUseCaseImpl @Inject constructor(
    private val earthquakeRepository: EarthquakeRepository,
    private val dateRepository: DateRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :
    GetEarthquakeListByMeUseCase {
    override suspend fun invoke(
        lat: Double,
        lang: Double,
        maxRadius: Int,
        limit: Int,
        offset: Int
    ): Flow<State<EQResponse>> = flow {
        //  emit(State.LoadingState)
        val result =
            earthquakeRepository.getNearByMeList(
                startData = dateRepository.getDateInPast(Constants.PAST_DAYS_IN_NEAR_ME)
                    ?: Constants.START_DATE_DEFAULT,
                minMagnitude = Constants.MIN_MAGNITUDE_IN_RECENT,
                lat,
                lang,
                maxRadius, limit = limit, offset = offset
            ).convertToState()

        when (result) {
            is State.DataState -> {
                result.data?.features?.forEach {
                    it?.properties?.timeHuman =
                        it?.properties?.time?.let { it1 -> dateRepository.getHumanRead(it1) }
                    it?.properties?.timeAgo =
                        it?.properties?.time?.let { it1 -> dateRepository.convertToTimeAgo(it1) }

                    it?.properties?.country = it?.properties?.place?.getPlaceFromEQ()
                }
            }
            else -> {}
        }

        emit(result)
    }.flowOn(ioDispatcher)
}