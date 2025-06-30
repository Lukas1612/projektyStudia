package com.example.flashcards2.presentation.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.flashcards2.R
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import com.example.flashcards2.presentation.feature_flashcard.FlashcardActivity

class FlashcardNotificationServiceImpl(
    private val context: Context
): FlashcardNotificationService {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showNotification(groupId: Long)
    {
        val activityIntent = Intent(context, FlashcardActivity::class.java)
        activityIntent.putExtra(GROUP_ID_KEY, groupId)


        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(context, FLASHCARD_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_book_24)
            .setAutoCancel(true)
            .setContentTitle("Repeat words")
            .setContentText("don't forget to repeat the words")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val FLASHCARD_CHANNEL_ID = "flashcard_channel"
    }
}