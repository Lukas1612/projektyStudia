package com.example.readyreadingkotlin.questions

import com.example.readyreadingkotlin.described_passage.PassagesAnswersTier

interface PassagesAnswersTierLoader {

    fun update(unitsList: MutableList<PassagesAnswersTier>)
}