package com.example.readyreadingkotlin.questionSheetsStates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.EditTextNotifier
import com.example.readyreadingkotlin.described_passage.Questions
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.UserAnswersListener
import com.ms.square.android.expandabletextview.ExpandableTextView


class TranslatedBottomSheetAdapter(var answers: String?, var secondAnswer: String?, var howTheAnswerChanged: Int?, var question: Questions, var context: Context):  UserAnswersListener,  RecyclerView.Adapter<TranslatedBottomSheetAdapter.ViewHolder>() {


   // var curPos: Int = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var id:Int = 0
            get() {
                return field
            }
            set(value) {
                field = value
            }
        var listener: UserAnswersListener? = null
            get() {
                return  field
            }
            set(value) {
                field = value
            }


        var howTheAnswerChanged: Int? = null


        var questionTextView: TextView
        var firstAnswerTextView: TextView
        var editTextAnswer: EditText
        var editTextNotifier: EditTextNotifier? = null

        var nothingBox: RadioButton? = null
        var slightlyBox: RadioButton? = null
        var partlyBox: RadioButton? = null
        var completelyBox: RadioButton? = null

        var expTv: ExpandableTextView? = null

        fun addCorrectAnswer(answer: String?)
        {
            expTv!!.setText("The correct answer: \n" + answer)
        }




        init {
            expTv = view.findViewById(R.id.expand_text_view)


            questionTextView = view.findViewById(R.id.questionTextView) as TextView
            firstAnswerTextView = view.findViewById(R.id.firstAnswerTextView) as TextView
            editTextAnswer = view.findViewById(R.id.editTextAnswer) as EditText


            nothingBox = view.findViewById(R.id.nothingBox) as RadioButton
            slightlyBox = view.findViewById(R.id.slightlyBox) as RadioButton
            partlyBox = view.findViewById(R.id.partlyBox) as RadioButton
            completelyBox = view.findViewById(R.id.completelyBox) as RadioButton


            nothingBox!!.setOnClickListener(View.OnClickListener {
                    howTheAnswerChanged = 0
                    listener!!.setAnswer(0)
            })

            slightlyBox!!.setOnClickListener(View.OnClickListener {
                howTheAnswerChanged = 1
                listener!!.setAnswer(1)

            })

            partlyBox!!.setOnClickListener(View.OnClickListener {
                howTheAnswerChanged = 2
                listener!!.setAnswer(2)

            })

            completelyBox!!.setOnClickListener(View.OnClickListener {
                howTheAnswerChanged = 3
                listener!!.setAnswer(3)

            })
        }

        fun setRadioButton(n: Int)
        {
            when (n) {
                0 -> nothingBox!!.isChecked = true
                1 -> slightlyBox!!.isChecked = true
                2 -> partlyBox!!.isChecked = true
                3 -> completelyBox!!.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_questions_bottom_sheet_translated, parent, false)

        if( secondAnswer == null)
        {
            secondAnswer = ""
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        if(question != null)
        {
            holder.id = question.id
            holder.listener = this
            holder.questionTextView.text = question.question
            holder.editTextAnswer.setText("")
            holder.addCorrectAnswer(question!!.answer!!)

        }


        if(answers != null)
        {
            holder.firstAnswerTextView.text = answers
        }

        if(secondAnswer != null)
        {
            holder.editTextAnswer.setText(secondAnswer)
        }

        if(howTheAnswerChanged != null)
        {
            holder.setRadioButton(howTheAnswerChanged!!)
            holder.howTheAnswerChanged = howTheAnswerChanged
        }

        holder.editTextNotifier =  EditTextNotifier(holder.editTextAnswer, this, holder.id)

    }

    override fun getItemCount(): Int {
       return 1
    }

    override fun setAnswer(id: Int, answer: String) {

       secondAnswer = answer
        println("  zzzzzz  id:  " + id + " an: " + answer + "  sec: " + secondAnswer)
    }

    override fun setAnswer(rb: Int) {

        howTheAnswerChanged = rb
    }



}