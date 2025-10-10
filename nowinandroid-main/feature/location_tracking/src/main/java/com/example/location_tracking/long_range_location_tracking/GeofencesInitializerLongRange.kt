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

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.location_tracking.data.LONG_RANGE_TRACKING_MINIMAL_RADIUS
import com.example.location_tracking.data.LocationTrackingData
import com.example.location_tracking.data.LocationTrackingDataProvider
import com.example.location_tracking.data.Sphere
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.samples.apps.nowinandroid.core.model.data.sight.Sight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GeofencesInitializerLongRange @Inject constructor(
    private val trackingDataProvider: LocationTrackingDataProvider
): LongRangeLocationTrackingInitializer {

    /**
     * Start geofences for 10 closest targets and a sphere that contains the targets.
     * When a user goes outside the sphere a new sphere and a new list of points should
     * be calculated
     */
    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ])
    override fun startLocationTracking(
        context: Context,
        scope: CoroutineScope
    ) {
        scope.launch {
            val locationTrackingData = trackingDataProvider.provideLocationData()
            startGeofences(context, locationTrackingData)
        }
    }

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ])
    fun startGeofences(
        context: Context,
        locationTrackingData: LocationTrackingData
    ){
        startSightsGeofences(context, locationTrackingData.targets)
        startSphereGeofence(context, locationTrackingData.sphere)
    }

   /* @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ])
    fun startGeofences(
        context: Context,
        locationTrackingData: LocationTrackingData
    ){
        val geofencingClient = LocationServices.getGeofencingClient(context)

        val sightsPendingIntent = createSightsPendingIntent(context)

        Log.d(" QQQQQQQQQQ GeofencesInitializerLongRange ", " targets "  + locationTrackingData.targets.size + " sphere.radius " + locationTrackingData.sphere.radius + " latitude " + locationTrackingData.sphere.center.latitude + " longitude " + locationTrackingData.sphere.center.longitude)

        // Remove previously registered geofences first
        geofencingClient.removeGeofences(pendingIntent).addOnCompleteListener {
            // Now prepare the new geofences
            val geofences = mutableListOf<Geofence>()

            locationTrackingData.targets.forEach { target ->
                geofences.add(
                    Geofence.Builder()
                        .setRequestId(target.id)
                        .setCircularRegion(
                            target.latitude,
                            target.longitude,
                            LONG_RANGE_TRACKING_MINIMAL_RADIUS
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .build()
                )
            }

            val sphere = locationTrackingData.sphere
            require(sphere.radius > 0f) { "Geofence radius must be positive" }

            // Add sphere exit geofence
            geofences.add(
                Geofence.Builder()
                    .setRequestId(SPHERE_GEOFENCE_ID)
                    .setCircularRegion(
                        sphere.center.latitude,
                        sphere.center.longitude,
                        sphere.radius
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            )

            val request = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(geofences)
                .build()

            geofencingClient.addGeofences(request, pendingIntent)
                .addOnSuccessListener {
                    Log.d(TAG, "Geofences registered successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to add geofences: $e")
                }
        }

    }*/

//  Log.d(" QQQQQQQQQQ GeofencesInitializerLongRange ", " targets "  + targets.size)
    //   Log.d(" QQQQQQQQQQ GeofencesInitializerLongRange ", " sphere.radius " + sphere.radius + " latitude " + sphere.center.latitude + " longitude " + sphere.center.longitude)

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ])
    fun startSightsGeofences(
        context: Context,
        targets: List<Sight>
    ){

        val geofencingClient = LocationServices.getGeofencingClient(context)

        val sightsPendingIntent = createSightsPendingIntent(context)

        // Remove previously registered geofences first
        geofencingClient.removeGeofences(sightsPendingIntent).addOnCompleteListener {
            // Now prepare the new geofences
            val geofences = mutableListOf<Geofence>()

            targets.forEach { target ->
                geofences.add(
                    Geofence.Builder()
                        .setRequestId(target.id)
                        .setCircularRegion(
                            target.latitude,
                            target.longitude,
                            LONG_RANGE_TRACKING_MINIMAL_RADIUS
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .build()
                )
            }

            val request = GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(geofences)
                .build()

            geofencingClient.addGeofences(request, sightsPendingIntent)
                .addOnSuccessListener {
                    Log.d(TAG, "Geofences registered successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to add geofences: $e")
                }
        }
    }

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ])
    fun startSphereGeofence(
        context: Context,
        sphere: Sphere
    ){

        val geofencingClient = LocationServices.getGeofencingClient(context)

        val spherePendingIntent = createSpherePendingIntent(context)

        // Remove previously registered geofences first
        geofencingClient.removeGeofences(spherePendingIntent).addOnCompleteListener {
            // Now prepare the new geofences
            val geofences = mutableListOf<Geofence>()

            require(sphere.radius > 0f) { "Geofence radius must be positive" }

            // Add sphere exit geofence
            geofences.add(
                Geofence.Builder()
                    .setRequestId(SPHERE_GEOFENCE_ID)
                    .setCircularRegion(
                        sphere.center.latitude,
                        sphere.center.longitude,
                        sphere.radius
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            )

            val request = GeofencingRequest.Builder()
                .addGeofences(geofences)
                .build()

            geofencingClient.addGeofences(request, spherePendingIntent)
                .addOnSuccessListener {
                    Log.d(TAG, "Geofences registered successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to add geofences: $e")
                }
        }
    }


    override fun stopLocationTracking(context: Context) {
        val sightsPendingIntent = createSightsPendingIntent(context)
        val spherePendingIntent = createSpherePendingIntent(context)

        removeGeofences(context,sightsPendingIntent)
        removeGeofences(context,spherePendingIntent)
    }

    private fun removeGeofences(context: Context, pendingIntent: PendingIntent){
        val geofencingClient = LocationServices.getGeofencingClient(context)

        geofencingClient.removeGeofences(pendingIntent)
            .addOnSuccessListener { Log.d(TAG, "Geofences removed successfully") }
            .addOnFailureListener { e -> Log.e(TAG, "Failed to remove geofences: $e") }
    }

    private fun createSightsPendingIntent(context: Context): PendingIntent{
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, SightGeofenceBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    private fun createSpherePendingIntent(context: Context): PendingIntent{
        return PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, SphereGeofenceBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    private companion object {
        const val TAG = "GeofenceInitializer"
        const val SPHERE_GEOFENCE_ID = "SPHERE"
    }
}