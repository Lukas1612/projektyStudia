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

import com.example.sight_map.Constants.ALL_SIGHTS_MODE
import com.example.sight_map.Constants.AWARD_SIGHTS_MODE
import com.example.sight_map.Constants.CHOSEN_SIGHT_MODE

sealed class MapMode {

    object Chosen : MapMode()
    object All : MapMode()
    object Award : MapMode()

    companion object {
        fun fromString(value: String?): MapMode {
            return when (value) {
                CHOSEN_SIGHT_MODE -> Chosen
                ALL_SIGHTS_MODE -> All
                AWARD_SIGHTS_MODE -> Award
                else -> throw IllegalArgumentException(
                    "Invalid mode: $value. Expected one of: $CHOSEN_SIGHT_MODE, $ALL_SIGHTS_MODE, $AWARD_SIGHTS_MODE"
                )
            }
        }
    }
}