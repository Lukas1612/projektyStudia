package com.example.readyreadingkotlin

interface Mediator {

    fun notify(sender: String, event: String)
    fun notify(event: String)
}