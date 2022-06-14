package com.example.readyreadingkotlin.ui.units

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.readyreadingkotlin.*
import com.example.readyreadingkotlin.auth.ApiClient
import com.example.readyreadingkotlin.auth.LoginActivity
import com.example.readyreadingkotlin.auth.SessionManager
import com.example.readyreadingkotlin.database.DbHelper
import com.example.readyreadingkotlin.database.UserTestQuestionChoicesSerialInserter
import com.example.readyreadingkotlin.learning_unit.LearningUnit
import com.example.readyreadingkotlin.learning_unit.UserTestQuestionChoices
import com.example.readyreadingkotlin.questionSheetsStates.TestQuestionnaireBottomSheetStateAdapter
import com.example.readyreadingkotlin.questions.database.AppDatabase
import com.example.readyreadingkotlin.questions.questionmodels.AnswerOptions
import com.example.readyreadingkotlin.questions.questionmodels.Data
import com.example.readyreadingkotlin.questions.questionmodels.QuestionDataModel
import com.example.readyreadingkotlin.questions.questionmodels.QuestionsItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TestPassageActivity : AppCompatActivity(), UserAnswersListener{

    private var timeTextView: TextView? = null
    private var learningUnit: LearningUnit? = null
    private var testPassagesRecyclerView: RecyclerView? = null
    var questionsSheet: FrameLayout? = null
    var buttonNext: Button? = null
    var buttonBack: Button? = null
    var textViewTP: TextView? = null
    var testPassageTextView: TextView? = null

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    var questionDataModel: QuestionDataModel? = null

    var questionsItems: List<QuestionsItem> = ArrayList()
    private var appDatabase: AppDatabase? = null
    private var totalQuestions = "1"
    private var gson: Gson? = null
    private var questionsViewPager: ViewPager? = null

    private var checkBoxChoices: MutableList<Int> = mutableListOf<Int>()

    private var fownloadFilesTask: DownloadFilesTask? = null

    var curPos = 0

    lateinit var testPassageBottomSheetAdapter: TestPassageBottomSheetAdapter


    private class DownloadFilesTask : AsyncTask<TextView?, Void, Void>() {

        var run: Boolean = true
        var time: Int = 0
        override fun doInBackground(vararg params: TextView?): Void? {

            var tStart = System.currentTimeMillis()
            var tEnd = System.currentTimeMillis()

            while (run)
            {
                if((tEnd - tStart)>1000)
                {
                    ++time
                    tStart = System.currentTimeMillis()
                    params[0]!!.setText(time.toString())
                }
                tEnd = System.currentTimeMillis()
            }


            return null
        }

        public fun stop()
        {
            run = false
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_passage)


        apiClient = ApiClient()
        sessionManager = SessionManager(applicationContext)

        learningUnit = intent.getParcelableExtra("UNIT")
        textViewTP = findViewById(R.id.textViewTP)
        timeTextView = findViewById(R.id.timeTextView)

        testPassageTextView = findViewById(R.id.testPassageTextView)
        testPassageTextView!!.setText(learningUnit!!.testPassage!!.text)


        innitButtons()
        initQuestionDataModel()
        innitBottomSheet()
        initQuestiobFragment()

        fownloadFilesTask = DownloadFilesTask().execute(timeTextView) as DownloadFilesTask?

    }

   fun next()
   {
       ++curPos
       initQuestiobFragment()
   }

    fun back()
    {
        --curPos
        initQuestiobFragment()
    }

    fun isNext(): Boolean
    {
        return curPos < (learningUnit!!.testQuestions!!.size - 1)
    }

    fun isPrevious(): Boolean
    {
        return curPos > 0
    }

    fun innitButtons()
    {

        buttonNext = findViewById(R.id.buttonNextTP)
        buttonNext?.setOnClickListener(View.OnClickListener {

            if (isNext()) {
                next()
            } else {
                var score = calculateScoreAndPostAnswers()


                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)


                if (sessionManager.fetchAuthToken() != null && sessionManager.fetchAuthToken()!!.length > 5) {
                    deleteOldUserTestAndAddNew(score.toString())
                }
            }


        })

        buttonBack = findViewById(R.id.buttonBackTP)
        buttonBack?.setOnClickListener(View.OnClickListener {
            if (isPrevious()) {
                back()
            }

        })

    }


    fun initQuestionDataModel(){
        var questionsItems: MutableList<QuestionsItem> = mutableListOf<QuestionsItem>()
        var questionDataModel: QuestionDataModel = QuestionDataModel()



        learningUnit!!.testQuestions!!.forEachIndexed { index, testQuestionsWithChoices ->
            var qi: QuestionsItem = QuestionsItem()
            qi.questionTypeName = "CheckBox"
            qi.questionName = testQuestionsWithChoices.text
            qi.id = testQuestionsWithChoices.id!!
            qi.questionTypeId = 1

            var answerOptions: MutableList<AnswerOptions> = mutableListOf<AnswerOptions>()

            testQuestionsWithChoices.questionChoices!!.forEachIndexed { index, testQuestionsChoices ->
                var ao: AnswerOptions = AnswerOptions()
                ao.answerId = testQuestionsChoices.id.toString()
                ao.name = testQuestionsChoices.text
                ao.correct_or_false = testQuestionsChoices.correct_or_false
                answerOptions.add(ao)
            }

            qi.answerOptions = answerOptions
            questionsItems.add(qi)
        }


        var data: Data = Data()
        data.questions = questionsItems

        questionDataModel.data = data
        questionDataModel.message = "The data was fetched successfully"
        questionDataModel.setStatus(true)

        this.questionDataModel = questionDataModel
    }



    fun innitBottomSheet()
    {

        if(learningUnit?.testQuestions != null && learningUnit?.testQuestions!!.size > 0)
        {
            testPassagesRecyclerView = findViewById(R.id.testPassagesRecyclerView) as RecyclerView

            questionsSheet = findViewById(R.id.questionsSheetTP) as FrameLayout
            val displayMetrics = this.applicationContext?.resources?.displayMetrics
            questionsSheet!!.layoutParams.height = displayMetrics?.heightPixels?.times(3)?.div(5)!!

            BottomSheetBehavior.from(questionsSheet!!).apply {
                peekHeight = 80
                this.state = BottomSheetBehavior.STATE_DRAGGING
            }

        }else
        {
            var questionsSheet: FrameLayout = findViewById(R.id.questionsSheetTP) as FrameLayout
            questionsSheet?.setVisibility(View.GONE)

        }

    }

    fun initQuestiobFragment()
    {
        textViewTP!!.setText(questionDataModel!!.data.questions[curPos].questionName)

      //  var questionsItems = questionDataModel!!.data.questions
         var questionsItems = learningUnit?.testQuestions
        var questionnaireBottomSheetStateAdapter: TestQuestionnaireBottomSheetStateAdapter = TestQuestionnaireBottomSheetStateAdapter(testPassagesRecyclerView!!.context, questionsItems?.get(curPos)!!, checkBoxChoices, this)
        testPassagesRecyclerView?.adapter = questionnaireBottomSheetStateAdapter
        testPassagesRecyclerView!!.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun setAnswer(id: Int, answer: String) {


        if(answer.equals("set"))
        {
            if(!checkBoxChoices.contains(id))
            {
                checkBoxChoices.add(id)
            }
        }

        if(answer.equals("delete"))
        {
            if(checkBoxChoices.contains(id))
            {
                checkBoxChoices.remove(id)
            }
        }

    }

    override fun setAnswer(id: Int) {
        textViewTP!!.setText(id.toString())
        println(" here " + id)
    }

    fun calculateScoreAndPostAnswers(): Float
    {
        fownloadFilesTask!!.stop()

        var wasFalseAnswer: Boolean = false
        var userScore: Float = 0F
        var totalScore: Float = 0F
        var oneQuestionScore: Float = 0F

        var userTestQuestionChoices = mutableListOf<UserTestQuestionChoices>()


        questionDataModel!!.data.questions.forEachIndexed { index, questionsItem ->

            wasFalseAnswer = false
            oneQuestionScore = 0F
            questionsItem.answerOptions.forEachIndexed { index, answerOption ->
                if(checkBoxChoices.contains(answerOption.answerId.toInt()))
                {

                   // DbHelper().postUserTestQuestionChoices(questionsItem.id, answerOption.answerId.toInt(), this, "Stefan", "qwerty")

                    userTestQuestionChoices.add(UserTestQuestionChoices(null, questionsItem.id, null, answerOption.answerId.toInt()))


                    if(answerOption.correct_or_false.equals("false"))
                    {
                        wasFalseAnswer = true
                        oneQuestionScore = 0F

                    }else if(answerOption.correct_or_false.equals("correct") && wasFalseAnswer == false)
                    {
                        ++oneQuestionScore
                    }
                }

                if(answerOption.correct_or_false.equals("correct"))
                {
                    ++totalScore
                }
            }

            userScore += oneQuestionScore
        }

        var iterator = userTestQuestionChoices!!.iterator()
        var userTestQuestionChoicesSerialInserter = UserTestQuestionChoicesSerialInserter(iterator, userTestQuestionChoices, sessionManager!!.fetchName()!!, sessionManager!!.fetchPassword()!!)
        userTestQuestionChoicesSerialInserter.startSerialInsertion()

        var string = learningUnit!!.testPassage!!.text!!
        var stringPattern = " "
        var numberOfQuestions = questionDataModel!!.data.questions.size

        println(" score:  " + userScore + " / " + totalScore + " number of questions  " + numberOfQuestions + " time " + fownloadFilesTask!!.time)
        println(" score:  " + (userScore / totalScore) * (numberOfQuestions.toFloat() / fownloadFilesTask!!.time.toFloat()))

        return (userScore/totalScore)*(numberOfQuestions.toFloat()/fownloadFilesTask!!.time.toFloat())

    }

    override fun onDestroy() {

        if(fownloadFilesTask != null)
        {
            fownloadFilesTask!!.stop()
        }

        super.onDestroy()
    }


    private fun deleteOldUserTestAndAddNew(score: String){

        // Pass the token as parameter
        apiClient.getApiService().deleteUserTest(token = "Bearer ${sessionManager.fetchAuthToken()}", learningUnit!!.testPassage!!.id!!)
                .enqueue(object : Callback<Boolean> {
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        // Error fetching posts
                        println(" error " + t.toString())
                    }


                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                        val current = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDateTime.now()
                        } else {
                            TODO("VERSION.SDK_INT < O")
                        }
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formatted = current.format(formatter)


                        DbHelper().addUserTest(learningUnit!!.testPassage!!.id!!, score, formatted, sessionManager!!.fetchName()!!, sessionManager!!.fetchPassword()!!)
                    }
                })

    }

}

