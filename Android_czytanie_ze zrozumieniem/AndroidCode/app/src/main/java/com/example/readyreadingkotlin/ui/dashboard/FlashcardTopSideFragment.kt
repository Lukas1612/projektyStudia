package com.example.readyreadingkotlin.ui.dashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.Flashcard
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.Flashcards_in_groups
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.auth.ApiClient
import com.example.readyreadingkotlin.auth.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlashcardTopSideFragment : Fragment() {

    companion object {
        fun newInstance() = FlashcardTopSideFragment()
    }

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager



    var buttonDeleteFlashcard: Button? = null

    var flashcardTvTop: TextView? = null

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

    fun getGroupId(): Int {
        return requireArguments()!!.getInt("group_id")
    }

    private lateinit var viewModel: FlashcardTopSideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.flashcard_top_side_fragment, container, false)

        var flashcard = getFlashcard()



        flashcardTvTop = view.findViewById(R.id.flashcardTvTop)
        flashcardTvTop!!.setText(flashcard!!.word)

        apiClient = ApiClient()
        sessionManager = SessionManager(flashcardTvTop!!.context)

        buttonDeleteFlashcard = view.findViewById(R.id.buttonDeleteFlashcard)

        if( getGroupId() == 1)
        {
            buttonDeleteFlashcard!!.setOnClickListener(View.OnClickListener {
                deleteFlashcards(flashcard!!.id!!)
            })
        }else
        {
            buttonDeleteFlashcard!!.setVisibility(View.GONE)
        }



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FlashcardTopSideViewModel::class.java)
        // TODO: Use the ViewModel
    }

   fun autenthicateAndDeleteFlashcard(data: Int) {

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

                            }
                        }

                    }
                })

    }

}