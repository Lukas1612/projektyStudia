package com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.Mediator
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.auth.ApiClient
import com.example.readyreadingkotlin.auth.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlashcardsAdapter(var questionsList: List<Flashcard>, var mediator: Mediator, var context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IApiCaller {


    private val TYPE_PUBLIC = 1
    private val TYPE_USER = 2

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    init {
        apiClient = ApiClient()
        sessionManager = SessionManager(context!!)
    }

    class UserFlashcardHolder(view: View,  var apiCaller: IApiCaller?) : RecyclerView.ViewHolder(view) {

        var flashcard_id: Int? = null
        var wordTextView: TextView = view.findViewById(R.id.word)
        var translationTextView: TextView = view.findViewById(R.id.translationTV)
        var sentenceTextView: TextView = view.findViewById(R.id.sentenceTV)
        var translatedSentenceTextView: TextView = view.findViewById(R.id.translatedSentenceTV)
        var deleteButton: Button = view.findViewById(R.id.delateButtonFlashcard)

        init {
            deleteButton.setOnClickListener(View.OnClickListener {
                apiCaller!!.callRestApi(flashcard_id!!)
            })

        }

    }

    class PublicFlashcardHolder(view: View) : RecyclerView.ViewHolder(view) {

        var flashcard_id: Int? = null
        var wordTextView: TextView = view.findViewById(R.id.word2)
        var translationTextView: TextView = view.findViewById(R.id.translationTV2)
        var sentenceTextView: TextView = view.findViewById(R.id.sentenceTV2)
        var translatedSentenceTextView: TextView = view.findViewById(R.id.translatedSentenceTV2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):  RecyclerView.ViewHolder {

        if(viewType == TYPE_USER)
        {
            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_flashcard, parent, false)
            return  UserFlashcardHolder(view, this)
        }else
        {
            val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_flashcard, parent, false)
            return  PublicFlashcardHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder.itemViewType == TYPE_USER)
        {
            (holder as UserFlashcardHolder).flashcard_id = questionsList[position].id
            (holder as UserFlashcardHolder).wordTextView.setText(questionsList[position].word)
            (holder as UserFlashcardHolder).translationTextView.setText(questionsList[position].translation)
            (holder as UserFlashcardHolder).sentenceTextView.setText(questionsList[position].example_sentence)
            (holder as UserFlashcardHolder).translatedSentenceTextView.setText(questionsList[position].translated_sentence)
        }

        if(holder.itemViewType == TYPE_PUBLIC)
        {
            (holder as PublicFlashcardHolder).flashcard_id = questionsList[position].id
            (holder as PublicFlashcardHolder).wordTextView.setText(questionsList[position].word)
            (holder as PublicFlashcardHolder).translationTextView.setText(questionsList[position].translation)
            (holder as PublicFlashcardHolder).sentenceTextView.setText(questionsList[position].example_sentence)
            (holder as PublicFlashcardHolder).translatedSentenceTextView.setText(questionsList[position].translated_sentence)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (questionsList[position].group_id == 1) TYPE_USER else TYPE_PUBLIC
    }

    override fun getItemCount(): Int {
       return questionsList.size
    }

    override fun callRestApi(data: Int) {

        if(sessionManager.fetchAuthToken() != null && sessionManager.fetchAuthToken()!!.length>5)
        {
            println(" flashcards:  " + sessionManager.fetchAuthToken())
            deleteFlashcards(data)
        }
    }

    private fun deleteFlashcards(id: Int){


        // Pass the token as parameter
        apiClient.getApiService().deleteUserFlashcard(token = "Bearer ${sessionManager.fetchAuthToken()}", id)
                .enqueue(object : Callback<Boolean> {
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        // Error fetching posts
                        println(" ----error " + t.toString() + "  " + call.toString())
                    }

                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if(response.body() != null)
                        {
                            if(response.body()!!)
                            {
                                mediator.notify("delete")
                            }
                        }

                    }
                })

    }
}