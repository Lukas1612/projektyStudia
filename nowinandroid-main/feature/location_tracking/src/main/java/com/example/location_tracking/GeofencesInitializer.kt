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

package com.example.location_tracking

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.location_tracking.data.LocationTrackingData
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofencesInitializer: LocationTrackingInitializer {
    /**
     * Start geofences for 10 closest targets and a sphere that contains the targets.
     * When a user goes outside the sphere a new sphere and a new list of points should
     * be calculated
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun startLocationTracking(
        context: Context,
        locationTrackingData: LocationTrackingData
    ){
        val geofencingClient = LocationServices.getGeofencingClient(context)
        val geofences = mutableListOf<Geofence>()


        locationTrackingData.targets.forEachIndexed { index, target ->
            geofences.add(
                Geofence.Builder()
                    .setRequestId(target.id)
                    .setCircularRegion(target.latitude, target.longitude, 100f)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build()
            )
        }

        geofences.add(
            Geofence.Builder()
                .setRequestId("SPHERE")
                .setCircularRegion(locationTrackingData.sphere.center.latitude, locationTrackingData.sphere.center.longitude, locationTrackingData.sphere.radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        )

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofences)
            .build()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, GeofenceBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        geofencingClient.addGeofences(request, pendingIntent)
            .addOnSuccessListener { Log.d("Geofence", "Geofences added") }
            .addOnFailureListener { e -> Log.e("Geofence", "Failed: $e") }
    }


}