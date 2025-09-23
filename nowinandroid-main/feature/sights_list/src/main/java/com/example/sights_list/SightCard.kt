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

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sight_map.Constants.CHOSEN_SIGHT_MODE
import com.example.sight_map.MapData
import com.google.samples.apps.nowinandroid.core.model.data.sight.UserSight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.ui.Alignment

@Composable
fun SightCard(
    userSight: UserSight,
    onToggleBookmark: () -> Unit,
    onLocationToggled: (MapData) -> Unit,
    onRouteToggled: (Double, Double) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle card click if needed */ },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column {
          /*  Image(
                painter = painterResource(id = userSight.sight.imageResId),
                contentDescription = userSight.sight.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
            )*/

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = userSight.sight.imageResId),
                    contentDescription = userSight.sight.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                VisitedStatusIndicator(
                    isVisited = userSight.isVisited,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }



            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = userSight.sight.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Text(
                text = userSight.sight.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )

            val buttonModifier = Modifier
                .height(48.dp) // or use `.defaultMinSize(minHeight = 48.dp)`
                .weight(1f)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    onClick = onToggleBookmark,
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (userSight.isBookmarked) MaterialTheme.colorScheme.primary
                        else Color.LightGray,
                        contentColor = if (!userSight.isBookmarked) Color.White
                        else Color.DarkGray,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = if (userSight.isBookmarked) "unBookmark" else "Bookmark"
                    )
                }

               // VisitedStatusIndicator(userSight.isVisited)

                Button(
                    onClick = {
                        onLocationToggled(
                            MapData(
                                latitude = userSight.sight.latitude,
                                longitude = userSight.sight.longitude,
                                mode = CHOSEN_SIGHT_MODE,
                                selectedSightId = userSight.sight.id,
                            ),
                        )
                    },
                    modifier = buttonModifier,
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "show on map"
                    )
                }

                Button(
                    onClick = {
                    Log.d("Route", "toggled")
                    onRouteToggled(userSight.sight.latitude,  userSight.sight.longitude)
                              },
                    modifier = buttonModifier,
                    )
                {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "show a route"
                    )
                }
            }
        }
    }
}