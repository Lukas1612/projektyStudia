package com.example.readyreadingkotlin

interface UserAnswersListener {

    fun setAnswer(id: Int, answer: String)
    fun setAnswer(id: Int)
}