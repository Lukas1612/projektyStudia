package com.example.readyreadingkotlin.ui.home

import android.content.Context
import com.example.readyreadingkotlin.learning_unit.LearningUnit

interface IHomeFragmentDataLoaderListener {

    fun doOnDataLoaded(learningUnits: List<LearningUnit>)
}