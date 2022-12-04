package ir.sadeghi.earthquake.ui.reports.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.sadeghi.earthquake.R
import ir.sadeghi.earthquake.data.entitiy.FeaturesItem
import ir.sadeghi.earthquake.databinding.EqHListItemBinding
import ir.sadeghi.earthquake.ext.getPlaceFromEQ
import ir.sadeghi.earthquake.utils.Constants
import ir.sadeghi.earthquake.utils.ImageLoader
import ir.sadeghi.earthquake.utils.setMagnitude

class HListAdapter(
    private val dataList: List<FeaturesItem?>,
    private val onItemClicked: (eq:FeaturesItem) -> Unit
) :
    RecyclerView.Adapter<HListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.eq_h_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eq = dataList[position] ?: return
        holder.binding.run {
            magnitude.setMagnitude(eq.properties?.mag)
            date.text = eq.properties?.timeHuman
            timeAgo.text = eq.properties?.timeAgo
            title.text = eq.properties?.place?.getPlaceFromEQ()

            ImageLoader.loadImageByUrl(
                Constants.BASE_URL_FOR_FLAG.plus(eq.properties?.country),
                countryFlag
            )
        }
        holder.itemView.setOnClickListener { onItemClicked(eq) }
    }

    override fun getItemCount(): Int = dataList.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = EqHListItemBinding.bind(view)
    }
}