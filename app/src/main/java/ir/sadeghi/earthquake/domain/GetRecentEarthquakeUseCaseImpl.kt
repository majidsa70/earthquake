package ir.sadeghi.earthquake.domain

import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.data.entitiy.ListOrder
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

class GetRecentEarthquakeUseCaseImpl @Inject constructor(
    private val repository: EarthquakeRepository,
    private val dateRepository: DateRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :
    GetRecentEarthquakeUseCase {
    override suspend fun invoke(limit: Int, offset: Int): Flow<State<EQResponse>> = flow {
        //  emit(State.LoadingState)
        val result = repository.getMostRecentList(
            startData = dateRepository.getDateInPast(Constants.PAST_DAYS_IN_BIGGEST)
                ?: Constants.START_DATE_DEFAULT,
            limit = limit, order = ListOrder.TIME, offset = offset
        ).convertToState()

        when (result) {
            is State.DataState -> {
                result.data?.features?.forEach { feature ->

                    feature?.properties?.let { properties ->
                        properties.timeHuman =
                            properties.time?.let { it1 -> dateRepository.getHumanRead(it1) }
                        properties.timeAgo =
                            properties.time?.let { it1 -> dateRepository.convertToTimeAgo(it1) }
                        properties.country = properties.place?.getPlaceFromEQ()
                    }
                }
            }
            else -> {}
        }

        emit(result)
    }.flowOn(ioDispatcher)
}