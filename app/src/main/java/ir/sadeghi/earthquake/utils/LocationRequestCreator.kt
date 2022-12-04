package ir.sadeghi.earthquake.utils

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority

object LocationRequestCreator {
    private const val INTERVAL = 60 * 1000L
    fun create(): LocationRequest {
        return LocationRequest.create().apply {
            interval = INTERVAL
            fastestInterval = 10
            priority = Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 60
        }
    }
}