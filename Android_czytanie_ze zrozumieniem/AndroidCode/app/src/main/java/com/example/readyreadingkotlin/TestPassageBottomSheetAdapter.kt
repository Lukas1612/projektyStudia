package com.example.readyreadingkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.learning_unit.LearningUnit
import com.example.readyreadingkotlin.learning_unit.TestQuestionsWithChoices
import com.example.readyreadingkotlin.questionSheetsStates.TranslatedBottomSheetAdapter
import com.ms.square.android.expandabletextview.ExpandableTextView

class TestPassageBottomSheetAdapter(var testQuestionsWithChoices: TestQuestionsWithChoices):  RecyclerView.Adapter<TestPassageBottomSheetAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        public var checkBox: CheckBox? = null

        init {
            checkBox = view.findViewById(R.id.checkBoxItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_questions_bottom_sheet_questionnaire, parent, false)


        return TestPassageBottomSheetAdapter.ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox!!.setText(testQuestionsWithChoices.questionChoices!!.get(position).text + "   id: " + testQuestionsWithChoices.questionChoices!!.get(position).id + " " +  testQuestionsWithChoices.questionChoices!!.get(position).correct_or_false)
    }

    override fun getItemCount(): Int {
        return testQuestionsWithChoices.questionChoices!!.size
    }
}