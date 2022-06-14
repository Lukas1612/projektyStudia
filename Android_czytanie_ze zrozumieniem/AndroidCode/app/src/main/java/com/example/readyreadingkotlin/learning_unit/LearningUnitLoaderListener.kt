package com.example.readyreadingkotlin.learning_unit

import com.example.readyreadingkotlin.described_passage.DescribedPassage

interface LearningUnitLoaderListener {

    fun update(unitsList: MutableList<LearningUnit>)
}