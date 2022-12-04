package ir.sadeghi.earthquake.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.sadeghi.earthquake.data.repository.*
import ir.sadeghi.earthquake.domain.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImplDI {


    @Binds
    abstract fun earthquakeRepoImpl(impl: EarthQuakeRepositoryImpl): EarthquakeRepository

    @Binds
    abstract fun calendarRepoImpl(impl: DateRepositoryImpl): DateRepository

    @Binds
    abstract fun getNearByMeListImpl(impl: GetEarthquakeListByMeUseCaseImpl): GetEarthquakeListByMeUseCase

    @Binds
    abstract fun getRecentListImpl(impl: GetRecentEarthquakeUseCaseImpl): GetRecentEarthquakeUseCase

}