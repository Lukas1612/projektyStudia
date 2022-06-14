package com.example.readyreadingkotlin.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.Flashcards_in_groups
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.questions.adapters.ViewPagerAdapter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.util.*


class FlashcardActivity : AppCompatActivity() {

    val fragmentArrayList: MutableList<Fragment> = mutableListOf<Fragment>()

    private var questionPositionTV: TextView? = null
    private var totalQuestions = "1"
    private var questionsViewPager: ViewPager? = null

    var flashcardsInGroup: Flashcards_in_groups? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        toolBarInit()
        parsingData()

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_Flashcard_activity_fragment, fragment)
            commit()
        }

    private fun toolBarInit() {
        val questionToolbar = findViewById<Toolbar>(R.id.questionToolbar)
        questionToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_forward_24)
        questionToolbar.setNavigationOnClickListener { v: View? -> nextQuestion() }

        //questionToolbarTitle = questionToolbar.findViewById(R.id.questionToolbarTitle);
        questionPositionTV = questionToolbar.findViewById(R.id.questionPositionTV)

        //questionToolbarTitle.setText("Questions");
    }

    private fun parsingData() {


        val extras = intent.extras
        var flashcardsString: String?


        /*flashcardsString = extras!!.getString("Flashcards_in_groups")
        val parser = JsonParser()
        val mJson: JsonElement = parser.parse(flashcardsString)
        val gson = Gson()
        val flashcardsInGroup: Flashcards_in_groups = gson.fromJson(mJson, Flashcards_in_groups::class.java)*/

        flashcardsString = extras!!.getString("Flashcards_in_groups")
        val gson = Gson()
        val flashcardsInGroup: Flashcards_in_groups = gson.fromJson(flashcardsString, Flashcards_in_groups::class.java)






        for (i in flashcardsInGroup.flashcards!!.indices) {



            var  flashcardTopSideFragment = FlashcardTopSideFragment()
            var checkBoxBundle = Bundle()
            checkBoxBundle.putParcelable("flashcard", flashcardsInGroup.flashcards!!.get(i))
            checkBoxBundle.putInt("group_id", flashcardsInGroup?.group?.id!!)
            flashcardTopSideFragment.arguments = checkBoxBundle
            fragmentArrayList.add(flashcardTopSideFragment)


            var flashcardBottomSideFragment = FlashcardBottomSideFragment()
            flashcardBottomSideFragment.arguments = checkBoxBundle
            fragmentArrayList.add(flashcardBottomSideFragment)




        }
        questionsViewPager = findViewById(R.id.pager)
        questionsViewPager!!.setOffscreenPageLimit(1)
        val mPagerAdapter = ViewPagerAdapter(supportFragmentManager, fragmentArrayList!! as ArrayList<Fragment>?)
        questionsViewPager!!.setAdapter(mPagerAdapter)


    }

    override fun onBackPressed() {
        if (questionsViewPager!!.currentItem == 0) {
            super.onBackPressed()
        } else {
            val item = questionsViewPager!!.currentItem - 1
            questionsViewPager!!.currentItem = item
            val currentQuestionPosition = (item + 1).toString()
            val questionPosition = "$currentQuestionPosition/$totalQuestions"

        }
    }

    
    fun nextQuestion() {
        val item = questionsViewPager!!.currentItem + 1
        questionsViewPager!!.currentItem = item
        val currentQuestionPosition = (item + 1).toString()
        val questionPosition = "$currentQuestionPosition/$totalQuestions"

    }

}