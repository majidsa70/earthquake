package ir.sadeghi.earthquake.ui.earthquakeList

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.sadeghi.earthquake.data.entitiy.FeaturesItem
import ir.sadeghi.earthquake.ui.reports.components.FullDetailCard

class EarthquakeVerticalListAdapter(
    private var dataList: List<FeaturesItem?>,
    private val onItemClicked: (eq: FeaturesItem) -> Unit
) :
    RecyclerView.Adapter<EarthquakeVerticalListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FullDetailCard(parent.context)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eq = dataList[position] ?: return
        holder.fullDetailCard.setData(eq, onItemClicked)
        //  holder.itemView.setOnClickListener {  }
    }

    override fun getItemCount(): Int = dataList.size

    fun clearList() {
        dataList = emptyList()
        notifyDataSetChanged()
    }

    class ViewHolder(view: FullDetailCard) : RecyclerView.ViewHolder(view) {
        val fullDetailCard: FullDetailCard = view
    }
}