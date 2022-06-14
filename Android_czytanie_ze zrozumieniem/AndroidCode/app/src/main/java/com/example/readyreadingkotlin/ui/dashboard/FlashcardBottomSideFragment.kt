package com.example.readyreadingkotlin.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.Flashcard
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.Flashcards_in_groups
import com.example.readyreadingkotlin.R

class FlashcardBottomSideFragment : Fragment() {

    companion object {
        fun newInstance() = FlashcardBottomSideFragment()
    }

    var wordFlashcardB: TextView? = null
    var translationTVFlashcardB: TextView? = null
    var sentenceTVFlashcardB: TextView? = null
   var translatedSentenceTVFlashcardB: TextView? = null


    fun newInstance(flashcard: Flashcard): FlashcardBottomSideFragment? {
        val f = FlashcardBottomSideFragment()

        // Supply index input as an argument.
        val args = Bundle()
        args.putParcelable("flashcard", flashcard)
        f.arguments = args
        return f
    }

    fun getFlashcard(): Flashcard? {
        return requireArguments()!!.getParcelable<Flashcard>("flashcard")
    }
    private lateinit var viewModel: FlashcardBottomSideViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
         var view = inflater.inflate(R.layout.flashcard_bottom_side_fragment, container, false)

        var flashcard= getFlashcard()

        wordFlashcardB = view.findViewById(R.id.wordFlashcardB)
        wordFlashcardB!!.setText(flashcard!!.word)

        translationTVFlashcardB = view.findViewById(R.id.translationTVFlashcardB)
        translationTVFlashcardB!!.setText(flashcard!!.translation)

        sentenceTVFlashcardB = view.findViewById(R.id.sentenceTVFlashcardB)
        sentenceTVFlashcardB!!.setText(flashcard!!.example_sentence)


        translatedSentenceTVFlashcardB = view.findViewById(R.id.translatedSentenceTVFlashcardB)
        translatedSentenceTVFlashcardB!!.setText(flashcard!!.translated_sentence)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FlashcardBottomSideViewModel::class.java)
        // TODO: Use the ViewModel
    }

}