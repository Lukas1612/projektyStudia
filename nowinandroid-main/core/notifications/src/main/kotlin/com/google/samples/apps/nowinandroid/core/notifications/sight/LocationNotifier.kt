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

package com.google.samples.apps.nowinandroid.core.notifications.sight

import android.Manifest.permission
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.location.Location
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.permissions.hasPostNotificationsPermission
import com.google.samples.apps.nowinandroid.core.notifications.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


private const val NOTIFICATION_CHANNEL_ID = "location_id"
private const val NOTIFICATION_CHANNEL_NAME = "Location"
private const val LOCATION_NOTIFICATION_ID =  1
private const val VISITED_LOCATION_NOTIFICATION_ID =  2

@Singleton
internal class LocationNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
): NotificationHelper{

    /* private val locationNotification: NotificationCompat.Builder by lazy {
         context.createLocationNotification()
     }*/

    private val locationTrackingNotification: NotificationCompat.Builder by lazy {
        createLocationTrackingNotification()
    }

    @RequiresPermission(permission.POST_NOTIFICATIONS)
    override fun updateLocationNotification(userLocation: Location, sightLocation: Location){

        if (!context.hasPostNotificationsPermission()) {
            return
        }

        val notificationManager = NotificationManagerCompat.from(context)

        val distanceToSight = userLocation.distanceTo(sightLocation)

        locationTrackingNotification.setContentText(
            "distance to sight: ($distanceToSight)"
        )

        notificationManager.notify(LOCATION_NOTIFICATION_ID, locationTrackingNotification.build())
    }

    override fun startForeground(service: Service) {
        service.startForeground(LOCATION_NOTIFICATION_ID, locationTrackingNotification.build())
    }

    override fun cancelLocationNotification() {
        NotificationManagerCompat.from(context).cancel(LOCATION_NOTIFICATION_ID)
    }

    @RequiresPermission(permission.POST_NOTIFICATIONS)
    override fun youHaveVisitedNewSightNotification(sightName: String) {
        val visitedSightNotification = createVisitedSightNotification(sightName)
        NotificationManagerCompat.from(context).notify(VISITED_LOCATION_NOTIFICATION_ID, visitedSightNotification.build())
    }

    private fun createLocationTrackingNotification(): NotificationCompat.Builder {
        return context.createLocationNotification{
            setContentTitle("Tracking location...")
                .setContentText("distance to sight: null")
                .setSmallIcon(R.drawable.outline_my_location_24)
                .setOngoing(true)
        }
    }

    private fun createVisitedSightNotification(sightName: String): NotificationCompat.Builder{
        return context.createLocationNotification{
            setContentTitle("You visited a new sight...")
                .setContentText("visited sight: $sightName")
                .setSmallIcon(R.drawable.outline_bookmark_check_24)
        }
    }
}

/**
 * Creates a notification for configured for news updates
 */
/*private fun Context.createLocationNotification(): NotificationCompat.Builder{
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        NOTIFICATION_CHANNEL_ID,
    )
        .setContentTitle("Tracking location...")
        .setContentText("Location: null")
        .setSmallIcon(R.drawable.outline_my_location_24)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOngoing(true)
}*/

/**
 * Creates a notification for location updates
 */
private fun Context.createLocationNotification(
    block: NotificationCompat.Builder.() -> Unit,
): NotificationCompat.Builder {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        NOTIFICATION_CHANNEL_ID,
    )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .apply(block)
}

/**
 * Ensures that a notification channel is present if applicable
 */
private fun Context.ensureNotificationChannelExists() {
    if (VERSION.SDK_INT < VERSION_CODES.O) return

    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = getString(R.string.core_notifications_location_notification_channel_description)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}