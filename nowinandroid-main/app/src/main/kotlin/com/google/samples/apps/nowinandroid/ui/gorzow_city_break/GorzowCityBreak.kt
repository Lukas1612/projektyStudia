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

package com.google.samples.apps.nowinandroid.ui.gorzow_city_break

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.sight_map.Constants.AWARD_ID_KEY
import com.example.sight_map.Constants.INITIAL_LOCATION_LATITUDE_KEY
import com.example.sight_map.Constants.INITIAL_LOCATION_LONGITUDE_KEY
import com.example.sight_map.Constants.MAP_MODE_KEY
import com.example.sight_map.Constants.SELECTED_SIGHT_ID_KEY
import com.example.sight_map.SightsMapActivity
import com.google.samples.apps.nowinandroid.navigation.AppNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GorzowCityBreak() {
    MaterialTheme {
        val navController = rememberNavController()
        val context = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Simple Navigation App") })
            }
        ) { padding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(padding),
                onStartMapActivity = { data ->
                    val intent = Intent(context, SightsMapActivity::class.java).apply {

                        putExtra(MAP_MODE_KEY, data.mode)

                        data.latitude?.let { lat ->
                            putExtra(INITIAL_LOCATION_LATITUDE_KEY, lat)
                        }

                        data.longitude?.let { lng ->
                            putExtra(INITIAL_LOCATION_LONGITUDE_KEY, data.longitude)
                        }

                        data.selectedSightId?.let { id ->
                            putExtra(SELECTED_SIGHT_ID_KEY, id)
                        }

                        data.awardId?.let{ id ->
                            putExtra(AWARD_ID_KEY, id)
                        }
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}
