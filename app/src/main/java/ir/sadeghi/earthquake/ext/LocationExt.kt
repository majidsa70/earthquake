package ir.sadeghi.earthquake.ext

import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun FusedLocationProviderClient.locationFlow(locationRequest: LocationRequest) =
    callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                try {
                    result.lastLocation?.let { trySend(it) }
                } catch (e: Exception) {
                }
            }
        }
        requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
            .addOnFailureListener { e ->
                close(e) // in case of exception, close the Flow
            }
        // clean up when Flow collection ends
        awaitClose {
            removeLocationUpdates(callback)
        }
    }

