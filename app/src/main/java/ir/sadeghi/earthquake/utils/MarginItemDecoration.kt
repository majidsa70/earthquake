package ir.sadeghi.earthquake.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val vSpaceSize: Int, private val hSpaceSize: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {


                left = hSpaceSize
            }
            top = vSpaceSize
            right = hSpaceSize
            bottom = vSpaceSize
        }
    }
}