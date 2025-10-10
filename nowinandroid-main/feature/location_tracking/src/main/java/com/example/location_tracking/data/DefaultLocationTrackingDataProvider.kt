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

package com.example.location_tracking.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.samples.apps.nowinandroid.core.domain.sights.GetSightsByIsVisitedValueUseCase
import com.google.samples.apps.nowinandroid.core.model.data.sight.Sight
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class DefaultLocationTrackingDataProvider @Inject constructor(
    val getSightsByIsVisitedValueUseCase: GetSightsByIsVisitedValueUseCase,
    @ApplicationContext private val context: Context
): LocationTrackingDataProvider {

    override suspend fun provideLocationData(): LocationTrackingData = coroutineScope {
        val allUnvisitedSightsDeferred = async { getUnvisitedSights() }

        val currentLocationDeferred = async { getCurrentLocationSuspend() }

        val nearestSights = nearestSights(currentLocationDeferred.await(), allUnvisitedSightsDeferred.await())
        val sphere = computeSurroundingSphere(nearestSights)

        Log.d(" ZZZZZZZZZZZ DefaultLocationTrackingDataProvider ", " nearestSights "+  nearestSights.size + " sphere.radius " + sphere.radius + " latitude " + sphere.center.latitude + " longitude " + sphere.center.longitude)
        LocationTrackingData(
            targets = nearestSights,
            sphere = sphere
        )
    }

    private fun nearestSights(userLocation: Location, allSights: List<Sight>, n: Int = TARGETS_LIMIT): List<Sight> {
        return allSights.sortedBy { it.distanceTo(userLocation) }.take(n)
    }

    private fun computeSurroundingSphere(sights: List<Sight>): Sphere {
        val center = Location("manual").apply {
            latitude = sights.map { it.latitude }.average()
            longitude = sights.map { it.longitude }.average()
        }

        val radius = sights.maxOf { it.distanceTo(center) } + 50f // extra margin

        return Sphere(
            center = center,
            radius = radius
        )
    }

    private suspend fun getCurrentLocationSuspend(): Location {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            throw SecurityException("Location permission not granted")
        }

        return suspendCancellableCoroutine { cont ->
            LocationServices.getFusedLocationProviderClient(context)
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) cont.resume(location)
                    else cont.resumeWithException(Exception("Location not found"))
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
        }
    }

    private suspend fun getUnvisitedSights(): List<Sight> {
        return getSightsByIsVisitedValueUseCase(false)
            .first()
            .map { it.sight }
    }

    private fun Sight.distanceTo(location: Location): Float =
        Location("").apply {
            latitude = this@distanceTo.latitude
            longitude = this@distanceTo.longitude
        }.distanceTo(location)
}