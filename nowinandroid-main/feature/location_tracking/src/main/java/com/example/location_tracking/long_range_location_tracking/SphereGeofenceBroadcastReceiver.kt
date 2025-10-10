/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.location_tracking.long_range_location_tracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SphereGeofenceBroadcastReceiver: BroadcastReceiver() {

    @Inject
    lateinit var locationTrackingInitializer: LongRangeLocationTrackingInitializer

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return
        if (geofencingEvent.hasError()) return

        val geofenceTransition = geofencingEvent.geofenceTransition

        Log.d(" SSSSSS SphereGeofenceBroadcastReceiver ", "onReceive" )


        handleGeofencingEvent(
            context,
            geofenceTransition,
        )
    }

    private fun handleGeofencingEvent(
        context: Context,
        geofenceTransition: Int,
    ) {

        when (geofenceTransition) {

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                startLocationTrackingInNewSphere(context)
            }

            else -> {
                Log.w("SphereGeofenceBroadcastReceiver", "Unhandled geofence transition: $geofenceTransition")
            }
        }
    }

    /**
     * restarts location tracking after exiting
     * the current sphere with currently tracked sights
     */
    private fun startLocationTrackingInNewSphere(context: Context){
        val pendingResult = goAsync()
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        locationTrackingInitializer.startLocationTracking(context, scope)

        // Make sure to finish the receiver once the scope is done
        scope.launch {
            try {
                // Optionally wait for any other work
            } finally {
                pendingResult.finish()
            }
        }
    }
}