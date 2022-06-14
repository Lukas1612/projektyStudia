package com.example.readyreadingkotlin.questionSheetsStates

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.UserAnswersListener
import com.example.readyreadingkotlin.learning_unit.TestQuestionsWithChoices
import com.example.readyreadingkotlin.questions.questionmodels.QuestionsItem
import java.util.*



class TestQuestionnaireBottomSheetStateAdapter(
        var mContext: Context?,
        var checkBoxTypeQuestion: TestQuestionsWithChoices,
        var checkBoxChoicesList: MutableList<Int>,
        var userAnswersListener: UserAnswersListener
):  RecyclerView.Adapter<TestQuestionnaireBottomSheetStateAdapter.ViewHolder>(), UserAnswersListener {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var userAnswersListener: UserAnswersListener? = null
        public var checkBox: CheckBox? = null
        public var checkBoxId = ""

        init {
            checkBox = view.findViewById(R.id.checkBoxItem)

            checkBox!!.setOnClickListener(View.OnClickListener { view1: View? ->

                check()
            })
        }

       fun check()
        {
            if (checkBox!!.isChecked) {
                userAnswersListener!!.setAnswer(checkBoxId.toInt(), "set")
            } else {
                userAnswersListener!!.setAnswer(checkBoxId.toInt(), "delete")
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestQuestionnaireBottomSheetStateAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_questions_bottom_sheet_questionnaire, parent, false)

        return TestQuestionnaireBottomSheetStateAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(
            holder: TestQuestionnaireBottomSheetStateAdapter.ViewHolder,
            position: Int
    ) {




        var checkBoxChoices = Objects.requireNonNull(checkBoxTypeQuestion)!!.questionChoices

        holder.userAnswersListener = this
        holder.checkBox!!.setText(checkBoxChoices!!.get(position).text + "  " + checkBoxChoices.get(position).correct_or_false + "  " + checkBoxChoices.get(position).id)
        holder.checkBoxId = if (checkBoxChoices != null) checkBoxChoices!!.get(position)!!.id!!.toString() else 0.toString()


        if(checkBoxChoicesList.contains(holder.checkBoxId.toInt()))
        {
            holder.checkBox!!.isChecked = true
        }

        if(checkBoxTypeQuestion.userQuestionChoices != null)
        {
            if(checkBoxTypeQuestion.userQuestionChoices!!.size > 0)
            {
                checkBoxTypeQuestion.userQuestionChoices!!.forEachIndexed { index, userTestQuestionChoices ->
                    if(userTestQuestionChoices!!.question_choice_id!!.toString() ==  holder!!.checkBoxId!!)
                    {
                        holder.checkBox!!.isChecked = true
                        holder.check()
                    }
                }
            }
        }



    }

    override fun getItemCount(): Int {
        val checkBoxChoices = Objects.requireNonNull(checkBoxTypeQuestion)!!.questionChoices
        return checkBoxChoices!!.size
    }

    override fun setAnswer(id: Int, answer: String) {
        userAnswersListener.setAnswer(id, answer)
    }

    override fun setAnswer(id: Int) {
        userAnswersListener.setAnswer(id);
    }
}