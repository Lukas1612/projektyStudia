package com.example.readyreadingkotlin.touched_text_view_facade

import android.graphics.Rect
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.example.readyreadingkotlin.InterfaceRefreshListPoint
import com.example.readyreadingkotlin.Point
import kotlin.properties.Delegates

class TouchableTextViewPreparer(var textView: TextView, var iOnTextTouchedHandler: IOnTextTouchedHandler)  {


    init
    {
        val spans: Spannable = textView.text as Spannable

        var indices: IntArray = getIndices(textView.text.toString(), ' ', '\n', '.', ',', '"', '!', '?', '“', ':', ';', '(', ')', '{', '}', '[', ']')

        var start: Int = 0
        var end: Int = 0

        for (i in 0 until indices.size){
            var clickableSpan: ClickableSpan = getClickableSpan()

            // to cater last/only word
            end = if (i < indices.size) indices[i] else spans.length
            spans.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            start = end + 1
        }
    }

    fun getIndices(s: String, vararg cs: Char): IntArray{

        var pos: Int
        var indices = mutableListOf<Int>()

        for (c in cs)
        {
            pos = s.indexOf(c, 0)
            while (pos != -1) {
                indices.add(pos)
                pos = s.indexOf(c, pos + 1)
            }

        }

        indices.add(s.length)

        indices.sort()
        return indices.toIntArray()
    }


    fun getClickableSpan(): ClickableSpan {

        return object:  ClickableSpan()
        {
            override fun onClick(widget: View) {

                var tv: TextView = widget as TextView


                //get text coordinates
               val point = coordinates(tv, tv.selectionStart,  tv.selectionEnd)

                //get selected String
                val text = tv.text
                        .subSequence(tv.selectionStart,
                                tv.selectionEnd).toString()



                // the returning answer method is here
                iOnTextTouchedHandler.onTextTouched(point.x, point.y, text)



            }

            override fun updateDrawState(ds: TextPaint) {
                //do nothing
            }

        }
    }

    fun coordinates(parentTextView: TextView, startOffsetOfClickedText: Int, endOffsetOfClickedText: Int): Point
    {


        var parentTextViewRect = Rect()

        // Initialize values for the computing of clickedText position

        // Initialize values for the computing of clickedText position
        val completeText = parentTextView.text as SpannableString
        val textViewLayout: Layout = parentTextView.layout

        //val startOffsetOfClickedText = completeText.getSpanStart(this).toDouble()
       // val endOffsetOfClickedText = completeText.getSpanEnd(this).toDouble()
        val startXCoordinatesOfClickedText: Float = textViewLayout.getPrimaryHorizontal(startOffsetOfClickedText)
        val endXCoordinatesOfClickedText: Float = textViewLayout.getPrimaryHorizontal(endOffsetOfClickedText)


        // Get the rectangle of the clicked text


        // Get the rectangle of the clicked text
        val currentLineStartOffset: Int = textViewLayout.getLineForOffset(startOffsetOfClickedText)
        val currentLineEndOffset: Int = textViewLayout.getLineForOffset(endOffsetOfClickedText)
        val keywordIsInMultiLine = currentLineStartOffset != currentLineEndOffset
        textViewLayout.getLineBounds(currentLineStartOffset, parentTextViewRect)


        // Update the rectangle position to his real position on screen


        // Update the rectangle position to his real position on screen
        val parentTextViewLocation = intArrayOf(0, 0)
        parentTextView.getLocationOnScreen(parentTextViewLocation)

        val parentTextViewTopAndBottomOffset = (parentTextViewLocation[1] -
                parentTextView.scrollY +
                parentTextView.compoundPaddingTop).toDouble()
        parentTextViewRect.top += parentTextViewTopAndBottomOffset.toInt()
        parentTextViewRect.bottom += parentTextViewTopAndBottomOffset.toInt()

        parentTextViewRect.left += parentTextViewLocation[0] +
                startXCoordinatesOfClickedText.toInt()  +
                parentTextView.compoundPaddingLeft -
                parentTextView.scrollX
        parentTextViewRect.right = (parentTextViewRect.left +
                endXCoordinatesOfClickedText.toInt()  -
                startXCoordinatesOfClickedText.toInt())

        var x: Int = (parentTextViewRect.left + parentTextViewRect.right) / 2
        val y: Int = parentTextViewRect.bottom
        if (keywordIsInMultiLine) {
            x = parentTextViewRect.left
        }

        return Point(x, y)

    }
}