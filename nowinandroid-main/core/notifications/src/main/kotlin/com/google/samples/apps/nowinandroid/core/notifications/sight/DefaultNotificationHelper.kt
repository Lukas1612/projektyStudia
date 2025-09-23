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

@Singleton
internal class DefaultNotificationHelper@Inject constructor(
    @ApplicationContext private val context: Context,
): NotificationHelper{

    private val _locationNotification: NotificationCompat.Builder by lazy {
        context.createLocationNotification()
    }

    @RequiresPermission(permission.POST_NOTIFICATIONS)
    override fun updateLocationNotification(lat: String, lng: String){

        if (!context.hasPostNotificationsPermission()) {
            return
        }

        val notificationManager = NotificationManagerCompat.from(context)

        _locationNotification.setContentText(
            "Location: ($lat, $lng)"
        )

        notificationManager.notify(LOCATION_NOTIFICATION_ID, _locationNotification.build())
    }

    override fun startForeground(service: Service) {
        service.startForeground(LOCATION_NOTIFICATION_ID, _locationNotification.build())
    }
}

/**
 * Creates a notification for configured for news updates
 */
private fun Context.createLocationNotification(): NotificationCompat.Builder{
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