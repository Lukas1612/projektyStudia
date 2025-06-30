package com.example.flashcards2.presentation.feature_create_edit_flashcard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.flashcards2.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.flashcards2.presentation.feature_create_edit_flashcard.helpers.PopUpWindowHelper
import com.example.flashcards2.presentation.feature_create_edit_flashcard.interfaces.OnChooseGroupClickListener
import com.example.flashcards2.presentation.feature_create_edit_flashcard.interfaces.OnCreateGroupClickListener
import com.example.flashcards2.presentation.feature_create_edit_flashcard.interfaces.OnSaveButtonClickListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlashcardCreatorActivity : AppCompatActivity() {

    private lateinit var editTextWord: EditText
    private lateinit var editTextTranslation: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var groupImageView: ImageView
    private lateinit var groupNameTextView: TextView
    private lateinit var groupNameLayout: LinearLayout


    private val viewModel: FlashcardCreatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flashcard_creator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextWord = findViewById(R.id.editTextWord)
        editTextTranslation = findViewById(R.id.editTextTranslation)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        groupImageView = findViewById(R.id.groupImageView)
        groupNameTextView = findViewById(R.id.groupNameTextView)

        groupNameLayout = findViewById(R.id.groupNameLayout)


        saveButton.setOnClickListener{
            viewModel.onEvent(FlashcardCreatorEvent.EnteredWord(editTextWord.text.toString()))
            viewModel.onEvent(FlashcardCreatorEvent.EnteredTranslation(editTextTranslation.text.toString()))
            viewModel.onEvent(FlashcardCreatorEvent.SaveFlashcard)
        }

        deleteButton.setOnClickListener{
            viewModel.onEvent(FlashcardCreatorEvent.DeleteFlashcard)
        }

        groupNameLayout.setOnClickListener {
            viewModel.onEvent(FlashcardCreatorEvent.SelectedExpandAllGroupsList)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){

                viewModel.flashcardWordState.collectLatest {

                    editTextWord.hint = it.hint
                    editTextWord.setText(it.text)
                }
            }
        }


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
               viewModel.flashcardTranslationState.collectLatest {
                   editTextTranslation.hint = it.hint
                   editTextTranslation.setText(it.text)
               }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.flashcardGroupState.collectLatest {
                     groupNameTextView.text = it.selectedGroup!!.name

                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.buttonsState.collectLatest {
                    deleteButton.isVisible = it.isDeleteButtonVisible
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is FlashcardCreatorViewModel.UiEvent.ShowCreateGroupPopUp -> {
                            showCreateGroupPopUp()
                        }
                        is FlashcardCreatorViewModel.UiEvent.ShowAllGroupsPopUp -> {
                            showAllGroupsPopUp()
                        }

                        is FlashcardCreatorViewModel.UiEvent.ShowFillAllFieldsToast -> {
                            showFillAllFieldsToast()
                        }

                        is FlashcardCreatorViewModel.UiEvent.FinishActivity -> {
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun showFillAllFieldsToast() {
        Toast.makeText(applicationContext,
            "fill out all fields",
            Toast.LENGTH_LONG).show();
    }

    private fun showCreateGroupPopUp() {

        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        PopUpWindowHelper().showGroupCreationWindow(
            inflater,
            object : OnSaveButtonClickListener {
                override fun onClick(groupName: String) {
                    viewModel.onEvent(FlashcardCreatorEvent.SaveFlashcardGroup(groupName))
                }
            }
        )
    }

    private fun showAllGroupsPopUp() {
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val items = viewModel.flashcardGroupState.value.items
        PopUpWindowHelper().showSelectableGroupsWindow(
            inflater,
            items,
            object : OnChooseGroupClickListener {
                override fun onClick(groupName: String) {
                    viewModel.onEvent(FlashcardCreatorEvent.SelectedFlashcardGroup(groupName))
                }
            },
            object : OnCreateGroupClickListener{
                override fun onClick() {
                    viewModel.onEvent(FlashcardCreatorEvent.OpenFlashcardGroupCreator)
                }
            }
        )
    }

}
