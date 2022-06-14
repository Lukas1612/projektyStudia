package com.example.readyreadingkotlin.touched_text_view_facade

interface IOnTextTouchedHandler {
    fun onTextTouched(coordinateX: Int, coordinateY: Int, text: String)
}