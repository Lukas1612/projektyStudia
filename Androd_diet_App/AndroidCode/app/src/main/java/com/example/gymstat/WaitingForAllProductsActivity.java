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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class WaitingForAllProductsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_all_products);

        getAllProducts();

    }


    public void getAllProducts() {

        RequestQueue queue = Volley.newRequestQueue(WaitingForAllProductsActivity.this);


        String url = "http://10.0.2.2:8080/app/products";

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
                finish();
            }
        });

        queue.add(jsonArrayRequest);

    }


}