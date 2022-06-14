package com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.readyreadingkotlin.database.DbHelper
import com.example.readyreadingkotlin.FullPassageMenu.ui.DecribedPassageViewModel
import com.example.readyreadingkotlin.R

class FlashcardFragment : Fragment() {

    val name: String = "Stefan"
    val password: String = "qwerty"

    private val viewModel: DecribedPassageViewModel by activityViewModels()

    var editTextWord: EditText? = null
    var editTextTranslation: EditText? = null
    var editTextExampleSentence: EditText? = null
    var editTextTranslatedSentence: EditText? = null

    var textViewWord: TextView? = null
    var textViewTranslation: TextView? = null
    var textViewExampleSentece: TextView? = null
    var textViewTranslatedSentence: TextView? = null
    var saveButton: Button? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {



        val root = inflater.inflate(R.layout.fragment_flashcard, container, false)

        editTextWord = root.findViewById(R.id.editTextWord)
        editTextTranslation = root.findViewById(R.id.editTextTranslation)
        editTextExampleSentence = root.findViewById(R.id.editTextExampleSentence)
        editTextTranslatedSentence = root.findViewById(R.id.editTextTranslatedSentence)

        textViewWord = root.findViewById(R.id.textViewWord)
        textViewTranslation = root.findViewById(R.id.textViewTranslation)
        textViewExampleSentece = root.findViewById(R.id.textViewExampleSentece)
        textViewTranslatedSentence = root.findViewById(R.id.textViewTranslatedSentence)

        saveButton = root.findViewById(R.id.saveButton)

        saveButton?.setOnClickListener(View.OnClickListener {

            if(editTextWord?.text != null &&
                    editTextWord?.text.toString().length > 0 &&
                    editTextTranslation?.text != null &&
                    editTextTranslation?.text.toString().length > 0)
                    {
                        DbHelper().addNewFlashcard(editTextWord?.text.toString(), editTextTranslation?.text.toString(), editTextExampleSentence?.text.toString(), editTextTranslatedSentence?.text.toString(), name, password, 1)
                        clear()
            }
        })



        return root
    }

    fun clear()
    {
        editTextWord?.setText("")
        editTextTranslation?.setText("")
        editTextExampleSentence?.setText("")
        editTextTranslatedSentence?.setText("")

    }

}