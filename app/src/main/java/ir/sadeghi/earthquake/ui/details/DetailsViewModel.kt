package ir.sadeghi.earthquake.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.data.entitiy.FeaturesItem
import ir.sadeghi.earthquake.domain.GetEarthquakeListByMeUseCase
import ir.sadeghi.earthquake.utils.Constants
import ir.sadeghi.earthquake.utils.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
) :
    ViewModel() {

    var earthquake: FeaturesItem? = null

}