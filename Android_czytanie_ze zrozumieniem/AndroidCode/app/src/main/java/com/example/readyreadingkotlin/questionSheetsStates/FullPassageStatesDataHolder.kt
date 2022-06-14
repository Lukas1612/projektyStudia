package com.example.readyreadingkotlin.questionSheetsStates

import com.example.readyreadingkotlin.described_passage.DescribedPassage

class FullPassageStatesDataHolder(val describedPassage: DescribedPassage?) {
    var firstAnswers : HashMap<Int, String>? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var secondAnswers: HashMap<Int, String>? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var howTheAnswerChanged: HashMap<Int, Int>? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }
}