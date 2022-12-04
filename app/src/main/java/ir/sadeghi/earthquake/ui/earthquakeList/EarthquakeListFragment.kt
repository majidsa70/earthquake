package ir.sadeghi.earthquake.ui.earthquakeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.sadeghi.earthquake.R
import ir.sadeghi.earthquake.data.entitiy.FeaturesItem
import ir.sadeghi.earthquake.databinding.FragmentEarthquakeListBinding
import ir.sadeghi.earthquake.ext.showToast
import ir.sadeghi.earthquake.utils.ListType
import ir.sadeghi.earthquake.utils.State
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EarthquakeListFragment : Fragment() {
    private var _binding: FragmentEarthquakeListBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<EarthquakeListViewModel>()

    private var listAdapter: EarthquakeVerticalListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEarthquakeListBinding.inflate(inflater, container, false)
        binding.toolbar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.menu_filter -> {
                        showFilterMenu(this.findViewById(R.id.menu_filter))
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }

            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var listType = ListType.RECENT
        arguments?.let {
            it.getString("listType")?.let { type ->
                listType = ListType.valueOf(type)
            }

            it.getLong("latitude").let { value ->
                viewModel.latitude = value.toDouble()
            }

            it.getLong("longitude").let { value ->
                viewModel.longitude = value.toDouble()
            }
        }


        getDataList(listType)
        collectDataList()

    }

    private fun showFilterMenu(v: View) {
        val popup = PopupMenu(context!!, v)
        popup.menuInflater.inflate(R.menu.list_filter_menu, popup.menu)

        popup.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.mostRecentList -> {
                    getDataList(ListType.RECENT)
                }
                R.id.nearYouList -> {
                    getDataList(ListType.NEAR_ME)
                }
            }

            return@setOnMenuItemClickListener false
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

    private fun getDataList(listType: ListType) {

        viewModel.getList(listType)
    }

    private fun collectDataList() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            launch {
                viewModel.dataList.collect {
                    hideLoading()
                    when (it) {
                        is State.DataState -> setDataToList(it.data?.features)
                        is State.ErrorState -> showToast(it.exception)
                        State.LoadingState -> {
                            listAdapter?.clearList()
                            showLoading()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
    }

    private fun setDataToList(list: List<FeaturesItem?>?) {

        list ?: return

        binding.recyclerView.apply {
            context ?: return
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = EarthquakeVerticalListAdapter(list) {
                val dir =
                    EarthquakeListFragmentDirections.actionEarthquakeListFragmentToNavigationDetails(
                        it
                    )
                findNavController().navigate(dir)
            }.also {
                listAdapter = it
            }
        }

    }


}