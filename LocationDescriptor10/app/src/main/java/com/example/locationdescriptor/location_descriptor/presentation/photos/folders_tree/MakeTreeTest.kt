package com.example.locationdescriptor.location_descriptor.presentation.photos.folders_tree

import org.junit.Assert.*


class MakeTreeTest {
 /*   var nodes: Array<TreeNode>? = null

     fun makeTree()
    {
        val tree = TreeNode("root", mutableListOf(0))

          nodes = arrayOf(
            TreeNode("1", mutableListOf(1, 2, 3)),
            TreeNode("2", mutableListOf(8, 8, 1, 2)),
            TreeNode("3", mutableListOf(5, 6, 7)),
            TreeNode("11", mutableListOf(1, 2, 3)),
            TreeNode("12", mutableListOf(8, 8, 1, 2)),
            TreeNode("13", mutableListOf(5, 6, 7)),
            TreeNode("21", mutableListOf(1, 2, 3)),
            TreeNode("22", mutableListOf(8, 8, 1, 2)),
            TreeNode("23", mutableListOf(5, 6, 7)),
            TreeNode("31", mutableListOf(1, 2, 3)),
            TreeNode("32", mutableListOf(8, 8, 1, 2)),
            TreeNode("33", mutableListOf(5, 6, 7)),
            TreeNode("111", mutableListOf(1, 2, 3)),
            TreeNode("112", mutableListOf(8, 8, 1, 2)),
            TreeNode("113", mutableListOf(5, 6, 7)),
            TreeNode("121", mutableListOf(1, 2, 3)),
            TreeNode("122", mutableListOf(8, 8, 1, 2)),
            TreeNode("123", mutableListOf(5, 6, 7)),
            TreeNode("131", mutableListOf(1, 2, 3)),
            TreeNode("132", mutableListOf(8, 8, 1, 2)),
            TreeNode("133", mutableListOf(5, 6, 7)),
            TreeNode("1331", mutableListOf(5, 6, 7))
        )

        val node_1 = TreeNode("1", mutableListOf(1, 2, 3))
        val node_2 = TreeNode("2", mutableListOf(8, 8, 1, 2))
        val node_3 = TreeNode("3", mutableListOf(5, 6, 7))

        val node_11 = TreeNode("11", mutableListOf(1, 2, 3))
        val node_12 = TreeNode("12", mutableListOf(8, 8, 1, 2))
        val node_13 = TreeNode("13", mutableListOf(5, 6, 7))

        val node_21 = TreeNode("21", mutableListOf(1, 2, 3))
        val node_22 = TreeNode("22", mutableListOf(8, 8, 1, 2))
        val node_23 = TreeNode("23", mutableListOf(5, 6, 7))

        val node_31 = TreeNode("31", mutableListOf(1, 2, 3))
        val node_32 = TreeNode("32", mutableListOf(8, 8, 1, 2))
        val node_33 = TreeNode("33", mutableListOf(5, 6, 7))

        val node_111 = TreeNode("111", mutableListOf(1, 2, 3))
        val node_112 = TreeNode("112", mutableListOf(8, 8, 1, 2))
        val node_113 = TreeNode("113", mutableListOf(5, 6, 7))

        val node_121 = TreeNode("121", mutableListOf(1, 2, 3))
        val node_122 = TreeNode("122", mutableListOf(8, 8, 1, 2))
        val node_123 = TreeNode("123", mutableListOf(5, 6, 7))

        val node_131 = TreeNode("131", mutableListOf(1, 2, 3))
        val node_132 = TreeNode("132", mutableListOf(8, 8, 1, 2))
        val node_133 = TreeNode("133", mutableListOf(5, 6, 7))

        val node_1331 = TreeNode("1331", mutableListOf(5, 6, 7))


        node_3.add(node_31)
        node_3.add(node_32)
        node_3.add(node_33)

        node_2.add(node_21)
        node_2.add(node_22)
        node_2.add(node_23)

        tree.add(node_2)
        tree.add(node_3)

        node_11.add(node_111)
        node_11.add(node_112)
        node_11.add(node_113)

        node_13.add(node_131)
        node_13.add(node_132)
        node_13.add(node_133)

        node_1.add(node_11)
        node_1.add(node_13)

        tree.add(node_1)

        tree.add(node_1331, "133")

        node_12.add(node_122)
        node_12.add(node_123)
        node_12.add(node_121)

        tree.add(node_12, "1")




      /*  tree.add(node_1)

        node_133.add(node_1331)

        node_11.add(node_111)
        node_11.add(node_112)
        node_11.add(node_113)

        node_12.add(node_121)
        node_12.add(node_122)
        node_12.add(node_123)

        node_13.add(node_131)
        node_13.add(node_132)
        node_13.add(node_133)

        tree.search("1")!!.add(node_11)
        tree.search("1")!!.add(node_12)
        tree.search("1")!!.add(node_13)

        node_2.add(node_21)
        node_2.add(node_22)
        node_2.add(node_23)

        node_3.add(node_31)
        node_3.add(node_32)
        node_3.add(node_33)


        tree.add(node_2)
        tree.add(node_3)*/




      /*  var table = arrayOf(
            TableRow("1", "root", 1),
            TableRow("2", "root", 1),
            TableRow("3", "root", 1),
            TableRow("11", "1", 2),
            TableRow("12", "1", 2),
            TableRow("13", "1", 2),
            TableRow("21", "2", 2),
            TableRow("22", "2", 2),
            TableRow("23", "2", 2),
            TableRow("31", "3", 2),
            TableRow("32", "3", 2),
            TableRow("33", "3", 2),
            TableRow("111", "11", 3),
            TableRow("112", "11", 3),
            TableRow("113", "11", 3),
            TableRow("121", "12", 3),
            TableRow("122", "12", 3),
            TableRow("123", "12", 3),
            TableRow("131", "13", 3),
            TableRow("132", "13", 3),
            TableRow("133", "13", 3),
            TableRow("1331", "133", 4)
        )

        table.forEach {
            tree.add(findByName(it.name)!!, it.parent!!)
        }


       var d = 1


        println(" xxxxxxxxxxxxxxxxxxxxxxxxxxxx ")

        println(tree.depth)
        println(" " + tree.name + " " + tree.descriptions)
        println("\n")

        while(d<=4)
        {
            println(d)
            table.forEach {
                if(it.depth == d)
                {
                    val node = findByName(it.name)!!
                    println(" " + node.name + " " + node.descriptions)
                }
            }
            println("\n")
            d += 1
        }
        println(" xxxxxxxxxxxxxxxxxxxxxxxxxxxx ")

*/










        var foundNode: TreeNode? = null




        foundNode = tree.findUpperNode("1331")
        assertEquals("133", foundNode?.name)
        assertEquals(3, foundNode?.depth)
        println(">>>>>>>>>>>> result: " + foundNode?.name + " " + foundNode?.depth)

        foundNode = tree.findUpperNode("133")
        assertEquals("13", foundNode?.name)
        assertEquals(2, foundNode?.depth)
        println(">>>>>>>>>>>> result: " + foundNode?.name + " " + foundNode?.depth)

        foundNode = tree.findUpperNode("31")
        assertEquals("3", foundNode?.name)
        assertEquals(1, foundNode?.depth)
        println(">>>>>>>>>>>> result: " + foundNode?.name + " " + foundNode?.depth)

        foundNode = tree.findUpperNode("2")
        assertEquals("root", foundNode?.name)
        assertEquals(0, foundNode?.depth)
        println(">>>>>>>>>>>> result: " + foundNode?.name + " " + foundNode?.depth)


        foundNode = tree.findUpperNode("123")
        assertEquals("12", foundNode?.name)
        assertEquals(2, foundNode?.depth)
        println(">>>>>>>>>>>> result: " + foundNode?.name + " " + foundNode?.depth)

        foundNode = tree.findUpperNode("12")
        assertEquals("1", foundNode?.name)
        assertEquals(1, foundNode?.depth)
        println(">>>>>>>>>>>> result: " + foundNode?.name + " " + foundNode?.depth)

        foundNode = tree.findUpperNode("root")
        assertEquals(null, foundNode?.name)
        assertEquals(null, foundNode?.depth)
        println(">>>>>>>>>>>> result: " + foundNode?.name + " " + foundNode?.depth)


        foundNode = tree.search("122")
        assertEquals("122", foundNode?.name)
        assertEquals(3, foundNode?.depth)
        println(">>>>>>>>>>>> result: " + foundNode?.name + " " + foundNode?.depth)


    }

    fun findByName(name: String): TreeNode?
    {
        var result: TreeNode? = null

        nodes?.forEach {
            if(it.name == name)
            {
                result = it
            }
        }

        return result
    }


*/

}