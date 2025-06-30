package com.example.locationdescriptor.location_descriptor

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
    get() = Configuration.Builder()
        .setWorkerFactory(hiltWorkerFactory)
        .build()
}