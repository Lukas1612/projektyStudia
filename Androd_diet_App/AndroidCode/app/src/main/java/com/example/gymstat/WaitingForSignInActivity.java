package com.example.gymstat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WaitingForSignInActivity extends AppCompatActivity implements AuthenticateActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_sign_in);

        Bundle extras = getIntent().getExtras();

        authenticate(extras.getString("name"),  extras.getString("password"),  WaitingForSignInActivity.this);

    }



    @Override
    public void setUserData(String key, Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);


        String url = "http://10.0.2.2:8080/app/users";

        JsonArrayRequest jsonArrayRequest = new  JsonArrayRequest(Request.Method.GET, url,  new JSONArray(),
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        Intent intent = new Intent();
                        intent.putExtra(Intent.EXTRA_TEXT, response.toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                queue.stop();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ key);
                return params;
            }
        };

        queue.add(jsonArrayRequest);

    }


    @Override
    public void _finish()
    {
        finish();
    }

}