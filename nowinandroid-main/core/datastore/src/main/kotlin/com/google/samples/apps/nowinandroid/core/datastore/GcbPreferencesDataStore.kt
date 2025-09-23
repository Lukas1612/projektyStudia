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

package com.google.samples.apps.nowinandroid.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GcbPreferencesDataStore@Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {

    val userPreferencesFlow: Flow<UserPreferences> = userPreferences.data

    val bookmarkedSightsIds: Flow<Set<String>> = userPreferencesFlow
        .map { prefs ->
            prefs.bookmarkedSightsIdsMap.filterValues { it }.keys
        }
    val visitedSightsIds: Flow<Set<String>>  = userPreferencesFlow
        .map { prefs ->
            prefs.visitedSightsIdsMap.filterValues { it }.keys
        }

    suspend fun setSightBookmarked(sightId: String, bookmarked: Boolean){
        try {
            userPreferences.updateData {
                it.copy {
                    if (bookmarked) {
                        bookmarkedSightsIds.put(sightId, true)
                    } else {
                        bookmarkedSightsIds.remove(sightId)
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e("GcbPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setSightVisited(sightId: String, visited: Boolean){
        try {
            userPreferences.updateData {
                it.copy {
                    if(visited){
                        visitedSightsIds.put(sightId, true)
                    }else{
                        visitedSightsIds.remove(sightId)
                    }
                }
            }
        }catch (ioException: IOException){
            Log.e(" GcbPreferences", "Failed to update user preferences", ioException)
        }
    }
}