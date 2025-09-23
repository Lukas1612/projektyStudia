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

package com.google.samples.apps.nowinandroid.core.database.model.sight

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.samples.apps.nowinandroid.core.model.data.sight.Sight

@Entity(
    tableName = "sights",
)

data class SightEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "image_res_id")
    val imageResId: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "year_Of_creation")
    val yearOfCreation: String,
    val type: String,
    val latitude: Double,
    val longitude: Double
)

fun SightEntity.asExternalModel() = Sight(
    id = id,
    imageResId = imageResId,
    name = name,
    description = description,
    yearOfCreation = yearOfCreation,
    type = type,
    latitude = latitude,
    longitude = longitude
)
