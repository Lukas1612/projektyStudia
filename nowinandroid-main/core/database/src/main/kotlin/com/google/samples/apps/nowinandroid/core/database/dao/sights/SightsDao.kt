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

package com.google.samples.apps.nowinandroid.core.database.dao.sights

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.samples.apps.nowinandroid.core.database.model.sight.SightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SightsDao {

    @Query("SELECT * FROM sights WHERE type = :type")
    fun getSightEntitiesByType(type: String) : Flow<List<SightEntity>>

    @Query("SELECT * FROM sights")
    fun getSightEntities() : Flow<List<SightEntity>>

    @Query("SELECT * FROM sights WHERE id IN (:ids)")
    fun getSightEntitiesById(ids: List<String>) : Flow<List<SightEntity>>

    @Query("SELECT * FROM sights WHERE id = :id")
    fun getSightEntityById(id: String) : Flow<SightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSights(items: List<SightEntity>)
}