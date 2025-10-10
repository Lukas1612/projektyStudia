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
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.location_tracking.close_range_location_tracking.CloseRangeLocationService
import com.example.location_tracking.close_range_location_tracking.CloseRangeLocationService.Companion.ACTION_START
import com.example.location_tracking.close_range_location_tracking.CloseRangeLocationService.Companion.SIGHT_ID
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class SightGeofenceBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return
        if (geofencingEvent.hasError()) return

        val geofenceTransition = geofencingEvent.geofenceTransition
        val triggeringGeofences = geofencingEvent.triggeringGeofences

        Log.d(" GGGGGG SightGeofenceBroadcastReceiver ", "onReceive" )


        handleGeofencingEvent(
            context,
            geofenceTransition,
            triggeringGeofences
        )
    }

    private fun handleGeofencingEvent(
        context: Context,
        geofenceTransition: Int,
        triggeringGeofences: MutableList<Geofence>?
    ) {

        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                // Start foreground service for precise tracking
                val serviceIntent = Intent(context, CloseRangeLocationService::class.java)

                triggeringGeofences!!.forEach { geofence ->
                    //targetId is the sight id in this case
                    val targetId = geofence.requestId

                    Log.d(" GGGGGG SightGeofenceBroadcastReceiver ", "GEOFENCE_TRANSITION_ENTER sightId: $targetId" )

                    serviceIntent.putExtra(SIGHT_ID, targetId)
                    serviceIntent.action = ACTION_START
                    ContextCompat.startForegroundService(context, serviceIntent)

                    Log.i(TAG, "startForegroundService CloseRangeLocationService SIGHT_ID = $targetId")
                }
            }

            else -> {
                Log.w("SightGeofenceBroadcastReceiver", "Unhandled geofence transition: $geofenceTransition")
            }
        }
    }
}


