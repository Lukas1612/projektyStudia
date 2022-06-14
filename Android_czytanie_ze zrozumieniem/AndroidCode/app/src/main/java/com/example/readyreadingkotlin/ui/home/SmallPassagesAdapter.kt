package com.example.readyreadingkotlin.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.IFragmentChanger
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.learning_unit.LearningUnit
import com.example.readyreadingkotlin.ui.units.LearningUnitsFragment

class SmallPassagesAdapter(
        var learningUnits: List<LearningUnit>,
        var fragmentChanger: IFragmentChanger,
        private val maxLength: Int
): RecyclerView.Adapter<SmallPassagesAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var fragmentChanger: IFragmentChanger? = null
        val titleTextView: TextView
        val textTextView: TextView
        var learningUnit: LearningUnit? = null


        init {
            //Define click listener for the ViewHolder's0 View
            view.setOnClickListener {v ->
                var learningUnitsFragment = LearningUnitsFragment()
                learningUnitsFragment.learningUnit = learningUnit
                fragmentChanger!!.changeFragment(learningUnitsFragment)
            }

            textTextView = view.findViewById<View>(R.id.textTextView) as TextView
            titleTextView = view.findViewById<View>(R.id.titleTextView) as TextView
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_small_passages, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.fragmentChanger = fragmentChanger

        if (learningUnits[position].testPassage!!.text?.length!! > maxLength) {
            holder.textTextView.setText(learningUnits[position].testPassage!!.text?.let { makeShortText(it)})
        } else {
            holder.textTextView.setText(learningUnits[position].testPassage!!.text)
        }
        holder.learningUnit = learningUnits?.get(position)
        holder.titleTextView.setText(learningUnits[position].testPassage!!.title)
    }

    fun makeShortText(string: String): String {
        return  string.substring(0, maxLength) + " (...)"
    }

    override fun getItemCount(): Int {
        return learningUnits.size
    }



}
