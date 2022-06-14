package com.example.readyreadingkotlin.application

import android.app.Application

//import com.facebook.stetho.Stetho;
class QuestionnaireApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        //Stetho is used to view the structure and values in Tables of Database.
        //Connect a device/emulator, run the app and complete the Questionnaire
        //then open Chrome browser and type this in address bar "chrome://inspect/#devices",
        //you will find connected device/emulator in below section of screen.
        //Stetho.initializeWithDefaults(this);
    }

    companion object {
        @get:Synchronized
        var instance: QuestionnaireApp? = null
            private set
    }
}