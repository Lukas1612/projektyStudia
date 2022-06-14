package com.example.readyreadingkotlin.touched_text_view_facade

import androidx.lifecycle.Lifecycle
import com.example.readyreadingkotlin.InterfaceRefreshListString
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlin.properties.Delegates

class TextTranslator(var lifecycle: Lifecycle, var iOnTranslationFinished: IOnTranslationFinished) {


    var englishPolishTranslator: Translator? = null
    var conditions: DownloadConditions? = null
    var translator: Translator? = null

    init {
        // Create an English-POLISH translator:
        val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.POLISH)
                .build()

        englishPolishTranslator = Translation.getClient(options)

        conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

        translator = Translation.getClient(options)
        lifecycle!!.addObserver(translator!!)

    }

    fun translate(foreginText: String){
        englishPolishTranslator!!.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {

                    englishPolishTranslator!!.translate(foreginText).addOnSuccessListener { translatedText ->


                        // the returning answer method is here
                        iOnTranslationFinished.doOnTranslationFinished(translatedText)



                    }
                            .addOnFailureListener { exception ->
                                // Error.
                                // ...

                                println(" OnFailureListener ")
                            }
                }
                .addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    // ...

                    println(" Model couldn’t be downloaded or other internal error. ")
                }
    }


}