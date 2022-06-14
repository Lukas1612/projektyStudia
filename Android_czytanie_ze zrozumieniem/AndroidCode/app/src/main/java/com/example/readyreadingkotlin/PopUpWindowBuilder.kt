package com.example.readyreadingkotlin

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.example.readyreadingkotlin.described_passage.Tips

class PopUpWindowBuilder {

    var tipPos: Int = 0
    @SuppressLint("ClickableViewAccessibility")
    fun showHintsPopUp(v: View, location: IntArray, inflater: LayoutInflater, tips: List<Tips>) {
        // inflate the layout of the popup window


        val popupView: View = inflater.inflate(R.layout.popup_hint_window, null)

        var popTextView: TextView = popupView.findViewById(R.id.popTextView)
        var nextButton: Button = popupView.findViewById(R.id.nextButton)
        var backButton: Button = popupView.findViewById(R.id.backButton)

        if(tips != null) {
            if (tips.size > tipPos && tipPos >= 0) {
                popTextView.setText(tips[tipPos!!].text)
            }
        }

        nextButton.setOnClickListener(View.OnClickListener {

            if (tipPos < (tips.size - 1)) {
                ++tipPos
                popTextView.setText(tips[tipPos].text)
            }
        })

        backButton.setOnClickListener(View.OnClickListener {

            if (tipPos > 0) {
                --tipPos
                popTextView.setText(tips[tipPos].text)
            }
        })


        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable: Boolean = true

        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] + 125)

        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }




    fun showTranslationPopUp(v: View, location: IntArray, inflater: LayoutInflater, englishText: String, trnaslatedText: String) {
        // inflate the layout of the popup window


        val popupView: View = inflater.inflate(R.layout.popup_translation_window, null)

        var translatedTextView: TextView = popupView.findViewById(R.id.translatedTextView)
        var englishTextView: TextView = popupView.findViewById(R.id.englishTextView)


        translatedTextView.setText(trnaslatedText)
        englishTextView.setText(englishText)


        var width = LinearLayout.LayoutParams.WRAP_CONTENT
        var height = LinearLayout.LayoutParams.WRAP_CONTENT
        var focusable: Boolean = true

        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] + 125)

        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }



    fun nextCancelPopUp(v: View, inflater: LayoutInflater, mediator: Mediator) {
        // inflate the layout of the popup window

        val popupView: View = inflater.inflate(R.layout.popup_save_first_answer_window, null)

        var saveAnswersButton: Button = popupView.findViewById(R.id.saveAnswersButton)
        var cancelButton: Button = popupView.findViewById(R.id.cancelButton2)



        var width = LinearLayout.LayoutParams.WRAP_CONTENT
        var height = LinearLayout.LayoutParams.WRAP_CONTENT
        var focusable: Boolean = true

        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 100, 700)

        saveAnswersButton.setOnClickListener(View.OnClickListener {
              mediator.notify("saveAnswersButton", "clicked")
            popupWindow.dismiss()
        })

        cancelButton.setOnClickListener(View.OnClickListener {
            popupWindow.dismiss()
        })



        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }






}