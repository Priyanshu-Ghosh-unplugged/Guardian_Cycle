package com.guardiancycle

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import com.google.android.gms.location.*
import android.location.Location
import android.util.Log

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    // Update location in repository
                    LocationRep.updateLocation(location)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000 // 10 seconds
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

// Define LocationRepository
object LocationRepo {
    fun updateLocation(location: Location) {
        // Handle location update (e.g., save to a database or send to a server)
        Log.d("LocationRepository", "Location updated: ${location.latitude}, ${location.longitude}")
    }



}

object LocationRep {

    private var currentLocation: Location? = null

    fun updateLocation(location: Location) {
        currentLocation = location
        // Optionally, log or store the location
        Log.d("LocationRepository", "Location updated: $location")
    }

    fun getCurrentLocation(): Location? {
        return currentLocation
    }
}
