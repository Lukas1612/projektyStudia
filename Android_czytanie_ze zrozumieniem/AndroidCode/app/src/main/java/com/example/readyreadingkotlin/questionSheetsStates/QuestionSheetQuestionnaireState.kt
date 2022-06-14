package com.example.readyreadingkotlin.questionSheetsStates

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readyreadingkotlin.database.DbHelper
import com.example.readyreadingkotlin.auth.ApiClient
import com.example.readyreadingkotlin.auth.LoginActivity
import com.example.readyreadingkotlin.auth.SessionManager
import com.example.readyreadingkotlin.database.PassagesAnswersTierSerialInserter
import com.example.readyreadingkotlin.database.QuestionnaireQuestionsSerialInserter
import com.example.readyreadingkotlin.questions.QuestionnaireQuestions
import com.example.readyreadingkotlin.questions.QuestionnaireQuestionsLoaderListener
import com.example.readyreadingkotlin.questions.UserQuestionnaireQuestions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionSheetQuestionnaireState(var fullPassageViewManager: FullPassageViewManager?,
                                      var fullPassageStatesDataHolder: FullPassageStatesDataHolder?, var iQuestionSheetStateChanger: IQuestionSheetStateChanger
) : INextBack, QuestionnaireQuestionsLoaderListener{

    var questionnaireBottomSheetStateAdapter: QuestionnaireBottomSheetStateAdapter? = null

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager




    init{
      DbHelper().loadQuestionnaireQuestions(this)
    }

    var checkBoxChoicesInex:Iterator<Int>? = null
    var howTheAnswerChangedIndex: MutableIterator<Int>? = null

    override fun next() {


        var iterator =   fullPassageStatesDataHolder?.howTheAnswerChanged?.keys?.iterator()
        val passagesAnswersTierSerialInserter = PassagesAnswersTierSerialInserter(iterator, fullPassageStatesDataHolder?.howTheAnswerChanged, fullPassageStatesDataHolder?.describedPassage!!.id,  sessionManager!!.fetchName()!!, sessionManager!!.fetchPassword()!!)
        passagesAnswersTierSerialInserter.startSerialInsertion()

        iterator = questionnaireBottomSheetStateAdapter!!.listOFUserCheckBoxChoices!!.iterator()
        val listOFUserCheckBoxChoices = questionnaireBottomSheetStateAdapter!!.listOFUserCheckBoxChoices
        val questionnaireQuestionsSerialInserter = QuestionnaireQuestionsSerialInserter(iterator, listOFUserCheckBoxChoices, fullPassageStatesDataHolder?.describedPassage!!.id,   sessionManager!!.fetchName()!!, sessionManager!!.fetchPassword()!!)
        questionnaireQuestionsSerialInserter.startSerialInsertion()

    }


    override fun back() {

        var questionSheeetTranslatedSheetState = QuestionSheetTranslatedSheetState(fullPassageViewManager, fullPassageStatesDataHolder, iQuestionSheetStateChanger)
        questionSheeetTranslatedSheetState.lastPage()
        iQuestionSheetStateChanger.changeState(questionSheeetTranslatedSheetState)

    }

    override fun update(unitsList: MutableList<QuestionnaireQuestions>) {


        apiClient = ApiClient()
        sessionManager = SessionManager(fullPassageViewManager?.context!!)

        var intent = Intent(fullPassageViewManager?.questionsPassagesRecyclerView!!.rootView.context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        fullPassageViewManager?.questionsPassagesRecyclerView!!.rootView.context.startActivity(intent)



        if(sessionManager.fetchAuthToken() != null && sessionManager.fetchAuthToken()!!.length>5)
        {
            println(" UserQuestionareQuestions:  " + sessionManager.fetchAuthToken())
            getUserQuestionareQuestions(unitsList)
        }


    }




    fun getUserQuestionareQuestions(unitsList: MutableList<QuestionnaireQuestions>)
    {

        // Pass the token as parameter
        apiClient.getApiService().getUserQuestionnaireQuestions(token = "Bearer ${sessionManager.fetchAuthToken()}")
                .enqueue(object : Callback<List<UserQuestionnaireQuestions>> {
                    override fun onFailure(call: Call<List<UserQuestionnaireQuestions>>, t: Throwable) {
                        // Error fetching posts
                        println(" ----error " + t.toString() + "  " + call.toString())
                    }

                    override fun onResponse(call: Call<List<UserQuestionnaireQuestions>>, response: Response<List<UserQuestionnaireQuestions>>) {

                        println( " response   " + response.body())

                        if(response.body() != null)
                        {
                            println( " userQuestionnaireQuestions   " + response.body())
                            response.body()!!.forEachIndexed { index, userQuestionnaireQuestions ->
                                println(" index " + userQuestionnaireQuestions.questionnaire_questions_id)
                            }
                        }

                        val userQuestionsUnitsList = response!!.body()

                        questionnaireBottomSheetStateAdapter = QuestionnaireBottomSheetStateAdapter(unitsList, userQuestionsUnitsList, fullPassageStatesDataHolder?.describedPassage)
                        fullPassageViewManager?.questionsPassagesRecyclerView?.adapter = questionnaireBottomSheetStateAdapter
                        fullPassageViewManager?.questionsPassagesRecyclerView?.layoutManager = LinearLayoutManager(fullPassageViewManager?.context)
                    }
                })

    }



}
