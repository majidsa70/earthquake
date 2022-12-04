package ir.sadeghi.earthquake.domain

import javax.inject.Inject

data class EarthquakeUseCases @Inject constructor(
    val getEarthquakeListByMeUseCase: GetEarthquakeListByMeUseCase,
    val getRecentEarthquakeUseCase: GetRecentEarthquakeUseCase
)
