package ir.sadeghi.earthquake.ui.reports.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import ir.sadeghi.earthquake.R
import ir.sadeghi.earthquake.databinding.HeadOfComponentLayoutBinding

@SuppressLint("ViewConstructor")
class HeadOfComponent(context: Context, headerData: HorizontalListHeader) : FrameLayout(context) {
    init {
        addView(
            LayoutInflater.from(context).inflate(R.layout.head_of_component_layout, this, false)
                .apply {
                    findViewById<TextView>(R.id.title).apply {
                        text = headerData.title
                        setOnClickListener { headerData.titleClick() }
                    }

                    findViewById<TextView>(R.id.moreTitle).apply {
                        text = headerData.moreTitle
                        setOnClickListener { headerData.moreTitleClick() }
                    }
                })
    }
}