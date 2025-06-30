package com.example.flashcards2.presentation.feature_flashcards_list.helpers


abstract class ItemSelector<T: Any> {

    private var selectedItemsIds: MutableList<Long> = mutableListOf()
    var itemList: MutableList<T> = mutableListOf()

    fun forEachSelectedId(combine: (id: Long) -> Unit) {
        selectedItemsIds.forEach { id ->
            combine(id)
        }
    }

    fun selectedItemListIsEmpty(): Boolean {
        return selectedItemsIds.isEmpty()
    }

    fun selectedItemListIsNoTEmpty(): Boolean {
        return selectedItemsIds.isNotEmpty()
    }

    fun isIdSelected(id: Long): Boolean {
        return selectedItemsIds.contains(id)
    }

    fun isItemSelected(item: T): Boolean {
        return isIdSelected(getItemId(item))
    }

    fun clearSelectedItems() {
        selectedItemsIds = mutableListOf()

        val newList = itemList.map { createUnselectedItemFromItem(it) }
        doOnItemListChanged(newList)
    }

    fun selectItem(item: T) {
        selectedItemsIds.add(getItemId(item))

        val itemsTmp = itemList

        val index = itemsTmp.indexOf(item)
        itemsTmp.remove(item)

        val newItem = createSelectedItemFromItem(item)
        itemsTmp.add(index, newItem)

        doOnItemListChanged(itemsTmp)
    }


    fun unselectItem(item: T) {
        selectedItemsIds.remove(getItemId(item))

        val itemsTmp = itemList

        val index = itemsTmp.indexOf(item)
        itemsTmp.remove(item)

        val newItem = createUnselectedItemFromItem(item)
        itemsTmp.add(index, newItem)

        doOnItemListChanged(itemsTmp)
    }

    protected abstract fun getItemId(item: T): Long
    protected abstract fun createSelectedItemFromItem(item: T): T
    protected abstract fun createUnselectedItemFromItem(item: T): T
    protected abstract fun doOnItemListChanged(updatedItemList: List<T>)
}