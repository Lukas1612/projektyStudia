package com.example.flashcards2.presentation.feature_flashcard

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.workDataOf
import com.example.flashcards2.R
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.permissions.PermissionHelper
import com.example.flashcards2.presentation.notification.Constants.TIME_KEY
import com.example.flashcards2.presentation.notification.DailyReminderBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlashcardActivity : AppCompatActivity() {

    private lateinit var flashcardTextView: TextView
    private lateinit var flipButton2: Button

    private lateinit var rateButtonsLayout: LinearLayout

    private lateinit var againButton: Button
    private lateinit var hardButton: Button
    private lateinit var goodButton: Button
    private lateinit var easyButton: Button

    private lateinit var permissionHelper: PermissionHelper


    private val viewModel: FlashcardActivityViewModel by viewModels()
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        permissionHelper = PermissionHelper(this, this)
        lifecycle.addObserver(permissionHelper)

        rateButtonsLayout = findViewById(R.id.rateButtonsLayout)

        flashcardTextView = findViewById(R.id.flashcardTextView)
        flipButton2 = findViewById(R.id.flipButton)

        flipButton2.setOnClickListener {
            viewModel.onEvent(FlashcardActivityEvent.ClickedFlipFlashcard)
        }

        againButton = findViewById(R.id.againButton)
        hardButton = findViewById(R.id.hardButton)
        goodButton = findViewById(R.id.goodButton)
        easyButton = findViewById(R.id.easyButton)


        againButton.setOnClickListener {
            viewModel.onEvent(FlashcardActivityEvent.ClickedRateButton(ButtonType.RepeatButton))
        }

        hardButton.setOnClickListener {
            viewModel.onEvent(FlashcardActivityEvent.ClickedRateButton(ButtonType.HardButton))
        }

        goodButton.setOnClickListener {
            viewModel.onEvent(FlashcardActivityEvent.ClickedRateButton(ButtonType.GoodButton))
        }

        easyButton.setOnClickListener {
            viewModel.onEvent(FlashcardActivityEvent.ClickedRateButton(ButtonType.EasyButton))
        }


        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.state.collectLatest {
                    flashcardTextView.text = it.text
                    rateButtonsLayout.isVisible = it.rateButtonsVisibility
                    flipButton2.isVisible = it.flipButtonVisibility
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.buttonsState.collectLatest {

                    againButton.text = it.againButtonText
                    hardButton.text = it.hardButtonText
                    goodButton.text = it.goodButtonText
                    easyButton.text = it.easyButtonText
                }

            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.eventFlow.collectLatest { event ->
                    when(event){
                        is FlashcardActivityViewModel.UiEvent.FinishActivity -> {
                            finish()
                        }

                        is FlashcardActivityViewModel.UiEvent.ShowRateFlashcardAlert -> {
                            showRateFlashcardAlert()
                        }
                    }
                }

            }
        }

        startNotificationReminder()
    }

    private fun startNotificationReminder() {
        val worker = DailyReminderBuilder(applicationContext)

        val inputData = workDataOf(
            GROUP_ID_KEY to viewModel.groupId,
            TIME_KEY to viewModel.creationTimestampSeconds
        )

        lifecycleScope.launch(Dispatchers.Main) {
            if(permissionHelper.permissionsAccepted() && !worker.isWorkerScheduled(this@FlashcardActivity, viewModel.groupId.toString())){
                worker.build(18, 0, inputData)
            }
        }

    }

    private fun showRateFlashcardAlert() {
        Toast.makeText(applicationContext,
            "chose flashcard rate first",
            Toast.LENGTH_LONG).show();
    }
}