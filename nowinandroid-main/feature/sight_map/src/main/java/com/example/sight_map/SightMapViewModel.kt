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

package com.example.sight_map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sight_map.Constants.DEFAULT_INITIAL_LOCATION
import com.example.sight_map.Constants.INITIAL_LOCATION_LATITUDE_KEY
import com.example.sight_map.Constants.INITIAL_LOCATION_LONGITUDE_KEY
import com.example.sight_map.Constants.MAP_MODE_KEY
import com.example.sight_map.Constants.SELECTED_SIGHT_ID_KEY
import com.google.samples.apps.nowinandroid.core.domain.sights.SightseeingUseCases
import com.google.samples.apps.nowinandroid.core.model.data.sight.Sight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SightMapViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val useCases: SightseeingUseCases
) : ViewModel() {

    val initialLatitude: Double = savedStateHandle.get<Double>(INITIAL_LOCATION_LATITUDE_KEY) ?: DEFAULT_INITIAL_LOCATION.latitude//
    val initialLongitude: Double = savedStateHandle.get<Double>(INITIAL_LOCATION_LONGITUDE_KEY) ?: DEFAULT_INITIAL_LOCATION.longitude// default is the Cathedral

    private val mode: MapMode = MapMode.fromString(savedStateHandle[MAP_MODE_KEY])


    fun getSights(): Flow<List<Sight>> {
        return when (mode) {
            is MapMode.Chosen -> getChosenSight()
            is MapMode.All -> getAllSights()
            is MapMode.Award -> getAwardSights()
        }
    }

    private fun getChosenSight(): Flow<List<Sight>> = flow {
        val selectedSightId: String = requireNotNull(savedStateHandle[SELECTED_SIGHT_ID_KEY]) {
            "SELECTED_SIGHT_ID_KEY is missing in SavedStateHandle"
        }

        val sight = useCases.getSightByIdUseCase(selectedSightId).sight// suspending function call
        emit(listOf(sight))
    }

    private fun getAllSights(): Flow<List<Sight>> {
        return useCases.getAllSightsUseCase().map { it.map{ it.sight} }
    }

    private fun getAwardSights(): Flow<List<Sight>> {
        throw UnsupportedOperationException("Award sights mode is not implemented yet")
    }
}