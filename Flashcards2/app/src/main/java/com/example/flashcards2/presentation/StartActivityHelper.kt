package com.example.flashcards2.presentation

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.example.flashcards2.presentation.Constants.FLASHCARD_ID_KEY
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import com.example.flashcards2.presentation.feature_create_edit_group.CreateEditGroupActivity
import com.example.flashcards2.presentation.feature_create_edit_flashcard.FlashcardCreatorActivity

class StartActivityHelper {
    fun startFlashcardCreatorActivity(activity: FragmentActivity?, flashcardId: Long, groupId: Long ){
        val intent = Intent(
            activity,
            FlashcardCreatorActivity::class.java
        )

        intent.putExtra(FLASHCARD_ID_KEY, flashcardId)
        intent.putExtra(GROUP_ID_KEY, groupId)

        activity?.startActivity(intent)
    }

    fun startFlashcardCreatorActivity(activity: Activity?, flashcardId: Long, groupId: Long ){
        val intent = Intent(
            activity,
            FlashcardCreatorActivity::class.java
        )

        intent.putExtra(FLASHCARD_ID_KEY, flashcardId)
        intent.putExtra(GROUP_ID_KEY, groupId)

        activity?.startActivity(intent)
    }

    fun startGroupCreatorActivity(activity: Activity?){

        val intent = Intent(
            activity,
            CreateEditGroupActivity::class.java
        )

        activity?.startActivity(intent)
    }

    fun startGroupCreatorActivity(activity: FragmentActivity?){
        val intent = Intent(
            activity,
            CreateEditGroupActivity::class.java
        )

        activity?.startActivity(intent)
    }
}