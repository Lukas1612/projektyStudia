package com.example.readyreadingkotlin.questionSheetsStates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.EditTextNotifier
import com.example.readyreadingkotlin.described_passage.Questions
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.UserAnswersListener
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

open class QuestionsBottomSheetAdapter(var userAnswers : HashMap<Int, String>?, var questionsList: List<Questions>, var context: Context): UserAnswersListener, RecyclerView.Adapter<QuestionsBottomSheetAdapter.ViewHolder>() {

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

        //nothing, slightly, partly, completly
        var questionTextView: TextView
        var editTextAnswer: EditText




        val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        var editTextNotifier: EditTextNotifier? = null


        init {
               questionTextView = view.findViewById(R.id.questionTextView) as TextView
               editTextAnswer = view.findViewById(R.id.editTextAnswer) as EditText
            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_questions_bottom_sheet, parent, false)

        if( userAnswers == null)
        {
            userAnswers = HashMap<Int, String> ()
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.questionTextView.setText(questionsList.get(position).question)
        holder.id = questionsList.get(position).id
        holder.listener = this


        if(userAnswers?.size!! > 0)
        {
            if(userAnswers!!.containsKey(questionsList.get(position).id))
            {
                holder.editTextAnswer.setText(userAnswers!![questionsList.get(position).id])
            }
            holder.editTextAnswer.setFocusable(false)
        }

        holder.editTextNotifier = EditTextNotifier(holder.editTextAnswer, this,  holder.id)
    }

    override fun getItemCount(): Int {
        return questionsList.size
    }


    override fun setAnswer(id: Int, answer: String) {
        if(!answer.equals(""))
        {
                if(userAnswers != null && userAnswers!!.containsKey(id))
                {
                    userAnswers!!.remove(id)
                }
            userAnswers?.put(id, answer)
        }else
        {
            userAnswers?.remove(id)
        }
    }

    override fun setAnswer(id: Int) {
        TODO("Not yet implemented")
    }
}