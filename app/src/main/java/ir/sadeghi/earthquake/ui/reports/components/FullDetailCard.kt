package ir.sadeghi.earthquake.ui.reports.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import ir.sadeghi.earthquake.R
import ir.sadeghi.earthquake.data.entitiy.FeaturesItem
import ir.sadeghi.earthquake.databinding.EqVListItemBinding
import ir.sadeghi.earthquake.ext.getPlaceFromEQ
import ir.sadeghi.earthquake.utils.Constants
import ir.sadeghi.earthquake.utils.ImageLoader
import ir.sadeghi.earthquake.utils.setMagnitude

class FullDetailCard : BaseHorizontalListComponent {

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

    init {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    fun setData(eq: FeaturesItem, onItemClicked: ((eq: FeaturesItem) -> Unit)? = null) {

        removeAllViews()

        addView(
            EqVListItemBinding.inflate(LayoutInflater.from(context)).apply {

                val details = eq.properties ?: return
                this.magnitude.setMagnitude(details.mag)
                date.text = details.timeHuman
                timeAgo.text = details.timeAgo
                title.text = eq.properties.place?.getPlaceFromEQ()
                ImageLoader.loadImageByUrl(
                    Constants.BASE_URL_FOR_FLAG.plus(details.country),
                    countryFlag
                )
                this.root.setOnClickListener { onItemClicked?.invoke(eq) }
            }.root
        )
    }


}