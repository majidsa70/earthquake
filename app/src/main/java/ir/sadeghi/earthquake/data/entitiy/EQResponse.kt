package ir.sadeghi.earthquake.data.entitiy

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class EQResponse(

    @Json(name = "features")
    val features: List<FeaturesItem?>? = null,

    @Json(name = "metadata")
    val metadata: Metadata? = null,

    @Json(name = "bbox")
    val bbox: List<Double?>? = null,

    @Json(name = "type")
    val type: String? = null
)

data class Metadata(

    @Json(name = "generated")
    val generated: Long? = null,

    @Json(name = "count")
    val count: Int? = null,

    @Json(name = "api")
    val api: String? = null,

    @Json(name = "title")
    val title: String? = null,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "status")
    val status: Int? = null
)

@Keep
@Parcelize
data class Geometry(

    @Json(name = "coordinates")
    val coordinates: List<Double?>? = null,

    @Json(name = "type")
    val type: String? = null
) : Parcelable

@Keep
@Parcelize
data class FeaturesItem(

    @Json(name = "geometry")
    val geometry: Geometry? = null,

    @Json(name = "id")
    val id: String? = null,

    @Json(name = "type")
    val type: String? = null,

    @Json(name = "properties")
    val properties: Properties? = null
) : Parcelable

@Keep
@Parcelize
data class Properties(

    @Json(name = "dmin")
    val dmin: Double? = null,

    @Json(name = "code")
    val code: String? = null,

    @Json(name = "sources")
    val sources: String? = null,


    @Json(name = "mmi")
    val mmi: Double? = null,

    @Json(name = "type")
    val type: String? = null,

    @Json(name = "title")
    val title: String? = null,

    @Json(name = "magType")
    val magType: String? = null,

    @Json(name = "nst")
    val nst: Double? = null,

    @Json(name = "sig")
    val sig: Double? = null,

    @Json(name = "tsunami")
    val tsunami: Double? = null,

    @Json(name = "mag")
    val mag: Double? = null,

    @Json(name = "alert")
    val alert: String? = null,

    @Json(name = "gap")
    val gap: Double? = null,

    @Json(name = "rms")
    val rms: Double? = null,

    @Json(name = "place")
    val place: String? = null,
    var country: String? = null,

    @Json(name = "net")
    val net: String? = null,

    @Json(name = "types")
    val types: String? = null,

    @Json(name = "felt")
    val felt: Double? = null,

    @Json(name = "cdi")
    val cdi: Double? = null,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "ids")
    val ids: String? = null,

    @Json(name = "time")
    var time: Long? = null,

    var timeHuman: String? = null,
    var timeAgo: String? = null,
    var distanceToMe: Float? = null,

    @Json(name = "detail")
    val detail: String? = null,

    @Json(name = "updated")
    val updated: Long? = null,

    @Json(name = "status")
    val status: String? = null
) : Parcelable
