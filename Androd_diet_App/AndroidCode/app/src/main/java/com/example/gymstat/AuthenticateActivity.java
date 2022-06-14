package com.example.gymstat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

public interface AuthenticateActivity{


    default void authenticate(String login, String Password, Context context) {


        JSONObject jsonBody = new  JSONObject();
        try {
            jsonBody.put("name", login);
            jsonBody.put("password", Password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String mRequestBody =  jsonBody.toString();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://10.0.2.2:8080/app/authenticate";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if(response != null)
                        {
                            setUserData(response, context);
                        }else
                        {
                            _finish();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _finish();
            }
        }){

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

        };

        queue.add(stringRequest);

    }

    void setUserData(String key, Context context);
    void _finish();

}