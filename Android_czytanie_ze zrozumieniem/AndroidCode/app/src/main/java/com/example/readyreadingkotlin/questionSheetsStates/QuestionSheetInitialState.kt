package com.example.readyreadingkotlin.questionSheetsStates

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readyreadingkotlin.*

class QuestionSheetInitialState(
        var fullPassageViewManager: FullPassageViewManager?,
        var fullPassageStatesDataHolder: FullPassageStatesDataHolder?,
        var iQuestionSheetStateChanger: IQuestionSheetStateChanger
) : Mediator,  INextBack {
    var questionsBottomSheetAdapter: QuestionsBottomSheetAdapter? = null

        init{
              fullPassageViewManager?.fab?.isVisible = false

            if(fullPassageStatesDataHolder?.describedPassage?.questions != null)
            {
                questionsBottomSheetAdapter = QuestionsBottomSheetAdapter(null, fullPassageStatesDataHolder?.describedPassage?.questions!!,  fullPassageViewManager?.context!!)
                fullPassageViewManager?.questionsPassagesRecyclerView?.adapter = questionsBottomSheetAdapter
                fullPassageViewManager?.questionsPassagesRecyclerView?.layoutManager = LinearLayoutManager(fullPassageViewManager?.context!!)
            }

    }

    override fun next() {

        val layoutInflater: LayoutInflater = questionsBottomSheetAdapter!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        PopUpWindowBuilder().nextCancelPopUp(fullPassageViewManager?.questionsPassagesRecyclerView!!.rootView, layoutInflater, this)

    }

    override fun back() {
        // do nothing
    }

    override fun notify(sender: String, event: String) {
        if(sender == "saveAnswersButton" && event == "clicked")
        {
            fullPassageViewManager?.navView!!.menu?.findItem(R.id.navigation_translated)?.isEnabled = true
            fullPassageViewManager?.navView!!.selectedItemId = R.id.navigation_translated


            fullPassageStatesDataHolder?.firstAnswers = questionsBottomSheetAdapter?.userAnswers
            iQuestionSheetStateChanger.changeState(QuestionSheetTranslatedSheetState(fullPassageViewManager, fullPassageStatesDataHolder,  iQuestionSheetStateChanger))
        }
    }

    override fun notify(event: String) {
        //
    }

}