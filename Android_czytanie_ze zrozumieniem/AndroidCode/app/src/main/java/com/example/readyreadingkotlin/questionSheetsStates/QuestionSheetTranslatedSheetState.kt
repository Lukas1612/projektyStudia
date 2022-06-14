package com.example.readyreadingkotlin.questionSheetsStates

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager


class QuestionSheetTranslatedSheetState(var fullPassageViewManager: FullPassageViewManager?,
                                        var fullPassageStatesDataHolder: FullPassageStatesDataHolder?, var iQuestionSheetStateChanger: IQuestionSheetStateChanger
) : INextBack {



    var curPos: Int = 0


        var translatedBottomSheetAdapter: TranslatedBottomSheetAdapter? = null
    init{

          fullPassageViewManager?.fab?.isVisible = true

        if(fullPassageStatesDataHolder?.secondAnswers == null)
        {
            fullPassageStatesDataHolder?.secondAnswers = HashMap<Int, String>()
        }

         if(fullPassageStatesDataHolder?.howTheAnswerChanged == null)
        {
            fullPassageStatesDataHolder?.howTheAnswerChanged = HashMap<Int, Int>()
        }

        if(fullPassageStatesDataHolder?.describedPassage?.answersTier != null)
        {
            fullPassageStatesDataHolder?.describedPassage?.answersTier!!.forEachIndexed { index, passagesAnswersTier ->
                saveHowTheAnswerChanged(passagesAnswersTier.question_id, passagesAnswersTier.tier)
                println(" TIER: " + passagesAnswersTier.question_id + "  " +  passagesAnswersTier.tier)
            }
        }

        if( fullPassageStatesDataHolder?.describedPassage?.questions != null)
        {
            if(isNext())
            {
                var id =  fullPassageStatesDataHolder?.describedPassage?.questions!![curPos]!!.id
                translatedBottomSheetAdapter = TranslatedBottomSheetAdapter(fullPassageStatesDataHolder?.firstAnswers?.get(id), fullPassageStatesDataHolder?.secondAnswers!!.get(id), fullPassageStatesDataHolder?.howTheAnswerChanged!!.get(id), fullPassageStatesDataHolder?.describedPassage?.questions!![curPos], fullPassageViewManager?.context!!)

                fullPassageViewManager?.questionsPassagesRecyclerView?.adapter = translatedBottomSheetAdapter
                fullPassageViewManager?.questionsPassagesRecyclerView?.layoutManager = LinearLayoutManager(fullPassageViewManager?.context)
            }
        }
    }

    override fun next() {

        if(this.isNext())
        {
            curPos += 1
            innitNewAdapter()
        }else
        {
            saveSecondAnswer(translatedBottomSheetAdapter?.question?.id, translatedBottomSheetAdapter?.secondAnswer)
            saveHowTheAnswerChanged(translatedBottomSheetAdapter?.question?.id, translatedBottomSheetAdapter?.howTheAnswerChanged)
            iQuestionSheetStateChanger.changeState(QuestionSheetQuestionnaireState(fullPassageViewManager, fullPassageStatesDataHolder, iQuestionSheetStateChanger))

        }
    }


    override fun back() {
        if(this.isPrevious())
        {
            curPos -= 1
            innitNewAdapter()
        }else
        {
            //saveSecondAnswer(translatedBottomSheetAdapter?.question?.id, translatedBottomSheetAdapter?.secondAnswer)
          //  saveHowTheAnswerChanged(translatedBottomSheetAdapter?.question?.id, translatedBottomSheetAdapter?.howTheAnswerChanged)
          //  iQuestionSheetStateChanger.chngeState(QuestionSheeetFilledState(answers, secondAnswers, howTheAnswerChanged, fab,  recyclerView, describedPassage, context, iQuestionSheetStateChanger))
        }
    }

    fun lastPage()
    {
        curPos =  fullPassageStatesDataHolder?.describedPassage?.questions!!.size - 1
        innitNewAdapter()
    }



    fun isNext(): Boolean
    {
        return curPos < ( fullPassageStatesDataHolder?.describedPassage?.questions!!.size - 1)
    }

    fun isPrevious(): Boolean
    {
        return curPos > 0
    }

    fun saveSecondAnswer(id: Int?, text: String?)
    {
        if(text != null && id != null)
        {
            if(fullPassageStatesDataHolder?.secondAnswers!!.containsKey(id))
            {
                fullPassageStatesDataHolder?.secondAnswers!!.remove(id)
            }

            if(!text.equals(""))
            {
                fullPassageStatesDataHolder?.secondAnswers!!.put(id, text)
            }

        }

    }

    fun saveHowTheAnswerChanged(id: Int?, degree: Int?)
    {
        if(degree != null && id != null && fullPassageStatesDataHolder?.howTheAnswerChanged!!.containsKey(id))
        {
            fullPassageStatesDataHolder?.howTheAnswerChanged!!.remove(id)
            fullPassageStatesDataHolder?.howTheAnswerChanged!!.put(id, degree)
        }else if(degree != null  && id != null)
        {
            fullPassageStatesDataHolder?.howTheAnswerChanged!!.put(id, degree)
        }
    }

    fun innitNewAdapter()
    {
        var id =  fullPassageStatesDataHolder?.describedPassage?.questions!![curPos]!!.id


        saveSecondAnswer(translatedBottomSheetAdapter?.question?.id, translatedBottomSheetAdapter?.secondAnswer)
        saveHowTheAnswerChanged(translatedBottomSheetAdapter?.question?.id, translatedBottomSheetAdapter?.howTheAnswerChanged)

        translatedBottomSheetAdapter = TranslatedBottomSheetAdapter(fullPassageStatesDataHolder?.firstAnswers?.get(id), fullPassageStatesDataHolder?.secondAnswers!!.get(id), fullPassageStatesDataHolder?.howTheAnswerChanged?.get(id), fullPassageStatesDataHolder?.describedPassage?.questions!![curPos], fullPassageViewManager?.context!!)
        fullPassageViewManager?.questionsPassagesRecyclerView?.adapter = translatedBottomSheetAdapter
        fullPassageViewManager?.questionsPassagesRecyclerView?.layoutManager = LinearLayoutManager(fullPassageViewManager?.context)
    }
}