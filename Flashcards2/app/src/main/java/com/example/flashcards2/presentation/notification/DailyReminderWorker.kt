package com.example.flashcards2.presentation.notification

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.flashcards2.domain.scheduler.card_state_updater.IntervalCalculatorHelper
import com.example.flashcards2.domain.scheduler.queue.FlashcardsLoader
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import com.example.flashcards2.presentation.notification.Constants.TIME_KEY
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltWorker
class DailyReminderWorker  @AssistedInject constructor(
    val appContext: Context,
    params: WorkerParameters,
    private val flashcardUseCases: FlashcardUseCases
) : CoroutineWorker(appContext, params) {

    private val alarmManager = appContext.getSystemService(AlarmManager::class.java)

    override suspend fun doWork(): Result {

        Log.d("DailyReminderWorker", "doWork")


        val groupId = inputData.getLong(GROUP_ID_KEY, -1)
        if(groupId == -1L)  throw IllegalArgumentException("groupId hasn't been loaded successfully")

        val creationTimestampSeconds = inputData.getLong(TIME_KEY, -1)
        if(creationTimestampSeconds == -1L)  throw IllegalArgumentException("time hasn't been loaded successfully")

        CoroutineScope(Dispatchers.IO).launch {
            if(checkIfThereAreFlashcardsToLearnForToday(groupId, creationTimestampSeconds)){
                val service = FlashcardNotificationServiceImpl(appContext.applicationContext)
                service.showNotification(groupId)
            }
        }

        return Result.success()
    }


    suspend fun checkIfThereAreFlashcardsToLearnForToday(groupId: Long, creationTimestampSeconds: Long): Boolean{

        val daysSinceColCreation = IntervalCalculatorHelper().days_elapsed(creationTimestampSeconds)

        val flashcardsLoader = FlashcardsLoader(
            flashcardUseCases = flashcardUseCases,
            daysSinceColCreation = daysSinceColCreation,
            groupId = groupId
        )

        val flashcardListsHolder = flashcardsLoader.gather_cards()

        return flashcardListsHolder.isHolderNotEmpty()
    }
}

