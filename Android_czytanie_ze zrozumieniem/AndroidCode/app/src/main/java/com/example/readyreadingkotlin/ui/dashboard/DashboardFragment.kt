package com.example.readyreadingkotlin.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.*
import com.example.readyreadingkotlin.Mediator
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.auth.ApiClient
import com.example.readyreadingkotlin.auth.LoginActivity
import com.example.readyreadingkotlin.auth.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardFragment : Fragment(), Mediator {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    var current_flashcards_id: Int? = null

    var call_from_adapter: Boolean = false

    private var  dashBoardRecyclerView: RecyclerView? = null

    var flashcards_in_groups: List<Flashcards_in_groups>? = null

    var fabF: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })

        dashBoardRecyclerView = root.findViewById<RecyclerView>(R.id.dashBoardRecyclerView)
        fabF = root.findViewById(R.id.fabF)

        fabF!!.setOnClickListener(View.OnClickListener {
            setCurrentFragment(FlashcardFragment())
        })

        apiClient = ApiClient()
        sessionManager = SessionManager(root.context)

        val intent = Intent(getActivity(), LoginActivity::class.java)
        startActivity(intent)


        if(sessionManager.fetchAuthToken() != null && sessionManager.fetchAuthToken()!!.length>5)
        {
            println(" flashcards:  " + sessionManager.fetchAuthToken())
            getFlashcards()
        }


        return root
    }

    private fun setCurrentFragment(fragment: Fragment)=
        getParentFragmentManager().beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            commit()
        }



    private fun getFlashcards(){


        // Pass the token as parameter
        apiClient.getApiService().getFlashcardsInGroups(token = "Bearer ${sessionManager.fetchAuthToken()}")
                .enqueue(object : Callback<List<Flashcards_in_groups>> {
                    override fun onFailure(call: Call<List<Flashcards_in_groups>>, t: Throwable) {
                        // Error fetching posts
                        println(" ----error " + t.toString() + "  " + call.toString())
                    }

                    override fun onResponse(
                        call: Call<List<Flashcards_in_groups>>,
                        response: Response<List<Flashcards_in_groups>>
                    ) {

                        println(" --- " + response.body())
                        if (response != null && response.body() != null && response!!.body()!!.size > 0) {
                            flashcards_in_groups = response.body()!!

                            if (call_from_adapter == false) {
                                setGroupsAdapter()
                            } else {
                                setFlashcardsAdapter(current_flashcards_id!!)
                                call_from_adapter = false
                            }

                        }

                    }
                })

    }

    fun setFlashcardsAdapter(id: Int)
    {
        flashcards_in_groups!!.forEachIndexed { index, flashcardsInGroups ->
            if(flashcardsInGroups!!.group!!.id!! == id)
            {
                var flashcardsAdapter: FlashcardsAdapter = FlashcardsAdapter(
                    flashcardsInGroups.flashcards!!,
                    this,
                    context
                )
                dashBoardRecyclerView!!.adapter = flashcardsAdapter
                dashBoardRecyclerView!!.layoutManager = LinearLayoutManager(context)
            }
        }
    }

    fun setGroupsAdapter()
    {
        var flashcardsInGroupsAdapter: FlashcardsGroupsAdapter = FlashcardsGroupsAdapter(
            flashcards_in_groups!!,
                requireActivity().applicationContext,
            this
        )
        dashBoardRecyclerView!!.adapter = flashcardsInGroupsAdapter
        dashBoardRecyclerView!!.layoutManager = LinearLayoutManager(context)

    }

    override fun notify(sender: String, event: String) {


        if( sender.equals("unit"))
        {
             current_flashcards_id = event.toInt()
            setFlashcardsAdapter(current_flashcards_id!!)

        }

    }

    override fun notify(event: String) {
        if( event.equals("delete"))
        {
            call_from_adapter = true
            val intent = Intent(getActivity(), LoginActivity::class.java)
            startActivity(intent)


            if(sessionManager.fetchAuthToken() != null && sessionManager.fetchAuthToken()!!.length>5)
            {
                println(" flashcards:  " + sessionManager.fetchAuthToken())

                getFlashcards()
            }

        }
    }
}