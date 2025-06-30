package com.example.flashcards2.presentation.feature_create_edit_flashcard.helpers

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards2.R
import com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters.FlashcardGroupsRecyclerViewAdapter
import com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters.GroupsAdapterListItem
import com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters.interfaces.OnFlashcardGroupItemClickListener
import com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters.interfaces.OnGroupCreatorItemClickListener
import com.example.flashcards2.presentation.feature_create_edit_flashcard.interfaces.OnChooseGroupClickListener
import com.example.flashcards2.presentation.feature_create_edit_flashcard.interfaces.OnCreateGroupClickListener
import com.example.flashcards2.presentation.feature_create_edit_flashcard.interfaces.OnSaveButtonClickListener

/*
TO DO:
  - createDefaultPopupWindow(): przetestuj czy działa onBackPress i kliknięcie poza oknem
  -   popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
 */
class PopUpWindowHelper {

    @SuppressLint("ClickableViewAccessibility")
    fun showGroupCreationWindow(inflater: LayoutInflater, onSaveButtonClickListener: OnSaveButtonClickListener){

        val popupView: View = inflater.inflate(R.layout.popup_create_a_new_group_window, null)
        val groupNameEditText: EditText = popupView.findViewById(R.id.groupNameEditText)
        val saveButton: Button = popupView.findViewById(R.id.saveButton)

        val popupWindow = createDefaultPopupWindow(popupView)

        saveButton.setOnClickListener {
            val groupName = groupNameEditText.text.toString()
            onSaveButtonClickListener.onClick(groupName)

            popupWindow.dismiss()
        }


        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun showSelectableGroupsWindow(
        inflater: LayoutInflater,
        items: List<GroupsAdapterListItem>,
        onChooseGroupClickListener: OnChooseGroupClickListener,
        onCreateGroupClickListener: OnCreateGroupClickListener
    ) {

        val popupView: View = inflater.inflate(R.layout.popup_selectable_groups_window, null)


        val selectableGroupsRecyclerView: RecyclerView =
            popupView.findViewById(R.id.selectableGroupsRecyclerView)

        selectableGroupsRecyclerView.layoutManager = LinearLayoutManager(inflater.context)

        val popupWindow = createDefaultPopupWindow(popupView)

        val adapter = flashcardGroupsRecyclerViewAdapter(
            items,
            popupWindow,
            onChooseGroupClickListener,
            onCreateGroupClickListener
        )

        selectableGroupsRecyclerView.adapter = adapter

        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    private fun flashcardGroupsRecyclerViewAdapter(
        items: List<GroupsAdapterListItem>,
        popupWindow: PopupWindow,
        onChooseGroupClickListener: OnChooseGroupClickListener,
        onCreateGroupClickListener: OnCreateGroupClickListener
    ): FlashcardGroupsRecyclerViewAdapter {
        val adapter = FlashcardGroupsRecyclerViewAdapter(
            items,
            object : OnFlashcardGroupItemClickListener {
                override fun onClick(groupName: String) {
                    onChooseGroupClickListener.onClick(groupName)
                    popupWindow.dismiss()
                }

            },
            object : OnGroupCreatorItemClickListener {
                override fun onClick() {
                    onCreateGroupClickListener.onClick()
                    popupWindow.dismiss()
                }
            }
        )
        return adapter
    }


    //**** close Window when back button pressed or when clicked outside of the window ***
    private fun createDefaultPopupWindow(view: View): PopupWindow {

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable: Boolean = true

        PopupWindow()
        val popupWindow = PopupWindow(view, width, height, focusable)
        popupWindow.isOutsideTouchable = false
        popupWindow.isTouchable = true

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.GRAY))

        return popupWindow
    }


}