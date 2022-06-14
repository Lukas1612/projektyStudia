package com.example.readyreadingkotlin

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.fragment.app.FragmentManager

class MyGestureListener(val SWIPE_TRESHOLD: Int, val supportFragmentManager: FragmentManager) :  GestureDetector.SimpleOnGestureListener() {

    override fun onDown(event: MotionEvent): Boolean {
        println("  gesture ")
        return true
    }




    override fun onDoubleTap(e: MotionEvent?): Boolean {
        println("  gesture ")
        return true
    }
    override fun onFling(
            downEvent: MotionEvent?,
            moveEvent: MotionEvent?,
            velocityX: Float,
            velocityY: Float
    ): Boolean {

/*
         val diffY = downEvent!!.x - moveEvent!!.x;


         if (Math.abs(diffY) > SWIPE_TRESHOLD)
        {
            swipeUp()
        }*/
        swipeUp()
        
        return true

    }

    override fun onLongPress(e: MotionEvent?) {
        println("  gesture ")
    }



    fun swipeUp() {
        println("  gesture ")
     //   var questionsBottomDialog = QuestionsBottomDialog()
        //questionsBottomDialog.show(supportFragmentManager, "TAG")
    }

}
