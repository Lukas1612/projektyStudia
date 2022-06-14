package com.example.readyreadingkotlin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.readyreadingkotlin.learning_unit.LearningUnit

class HomeViewModel : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<List<LearningUnit>?>()
    val selectedItem: LiveData<List<LearningUnit>?> get() = mutableSelectedItem

    fun selectItem(item: List<LearningUnit>?) {
        mutableSelectedItem.value = item
    }

}