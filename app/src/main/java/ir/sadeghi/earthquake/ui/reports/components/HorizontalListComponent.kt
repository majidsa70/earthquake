package ir.sadeghi.earthquake.ui.reports.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.sadeghi.earthquake.R
import ir.sadeghi.earthquake.data.entitiy.EQResponse
import ir.sadeghi.earthquake.data.entitiy.FeaturesItem
import ir.sadeghi.earthquake.utils.MarginItemDecoration

class HorizontalListComponent : BaseHorizontalListComponent {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    fun setError() {
        removeAllViews()
    }

    fun setData(
        headerTitle: String,
        data: EQResponse,
        onItemClicked: (eq: FeaturesItem) -> Unit,
        onMoreClicked: () -> Unit
    ) {

        data.features ?: return

        removeAllViews()

        addView(
            HeadOfComponent(
                context,
                HorizontalListHeader(
                    headerTitle,
                    context.getString(R.string.showMore),
                    {},
                    { onMoreClicked() })
            )
        )

        val list = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.cardPadding),
                    resources.getDimensionPixelSize(R.dimen.hCardMargin)
                )
            )
            adapter = HListAdapter(data.features, onItemClicked)
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
            clipToPadding = false

        }



        addView(list)
    }
}