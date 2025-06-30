package com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment.adapters

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
import com.example.flashcards2.domain.model.Flashcard

class FlashcardsRecyclerViewAdapter(val onFlashcardClickListener: OnFlashcardClickListener):  RecyclerView.Adapter<FlashcardsRecyclerViewAdapter.ViewHolder>() {


    private val diffUtil = object : DiffUtil.ItemCallback<FlashcardListAdapterItem>() {
        override fun areItemsTheSame(oldItem: FlashcardListAdapterItem, newItem: FlashcardListAdapterItem): Boolean {
           return oldItem.flashcard.id == newItem.flashcard.id
        }

        override fun areContentsTheSame(oldItem: FlashcardListAdapterItem, newItem: FlashcardListAdapterItem): Boolean {
            return oldItem == newItem
        }
    }


    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(dataResponse: List<FlashcardListAdapterItem>) {
        asyncListDiffer.submitList(dataResponse)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var topText: TextView = view.findViewById(R.id.TopText)
        var bottomText: TextView = view.findViewById(R.id.BottomText)
        val linearLayoutFlashcard: LinearLayout = view.findViewById(R.id.linearLayoutFlashcard)

        fun bindListener(onFlashcardClickListener: OnFlashcardClickListener, flashcard: Flashcard){
            linearLayoutFlashcard.setOnClickListener {
                onFlashcardClickListener.onShortClick(flashcard.id!!)
            }

            linearLayoutFlashcard.setOnLongClickListener {
                onFlashcardClickListener.onLongClick(flashcard.id!!)
                true
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flashcard, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flashcardItem =  asyncListDiffer.currentList[position]

        if(flashcardItem.isSelected){
            holder.linearLayoutFlashcard.setBackgroundColor(Color.Blue.toArgb())
        }else{
            holder.linearLayoutFlashcard.setBackgroundColor(Color.Yellow.toArgb())
        }

        holder.topText.text = flashcardItem.flashcard.front
        holder.bottomText.text = flashcardItem.flashcard.back

        holder.bindListener(onFlashcardClickListener, flashcardItem.flashcard)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

}