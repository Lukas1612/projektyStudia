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

import android.util.Log
import com.google.samples.apps.nowinandroid.core.model.data.sight.UserSight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
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

    override suspend fun getSightById(id: String): UserSight{
        val sight = sightsRepository.getById(id)
        val visitedSightsIds = userDataSightsRepository.getVisitedSightsIds().first()
        val bookmarkedSightsIds = userDataSightsRepository.getBookmarkedSightsIds().first()

        return UserSight(
            sight = sight,
            isVisited = sight.id in visitedSightsIds,
            isBookmarked = sight.id in bookmarkedSightsIds
        )
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

    override fun getSightsByIsVisitedValue(isVisited: Boolean): Flow<List<UserSight>>{

         val allSightsFlow = sightsRepository.getAll()
         val visitedSightsIdsFlow = userDataSightsRepository.getVisitedSightsIds()
         val bookmarkedSightsIdsFlow = userDataSightsRepository.getBookmarkedSightsIds()

         return combine(allSightsFlow, visitedSightsIdsFlow, bookmarkedSightsIdsFlow){ all, visited, bookmarked ->
             if(isVisited){
                 all.filter { it.id in visited}
                     .map { sight ->
                         UserSight(
                             sight = sight,
                             isVisited = true,
                             isBookmarked = bookmarked.contains(sight.id)
                         )
                     }
             }else{
                 all.filterNot { it.id in visited}
                     .map { sight ->
                         UserSight(
                             sight = sight,
                             isVisited = false,
                             isBookmarked = bookmarked.contains(sight.id)
                         )
                     }
             }
         }
    }
}