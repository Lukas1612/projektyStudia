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
import kotlinx.coroutines.flow.Flow

interface SightsRepository {

    /**
     * Returns available Sights
     */
    fun getAll(): Flow<List<Sight>>
    /**
     * Returns available Sights of a given type
     */
    fun getByType(type: String): Flow<List<Sight>>
    /**
     * Returns Sights with specified ids
     */
    fun getById(ids: Set<String>): Flow<List<Sight>>

    /**
     * Returns the Sight with specified id
     */
    fun getById(ids: String): Flow<Sight>

    /**
     * Insert sights into database
     */
    suspend fun addSights(sights: List<Sight>)

}