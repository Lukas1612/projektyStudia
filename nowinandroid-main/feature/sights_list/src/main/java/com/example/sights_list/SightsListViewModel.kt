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

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.domain.sights.SightseeingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SightsListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val useCases: SightseeingUseCases,
   // private val longRangeLocationTrackingInitializer: LongRangeLocationTrackingInitializer
) : ViewModel() {

    private val _state = MutableStateFlow(SightListState())
    val state: StateFlow<SightListState> = _state

    private val mode: ListMode = ListMode.fromString(savedStateHandle[SIGHT_TYPE])

    private var getSightsJob: Job? = null

    init {
        loadSights()
    }


   private fun loadSights(){
        return when (mode) {
            is ListMode.All -> getAllSights()
            is ListMode.Bookmarked -> getBookmarkedSights()
            is ListMode.Visited -> getVisitedSights()
            is ListMode.OfAGivenType -> getSightsByType(mode.type)
        }
    }

    suspend fun bookmarkSight(sightId: String) {
        _state.value.userSights
            .find { it.sight.id == sightId }
            .also{ sight ->
                if(sight!!.isBookmarked){
                    useCases.bookmarkSightUseCase(sightId, false)
                }else{
                    useCases.bookmarkSightUseCase(sightId, true)
                }
            }

    }

    private fun getBookmarkedSights(){
        getSightsJob?.cancel()
        getSightsJob = useCases.getBookmarkedSightsUseCase()
            .onEach { updatedList ->
                _state.update { previousView ->
                    previousView.copy(
                        userSights = updatedList
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun getVisitedSights(){
        getSightsJob?.cancel()
        getSightsJob = useCases.getSightsByIsVisitedValueUseCase(true)
            .onEach { updatedList ->
                _state.update { previousView ->
                    previousView.copy(
                        userSights = updatedList
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun getAllSights(){
        getSightsJob?.cancel()
        getSightsJob = useCases.getAllSightsUseCase()
            .onEach { updatedList ->
                _state.update { previousView ->
                    previousView.copy(
                        userSights = updatedList
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun getSightsByType(sighType: String){
        getSightsJob?.cancel()
        getSightsJob = useCases.getSightsOfAGivenTypeUseCase(sighType)
            .onEach { updatedList ->
                _state.update { previousView ->
                    previousView.copy(
                        userSights = updatedList
                    )
                }
            }.launchIn(viewModelScope)
    }
}

const val SIGHT_TYPE = "sight_type"