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

package com.example.sight_map

import com.google.android.gms.maps.model.LatLng

object Constants {
    val DEFAULT_INITIAL_LOCATION =  LatLng(52.73, 15.23) //Cathedral

    val INITIAL_LOCATION_LATITUDE_KEY = "initial_location_latitude"
    val INITIAL_LOCATION_LONGITUDE_KEY = "initial_location_longitude"

    val DEFAULT_INITIAL_SIGHT_ID = "1"
    val SELECTED_SIGHT_ID_KEY = "selected_sight_id"

    val MAP_MODE_KEY = "map_mode_key"
    val CHOSEN_SIGHT_MODE = "chosen_sight_mode"
    val ALL_SIGHTS_MODE = "all_sights_mode"
    val AWARD_SIGHTS_MODE = "award_sights_mode"

    val AWARD_ID_KEY = "award_id_key"
}