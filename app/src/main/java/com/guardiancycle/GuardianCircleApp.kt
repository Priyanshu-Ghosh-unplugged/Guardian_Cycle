package com.guardiancycle

import android.app.Application
import android.content.Context

class GuardianCircleApp : Application() {
    lateinit var sosCoordinator: SOSManager
    override fun onCreate() {
        super.onCreate()
        sosCoordinator = SOSManager(
            this,
            contactsRepository = EmergencyContactsRepository(this),
            locationRepository = LocationRepository(this)
        )


        // In your activity/fragment
        //binding.sosButton.setOnClickListener {
          //  (application as GuardianCircleApp).SOSManager
            //    .activateSOS(SOSManager.triggerSOS.BUTTON)
        }
    }



