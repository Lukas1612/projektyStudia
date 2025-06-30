package com.example.locationdescriptor.location_descriptor.presentation.photos.folders_tree

import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription


typealias Visitor = (Folder) -> Unit
class Folder(var name: String, var photoDescriptions: MutableList<PhotoDescription>, var parentName: String = "", var depth: Int = 0) {

    private val children: MutableList<Folder> = mutableListOf()

    fun add(child: Folder){

        child.parentName = this.name
        children.add(child)

        increaseDepthForEveryChild()
    }

    private fun increaseDepthForEveryChild()
    {
        children.forEach {
            val childDepth = depth + 1
            it.depth = childDepth

            it.increaseDepthForEveryChild()
        }
    }


    fun forEachDepthFirst(visit: Visitor)
    {
        visit(this)

        children.forEach {
            it.forEachDepthFirst(visit)
        }
    }

   /* fun forEachNonSelectedFolder(selectedFoldersName: List<String>, visit: Visitor)
    {
        visit(this)

        children.forEach {
            if(!selectedFoldersName.contains(it.name))
            {
                it.forEachNonSelectedFolder(selectedFoldersName, visit)
            }

        }
    }*/

    fun forEachNonSelectedFolder(
        isSelectedWhen: (folder: Folder) -> Boolean,
        visit: Visitor
    )
    {
        visit(this)

        children.forEach {
            if(!isSelectedWhen(it))
            {
                it.forEachNonSelectedFolder(isSelectedWhen, visit)
            }

        }
    }


    fun add(child: Folder, parentName: String)
    {
        val node = search(parentName)
        child.parentName = parentName
        node?.add(child)
    }


    fun search(name: String): Folder?{
        var result: Folder? = null
        forEachDepthFirst {
            if(it.name == name)
            {
                result = it
            }
        }
        return result
    }

    fun findParentFolder(folderName: String): Folder?{
        var result: Folder? = null

        forEachDepthFirst {folder ->

            folder.children.forEach { child ->
                if(child.name == folderName)
                {
                    result = folder
                }
            }
        }

        return result
    }



    fun getChildFolders(): List<Folder>
    {
        return children
    }
}