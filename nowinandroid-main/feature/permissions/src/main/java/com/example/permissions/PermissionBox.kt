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

package com.example.permissions

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

/**
 * Show permissions buttons if permissions not granted, otherwise do the onAllGranted() method
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionFlow(
    onAllGranted: @Composable () -> Unit
) {
    // Foreground permissions
    val foregroundPermissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val foregroundState = rememberMultiplePermissionsState(foregroundPermissions)

    // Background (only for Android 10+)
    val backgroundState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            rememberPermissionState(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        else null

    val allForegroundGranted = foregroundState.allPermissionsGranted
    val backgroundGranted = backgroundState?.status?.isGranted ?: true
    val backgroundRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    if(!allForegroundGranted || (backgroundRequired && !backgroundGranted)){
        PermissionsUI(
            allForegroundGranted,
            foregroundState,
            backgroundRequired,
            backgroundGranted,
            backgroundState
        )
    }

    if(allForegroundGranted && backgroundGranted){
        onAllGranted()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionsUI(
    allForegroundGranted: Boolean,
    foregroundState: MultiplePermissionsState,
    backgroundRequired: Boolean,
    backgroundGranted: Boolean,
    backgroundState: PermissionState?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()  // take full width but not full height
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,  // align at the top of this Column
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(!allForegroundGranted){
            NumberCircle(number = 1)
            ForegroundPermissionButton(foregroundState)
            DownArrow()
        }

        Spacer(Modifier.height(16.dp))

        if(backgroundRequired && !backgroundGranted){
            BackgroundPermissionButton(
                allForegroundGranted,
                backgroundState
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun BackgroundPermissionButton(
    allForegroundGranted: Boolean,
    backgroundState: PermissionState?
){
    // you can't allow the background permission before the foreground permissions
    val isButtonEnabled = allForegroundGranted

    ButtonWithExplanation(
        onButtonClick = { backgroundState?.launchPermissionRequest() },
        isButtonEnabled = isButtonEnabled
    )
}

@Composable
private fun ButtonWithExplanation(
    onButtonClick: () -> Unit,
    isButtonEnabled: Boolean
) {
    var showHelp by remember { mutableStateOf(false) }

    val buttonText = "Allow Background Location Access"
    val helpText = "We need this permission to enable geofence tracking in the background. You need to enable foreground tracking first."

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        // Regular button
        Button(
            onClick = onButtonClick,
            enabled = isButtonEnabled
        ) {
            Text(buttonText)
        }

        // Small circular "?" button
        IconButton(
            onClick = { showHelp = true },
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    shape = CircleShape
                )
        ) {
            Text(
                "?",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }

    // Help popup
    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            title = { Text("Help") },
            text = { Text(helpText) },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ForegroundPermissionButton(
    foregroundState: MultiplePermissionsState
){
    val onClick = { foregroundState.launchMultiplePermissionRequest() }
    val buttonText = "Grant Foreground Location Access"
    Button(onClick = onClick) {
        Text(buttonText)
    }
}

@Composable
private fun NumberCircle(
    number: Int,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 24.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DownArrow(
    tint: Color = MaterialTheme.colorScheme.onBackground,
    size: Dp = 32.dp
) {
    Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "Arrow Down",
        tint = tint,
        modifier = Modifier.size(size)
    )
}

