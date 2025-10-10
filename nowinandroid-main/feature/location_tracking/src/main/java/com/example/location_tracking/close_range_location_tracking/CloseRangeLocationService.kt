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

package com.example.location_tracking.close_range_location_tracking

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.example.location_tracking.data.LONG_RANGE_TRACKING_MINIMAL_RADIUS
import com.example.location_tracking.data.VISIT_SIGHT_MINIMAL_DISTANCE
import com.google.android.gms.location.LocationServices
import com.google.samples.apps.nowinandroid.core.domain.sights.GetSightByIdUseCase
import com.google.samples.apps.nowinandroid.core.domain.sights.VisitSightUseCase
import com.google.samples.apps.nowinandroid.core.model.data.sight.Sight
import com.google.samples.apps.nowinandroid.core.notifications.sight.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CloseRangeLocationService: Service() {

    @Inject
    lateinit var locationNotifier: NotificationHelper

    @Inject
    lateinit var visitSightUseCase: VisitSightUseCase

    @Inject
    lateinit var getSightByIdUseCase: GetSightByIdUseCase

    private var sightLocation: Location? = null
    private var sightName: String? = null
    private var sightId: String? = null

    private var hasVisitedCurrentSight = false

    private val TAG = "CloseRangeLocationService"

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var closeRangeLocationClient: CloseRangeLocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        closeRangeLocationClient = DefaultCloseRangeLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent == null) {
            Log.w(TAG, "Service restarted with null intent")
            return START_STICKY
        }

        when(intent.action) {
            ACTION_START -> {

                sightId = intent.getStringExtra(SIGHT_ID)

                Log.d(" CZCZCZ CloseRangeLocationService", "sightId $sightId")

                if (sightId.isNullOrEmpty()) {
                    Log.e(TAG, "Missing sightId in intent")
                    stopSelf()
                    return START_NOT_STICKY
                }

                start()


            }

            ACTION_STOP -> stop()
        }

        return START_STICKY
    }

    private fun start() {
        serviceScope.coroutineContext.cancelChildren()

        serviceScope.launch {
            try {


                val currentSight = getSightByIdUseCase(sightId!!).sight

                Log.d(" CCCCCC CloseRangeLocationService", "currentSight name " + currentSight.name)

                sightLocation = getSightLocation(currentSight)
                sightName = currentSight.name



                if (sightName.isNullOrEmpty() || sightLocation == null) {
                    Log.w(TAG, "Invalid sight data: $sightName at $sightLocation")
                    return@launch
                }

                Log.d(TAG, "Sight location set to " + sightLocation!!.latitude + " " + sightLocation!!.longitude )

                launchCloseRangeLocationClient()


            } catch (e: Exception) {
                Log.e("CloseRangeService", "Error in coroutine", e)
                stopSelf() // stop service on failure if needed
            }
        }

    }

    private fun launchCloseRangeLocationClient() {
        locationNotifier.startForeground(this)

        closeRangeLocationClient
            .getLocationUpdates(LOCATION_UPDATES_INTERVAL_MILLISECONDS)
            .catch { e -> e.printStackTrace() }
            .distinctUntilChanged { old, new ->
                old.distanceTo(new) < MINIMAL_TRACKED_LOCATION_CHANGE_METERS
            }
            .onEach { userLocation ->
                doOnLocationUpdate(userLocation)
            }
            .launchIn(serviceScope)

    }

    private fun stopNotifications(){
        stopForeground(STOP_FOREGROUND_REMOVE)
        locationNotifier.cancelLocationNotification()
    }

    private fun stop() {
        stopNotifications()
        stopSelf()
    }

    override fun onDestroy() {
        stopNotifications()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun doOnLocationUpdate(userLocation: Location){

        val targetLocation = sightLocation ?: return
        locationNotifier.updateLocationNotification(userLocation, targetLocation)

        val distanceToSight = userLocation.distanceTo(sightLocation!!)

        Log.d(" CLCLCLC CloseRangeLocationService", " distance to sight $distanceToSight" )

        if (distanceToSight > LONG_RANGE_TRACKING_MINIMAL_RADIUS) {
            stop()
            return
        }

        if(distanceToSight < VISIT_SIGHT_MINIMAL_DISTANCE && !hasVisitedCurrentSight)
        {
            //prevents the method from firing multiple times for the same sight
            hasVisitedCurrentSight = true

            serviceScope.launch {
                try {
                    visitSightUseCase(sightId!!)

                    withContext(Dispatchers.Main) {
                        locationNotifier.youHaveVisitedNewSightNotification(sightName!!)
                    }

                    serviceScope.coroutineContext.cancelChildren()
                    stop()
                } catch (e: Exception) {
                    Log.e("CloseRangeLocationService", "Coroutine failed", e)
                }
            }

        }
    }

    private fun getSightLocation(sight: Sight): Location {
        return Location("SightProvider").apply {
            latitude = sight.latitude
            longitude = sight.longitude
        }
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val SIGHT_ID = "sight_id"

        const val LOCATION_UPDATES_INTERVAL_MILLISECONDS = 5000L
        const val MINIMAL_TRACKED_LOCATION_CHANGE_METERS = 2
    }
}