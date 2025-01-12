package com.guardiancycle

import android.annotation.SuppressLint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import android.location.LocationProvider

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.delay

// Stops all current SOS operations (e.g., alerts, notifications, location tracking)
suspend fun stopCurrentOperations(context: Context) {
    println("Stopping all current SOS operations...")

    // Stop notifications
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()

    // Stop location tracking
    println("Stopping location tracking...")
    // Simulated location tracking stop; add your implementation here
}

// Verifies critical components (e.g., checking permissions, system status, etc.)
suspend fun verifyComponents(context: Context) {
    println("Verifying critical components...")

    if (!checkPermissions(context)) {
        throw IllegalStateException("Critical permissions are missing!")
    }

    if (!checkNetworkConnectivity(context)) {
        throw IllegalStateException("Network connectivity is unavailable!")
    }

    if (!isGPSEnabled(context)) {
        throw IllegalStateException("GPS is disabled!")
    }

    println("All critical components verified successfully.")
}

// Reinitialized the SOS system
suspend fun reinitializeSOSSystem(context: Context) {
    println("Reinitializing SOS system...")
    // Restart background services, initialize variables, or reset states
    println("SOS system reinitialized.")
}

// Verifies if the system recovery was successful
suspend fun verifyRecovery(context: Context): Boolean {
    println("Verifying recovery...")

    val permissionsOk = checkPermissions(context)
    val networkOk = checkNetworkConnectivity(context)
    val gpsOk = isGPSEnabled(context)

    val recoverySuccessful = permissionsOk && networkOk && gpsOk
    println("Recovery status: $recoverySuccessful")
    return recoverySuccessful
}

// Activates a backup system in case of failure
suspend fun activateBackupSystem(context: Context) {
    println("Activating backup system...")

    // Notify emergency contacts (example)
    sendEmergencySMS("+123456789", "Backup system activated. Please check on me immediately.")
}

// Helper function to check network connectivity
fun checkNetworkConnectivity(context: Context): Boolean {
    println("Checking network connectivity...")
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

// Helper function to check permissions
fun checkPermissions(context: Context): Boolean {
    println("Checking permissions...")
    val hasLocationPermission = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val hasSMSPermission = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.SEND_SMS
    ) == PackageManager.PERMISSION_GRANTED

    return hasLocationPermission && hasSMSPermission
}

// Helper function to check if GPS is enabled
fun isGPSEnabled(context: Context): Boolean {
    println("Checking GPS status...")
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

// Function to send an emergency SMS (example implementation)
fun sendEmergencySMS(phoneNumber: String, message: String) {
    println("Sending SMS to $phoneNumber: $message")
    // Use SMSManager or other services to send the SMS in a real implementation
}



@Suppress("DEPRECATION")
class RecoveryManager(private val context: Context) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    suspend fun recoverSOSSystem() {
        try {
            // 1. Stop all current SOS operations
            stopCurrentOperations(context)

            // 2. Verify critical components
            verifyComponents()

            // 3. Reinitialize system
            reinitializeSOSSystem(context)

            // 4. Verify recovery
            if (!verifyRecovery(context)) {
                activateBackupSystem(context)
            }
        } catch (e: Exception) {
            activateBackupSystem(context)
        }
    }

    private suspend fun verifyComponents(): Boolean {
        return withContext(Dispatchers.Default) {
            val results = mutableListOf<Deferred<Boolean>>()

            results.add(async { verifyLocation() })
            results.add(async { verifySensors() })
            results.add(async { verifyNetwork() })
            results.add(async { verifyStorage() })

            results.awaitAll().all { it }
        }
    }

    suspend fun tryAlternativeLocation() {
        val locationProvider = LocationProvider1(context)

        // Try different location methods in order of accuracy
        val location = locationProvider.getLastKnownLocation()
            ?: locationProvider.getNetworkLocation()
            ?: locationProvider.getCellLocation()

        if (location != null) {
            // Update location repository with best available location
            LocationRepository.updateLocation(location)
        }
    }

    @SuppressLint("ServiceCast")
    suspend fun recoverNetworkConnection() {
        withContext(Dispatchers.IO) {
            // 1. Check network type availability
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // 2. Try to switch between mobile and WiFi
            if (!isWifiConnected(context)) {
                enableMobileData()
            } else if (!isMobileDataConnected(context)) {
                enableWifi(context)
            }

            // 3. Verify network recovery
            if (!verifyNetworkConnection(context)) {
                activateOfflineMode()
            }
        }
    }

    private fun isWifiConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }
    private fun enableMobileData() {
        println("Please enable mobile data manually. This operation requires user interaction.")
    }
    private fun isMobileDataConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    }
    @SuppressLint("ServiceCast")
    private fun enableWifi(context: Context) {
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

            if (wifiManager.isWifiEnabled) {
                println("WiFi is already enabled.")
            } else {
                wifiManager.isWifiEnabled = true
                println("WiFi has been enabled successfully.")
            }
        } catch (e: SecurityException) {
            println("Failed to enable WiFi due to security restrictions: ${e.message}")
            // Notify the user to enable WiFi manually
            //notifyUserToEnableWifi(context)
            println("Turn on your WiFi")
        } catch (e: Exception) {
            println("An error occurred while enabling WiFi: ${e.message}")
        }
    }

    private fun verifyNetworkConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null
    }
    private fun activateOfflineMode() {
        println("No network connection available. Activating offline mode.")
        // Add offline handling logic here (e.g., notify user, save data locally, etc.)
    }


    private suspend fun verifyLocation(): Boolean {
        println("Verifying location services...")
        // Simulating a location verification process
        delay(100) // Simulate async work
        val locationEnabled = true // Replace with actual location status
        return locationEnabled
    }

    private suspend fun verifySensors(): Boolean {
        println("Verifying sensors...")
        // Simulating sensor verification
        delay(100) // Simulate async work
        val sensorsAvailable = true // Replace with actual sensor status
        return sensorsAvailable
    }
    private suspend fun verifyNetwork(): Boolean {
        println("Verifying network connectivity...")
        // Simulating network verification
        delay(100) // Simulate async work
        val networkAvailable = true // Replace with actual network status
        return networkAvailable
    }

    private suspend fun verifyStorage(): Boolean {
        println("Verifying storage...")
        // Simulating storage verification
        delay(100) // Simulate async work
        val storageAvailable = true // Replace with actual storage status
        return storageAvailable
    }



}