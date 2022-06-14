package com.example.readyreadingkotlin.ui.units

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.described_passage.DescribedPassage
import com.example.readyreadingkotlin.learning_unit.LearningUnit


class TheLUnitAdapter(
    val learningUnit: LearningUnit?,
    private val maxLength: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_PASSAGE = 1
    private val TYPE_TEST = 2

    class TestViewHolder(view: View) :  RecyclerView.ViewHolder(view){


        val titleTextView: TextView
        val textTextView: TextView
        var learningUnit: LearningUnit? = null


        init {
            //Define click listener for the ViewHolder's0 View
             view.setOnClickListener {v ->
                val intent = Intent(view.context, TestPassageActivity::class.java)
                 intent.putExtra("UNIT", learningUnit)
                v.context.startActivity(intent)

            }
            textTextView = view.findViewById<View>(R.id.textTextView) as TextView
            titleTextView = view.findViewById<View>(R.id.titleTextView) as TextView
        }

    }

    class PassageViewHolder(view: View) :  RecyclerView.ViewHolder(view) {

        val titleTextView: TextView
        val textTextView: TextView
        var describedPassage: DescribedPassage? = null


        init {
            //Define click listener for the ViewHolder's0 View
            view.setOnClickListener { v ->
                  val intent = Intent(view.context, FullPassageActivity::class.java)
                  intent.putExtra("PASSAGE", describedPassage)
                  v.context.startActivity(intent)

            }
            textTextView = view.findViewById<View>(R.id.textTextView) as TextView
            titleTextView = view.findViewById<View>(R.id.titleTextView) as TextView
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if(viewType == TYPE_PASSAGE)
        {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(com.example.readyreadingkotlin.R.layout.item_small_passages, parent, false)
            return PassageViewHolder(view)
        }else
        {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(com.example.readyreadingkotlin.R.layout.item_small_passages, parent, false)
            return TestViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == TYPE_PASSAGE) {
               (holder as PassageViewHolder).describedPassage = learningUnit?.describedPassages?.get(position)
               (holder as PassageViewHolder).titleTextView.setText(learningUnit?.describedPassages?.get(position)!!.title + " " +learningUnit?.describedPassages?.get(position)!!.id)

            if (learningUnit!!.describedPassages?.get(position)!!.text?.length!! > maxLength) {
                (holder as PassageViewHolder).textTextView.setText(learningUnit?.describedPassages!![position]!!.text?.let { makeShortText(it)})
            } else {
                (holder as PassageViewHolder).textTextView.setText(learningUnit?.describedPassages!![position]!!.text)
            }
        }

        if (holder.itemViewType == TYPE_TEST) {
            (holder as TestViewHolder).learningUnit = learningUnit
            (holder as TestViewHolder).titleTextView.setText(learningUnit?.testPassage!!.title)

            if(!learningUnit!!.userTests!!.isEmpty())
            {
                (holder as TestViewHolder).titleTextView.setText(learningUnit?.testPassage!!.title + "\n"+"score: "  + learningUnit!!.userTests!![0].score)
            }

            if (learningUnit!!.testPassage!!.text?.length!! > maxLength) {
                (holder as TestViewHolder).textTextView.setText(learningUnit?.testPassage!!.text?.let { makeShortText(it)})
            } else {
                (holder as TestViewHolder).textTextView.setText(learningUnit?.testPassage!!.text)
            }
        }




    }

    override fun getItemCount(): Int {
        return learningUnit!!.describedPassages!!.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == (learningUnit!!.describedPassages!!.size)) TYPE_TEST else TYPE_PASSAGE
    }

    fun makeShortText(string: String): String {
        return  string.substring(0, maxLength) + " (...)"
    }

}