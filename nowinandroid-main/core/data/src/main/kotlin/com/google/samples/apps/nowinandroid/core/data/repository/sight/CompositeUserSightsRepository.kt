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

import com.google.samples.apps.nowinandroid.core.model.data.sight.UserSight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CompositeUserSightsRepository @Inject constructor(
    val sightsRepository: SightsRepository,
    val userDataSightsRepository: UserDataSightsRepository
): UserSightsRepository {

    override fun getAll(): Flow<List<UserSight>>{
        val sightsFlow = sightsRepository.getAll()
        val visitedSightsIdsFlow = userDataSightsRepository.getVisitedSightsIds()
        val bookmarkedSightsIdsFlow = userDataSightsRepository.getBookmarkedSightsIds()

        return combine(sightsFlow, visitedSightsIdsFlow, bookmarkedSightsIdsFlow){ sights, visited, bookmarked ->
            sights.map { sight ->
                UserSight(
                    sight = sight,
                    isVisited = visited.contains(sight.id),
                    isBookmarked = bookmarked.contains(sight.id),
                )
            }
        }
    }

    override fun getSightById(id: String): Flow<UserSight>{

        val sightsFlow = sightsRepository.getById(id)
        val visitedSightsIdsFlow = userDataSightsRepository.getVisitedSightsIds()
        val bookmarkedSightsIdsFlow = userDataSightsRepository.getBookmarkedSightsIds()

        return combine(sightsFlow, visitedSightsIdsFlow, bookmarkedSightsIdsFlow){ sight, visited, bookmarked ->
            UserSight(
                sight = sight,
                isVisited = visited.contains(sight.id),
                isBookmarked = bookmarked.contains(sight.id),
            )
        }
    }

    override fun getSightsByType(type: String): Flow<List<UserSight>> {
        val sightsFlow = sightsRepository.getByType(type)
        val visitedSightsIdsFlow = userDataSightsRepository.getVisitedSightsIds()
        val bookmarkedSightsIdsFlow = userDataSightsRepository.getBookmarkedSightsIds()

        return combine(sightsFlow, visitedSightsIdsFlow, bookmarkedSightsIdsFlow){ sights, visited, bookmarked ->
            sights.map { sight ->
                UserSight(
                    sight = sight,
                    isVisited = visited.contains(sight.id),
                    isBookmarked = bookmarked.contains(sight.id),
                )
            }
        }
    }

    override fun getBookmarkedSights(): Flow<List<UserSight>> {
        val sightsFlow = userDataSightsRepository.getBookmarkedSightsIds()
            .flatMapLatest { bookmarkedSightsIds ->
                when{
                    bookmarkedSightsIds.isEmpty() -> flowOf(emptyList())
                    else -> sightsRepository.getById(bookmarkedSightsIds)
                }
            }
        val visitedSightsIdsFlow = userDataSightsRepository.getVisitedSightsIds()

        return combine(sightsFlow, visitedSightsIdsFlow){ sights, visited ->
            sights.map { sight ->
                UserSight(
                    sight = sight,
                    isVisited = visited.contains(sight.id),
                    isBookmarked = true
                )
            }
        }
    }

    override fun getVisitedSights(): Flow<List<UserSight>> {
        val sightsFlow = userDataSightsRepository.getVisitedSightsIds()
            .flatMapLatest { visitedSightsIds ->
                when {
                    visitedSightsIds.isEmpty() -> flowOf(emptyList())
                    else -> sightsRepository.getById(visitedSightsIds)
                }
            }
        val bookmarkedSightsIdsFlow = userDataSightsRepository.getBookmarkedSightsIds()

        return combine(sightsFlow, bookmarkedSightsIdsFlow){ sights, bookmarked ->
            sights.map { sight ->
                UserSight(
                    sight = sight,
                    isVisited = true,
                    isBookmarked = bookmarked.contains(sight.id)
                )
            }
        }
    }

}