package com.example.locationdescriptor.location_descriptor.presentation.photos.folders_tree

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.util.*

//typealias Visitor = (TreeNode) -> Unit
class TreeNode(val name: String, var descriptions: MutableList<Int>,var depth: Int = 0) {
    private val children: MutableList<TreeNode> = mutableListOf()

   /* fun add(child: TreeNode){

        children.add(child)

        val queue = ArrayDeque<TreeNode?>()

        var parentDepth = depth

        children.forEach {
            it.depth = parentDepth + 1
            queue.addLast(it)
        }
        var node: TreeNode? = null

        if(queue.isNotEmpty())
        {
            node = queue.first
            queue.removeFirst()
        }


        while (node != null)
        {
            parentDepth = node.depth

            node.children.forEach {
                it.depth = parentDepth + 1
                queue.addLast(it)
            }


            if(queue.isNotEmpty())
            {
                node = queue.first
                queue.removeFirst()
            }else
            {
                node = null
            }

        }

    }
    fun add(child: TreeNode, parentName: String)
    {
        val node = search(parentName)
        node?.add(child)
    }

    fun addDescription(description: Int) = descriptions.add(description)


    fun forEachDepthFirst(visit: Visitor)
    {
        visit(this)

        children.forEach {
            it.forEachDepthFirst(visit)
        }
    }

  /*  fun forEachLevelOrder(visit: Visitor)
    {
        visit(this)

        val queue = ArrayDeque<TreeNode?>()

        children.forEach {
            queue.addLast(it)
        }

        var node = queue.first

        if(queue.isNotEmpty())
        {
            queue.removeFirst()
        }


        while (node != null)
        {
            visit(node)
            node.children.forEach {
                queue.addLast(it)
            }


            if(queue.isNotEmpty())
            {
                node = queue.first
                queue.removeFirst()
            }else
            {
                node = null
            }

        }
    }*/

    fun search(name: String): TreeNode?{
        var result: TreeNode? = null
        forEachDepthFirst {
            if(it.name == name)
            {
                result = it
            }
        }
        return result
    }

    fun findUpperNode(curNodeNme: String): TreeNode?{
        var result: TreeNode? = null


        forEachDepthFirst {node ->

            node.children.forEach { child ->
                if(child.name == curNodeNme)
                {
                    result = node
                }
            }
        }

        return result
    }

*/

}