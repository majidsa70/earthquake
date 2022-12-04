package ir.sadeghi.earthquake.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ir.sadeghi.earthquake.R

object ImageLoader {

    fun loadImageByUrl(url: String, targetView: ImageView) {


        Glide.with(targetView)
            .load(url)
            .optionalFitCenter()
           // .override(55,55)
            .apply(RequestOptions.circleCropTransform())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(targetView)
    }
}