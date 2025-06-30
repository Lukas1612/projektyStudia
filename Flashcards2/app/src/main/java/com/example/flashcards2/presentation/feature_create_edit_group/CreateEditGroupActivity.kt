package com.example.flashcards2.presentation.feature_create_edit_group

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.flashcards2.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateEditGroupActivity : AppCompatActivity() {

    lateinit var groupNameEditText: EditText
    lateinit var saveButton: Button

    private val viewModel: CreateEditGroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_edit_group)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        saveButton = findViewById(R.id.saveButton)
        groupNameEditText = findViewById(R.id.groupNameEditText)

        groupNameEditText.hint = viewModel.getHint()
        groupNameEditText.setText("")

        saveButton.setOnClickListener {
            viewModel.onEvent(CreateEditGroupEvent.SaveGroup(groupNameEditText.text.toString()))
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){

                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is CreateEditGroupViewModel.UiEvent.FinishActivity -> {
                            finish()
                        }
                        is CreateEditGroupViewModel.UiEvent.ShowFillAllFieldsToast -> {
                            showFillAllFieldsToast()
                        }
                    }
                }
            }
        }
    }

    private fun showFillAllFieldsToast() {
        Toast.makeText(applicationContext,
            "fill out the group name",
            Toast.LENGTH_LONG).show();
    }

}