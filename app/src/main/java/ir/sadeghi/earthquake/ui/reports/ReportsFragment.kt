package ir.sadeghi.earthquake.ui.reports

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import ir.sadeghi.earthquake.R
import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.databinding.FragmentReportsBinding
import ir.sadeghi.earthquake.ext.*
import ir.sadeghi.earthquake.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ReportsFragment : Fragment(), OnMapReadyCallback {

    private var locationUpdatesJob: Job? = null
    private var fusedLocationProvider: FusedLocationProviderClient? = null

    private var _binding: FragmentReportsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ReportsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReportsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.run {
            val behavior: AppBarLayout.Behavior = AppBarLayout.Behavior()
            val params = appbar.layoutParams as CoordinatorLayout.LayoutParams
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })
            params.behavior = behavior
        }


        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupMap()
        collectData()
    }




    override fun onResume() {
        super.onResume()

        startLocationListening()

    }

    override fun onPause() {
        super.onPause()

        stopLocationListening()
    }

    private fun stopLocationListening() {
        locationUpdatesJob?.cancel()
    }

    private fun startLocationListening() {

        activity?.let {
            it.gpsIsOn().iF({
                it.isPermittedLocationAccess().iF({

                    locationUpdatesJob = lifecycleScope.launch {
                        fusedLocationProvider?.locationFlow(LocationRequestCreator.create())
                            ?.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                            ?.collect { location ->
                                getEarthQuakeList(location.latitude, location.longitude)
                                setupMapSettings(location)
                            }
                    }

                }, {
                    removeNearList()
                })
            }, {
                removeNearList()
            })
        }
    }

    private fun removeNearList() {
        binding.nearList.setError()
    }

    private fun getEarthQuakeList(lat: Double?, lon: Double?, withFreshData: Boolean = false) {
        viewModel.getNearByMeList(lat, lon, withFreshData)
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                collectNearMe()
            }
            launch {
                collectRecentList()
            }
        }
    }

    private suspend fun collectNearMe() {
        viewModel.nearMeList.collect {

            when (it) {
                is State.DataState -> it.data?.let { it1 -> addNearListData(it1) }
                is State.ErrorState -> showToast(it.exception)
                State.LoadingState -> {}
            }
        }
    }

    private suspend fun collectRecentList() {

        viewModel.recentList.collect {

            when (it) {
                is State.DataState -> it.data?.let { it1 -> addRecentListData(it1) }
                is State.ErrorState -> showToast(it.exception)
                State.LoadingState -> {}
            }
        }
    }


    private fun addNearListData(data: EQResponse) {
        binding.nearList.setData(getString(R.string.nearRowTitle), data, {
            val dir = ReportsFragmentDirections.actionNavigationReportsToNavigationDetails(it)
            findNavController().navigate(dir)
        }, {
            val dir =
                ReportsFragmentDirections.actionNavigationReportsToEarthquakeListFragment(
                    ListType.NEAR_ME.name,
                    viewModel.latitude?.toLong() ?: 0, viewModel.longitude?.toLong() ?: 0
                )
            findNavController().navigate(dir)
        })
    }

    private fun addRecentListData(data: EQResponse) {
        binding.mostRecentList.setData(getString(R.string.recentRowTitle), data, {
            val dir = ReportsFragmentDirections.actionNavigationReportsToNavigationDetails(it)
            findNavController().navigate(dir)
        }, {
            val dir =
                ReportsFragmentDirections.actionNavigationReportsToEarthquakeListFragment(ListType.RECENT.name)
            findNavController().navigate(dir)
        })

    }


    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var googleMap: GoogleMap? = null
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }

    private fun setupMapSettings(location: Location) {
        val latLng = LatLng(
            location.latitude,
            location.longitude
        )
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, 10f
            )
        )
        val markerOptions = MarkerOptions()

        markerOptions.position(latLng)
        googleMap?.addMarker(markerOptions)
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        googleMap?.uiSettings?.isRotateGesturesEnabled = false
        googleMap?.uiSettings?.isScrollGesturesEnabled = true
        googleMap?.uiSettings?.isMapToolbarEnabled = false
    }
}