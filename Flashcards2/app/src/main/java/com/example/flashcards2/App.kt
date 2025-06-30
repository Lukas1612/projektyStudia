package com.example.flashcards2

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.app.NotificationChannel
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.flashcards2.presentation.notification.FlashcardNotificationServiceImpl
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App: Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                FlashcardNotificationServiceImpl.FLASHCARD_CHANNEL_ID,
                "words repetition",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = "Used to remind about practicing a word"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


}