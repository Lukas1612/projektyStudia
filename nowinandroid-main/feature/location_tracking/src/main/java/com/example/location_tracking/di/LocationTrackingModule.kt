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

package com.example.location_tracking.di

import com.example.location_tracking.close_range_location_tracking.CloseRangeLocationClient
import com.example.location_tracking.close_range_location_tracking.CloseRangeLocationService
import com.example.location_tracking.close_range_location_tracking.DefaultCloseRangeLocationClient
import com.example.location_tracking.data.DefaultLocationTrackingDataProvider
import com.example.location_tracking.data.LocationTrackingDataProvider
import com.example.location_tracking.long_range_location_tracking.GeofencesInitializerLongRange
import com.example.location_tracking.long_range_location_tracking.LongRangeLocationTrackingInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationTrackingModule {

    @Binds
    @Singleton
    abstract fun bindLongRangeInitializer(
        geofencesInitializerLongRange: GeofencesInitializerLongRange
    ): LongRangeLocationTrackingInitializer

    @Binds
    abstract fun bindLocationTrackingDataProvider(
        defaultLocationTrackingDataProvider: DefaultLocationTrackingDataProvider
    ): LocationTrackingDataProvider

    @Binds
    abstract fun bindCloseRangeLocationClient(
        defaultCloseRangeLocationClient: DefaultCloseRangeLocationClient
    ): CloseRangeLocationClient

    @Binds
    abstract fun bindCloseRangeLocationService(
        closeRangeLocationService: CloseRangeLocationService
    ): CloseRangeLocationService

}