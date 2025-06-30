package com.example.locationdescriptor.location_descriptor.presentation.photos.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.locationdescriptor.R
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.presentation.photos.interfaces.*


class PhotosRecyclerViewAdapter(private val imageLoader: PhotosRVAdapterImageLoader, /*private val imagePath: String,*/ private val onPhotoClickListener: OnPhotoClickListener, private val onFolderClickListener: OnFolderClickListener, private val onParentClickListener: OnParentClickListener, private val onOptionsMenuClickListener: OnOptionsMenuClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    private val LAYOUT_TYPE_PARENT = 0
    private val LAYOUT_TYPE_FOLDER = 1
    private val LAYOUT_TYPE_PHOTO = 2

    lateinit var deleteAlertDialogBuilder: AlertDialog.Builder

    private val diffUtil = object : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(
            oldItem: ListItem,
            newItem: ListItem
        ): Boolean {
            when(oldItem)
            {
                is ListItem.ParentFolderItem -> {

                    return when(newItem) {
                        is ListItem.ParentFolderItem -> {
                            true
                        }

                        is ListItem.PhotoItem -> {
                            false
                        }

                        is ListItem.FolderItem -> {
                            false
                        }
                    }

                }

                is ListItem.PhotoItem -> {

                    return when(newItem) {
                        is ListItem.ParentFolderItem -> {
                            false
                        }

                        is ListItem.PhotoItem -> {
                            oldItem.photoDescription.id == newItem.photoDescription.id
                        }

                        is ListItem.FolderItem -> {
                            false
                        }
                    }
                }

                is ListItem.FolderItem -> {
                    return when(newItem) {
                        is ListItem.ParentFolderItem -> {
                            false
                        }

                        is ListItem.PhotoItem -> {
                            false
                        }

                        is ListItem.FolderItem -> {
                            oldItem.name == newItem.name
                        }
                    }
                }
            }
        }

        override fun areContentsTheSame(
            oldItem: ListItem,
            newItem: ListItem
        ): Boolean {
            when(oldItem)
            {
                is ListItem.ParentFolderItem -> {

                    return when(newItem) {
                        is ListItem.ParentFolderItem -> {
                            true
                        }

                        is ListItem.PhotoItem -> {
                            false
                        }

                        is ListItem.FolderItem -> {
                            false
                        }
                    }

                }

                is ListItem.PhotoItem -> {

                    return when(newItem) {
                        is ListItem.ParentFolderItem -> {
                            false
                        }

                        is ListItem.PhotoItem -> {
                            oldItem.isSelected == newItem.isSelected && oldItem.photoDescription == newItem.photoDescription
                        }

                        is ListItem.FolderItem -> {
                            false
                        }
                    }
                }

                is ListItem.FolderItem -> {
                    return when(newItem) {
                        is ListItem.ParentFolderItem -> {
                            false
                        }

                        is ListItem.PhotoItem -> {
                            false
                        }

                        is ListItem.FolderItem -> {
                            oldItem.isSelected == newItem.isSelected && newItem.name == oldItem.name
                        }
                    }
                }
            }
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(dataResponse: List<ListItem>) {
        asyncListDiffer.submitList(dataResponse)
    }

    class PhotoViewHolder(view: View): RecyclerView.ViewHolder(view)
        {
            val thumbnail: ImageView
            val title: TextView
            val photoCount: TextView
            val textViewOptions: TextView
            val checkBox: CheckBox

             fun bindListener(onPhotoClickListener: OnPhotoClickListener, description: PhotoDescription)
            {
                thumbnail.setOnClickListener { onPhotoClickListener.onClick(description) }
            }


           init {
               thumbnail = view.findViewById(R.id.thumbnail)
               title = view.findViewById(R.id.title)
               photoCount = view.findViewById(R.id.photoCount)
               textViewOptions = view.findViewById(R.id.textViewOptions)
               checkBox = view.findViewById(R.id.checkBox)
               checkBox.isEnabled = false
               checkBox.isChecked = true
               checkBox.visibility = View.GONE

           }

        }

    class FolderViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val title: TextView
        val textViewOptions: TextView
        val thumbnail: ImageView
        val checkBox: CheckBox

         fun bindListener(onFolderClickListener: OnFolderClickListener, name: String)
        {
            thumbnail.setOnClickListener {onFolderClickListener.onClick(name)}
        }


        init {
            title = view.findViewById(R.id.nameTextview)
            textViewOptions = view.findViewById(R.id.textViewOptions)
            thumbnail = view.findViewById(R.id.thumbnail)
            checkBox = view.findViewById(R.id.checkBox)
            checkBox.isEnabled = false
            checkBox.isChecked = true
            checkBox.visibility = View.GONE
        }

    }

    class ParentViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val title: TextView

        fun bindListener(onParentClickListener: OnParentClickListener)
        {
            itemView.setOnClickListener {onParentClickListener.onClick()}
        }

        init {
            title = view.findViewById(R.id.nameTextview)
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        deleteAlertDialogBuilder = AlertDialog.Builder(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return when (viewType) {
            LAYOUT_TYPE_PHOTO -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.photos_item, parent, false)
                PhotoViewHolder(view)
            }
            LAYOUT_TYPE_PARENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.parent_item, parent, false)
                ParentViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.foder_item, parent, false)
                FolderViewHolder(view)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val curItem = asyncListDiffer.currentList[position]

        when(curItem){
            is ListItem.ParentFolderItem ->
            {
                val folderHolder = holder as ParentViewHolder
                val folderName = ".."
                folderHolder.title.text = folderName
                folderHolder.bindListener(onParentClickListener)
            }

            is ListItem.FolderItem -> {
                val folderHolder = holder as FolderViewHolder
                val folderName = curItem.name
                folderHolder.title.text = folderName
                folderHolder.bindListener(onFolderClickListener, folderName)

                if(curItem.isSelected)
                {
                    folderHolder.checkBox.visibility = View.VISIBLE
                }else
                {
                    folderHolder.checkBox.visibility = View.GONE
                }

                folderHolder.textViewOptions.setOnClickListener {view ->
                    showPopupMenu(view, curItem)
                }
            }

            is ListItem.PhotoItem -> {

                val photoHolder = holder as PhotoViewHolder
                val description = curItem.photoDescription
                val title: String = description.title

                if(curItem.isSelected)
                {
                    photoHolder.checkBox.visibility = View.VISIBLE
                }else
                {
                    photoHolder.checkBox.visibility = View.GONE
                }

                photoHolder.bindListener(onPhotoClickListener, description)


                photoHolder.textViewOptions.setOnClickListener {view ->
                    showPopupMenu(view, curItem)
                }

                photoHolder.title.text = title
                photoHolder.photoCount.text = description.id.toString()
               // val completePath = "$imagePath/$title.jpg"

                imageLoader.loadImageToThumbnail(photoHolder.thumbnail, title)

               /* glide.asBitmap()
                    .load(completePath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .sizeMultiplier(0.4f)
                    .into(photoHolder.thumbnail)*/

            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val curItem = asyncListDiffer.currentList[position]
        return when(curItem)
        {
             is ListItem.FolderItem -> {
                LAYOUT_TYPE_FOLDER
            }

            is ListItem.ParentFolderItem -> {
                LAYOUT_TYPE_PARENT
            }

            is ListItem.PhotoItem -> {
                LAYOUT_TYPE_PHOTO
            }
        }
    }



    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }


    private fun showPopupMenu(view: View, curItem: ListItem)
    {

        val popupMenu = PopupMenu(context, view)

        popupMenu.inflate(R.menu.popup_menu)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.action_delete -> {
                        deleteAlertDialogBuilder.setMessage("are you sure you want to delete the selected item?")
                            .setTitle("deleting item")
                            .setCancelable(false)
                            .setNegativeButton("cancel"){dialog, which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("ok"){dialog, which ->

                                onOptionsMenuClickListener.onDelete(curItem)
                                dialog.dismiss()
                            }.show()

                        return true
                    }

                    R.id.action_select -> {
                        onOptionsMenuClickListener.onSelect(curItem)
                        return true
                    }
                }

                return false
            }

        })

        popupMenu.show()

    }

}




