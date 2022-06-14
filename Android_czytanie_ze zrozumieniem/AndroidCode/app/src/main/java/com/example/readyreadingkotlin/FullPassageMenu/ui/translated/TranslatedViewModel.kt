package com.example.readyreadingkotlin.FullPassageMenu.ui.translated

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TranslatedViewModel : ViewModel() {
    private val mutableSelectedString = MutableLiveData<String>()
    val selectedItem: LiveData<String> get() = mutableSelectedString

    fun selectItem(item: String) {
        mutableSelectedString.value = item
    }
}