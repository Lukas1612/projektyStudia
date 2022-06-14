package com.example.readyreadingkotlin.ui.units

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.FullPassageMenu.ui.DecribedPassageViewModel
import com.example.readyreadingkotlin.FullPassageMenu.ui.english.EnglishFragment
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.FlashcardFragment
import com.example.readyreadingkotlin.FullPassageMenu.ui.translated.TranslatedFragment
import com.example.readyreadingkotlin.MovableFloatingActionButton
import com.example.readyreadingkotlin.PopUpWindowBuilder
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.described_passage.DescribedPassage
import com.example.readyreadingkotlin.questionSheetsStates.*
import com.example.readyreadingkotlin.questions.AnswerFragment
import com.example.readyreadingkotlin.questions.QuestionViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.IOException
import java.nio.charset.Charset


class FullPassageActivity : AppCompatActivity(), IQuestionSheetStateChanger{

   // private val DEBUG_TAG: String? = "tag"
    var navView: BottomNavigationView? = null
  //  var SWIPE_TRESHOLD = 0

    var questionSheetState:INextBack? = null

    var fab: MovableFloatingActionButton? = null
    var buttonNext: Button? = null
    var buttonBack: Button? = null

    var questionsSheet: FrameLayout? = null
    var fullPassageViewManager: FullPassageViewManager? = null
    var fullPassageStatesDataHolder: FullPassageStatesDataHolder? = null

    private val questionViewModel: QuestionViewModel by viewModels()



    var questionsPassagesRecyclerView: RecyclerView? = null

    //private lateinit var mDetector: GestureDetectorCompat
    private var pasageTextView: TextView? = null

    private var describedPassage: DescribedPassage? = null

    private val decribedPassageViewModel: DecribedPassageViewModel by viewModels()

      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_passage)

          questionViewModel.selectItem(loadQuestionnaireJson("questions_example.json"))

        describedPassage = intent.getParcelableExtra("PASSAGE")
          if(describedPassage != null)
          {
              decribedPassageViewModel.selectItem(describedPassage!!)
          }



          navView = innitBottomNavView()

          innitBottomSheet()

          innitButtons(navView!!)

          innitFabButton()



          fullPassageViewManager = FullPassageViewManager(navView!!, fab, questionsPassagesRecyclerView, applicationContext)

          fullPassageStatesDataHolder = FullPassageStatesDataHolder(describedPassage)

          questionSheetState = QuestionSheetInitialState(fullPassageViewManager, fullPassageStatesDataHolder, this)


      }






    private fun innitBottomNavView(): BottomNavigationView{

        val navView: BottomNavigationView = findViewById(R.id.nav_view_full_passage)
        navView?.menu?.findItem(R.id.navigation_translated)?.isEnabled = false

        val englishFragment=EnglishFragment()
        val translatedFragment=TranslatedFragment()
        val flashcardFragment=FlashcardFragment()
      //  val questionFragment= QuestionFragmentKotlin(navView)
        val answerFragment=AnswerFragment(navView)

        setCurrentFragment(englishFragment)


        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_english -> {
                    setCurrentFragment(englishFragment)
                    questionsSheet?.isVisible = true
                }
                R.id.navigation_translated -> {
                    setCurrentFragment(translatedFragment)
                    fab?.isVisible = true
                    questionsSheet?.isVisible = true
                }
                R.id.navigation_flashcard -> {
                    setCurrentFragment(flashcardFragment)
                    fab?.isVisible = false
                    questionsSheet?.isVisible = false
                }

            }
            true
        }

        return navView
    }

    private fun setCurrentFragment(fragment: Fragment)=
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment_full_passage, fragment)
                commit()
            }


    override fun changeState(state: INextBack) {
        this.questionSheetState = state
    }

    fun innitBottomSheet()
    {

        if(describedPassage?.questions != null && describedPassage!!.questions!!.size > 0)
        {
            questionsPassagesRecyclerView = findViewById(R.id.questionsPassagesRecyclerView) as RecyclerView

            questionsSheet = findViewById(R.id.questionsSheet) as FrameLayout
            val displayMetrics = this.applicationContext?.resources?.displayMetrics
            questionsSheet!!.layoutParams.height = displayMetrics?.heightPixels?.times(3)?.div(5)!!

           val bottomSheetBehavior = BottomSheetBehavior.from(questionsSheet!!).apply {
                peekHeight = 80
                this.state = BottomSheetBehavior.STATE_DRAGGING
            }

        }else
        {
            var questionsSheet: FrameLayout = findViewById(R.id.questionsSheet) as FrameLayout
            questionsSheet?.setVisibility(View.GONE)

        }

    }

    fun innitButtons(bottomNavigationView: BottomNavigationView)
    {

        buttonNext = findViewById(R.id.buttonNext)
        buttonNext?.setOnClickListener(View.OnClickListener {
            questionSheetState?.next()

        })

        buttonBack = findViewById(R.id.buttonBack)
        buttonBack?.setOnClickListener(View.OnClickListener {
            questionSheetState?.back()
        })

    }


    val popUpWindowBuilder: PopUpWindowBuilder = PopUpWindowBuilder()
    fun innitFabButton()
    {
        fab = findViewById<View>(R.id.fab) as MovableFloatingActionButton



        if( describedPassage?.tips != null)
        {
            if(describedPassage?.tips!!.size > 0)
            {
                fab!!.setOnTouchListener { view, event ->
                    fab!!.onTouch(view, event)
                    true
                }

                fab!!.setOnClickListener(View.OnClickListener { v ->

                    val location = IntArray(2)
                    fab!!.getLocationOnScreen(location)

                    var tipsString: String = ""
                    for (tip in describedPassage?.tips!!) {
                        tipsString = tipsString + tip.text + "\n"
                    }

                    popUpWindowBuilder.showHintsPopUp(v, location, getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater, describedPassage?.tips!!)

                })

            }else
            {
                fab!!.setVisibility(View.GONE)
            }
        }else
        {
            fab!!.setVisibility(View.GONE)
        }

    }


    private fun loadQuestionnaireJson(filename: String): String? {
        return try {
            val `is` = assets.open(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }



}

