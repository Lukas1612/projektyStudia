package com.example.readyreadingkotlin.questionSheetsStates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.UserAnswersListener
import com.example.readyreadingkotlin.described_passage.DescribedPassage
import com.example.readyreadingkotlin.questions.QuestionnaireQuestions
import com.example.readyreadingkotlin.questions.UserQuestionnaireQuestions
import java.util.*


/**
 * This fragment provide the Checkbox/Multiple related Options/Choices.
 */
class QuestionnaireBottomSheetStateAdapter(
        var questionsUnitsList: MutableList<QuestionnaireQuestions>?,
        var userQuestionsUnitsList: List<UserQuestionnaireQuestions>?,
        var describedPassage: DescribedPassage?,
):  RecyclerView.Adapter<QuestionnaireBottomSheetStateAdapter.ViewHolder>(), UserAnswersListener {

    var listOFUserCheckBoxChoices = mutableListOf<Int>()


    class ViewHolder(view: View, var userAnswersListener: UserAnswersListener?) : RecyclerView.ViewHolder(view) {


        private var qState = "0"

        public var checkBox: CheckBox? = null

        public var questionId: Int? = null

        init {
            checkBox = view.findViewById(R.id.checkBoxItem)

            checkBox!!.setOnClickListener(View.OnClickListener {
              check()
            })
        }

        fun check()
        {
            if(checkBox!!.isChecked)
            {
                userAnswersListener!!.setAnswer(questionId!!, "checked")
            }else
            {
                userAnswersListener!!.setAnswer(questionId!!, "unchecked")
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireBottomSheetStateAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_questions_bottom_sheet_questionnaire, parent, false)

        return QuestionnaireBottomSheetStateAdapter.ViewHolder(view, this)
    }

    override fun onBindViewHolder(
        holder: QuestionnaireBottomSheetStateAdapter.ViewHolder,
        position: Int
    ) {



        val checkBoxChoices = Objects.requireNonNull(questionsUnitsList)!!

        holder.checkBox!!.setText(checkBoxChoices.get(position).text + "    " + checkBoxChoices.get(position).id)
        holder.questionId = checkBoxChoices.get(position).id


        if(userQuestionsUnitsList != null)
        {
            userQuestionsUnitsList!!.forEachIndexed { index, questionnaireQuestions ->
                if(questionnaireQuestions.questionnaire_questions_id ==  holder.questionId && describedPassage?.id == questionnaireQuestions.passage_id)
                {
                    println(" holder.questionId " + holder.questionId)
                    holder.checkBox!!.isChecked = true
                }
            }
        }





    }

    override fun getItemCount(): Int {
        val checkBoxChoices = Objects.requireNonNull(questionsUnitsList)!!
        return checkBoxChoices.size
    }

    override fun setAnswer(id: Int, answer: String) {
        if(answer.equals("checked"))
        {
            if(!listOFUserCheckBoxChoices.contains(id))
            {
                listOFUserCheckBoxChoices.add(id)

                println(listOFUserCheckBoxChoices)
            }

        }else{
            if(listOFUserCheckBoxChoices.contains(id))
            {
                listOFUserCheckBoxChoices.remove(id)
                println(listOFUserCheckBoxChoices)
            }
        }

    }

    override fun setAnswer(id: Int) {

    }
}