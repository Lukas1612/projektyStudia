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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.location_tracking.long_range_location_tracking.LongRangeLocationTrackingInitializer
import com.google.samples.apps.nowinandroid.core.domain.sights.SightseeingUseCases
import com.google.samples.apps.nowinandroid.core.model.data.sight.Sight
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SightsListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val useCases: SightseeingUseCases,
    private val longRangeLocationTrackingInitializer: LongRangeLocationTrackingInitializer
) : ViewModel() {

    //********************
    val sampleSights = listOf(
        Sight(
            id = "1",
            imageResId = R.drawable.colosseum, // Replace with actual drawable
            name = "Colosseum",
            description = "An ancient Roman amphitheater in the heart of Rome.",
            yearOfCreation = "80 AD",
            type = "monuments",
            latitude = 41.8902,
            longitude = 12.4922
        ),
        Sight(
            id = "2",
            imageResId = R.drawable.central_park, // Replace with actual drawable
            name = "Central Park",
            description = "A large public park in New York City.",
            yearOfCreation = "1857",
            type = "parks_and_places",
            latitude = 40.7851,
            longitude = -73.9683
        ),
        Sight(
            id = "3",
            imageResId = R.drawable.vietnam_memorial, // Replace with actual drawable
            name = "Vietnam Veterans Memorial",
            description = "A memorial honoring U.S. service members who fought in the Vietnam War.",
            yearOfCreation = "1982",
            type = "memorials",
            latitude = 38.8913,
            longitude = -77.0477
        ),
        Sight(
            id = "4",
            imageResId = R.drawable.berlin_wall, // Replace with actual drawable
            name = "Berlin Wall Remnants",
            description = "A historic remnant of the Berlin Wall built during the Cold War.",
            yearOfCreation = "1961",
            type = "post_war_architecture",
            latitude = 52.5163,
            longitude = 13.3777
        ),
        Sight(
            id = "5",
            imageResId = R.drawable.eiffel_tower, // Replace with actual drawable
            name = "Eiffel Tower",
            description = "An iconic Parisian monument built for the 1889 World's Fair.",
            yearOfCreation = "1889",
            type = "monuments",
            latitude = 48.8584,
            longitude = 2.2945
        ),
        Sight(
            id = "6",
            imageResId = R.drawable.hyde_park, // Replace with actual drawable
            name = "Hyde Park",
            description = "One of London's largest and most famous parks.",
            yearOfCreation = "1637",
            type = "parks_and_places",
            latitude = 51.5074,
            longitude = -0.1657
        ),
        Sight(
            id = "7",
            imageResId = R.drawable.great_wall, // Replace with actual drawable
            name = "Great Wall of China",
            description = "A historic fortification stretching thousands of miles across northern China.",
            yearOfCreation = "7th century BC",
            type = "monuments",
            latitude = 40.4319,
            longitude = 116.5704
        )
    )
    //********************

    private val _state = MutableStateFlow(SightListState())
    val state: StateFlow<SightListState> = _state

    private val mode: ListMode = ListMode.fromString(savedStateHandle[SIGHT_TYPE])

    private var getSightsJob: Job? = null

    init {
        viewModelScope.launch {
            useCases.addSightsUseCase(sampleSights)
        }
        loadSights()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startService(context: Context) {

        longRangeLocationTrackingInitializer.startLocationTracking(
            context,
            viewModelScope
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stopService(context: Context) {
        longRangeLocationTrackingInitializer.stopLocationTracking(context)
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