package com.example.readyreadingkotlin.FullPassageMenu.ui.translated

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.readyreadingkotlin.described_passage.DescribedPassage
import com.example.readyreadingkotlin.FullPassageMenu.ui.DecribedPassageViewModel
import com.example.readyreadingkotlin.R

class TranslatedFragment : Fragment() {

    private val viewModel: DecribedPassageViewModel by activityViewModels()
    var translatedPasageTextView: TextView? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var describedPassage: DescribedPassage

        val root = inflater.inflate(R.layout.fragment_translated, container, false)


        translatedPasageTextView = root.findViewById(R.id.translatedPasageTextView) as TextView
        translatedPasageTextView?.setMovementMethod(ScrollingMovementMethod())
        translatedPasageTextView?.setMovementMethod(LinkMovementMethod.getInstance())

        if(viewModel.selectedItem.value != null)
        {
            describedPassage = viewModel.selectedItem.value!!
            translatedPasageTextView?.setText(describedPassage?.translationtext)
        }


        return root
    }

}