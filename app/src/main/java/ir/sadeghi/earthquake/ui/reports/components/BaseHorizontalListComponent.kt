package ir.sadeghi.earthquake.ui.reports.components

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

open class BaseHorizontalListComponent : LinearLayout {
    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }


    open fun onBaseReady() {

    }
    private fun initView() {

        orientation = VERTICAL





        onBaseReady()
    }
}