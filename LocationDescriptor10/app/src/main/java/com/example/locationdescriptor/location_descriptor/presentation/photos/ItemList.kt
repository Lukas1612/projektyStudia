package com.example.locationdescriptor.location_descriptor.presentation.photos

import com.example.locationdescriptor.location_descriptor.domain.model.FolderDescriptor
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.presentation.photos.adapters.ListItem
import com.example.locationdescriptor.location_descriptor.presentation.photos.folders_tree.Folder

class ItemList {

    private var rootFolder: Folder = Folder("root", mutableListOf<PhotoDescription>())
    private var curFolder: Folder = Folder("root",  mutableListOf<PhotoDescription>())

    private var items: List<ListItem>? = emptyList()

    private var photoDescriptions: List<PhotoDescription> = emptyList()
    private var folderDescriptors: List<FolderDescriptor> = emptyList()

    private val selectedFolderNames = mutableListOf<String>()
    private val selectedPhotoDescriptions = mutableListOf<PhotoDescription>()

    //flags
    private var selectedFolderNameYouAreIn: String? = null
    private var theFirstItemHasBeenSelected: Boolean = false

    fun getSelectedFoldersNames(): List<String>{
        return selectedFolderNames
    }


    fun getSelectedPhotoDescriptions(): List<PhotoDescription>{
        return selectedPhotoDescriptions
    }

    fun getCurFolder(): Folder
    {
        return curFolder
    }

    fun setPhotoDescriptions(_photoDescriptions: List<PhotoDescription>){
        photoDescriptions = _photoDescriptions
    }

    fun setFolderDescriptors(_folderDescriptors: List<FolderDescriptor>){
        folderDescriptors = _folderDescriptors
    }

    fun notifyDataSetChanged()
    {
        prepareRootFolder()
        makeFoldersTree()
        prepareItemList()
    }

    fun getItemList(): List<ListItem>?
    {
        return items
    }

    fun searchFolderByName(folderName: String): Folder?
    {
        return rootFolder.search(folderName)
    }

     fun unselectAll() {
        val _items = items?.toMutableList()
        if(selectedFolderNames.isNotEmpty())
        {
            selectedFolderNames.clear()
        }

        if(selectedPhotoDescriptions.isNotEmpty())
        {
            selectedPhotoDescriptions.clear()
        }

        for(pos in 0 until _items!!.size)
        {
            val item = _items[pos]
            when(item)
            {
                is ListItem.PhotoItem -> {
                    val newItem =  ListItem.PhotoItem(item.photoDescription, false)
                    _items[pos] = newItem
                }

                is ListItem.FolderItem -> {
                    val newItem = ListItem.FolderItem(item.name, false)
                    _items[pos] = newItem
                }

                else -> {}
            }
        }

         selectedFolderNameYouAreIn = null
         theFirstItemHasBeenSelected = false

        items = _items
    }

     fun goToTheClickedFolder(folderName: String) {
        if (folderNameExists(folderName)) {

            if(selectedFoldersNameContains(folderName) && selectedFolderNameYouAreIn == null)
            {
                selectedFolderNameYouAreIn = folderName
            }

            curFolder = getFolderByItsName(folderName)

            prepareItemList()
        }
    }

    fun getFolderByItsName(folderName: String): Folder
    {
        return rootFolder.search(folderName)!!
    }

    fun folderNameExists(folderName: String): Boolean{
        return rootFolder.search(folderName) != null
    }

     fun selectPhotoItem(selectedItem: ListItem.PhotoItem) {
        var _items = items!!.toMutableList()
        val position = _items.indexOf(selectedItem)

        val isSelected = !selectedItem.isSelected

        val updatedItem = ListItem.PhotoItem(selectedItem.photoDescription, isSelected)
        _items[position] = updatedItem
        items = _items

         //set the flag after selecting the first item
         theFirstItemHasBeenSelected = thereIsNoSelectedItem() && isSelected

         if (isSelected) {
             selectedPhotoDescriptions.add(selectedItem.photoDescription)

         } else {
             selectedPhotoDescriptions.remove(selectedItem.photoDescription)
         }
    }

     fun selectFolderItem(selectedItem: ListItem.FolderItem) {
        var _items = items!!.toMutableList()
        val position = _items.indexOf(selectedItem)

        val isSelected = !selectedItem.isSelected

        val updatedItem = ListItem.FolderItem(selectedItem.name, isSelected)
        _items[position] = updatedItem

         items = _items

         //set the flag after selecting the first item
         theFirstItemHasBeenSelected = thereIsNoSelectedItem() && isSelected

         if (isSelected) {
             selectedFolderNames.add(updatedItem.name)

         } else {
             selectedFolderNames.remove(updatedItem.name)
         }
    }

    fun theFirstItemHasBeenSelected(): Boolean
    {
        return theFirstItemHasBeenSelected
    }

    fun thereIsNoSelectedItem(): Boolean
    {
        return selectedFolderNames.isEmpty() && selectedPhotoDescriptions.isEmpty()
    }

    private fun prepareFolderList(): MutableList<Folder>?
    {
        var folders: MutableList<Folder> = mutableListOf<Folder>()

        folderDescriptors.forEach { folderDescriptor ->
            val foldersPhotoDescriptions = getAllPhotoDescriptionsForGivenFolderByItsName(folderDescriptor.name)

            val folder = Folder(
                name = folderDescriptor.name,
                foldersPhotoDescriptions,
            )

            folders.add(folder)

        }

        return folders
    }

    private fun getAllPhotoDescriptionsForGivenFolderByItsName(folderName: String): MutableList<PhotoDescription>{
        val folderPhotoDescriptions = mutableListOf<PhotoDescription>()

        photoDescriptions.forEach { photoDescription ->
            if(photoDescription.folderName == folderName)
            {
                folderPhotoDescriptions.add(photoDescription)
            }
        }

        return folderPhotoDescriptions
    }

    private fun prepareRootFolder()
    {

        rootFolder =  Folder("root", mutableListOf<PhotoDescription>())

       val rootFolderPhotoDescriptions = getAllPhotoDescriptionsForGivenFolderByItsName("root")

        rootFolder.photoDescriptions = rootFolderPhotoDescriptions
    }

    private fun makeFoldersTree(){
        val folders = prepareFolderList()

        folderDescriptors.forEach {descriptor ->
            val folder = folders?.find { it.name == descriptor.name}
            rootFolder.add(folder!!, descriptor.parentName)
        }
    }

     fun goToParentFolder()
    {
        val parentFolder = rootFolder.findParentFolder(curFolder.name)
        if(parentFolder != null) {

            if(curFolder.name == selectedFolderNameYouAreIn)
            {
                selectedFolderNameYouAreIn = null
            }

            curFolder = parentFolder
            prepareItemList()
        }
    }

    fun youAreInsideSelectedFolder(): Boolean{
        return selectedFolderNameYouAreIn != null
    }

    fun selectedFoldersNameContains(folderName: String): Boolean
    {
        return selectedFolderNames.contains(folderName)
    }

     fun selectedPhotosList(): List<PhotoDescription>
    {
        val _selectedPhotos: MutableList<PhotoDescription> = selectedPhotoDescriptions.toList().map { it }.toMutableList()

        //add selected photos from selected folders to the list of selected photos
        selectedFolderNames.forEach { name ->
            val folder = rootFolder.search(name)
            folder!!.forEachDepthFirst { childFolder ->
                childFolder!!.photoDescriptions.forEach { description ->

                    if(!_selectedPhotos.contains(description))
                    {
                        _selectedPhotos.add(description)
                    }
                }
            }
        }

        return _selectedPhotos
    }

     fun prepareItemList()
    {
        val _items = mutableListOf<ListItem>()

        curFolder = if(curFolder.name == "root") {
            rootFolder
        }else if(rootFolder.search(curFolder.name) != null) {
            rootFolder.search(curFolder.name)!!
        }else{
            rootFolder
        }

        //add item to the list allowing to navigate to the parent folder
        if(curFolder.name != "root")
        {
            _items.add(ListItem.ParentFolderItem)
        }

        curFolder.getChildFolders().forEach {childFolder ->
            val isSelected: Boolean = selectedFolderNames.contains(childFolder.name)
            _items.add(ListItem.FolderItem(childFolder.name, isSelected))
        }


        curFolder.photoDescriptions.forEach { photoDescription ->
            val isSelected: Boolean  = selectedPhotoDescriptions.contains(photoDescription)
            _items.add(ListItem.PhotoItem(photoDescription, isSelected))

        }

        items = _items
    }

    fun makeAListOfMovedFolderDescriptors(): List<FolderDescriptor>
    {
        val movedFolderDescriptors: MutableList<FolderDescriptor> = mutableListOf()
        if (selectedFolderNames.isNotEmpty()) {

            val curFolderCopy = Folder(
                name = curFolder.name,
                photoDescriptions = mutableListOf<PhotoDescription>(),
                parentName = curFolder.parentName,
                depth = curFolder.depth
            )

            // we add the moved folders to the curFolderCopy
            // to calculate the depth of all the folders
            // and set the parentName variable for all of them

            selectedFolderNames.forEach { name ->

                var selectedFolder = searchFolderByName(name)

                curFolderCopy.add(selectedFolder!!)
            }


            // forEachNonSelectedFolder doesn't visit a child folder
            //if it is selected to avoid adding the same folder
            //many times, it doesn't visit a child folder if selectedFolderNames contains
            //the folder's name
            curFolderCopy.getChildFolders().forEach { childFolder ->
                childFolder.forEachNonSelectedFolder(
                    {
                        folder -> return@forEachNonSelectedFolder selectedFolderNames.contains(folder.name)
                    },

                    {
                            folder ->

                        println(" folder.name " + folder.name)
                        val folderDescriptor = folderDescriptors.find { it.name == folder.name }

                        val id = folderDescriptor!!.id

                        val movedFolderDescriptor = FolderDescriptor(
                            name = folder.name,
                            parentName = folder.parentName,
                            depth = folder.depth,
                            id = id
                        )
                        movedFolderDescriptors.add(movedFolderDescriptor)
                    }
                )
            }

        }

        return movedFolderDescriptors
    }






   fun makeAListOfMovedPhotoDescriptors(): List<PhotoDescription>?{

       val listOfMovedPhotoDescriptors: MutableList<PhotoDescription> = mutableListOf()
       if (selectedPhotoDescriptions.isNotEmpty()) {
           //save the values in the case of if a user changed the current folder while the algorithm is performing in the background
           val curFolderName = curFolder.name
           selectedPhotoDescriptions.forEach { photo ->
               if (photo.folderName != curFolderName) {
                   val description = PhotoDescription(
                       title = photo.title,
                       note = photo.note,
                       latitude = photo.latitude,
                       longitude = photo.longitude,
                       folderName = curFolderName,
                       id = photo.id
                   )

                   listOfMovedPhotoDescriptors.add(description)
               }

           }
       }

       return listOfMovedPhotoDescriptors
   }
}