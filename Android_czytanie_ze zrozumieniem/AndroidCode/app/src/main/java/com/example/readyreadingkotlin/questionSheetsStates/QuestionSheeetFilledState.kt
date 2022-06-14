package com.example.readyreadingkotlin.questionSheetsStates

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager

class QuestionSheeetFilledState(var fullPassageViewManager: FullPassageViewManager?,
                                var fullPassageStatesDataHolder: FullPassageStatesDataHolder?, var iQuestionSheetStateChanger: IQuestionSheetStateChanger
) :  INextBack {
    var questionsBottomSheetAdapter: QuestionsBottomSheetAdapter? = null
    init{

         fullPassageViewManager?.fab?.isVisible = true

        if(fullPassageStatesDataHolder?.describedPassage?.questions != null)
        {

            questionsBottomSheetAdapter = QuestionsBottomSheetAdapter(fullPassageStatesDataHolder?.firstAnswers, fullPassageStatesDataHolder?.describedPassage?.questions!!, fullPassageViewManager?.context!!)
            fullPassageViewManager?.questionsPassagesRecyclerView?.adapter = questionsBottomSheetAdapter
            fullPassageViewManager?.questionsPassagesRecyclerView?.layoutManager = LinearLayoutManager(fullPassageViewManager?.context)
        }

    }

    override fun next() {
        iQuestionSheetStateChanger.changeState(QuestionSheetTranslatedSheetState(fullPassageViewManager, fullPassageStatesDataHolder,  iQuestionSheetStateChanger))
    }


    override fun back() {
        // do nothing
    }

}