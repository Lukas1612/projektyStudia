package com.example.flashcards2.presentation.feature_flashcards_list.helpers

abstract class SelectionBehaviourHelper <T: Any> {


    private var toolbarVisibility: Boolean = false
    private lateinit var itemSelector: ItemSelector<T>

    init {
        itemSelector = object : ItemSelector<T>(){
            override fun getItemId(item: T): Long {
                return this@SelectionBehaviourHelper.getItemId(item)
            }

            override fun createSelectedItemFromItem(item: T): T {
                return this@SelectionBehaviourHelper.createSelectedItemFromItem(item)
            }

            override fun createUnselectedItemFromItem(item: T): T {
                return this@SelectionBehaviourHelper.createUnelectedItemFromItem(item)
            }

            override fun doOnItemListChanged(updatedItemList: List<T>) {
                itemSelector.itemList = updatedItemList.toMutableList()
                changeToolBarVisibility()
                updateViewData(updatedItemList)
            }
        }

    }

    fun updateItemList(items: List<T>)
    {
        itemSelector.itemList = items.toMutableList()
    }

    fun isIdSelected(itemId: Long): Boolean{
        return itemSelector.isIdSelected(itemId)
    }

    fun clearSelectedMenuItems() {
        itemSelector.clearSelectedItems()
    }

     fun onLongClickedItem(itemId: Long) {

        val clickedItem = getItemById(itemId)

        if(isToolBarVisible()) {
            if(!itemSelector.isItemSelected(clickedItem))
            {
                itemSelector.selectItem(clickedItem)

            }
        }else{
            itemSelector.selectItem(clickedItem)
        }
    }

     fun onShortClickedItem(
         itemId: Long,
        doOnToolbarInvisible: () -> Unit
    ) {

        val clickedItem = getItemById(itemId)

        if(isToolBarVisible()) {
            if(itemSelector.isItemSelected(clickedItem))
            {
                itemSelector.unselectItem(clickedItem)
            }else
            {
                itemSelector.selectItem(clickedItem)
            }

        }else{
            doOnToolbarInvisible()
        }
    }

     private fun isToolBarVisible(): Boolean {
        return toolbarVisibility
    }


     fun changeToolBarVisibility() {
        if(toolbarVisibility && itemSelector.selectedItemListIsEmpty()){
            toolbarVisibility = false
            doOnToolBarVisibilityChanged(toolbarVisibility)
        }else if(!toolbarVisibility && itemSelector.selectedItemListIsNoTEmpty()){
            toolbarVisibility = true
            doOnToolBarVisibilityChanged(toolbarVisibility)
        }
    }


     fun deleteSelectedMenuItems(
        deleteSelectedItemData: (itemId: Long) -> Unit
    ) {
        itemSelector.forEachSelectedId { id ->
            deleteSelectedItemData(id)
        }

         itemSelector.clearSelectedItems()
    }

    private fun getItemById(id: Long): T{
       return itemSelector.itemList.find { getItemId(it) == id}!!
    }

    protected abstract fun updateViewData(items: List<T>)
    protected abstract fun doOnToolBarVisibilityChanged(isToolBarVisible: Boolean)
    protected abstract fun getItemId(item: T): Long
    protected abstract fun createSelectedItemFromItem(item: T): T
    protected abstract fun createUnelectedItemFromItem(item: T): T
}