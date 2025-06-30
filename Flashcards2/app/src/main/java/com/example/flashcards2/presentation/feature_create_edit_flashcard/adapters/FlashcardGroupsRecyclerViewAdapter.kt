package com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards2.R
import com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters.interfaces.OnGroupCreatorItemClickListener
import com.example.flashcards2.presentation.feature_create_edit_flashcard.adapters.interfaces.OnFlashcardGroupItemClickListener


class FlashcardGroupsRecyclerViewAdapter(private val items: List<GroupsAdapterListItem>, private val onFlashcardGroupItemClickListener: OnFlashcardGroupItemClickListener, private val onGroupCreatorItemClickListener: OnGroupCreatorItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val LAYOUT_TYPE_GROUP_CREATOR = 0
    private val LAYOUT_TYPE_FLASHCARD_GROUP = 1


    class GroupCreatorViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val layout: LinearLayout = view.findViewById(R.id.linearLayoutGroup)

        fun bindListener(onGroupCreatorItemClickListener: OnGroupCreatorItemClickListener)
        {
            layout.setOnClickListener {
                onGroupCreatorItemClickListener.onClick()
            }
        }
    }

    class FlashcardGroupViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val layout: LinearLayout = view.findViewById(R.id.linearLayoutGroup)
        val groupNameTextView: TextView = view.findViewById(R.id.groupNameTextView)

        fun bindListener(onFlashcardGroupItemClickListener: OnFlashcardGroupItemClickListener, groupName: String)
        {
            layout.setOnClickListener {
                onFlashcardGroupItemClickListener.onClick(groupName)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val curItem = items[position]
        return when(curItem)
        {
            is GroupsAdapterListItem.GroupCreatorItem -> {
                LAYOUT_TYPE_GROUP_CREATOR
            }

            is GroupsAdapterListItem.FlashcardGroupItem -> {
                LAYOUT_TYPE_FLASHCARD_GROUP
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_TYPE_GROUP_CREATOR -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_create_flashcard_group, parent, false)
                GroupCreatorViewHolder(view)
            }
            LAYOUT_TYPE_FLASHCARD_GROUP -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_choose_flashcard_group, parent, false)
                FlashcardGroupViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_choose_flashcard_group, parent, false)
                FlashcardGroupViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curItem = items[position]

        when(curItem){
            is GroupsAdapterListItem.GroupCreatorItem -> {
                val groupCreatorViewHolder = holder as GroupCreatorViewHolder

                groupCreatorViewHolder.bindListener(onGroupCreatorItemClickListener)
            }

            is GroupsAdapterListItem.FlashcardGroupItem -> {
                val flashcardGroupViewHolder = holder as FlashcardGroupViewHolder

                flashcardGroupViewHolder.groupNameTextView.text = curItem.group.name
                flashcardGroupViewHolder.bindListener(onFlashcardGroupItemClickListener, curItem.group.name)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}