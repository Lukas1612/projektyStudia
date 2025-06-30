package com.example.flashcards2.presentation.notification

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import kotlinx.coroutines.guava.await
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit


class DailyReminderBuilder(val context: Context) {

    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
        val now = LocalDateTime.now()
        val targetTime = now.toLocalDate().atTime(hour, minute)

        val nextRunTime = if (now >= targetTime) {
            targetTime.plusDays(1)
        } else {
            targetTime
        }

        return Duration.between(now, nextRunTime).toMinutes()
    }

    fun build(hour: Int, minute: Int, inputData: Data){
        val groupId = inputData.getLong(GROUP_ID_KEY, -1L).toString()

        val delayMinutes = calculateInitialDelay(hour, minute)

        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            groupId,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    suspend fun isWorkerScheduled(context: Context, groupId: String): Boolean {

        val workInfos = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork(groupId)
            .await()

        return workInfos.any { workInfo ->
            workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.RUNNING
        }
    }

    fun cancel(groupId: String){
        WorkManager.getInstance(context)
            .cancelUniqueWork(groupId)
    }
}