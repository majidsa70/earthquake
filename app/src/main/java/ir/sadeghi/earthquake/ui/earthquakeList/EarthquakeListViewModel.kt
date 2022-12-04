package ir.sadeghi.earthquake.ui.earthquakeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.domain.EarthquakeUseCases
import ir.sadeghi.earthquake.utils.Constants
import ir.sadeghi.earthquake.utils.ListType
import ir.sadeghi.earthquake.utils.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EarthquakeListViewModel @Inject constructor(private val earthquakeUseCases: EarthquakeUseCases) :
    ViewModel() {

    private val limit = 30
    private val maxRadius = Constants.EARTHQUAKE_NERA_ME_RADIUS
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private var _listType: ListType? = null

    private val _dataList: MutableStateFlow<State<EQResponse>> =
        MutableStateFlow(State.LoadingState)
    val dataList: StateFlow<State<EQResponse>> = _dataList

    fun getList(listType: ListType) = viewModelScope.launch {

        if (listType == _listType) {
            return@launch
        }
        _listType = listType

        _dataList.value = State.LoadingState
        when (listType) {
            ListType.NEAR_ME -> earthquakeUseCases.getEarthquakeListByMeUseCase(
                latitude,
                longitude,
                maxRadius,
                limit = limit
            )
            ListType.RECENT -> earthquakeUseCases.getRecentEarthquakeUseCase(limit = limit)
        }.collect {
            _dataList.value = it
        }
    }
}