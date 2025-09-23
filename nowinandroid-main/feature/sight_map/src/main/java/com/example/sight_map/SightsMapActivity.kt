package com.example.sight_map

import android.Manifest
import android.content.pm.PackageManager
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@AndroidEntryPoint
class SightsMapActivity : AppCompatActivity() {
    lateinit var map: MapView

    private val viewModel: SightMapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(applicationContext, getSharedPreferences("osm_prefs", MODE_PRIVATE))

        setContentView(R.layout.feature_sight_map_activity_sights_map)

        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val mapController = map.controller
        mapController.setZoom(18.0)

        val lat = viewModel.initialLatitude
        val lng = viewModel.initialLongitude
        mapController.setCenter(GeoPoint(lat, lng))


        setUserLocation()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED)
            {
                viewModel.getSights().collectLatest { sights ->

                    sights.onEach { sight ->
                        val sLat = sight.latitude
                        val sLng = sight.longitude
                        val sTitle = sight.name
                        val sDescription = sight.description
                        addMarker(sLat,sLng, sTitle, sDescription)
                    }
                }
            }
        }
    }

    private fun setUserLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
            locationOverlay.enableMyLocation()
           //locationOverlay.enableFollowLocation()
            map.overlays.add(locationOverlay)
        }
    }

    private fun addMarker(lat: Double, lon: Double, title: String, description: String) {
        val marker = Marker(map)
        marker.position = GeoPoint(lat, lon)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title
        marker.subDescription = description

        marker.infoWindow = CustomInfoWindow(map)
        map.overlays.add(marker)

        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

}