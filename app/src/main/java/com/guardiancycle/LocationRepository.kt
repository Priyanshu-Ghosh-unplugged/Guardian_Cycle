package com.guardiancycle
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepository(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Fetches the current location using Fused Location Provider.
     * @return The current location or null if unavailable.
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    continuation.resume(location)
                } else {
                    continuation.resumeWithException(Exception("Location unavailable"))
                }
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }
    }

      companion object {
        private var currentLocation: Location? = null
        fun updateLocation(location: Location) {
            currentLocation = location
            println("Location updated: Latitude = ${location.latitude}, Longitude = ${location.longitude}")

        }
    }
    object LocationRepository {
        private var currentLocation: Location? = null

        fun updateLocation(location: Location) {
            currentLocation = location
        }

        fun getCurrentLocation(): Location? {
            return currentLocation
        }
    }


}
