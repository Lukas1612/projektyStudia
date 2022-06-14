package com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.Mediator
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.ui.dashboard.FlashcardActivity
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONObject


class FlashcardsGroupsAdapter(var groupsList: List<Flashcards_in_groups>, var context: Context, var mediator: Mediator) : RecyclerView.Adapter<FlashcardsGroupsAdapter.ViewHolder>(), Mediator{

    class ViewHolder(view: View, var mediator: Mediator?) : RecyclerView.ViewHolder(view) {

        var context: Context? = null
        var groupNameTV: TextView = view.findViewById(R.id.groupName)
        var group: Flashcard_groups? = null
            var flashcards_in_groups: Flashcards_in_groups? = null
            get() {
                return field
            }
            set(value) {field = value}


        init {
            view.setOnClickListener { v ->
               // mediator!!.notify("unit", group?.id.toString())

                val gson = Gson()
                val json = gson.toJson(flashcards_in_groups)


                val intent = Intent(context, FlashcardActivity::class.java).apply {
                    putExtra("Flashcards_in_groups", json.toString())
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)

            }

        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_flashcards_group, parent, false)
        return  ViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.groupNameTV.setText(groupsList[position].group!!.title)
        holder.group = groupsList[position].group!!
        holder.flashcards_in_groups = groupsList[position]

        holder.context = context
    }

    override fun getItemCount(): Int {
        return groupsList.size
    }

    override fun notify(sender: String, event: String) {
       // mediator.notify(sender, event)
    }

    override fun notify(event: String) {
    }
}