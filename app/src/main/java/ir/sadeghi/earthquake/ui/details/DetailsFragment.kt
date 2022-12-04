package ir.sadeghi.earthquake.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import ir.sadeghi.earthquake.R
import ir.sadeghi.earthquake.data.entitiy.FeaturesItem
import ir.sadeghi.earthquake.databinding.FragmentDetailsBinding


@AndroidEntryPoint
class DetailsFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentDetailsBinding? = null

    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels()
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {
            viewModel.earthquake = it.getParcelable("earthquake") ?: return
        }


        binding.run {
            viewModel.earthquake?.let { earthquake ->
                eqDetailCard.magnitudeView.text = earthquake.properties?.mag.toString()
                eqDetailCard.title.text = earthquake.properties?.place

                detailCard.setData(earthquake)
            }


            toolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }


            val behavior: AppBarLayout.Behavior = AppBarLayout.Behavior()
            val params = appbar.layoutParams as CoordinatorLayout.LayoutParams
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })
            params.behavior = behavior


        }

        setupMap()
    }


    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        viewModel.earthquake?.let {

            setupMapSettings(it)
        }
    }

    private fun setupMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupMapSettings(earthquake: FeaturesItem) {
        val latLng = LatLng(
            earthquake.geometry?.coordinates?.get(1) ?: 0.0,
            earthquake.geometry?.coordinates?.get(0) ?: 0.0
        )
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, 7f
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

