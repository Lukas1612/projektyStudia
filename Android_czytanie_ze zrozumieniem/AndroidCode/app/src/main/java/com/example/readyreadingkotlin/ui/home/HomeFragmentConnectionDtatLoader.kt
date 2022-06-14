package com.example.readyreadingkotlin.ui.home

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.auth.ApiClient
import com.example.readyreadingkotlin.auth.LoginActivity
import com.example.readyreadingkotlin.auth.SessionManager
import com.example.readyreadingkotlin.learning_unit.LearningUnit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentConnectionDtatLoader(var dataLoaderListener: IHomeFragmentDataLoaderListener): IHomeFragmentDataLoader {

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager



    private fun getLearningUnits(rcontext: Context){


        // Pass the token as parameter
        apiClient.getApiService().getLearningUnits(token = "Bearer ${sessionManager.fetchAuthToken()}", name = "polish")
                .enqueue(object : Callback<List<LearningUnit>> {
                    override fun onFailure(call: Call<List<LearningUnit>>, t: Throwable) {
                        // Error fetching posts
                        println(" ----error " + t.toString() + "  " + call.toString())
                    }

                    override fun onResponse(call: Call<List<LearningUnit>>, response: Response<List<LearningUnit>>) {

                        println(" --- "  + response.body())

                        if(response != null && response.body() != null)
                        {
                            dataLoaderListener!!.doOnDataLoaded(response!!.body()!!)
                        }
                    }
                })

    }

    override fun load(context: Context) {
        apiClient = ApiClient()
        sessionManager = SessionManager(context)

        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)


        if(sessionManager.fetchAuthToken() != null && sessionManager.fetchAuthToken()!!.length>5)
        {
            println(" Learning Units:  " + sessionManager.fetchAuthToken())
            getLearningUnits(context)
        }
    }



}