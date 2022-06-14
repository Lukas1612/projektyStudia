package com.example.readyreadingkotlin

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class EditTextNotifier(var editText: EditText, var listener: UserAnswersListener, var id:Int) {

    var executorIsRunning: Boolean = false
    private val DELAY: Long = 1000
    val backgroundExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if(s != null)
                {
                    if (/*s.length!! >= 1 &&*/ !executorIsRunning) {

                        // Execute a task in the background thread after 3 seconds.

                        executorIsRunning = true

                        backgroundExecutor.schedule({
                            listener?.setAnswer(id, s.toString())
                            executorIsRunning = false
                        }, DELAY, TimeUnit.MILLISECONDS)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }
}