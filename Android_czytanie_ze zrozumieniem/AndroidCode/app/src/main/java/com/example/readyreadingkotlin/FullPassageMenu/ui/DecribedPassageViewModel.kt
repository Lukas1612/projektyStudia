package com.example.readyreadingkotlin.FullPassageMenu.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.readyreadingkotlin.described_passage.DescribedPassage

class DecribedPassageViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<DescribedPassage>()
    val selectedItem: LiveData<DescribedPassage> get() = mutableSelectedItem

    fun selectItem(item: DescribedPassage) {
        mutableSelectedItem.value = item
    }
}