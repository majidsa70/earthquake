package ir.sadeghi.earthquake.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.domain.EarthquakeUseCases
import ir.sadeghi.earthquake.utils.Constants
import ir.sadeghi.earthquake.utils.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val earthquakeUseCases: EarthquakeUseCases
) :
    ViewModel() {

    private var _recentList: MutableStateFlow<State<EQResponse>> =
        MutableStateFlow(State.LoadingState)
    var recentList: StateFlow<State<EQResponse>> = _recentList

    private var _nearMeList: MutableStateFlow<State<EQResponse>> =
        MutableStateFlow(State.LoadingState)
    var nearMeList: StateFlow<State<EQResponse>> = _nearMeList

    var latitude: Double? = null
    var longitude: Double? = null

    init {
        getRecentList()
    }

    fun getNearByMeList(lat: Double?, long: Double?, withFreshData: Boolean = false) {

        if (!withFreshData && null != latitude && null != longitude)
            return

        latitude = lat ?: return
        longitude = long ?: return

        viewModelScope.launch {

            earthquakeUseCases.getEarthquakeListByMeUseCase(
                latitude!!,
                longitude!!,
                Constants.EARTHQUAKE_NERA_ME_RADIUS
            ).collect {
                _nearMeList.emit(it)
            }
        }
    }

    fun getRecentList() {


        viewModelScope.launch {

            earthquakeUseCases.getRecentEarthquakeUseCase().collect {
                _recentList.emit(it)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }

}