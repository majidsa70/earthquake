package ir.sadeghi.earthquake.domain

import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.utils.Constants
import ir.sadeghi.earthquake.utils.State
import kotlinx.coroutines.flow.Flow

interface GetRecentEarthquakeUseCase {
    suspend operator fun invoke(limit: Int = Constants.LIST_LIMIT,
                                offset: Int = 1): Flow<State<EQResponse>>
}