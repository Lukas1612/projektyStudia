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

package com.google.samples.apps.nowinandroid.core.data.repository.sight

import com.google.samples.apps.nowinandroid.core.datastore.GcbPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataSightsRepository @Inject constructor(
    private val gcbPreferencesDataStore: GcbPreferencesDataStore,
) : UserDataSightsRepository {

    override fun getBookmarkedSightsIds(): Flow<Set<String>> = gcbPreferencesDataStore.bookmarkedSightsIds

    override fun getVisitedSightsIds(): Flow<Set<String>>  = gcbPreferencesDataStore.visitedSightsIds

    override suspend fun setSightBookmarked(sightId: String, bookmarked: Boolean) {
        gcbPreferencesDataStore.setSightBookmarked(sightId, bookmarked)
    }

    override suspend fun setSightVisited(sightId: String, visited: Boolean) {
        gcbPreferencesDataStore.setSightVisited(sightId, visited)
    }
}