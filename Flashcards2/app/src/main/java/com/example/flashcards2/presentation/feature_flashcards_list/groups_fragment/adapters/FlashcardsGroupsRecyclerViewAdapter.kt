package com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards2.R

class FlashcardsGroupsRecyclerViewAdapter(val onFlashcardGroupItemClickListener: OnFlashcardGroupItemClickListener):  RecyclerView.Adapter<FlashcardsGroupsRecyclerViewAdapter.ViewHolder>() {


    private val diffUtil = object : DiffUtil.ItemCallback<GroupAdapterItem>() {
        override fun areItemsTheSame(oldItem: GroupAdapterItem, newItem: GroupAdapterItem): Boolean {
            return oldItem.group.id == newItem.group.id
        }

        override fun areContentsTheSame(oldItem: GroupAdapterItem, newItem: GroupAdapterItem): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(dataResponse: List<GroupAdapterItem>) {
        asyncListDiffer.submitList(dataResponse)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var groupName: TextView = view.findViewById(R.id.groupName)
        val linearLayoutGroup: LinearLayout = view.findViewById(R.id.linearLayoutGroup)

        fun bindListener(onFlashcardGroupItemClickListener: OnFlashcardGroupItemClickListener, item: GroupAdapterItem){
            linearLayoutGroup.setOnClickListener {
                onFlashcardGroupItemClickListener.onShortClick(item.group.id!!)
            }

            linearLayoutGroup.setOnLongClickListener{
                onFlashcardGroupItemClickListener.onLongClick(item.group.id!!)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flashcard_group, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]

        if(item.isSelected){
            holder.linearLayoutGroup.setBackgroundColor(Color.Blue.toArgb())
        }else{
            holder.linearLayoutGroup.setBackgroundColor(Color.Yellow.toArgb())

        }

        holder.groupName.text = item.group.name
        holder.bindListener(onFlashcardGroupItemClickListener, item)
    }



    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }


}