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

package com.example.route_map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.route_map.Constants.DESTINATION_LATITUDE_KEY
import com.example.route_map.Constants.DESTINATION_LONGITUDE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouteMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val destinationLatitude: Double = savedStateHandle.get<String>(DESTINATION_LATITUDE_KEY)!!.toDouble()
    val destinationLongitude: Double = savedStateHandle.get<String>(DESTINATION_LONGITUDE_KEY)!!.toDouble()
}