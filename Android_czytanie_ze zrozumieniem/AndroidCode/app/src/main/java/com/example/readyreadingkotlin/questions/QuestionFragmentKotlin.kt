package com.example.readyreadingkotlin.questions

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.readyreadingkotlin.questionSheetsStates.QuestionnaireBottomSheetStateAdapter
import com.example.readyreadingkotlin.questions.database.AppDatabase
import com.example.readyreadingkotlin.questions.qdb.QuestionEntity
import com.example.readyreadingkotlin.questions.qdb.QuestionWithChoicesEntity
import com.example.readyreadingkotlin.questions.questionmodels.QuestionDataModel
import com.example.readyreadingkotlin.questions.questionmodels.QuestionsItem
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

class QuestionFragmentKotlin(var questionnaireRV: RecyclerView, var questionsJSON: String){
    private val mViewModel: QuestionViewModel? = null
    val fragmentArrayList = ArrayList<Fragment>()
    var questionsItems: List<QuestionsItem> = ArrayList()
    private var appDatabase: AppDatabase? = null


    //private TextView questionToolbarTitle;
    private var totalQuestions = "1"
    private var gson: Gson? = null
    private var questionsViewPager: ViewPager? = null
    private var qestionViewModel: QuestionViewModel? = null
    private var questionPositionTV: View? = null

    //private var questionnaireRV: RecyclerView? = null

    /* public static QuestionFragment newInstance(BottomNavigationView navView) {
      return new QuestionFragment(navView);
  }*/

    init {

        appDatabase = AppDatabase.getAppDatabase(questionnaireRV.context)
        gson = Gson()
       // toolBarInit(navView)
       // val string = qestionViewModel!!.selectedItem
        parsingData(questionsJSON, questionnaireRV)

    }
   /* override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.question_fragment, container, false)

        questionnaireRV = root.findViewById(R.id.questionnaireRV)

        qestionViewModel = ViewModelProvider(requireActivity()).get(QuestionViewModel::class.java)
        appDatabase = AppDatabase.getAppDatabase(this.context)
        gson = Gson()
        toolBarInit(root)
        val string = qestionViewModel!!.selectedItem
        parsingData(string.value, root)
        return root
    }*/

   /* private fun toolBarInit(root: View) {
        val questionToolbar: Toolbar = root.findViewById(R.id.questionToolbar)
        questionToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        questionToolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }

        //questionToolbarTitle = questionToolbar.findViewById(R.id.questionToolbarTitle);
        questionPositionTV = questionToolbar.findViewById(R.id.questionPositionTV)
        //questionToolbarTitle.setText("Questions");
    }*/

    private fun parsingData(string: String?, root: View) {
        var questionDataModel = QuestionDataModel()
        questionDataModel = gson!!.fromJson(string, QuestionDataModel::class.java)
        questionsItems = questionDataModel.data.questions
        totalQuestions = questionsItems.size.toString()
        val questionPosition = "1/$totalQuestions"
        setTextWithSpan(questionPosition)



        preparingQuestionInsertionInDb(questionsItems)
        preparingInsertionInDb(questionsItems)



       /* var questionnaireBottomSheetStateAdapter: QuestionnaireBottomSheetStateAdapter = QuestionnaireBottomSheetStateAdapter(
                null,
                response!!.body())
        questionnaireRV?.adapter = questionnaireBottomSheetStateAdapter
        questionnaireRV!!.layoutManager = LinearLayoutManager(root.context)*/

    }

    fun nextQuestion() {
        val item = questionsViewPager!!.currentItem + 1
        questionsViewPager!!.currentItem = item
        val currentQuestionPosition = (item + 1).toString()
        val questionPosition = "$currentQuestionPosition/$totalQuestions"
        setTextWithSpan(questionPosition)
    }

    fun getTotalQuestionsSize(): Int {
        return questionsItems.size
    }

    private fun preparingQuestionInsertionInDb(questionsItems: List<QuestionsItem>) {
        val questionEntities: MutableList<QuestionEntity> = ArrayList()
        for (i in questionsItems.indices) {
            val questionEntity = QuestionEntity()
            questionEntity.questionId = questionsItems[i].id
            questionEntity.question = questionsItems[i].questionName
            questionEntities.add(questionEntity)
        }
        insertQuestionInDatabase(questionEntities)
    }



    private fun insertQuestionInDatabase(questionEntities: List<QuestionEntity>) {
        Observable.just(questionEntities)
                .map { questionEntities: List<QuestionEntity> -> insertingQuestionInDb(questionEntities) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    /*First, clear the table, if any previous data saved in it. Otherwise, we get repeated data.*/
    private fun insertingQuestionInDb(questionEntities: List<QuestionEntity>): String {
        appDatabase!!.questionDao.deleteAllQuestions()
        appDatabase!!.questionDao.insertAllQuestions(questionEntities)
        return ""
    }



    private fun preparingInsertionInDb(questionsItems: List<QuestionsItem>) {
        val questionWithChoicesEntities = ArrayList<QuestionWithChoicesEntity>()
        for (i in questionsItems.indices) {
            val answerOptions = questionsItems[i].answerOptions
            for (j in answerOptions.indices) {
                val questionWithChoicesEntity = QuestionWithChoicesEntity()
                questionWithChoicesEntity.questionId = questionsItems[i].id.toString()
                questionWithChoicesEntity.answerChoice = answerOptions[j].name
                questionWithChoicesEntity.answerChoicePosition = j.toString()
                questionWithChoicesEntity.answerChoiceId = answerOptions[j].answerId
                questionWithChoicesEntity.answerChoiceState = "0"
                questionWithChoicesEntities.add(questionWithChoicesEntity)
            }
        }
        insertQuestionWithChoicesInDatabase(questionWithChoicesEntities)
    }


    private fun insertQuestionWithChoicesInDatabase(questionWithChoicesEntities: List<QuestionWithChoicesEntity>) {
        Observable.just(questionWithChoicesEntities)
                .map { questionWithChoicesEntities: List<QuestionWithChoicesEntity> -> insertingQuestionWithChoicesInDb(questionWithChoicesEntities) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    /*First, clear the table, if any previous data saved in it. Otherwise, we get repeated data.*/
    private fun insertingQuestionWithChoicesInDb(questionWithChoicesEntities: List<QuestionWithChoicesEntity>): String {

        appDatabase!!.questionChoicesDao.deleteAllChoicesOfQuestion()
        appDatabase!!.questionChoicesDao.insertAllChoicesOfQuestion(questionWithChoicesEntities)
        return ""
    }


   /* fun onBackPressed() {
        if (questionsViewPager!!.currentItem == 0) {
            navView.selectedItemId = R.id.navigation_english
        } else {
            val item = questionsViewPager!!.currentItem - 1
            questionsViewPager!!.currentItem = item
            val currentQuestionPosition = (item + 1).toString()
            val questionPosition = "$currentQuestionPosition/$totalQuestions"
            setTextWithSpan(questionPosition)
        }
    }*/

    private fun setTextWithSpan(questionPosition: String) {
        val slashPosition = questionPosition.indexOf("/")
        val spanText: Spannable = SpannableString(questionPosition)
        spanText.setSpan(RelativeSizeSpan(0.7f), slashPosition, questionPosition.length, 0)
    }
}