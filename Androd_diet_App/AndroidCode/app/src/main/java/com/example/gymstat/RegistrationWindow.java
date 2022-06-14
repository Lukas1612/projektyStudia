package com.example.gymstat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

public class RegistrationWindow extends AppCompatActivity {

    private android.widget.EditText editTextLogin;
    private android.widget.Button button_reg_and_back_to_login;
    private android.widget.EditText editTextTextPassword3;
    private android.widget.EditText editTextAge;
    private android.widget.EditText editTextWeight;
    private android.widget.EditText editTextHeight;
    private android.widget.RadioButton radioMan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_window);

        button_reg_and_back_to_login = findViewById(R.id.button_reg_and_back_to_login);
        editTextLogin = (EditText)findViewById(R.id.editTextLogin);
        editTextTextPassword3 = (EditText)findViewById(R.id.editTextTextPassword3);
        editTextAge = (EditText)findViewById(R.id.editTextAge);
        editTextWeight = (EditText)findViewById(R.id.editTextWeight);
        editTextHeight = (EditText)findViewById(R.id.editTextHeight);

        radioMan = (RadioButton) findViewById(R.id.radioMan);

        button_reg_and_back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkUserData()){
                    getUserData();
                    openMainActivity();
                } else {
                    System.out.println("Bledne Dane");
                    openMainActivity();
                }
                }

        });

    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public Boolean checkUserData(){

        if(String.valueOf(editTextLogin.getText()).equals("")
                || String.valueOf(editTextTextPassword3.getText()).equals("")
                || String.valueOf(editTextAge.getText()).equals("")
                || String.valueOf(editTextWeight.getText()).equals("")
                || String.valueOf(editTextHeight.getText()).equals("")
        ){
            return false;
        } else {
            return true;
        }
        }

    public void getUserData(){

        String gender;

        if(radioMan.isChecked()){
            gender = "man";
        } else {
            gender = "woman";
        }



       //("users/gender={gender}&weight={weight}&height={height}&age={age}&name={name}&password={password}")
        final String url = "http://10.0.2.2:8080/app/users/gender=" + gender +
               "&weight=" +  String.valueOf(editTextWeight.getText()) +
               "&height=" + String.valueOf(editTextHeight.getText()) +
               "&age=" + String.valueOf(editTextAge.getText()) +
                "&name=" + String.valueOf(editTextLogin.getText()) +"&password=" + String.valueOf(editTextTextPassword3.getText());

        System.out.println(url);





        RequestQueue queue = Volley.newRequestQueue(RegistrationWindow.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error8", error.toString());
            }
        });

        queue.add(stringRequest);









        /*
        MainActivity.setUserData(1, String.valueOf(editTextLogin.getText())
                , String.valueOf(editTextTextPassword3.getText())
                , Integer.parseInt(String.valueOf(editTextAge.getText()))
                , Integer.parseInt(String.valueOf(editTextWeight.getText()))
                , Integer.parseInt(String.valueOf(editTextHeight.getText()))
                , gender );*/
    }
}