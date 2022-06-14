package com.example.readyreadingkotlin.FullPassageMenu.ui.english

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.readyreadingkotlin.FullPassageMenu.ui.DecribedPassageViewModel
import com.example.readyreadingkotlin.InterfaceRefreshListString
import com.example.readyreadingkotlin.PopUpWindowBuilder
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.described_passage.DescribedPassage
import com.example.readyreadingkotlin.touched_text_view_facade.*


class EnglishFragment : Fragment() {


    private val viewModel: DecribedPassageViewModel by activityViewModels()
    var englishPasageTextView: TextView? = null
    private lateinit var touchableTextViewPreparer: TouchableTextViewPreparer



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

        var describedPassage: DescribedPassage




        val root = inflater.inflate(R.layout.fragment_english, container, false)

        englishPasageTextView = root.findViewById(R.id.englishPasageTextView) as TextView
        englishPasageTextView?.setMovementMethod(ScrollingMovementMethod())
        englishPasageTextView?.setMovementMethod(LinkMovementMethod.getInstance())

        if(viewModel.selectedItem.value != null)
        {
            describedPassage = viewModel.selectedItem.value!!
            englishPasageTextView?.setText(describedPassage?.text)


            touchableTextViewPreparer = TouchableTextViewPreparer(englishPasageTextView!!, object : IOnTextTouchedHandler {
                override fun onTextTouched(coordinateX: Int, coordinateY: Int, touchedText: String) {

                    TextTranslator(lifecycle!!, object : IOnTranslationFinished{
                        override fun doOnTranslationFinished(translatedText: String)
                        {
                            PopUpWindowBuilder().showTranslationPopUp(englishPasageTextView!!.rootView, intArrayOf(coordinateX, coordinateY), layoutInflater, touchedText,  translatedText)
                        }
                    }).translate(touchedText)
                }
            })

        }

        //*************************

        return root
    }

}