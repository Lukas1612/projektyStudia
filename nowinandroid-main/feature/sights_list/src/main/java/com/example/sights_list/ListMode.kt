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

package com.example.sights_list

import com.example.sights_list.SightType.ALL_SIGHTS
import com.example.sights_list.SightType.BOOKMARKED_SIGHTS
import com.example.sights_list.SightType.MEMORIALS
import com.example.sights_list.SightType.MONUMENTS
import com.example.sights_list.SightType.PARKS_AND_PLACES
import com.example.sights_list.SightType.POST_WAR_ARCHITECTURE
import com.example.sights_list.SightType.VISITED_SIGHTS

sealed class ListMode {
    object All : ListMode()
    object Visited : ListMode()
    object Bookmarked : ListMode()
    data class OfAGivenType(val type: String) : ListMode()

    companion object {
        fun fromString(value: String?): ListMode {
            return when (value) {
                BOOKMARKED_SIGHTS -> Bookmarked
                VISITED_SIGHTS -> Visited
                ALL_SIGHTS -> All
                MONUMENTS -> OfAGivenType(MONUMENTS)
                PARKS_AND_PLACES -> OfAGivenType(PARKS_AND_PLACES)
                MEMORIALS -> OfAGivenType(MEMORIALS)
                POST_WAR_ARCHITECTURE -> OfAGivenType(POST_WAR_ARCHITECTURE)
                else -> throw IllegalArgumentException(
                    "Invalid mode: $value. Expected one of: $BOOKMARKED_SIGHTS, $VISITED_SIGHTS, $ALL_SIGHTS, $MONUMENTS, $PARKS_AND_PLACES, $MEMORIALS, $POST_WAR_ARCHITECTURE"
                )
            }
        }
    }


}