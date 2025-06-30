package com.example.flashcards2.presentation.feature_flashcard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards2.domain.model.Flashcard
import com.example.flashcards2.domain.scheduler.card_state_updater.Rating
import com.example.flashcards2.domain.scheduler.Scheduler
import com.example.flashcards2.domain.use_case.col.ColUseCases
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FlashcardActivityViewModel @Inject constructor(
    private val flashcardUseCases: FlashcardUseCases,
    private val colUseCases: ColUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

     val FLASHCARD_FRONT = 0
     val FLASHCARD_BACK = 1


    private val _state = MutableStateFlow(FlashcardActivityState())
    val state: StateFlow<FlashcardActivityState> = _state

    private val _buttonsState = MutableStateFlow(ButtonsTextState())
    val buttonsState: StateFlow<ButtonsTextState> = _buttonsState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var curFlashcardSide = FLASHCARD_FRONT

    private var scheduler: Scheduler? = null

    var groupId: Long? = null
    var creationTimestampSeconds: Long? = null

    init {
        savedStateHandle.get<Long>(GROUP_ID_KEY)?.let { id ->
            groupId = id
        }

        viewModelScope.launch {
            colUseCases.getCol().collectLatest { col ->

                if(col.isNotEmpty()){
                    creationTimestampSeconds = col.get(0).creationTimestampSeconds
                    scheduler = Scheduler(flashcardUseCases, col[0].creationTimestampSeconds, groupId!!)
                    scheduler?.get_queued_cards()

                    scheduler?.get_next_card()?.let { flashcard ->
                        showFlashcardFront(flashcard)
                    }
                }else{
                    throw IllegalArgumentException("collection table is empty")
                }
            }
        }
    }


    fun onEvent(event: FlashcardActivityEvent){
        when(event){

            is FlashcardActivityEvent.ClickedFlipFlashcard -> {
                viewModelScope.launch {
                    getCurFlashcard()?.let { flashcard ->
                        showFlashcardBack(flashcard)
                    }
                }
            }

            is FlashcardActivityEvent.ClickedRateButton -> {

                viewModelScope.launch {
                    when(event.buttonType){
                        is ButtonType.EasyButton -> {
                            rateFlashcard(5)
                        }

                        is ButtonType.HardButton -> {
                            rateFlashcard(3)
                        }
                        is ButtonType.GoodButton -> {
                            rateFlashcard(4)
                        }
                        is ButtonType.RepeatButton -> {
                            rateFlashcard(0)
                        }
                    }

                    if(!nextFlashcard()){
                        finishActivity()
                    }
                }
            }
        }
    }

    private fun showFlashcardFront(flashcard: Flashcard){

        curFlashcardSide = FLASHCARD_FRONT

        _state.update { previousState ->
            previousState.copy(
                text = flashcard.front,
                flipButtonVisibility = true,
                rateButtonsVisibility = false
            )
        }
    }

    private fun showFlashcardBack(flashcard: Flashcard){

        viewModelScope.launch {
            curFlashcardSide = FLASHCARD_BACK

            scheduler?.get_scheduling_states(flashcard)
                ?.asString()
                ?.let { strings ->
                    showButtonsDueTimeIntervals(strings)
                }

            _state.update { previousState ->
                previousState.copy(
                    text = flashcard.back,
                    flipButtonVisibility = false,
                    rateButtonsVisibility = true
                )
            }
        }
    }


    private fun showButtonsDueTimeIntervals(nextStates: List<String>){


        _buttonsState.update { previousState ->
            previousState.copy(
                easyButtonText = nextStates[3],
                goodButtonText = nextStates[2],
                hardButtonText = nextStates[1],
                againButtonText = nextStates[0]
            )
        }

    }

    private suspend fun nextFlashcard(): Boolean{
        val flashcard = scheduler?.get_next_card()
        if(flashcard == null){
            return false
        }else{

            showFlashcardFront(flashcard)
            return true
        }
    }

    private suspend fun rateFlashcard(qualityRate: Int) = withContext(Dispatchers.IO) {

        getCurFlashcard()?.let { card ->

            when(qualityRate){
                0 -> {
                    scheduler?.answer_card(card, Rating.Again)
                }
                3 -> {
                    scheduler?.answer_card(card, Rating.Hard)
                }
                4 -> {
                    scheduler?.answer_card(card, Rating.Good)
                }
                5 -> {
                    scheduler?.answer_card(card, Rating.Easy)
                }
                else -> {
                    null
                }
            }

        }
    }

    private fun finishActivity(){
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.FinishActivity
            )
        }
    }

    private suspend fun getCurFlashcard(): Flashcard?{
        return scheduler?.get_next_card()
    }


    sealed class UiEvent{
        object ShowRateFlashcardAlert: UiEvent()
        object FinishActivity: UiEvent()
    }
}