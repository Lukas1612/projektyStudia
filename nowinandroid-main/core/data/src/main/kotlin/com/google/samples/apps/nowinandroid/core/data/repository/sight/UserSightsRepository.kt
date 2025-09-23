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

import com.google.samples.apps.nowinandroid.core.model.data.sight.Sight
import com.google.samples.apps.nowinandroid.core.model.data.sight.UserSight
import kotlinx.coroutines.flow.Flow

interface UserSightsRepository {

    /**
     * Gets the available sights as a stream
     */
    fun getAll(): Flow<List<UserSight>>

    /**
     * Gets sight of a given id as a stream
     */
    fun getSightById(id: String): Flow<UserSight>

    /**
     * Gets the available sights of a given type as a stream
     */
    fun getSightsByType(type: String): Flow<List<UserSight>>

    /**
     * Gets sights bookmarked by the user as a stream
     */
    fun getBookmarkedSights(): Flow<List<UserSight>>

    /**
     * Gets sights visited by the user as a stream
     */
    fun getVisitedSights(): Flow<List<UserSight>>
}