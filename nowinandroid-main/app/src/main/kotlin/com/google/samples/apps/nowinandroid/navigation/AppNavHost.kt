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

package com.google.samples.apps.nowinandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.home_screen.HomeScreen
import com.example.route_map.Constants.DESTINATION_LATITUDE_KEY
import com.example.route_map.Constants.DESTINATION_LONGITUDE_KEY
import com.example.sights_list.SightsList
import com.example.route_map.RouteMap
import com.example.sight_map.Constants.ALL_SIGHTS_MODE
import com.example.sight_map.Constants.MAP_MODE_KEY
import com.example.sight_map.MapData
import com.example.sights_list.SightType.ALL_SIGHTS
import com.example.sights_list.SightsListViewModel
import com.example.sights_list.SIGHT_TYPE
import com.example.sights_list.SightType.BOOKMARKED_SIGHTS
import com.example.sights_list.SightType.VISITED_SIGHTS

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onStartMapActivity: (MapData) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToSightsList = { navController.navigate("sight_list/$ALL_SIGHTS") },

                onNavigateToMapOfSights =  {
                    onStartMapActivity(
                        MapData(
                            mode = ALL_SIGHTS_MODE
                        )
                    ) },

                onNavigateToFavourites = {navController.navigate("sight_list/$BOOKMARKED_SIGHTS")},
                onNavigateToVisited = {navController.navigate("sight_list/$VISITED_SIGHTS")},
                )
        }
        composable(
            "sight_list/{sight_type}",
            arguments = listOf(
                navArgument(SIGHT_TYPE) { type = NavType.StringType }
            )
        ) {backStackEntry ->

            val viewModel: SightsListViewModel = hiltViewModel()

            SightsList(
                onBack = {
                navController.popBackStack()
            },
                onNavigateToMapScreen = onStartMapActivity,
                onNavigateToRouteMap = { lat, lng ->
                    navController.navigate("route_map/$lat/$lng")
                },
            )
        }

        composable(
            route = "route_map/{$DESTINATION_LATITUDE_KEY}/{$DESTINATION_LONGITUDE_KEY}",
            arguments = listOf(
                navArgument(DESTINATION_LATITUDE_KEY) { type = NavType.StringType },
                navArgument(DESTINATION_LONGITUDE_KEY) { type = NavType.StringType }
            )
        ) {
            RouteMap(
                onBack = {
                    navController.popBackStack()
                })
        }


        composable(
            "sights_map/{$MAP_MODE_KEY}",
            arguments = listOf(
                navArgument(MAP_MODE_KEY) { type = NavType.StringType }
            )
        ) {backStackEntry ->

            val viewModel: SightsListViewModel = hiltViewModel()

            SightsList(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToMapScreen = onStartMapActivity,
                onNavigateToRouteMap = { lat, lng ->
                    navController.navigate("route_map/$lat/$lng")
                },
            )
        }
    }
}