package ir.sadeghi.earthquake.data.entitiy

import com.squareup.moshi.Json


data class MyLocation(
    @Json(name = "latitude")
    val latitude: Double? = 0.0,
    @Json(name = "longitude")
    val longitude: Double? = 0.0
)
