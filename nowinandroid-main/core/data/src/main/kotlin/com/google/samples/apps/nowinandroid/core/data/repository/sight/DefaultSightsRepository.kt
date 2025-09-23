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

import com.google.samples.apps.nowinandroid.core.data.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.database.dao.sights.SightsDao
import com.google.samples.apps.nowinandroid.core.database.model.sight.SightEntity
import com.google.samples.apps.nowinandroid.core.database.model.sight.asExternalModel
import com.google.samples.apps.nowinandroid.core.model.data.sight.Sight
import com.google.samples.apps.nowinandroid.core.network.Dispatcher
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class DefaultSightsRepository @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val sightsDao: SightsDao,
) : SightsRepository {

    override fun getAll(): Flow<List<Sight>> =
        sightsDao.getSightEntities()
            .map { it.map (SightEntity::asExternalModel ) }

    override fun getByType(type: String): Flow<List<Sight>> =
        sightsDao.getSightEntitiesByType(type)
            .map { it.map (SightEntity::asExternalModel ) }

    override fun getById(ids: Set<String>): Flow<List<Sight>> =
        sightsDao.getSightEntitiesById(ids.toList())
            .map { it.map(SightEntity::asExternalModel)  }

    override fun getById(id: String): Flow<Sight> =
        sightsDao.getSightEntityById(id)
            .map { it.asExternalModel() }

    override suspend fun addSights(sights: List<Sight>) {
        sightsDao.insertSights(sights.map { it.asExternalModel()})
    }
}