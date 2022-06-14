package com.example.readyreadingkotlin.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.readyreadingkotlin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var cancelButton: Button
    private lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.progressBar)
        cancelButton = findViewById(R.id.cancelButton)
        cancelButton.isVisible = true

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        getToken()

        cancelButton.setOnClickListener(View.OnClickListener {
            finish()
        })


    }

    fun getToken() {
        apiClient.getApiService().login(LoginRequest(name = "Stefan", password = "qwerty"))
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Error logging in
                    progressBar.isVisible = true
                    cancelButton.isVisible = true
                    finish()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val loginResponse = response.body()

                    progressBar.isVisible = false
                    cancelButton.isVisible = false

                    if(loginResponse!=null)
                    {
                        sessionManager.saveAuthToken(loginResponse)
                        sessionManager.saveName( "Stefan")
                        sessionManager.savePassword("qwerty")
                        finish()
                    }
                }
            })
    }
}

