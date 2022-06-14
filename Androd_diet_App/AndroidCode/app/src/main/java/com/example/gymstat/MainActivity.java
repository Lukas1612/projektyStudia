package com.example.gymstat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private android.widget.Button button_Sign_In;
    private android.widget.Button button_Register;

    private android.widget.TextView logginTextView;
    private android.widget.TextView passwordTextView;

    private static final int SIGNING_UP_ACTIVITY_REQUEST_CODE = 0;

    private static JSONObject user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logginTextView = findViewById(R.id.logginText);
        passwordTextView = findViewById(R.id.passwordText);

        button_Sign_In = findViewById(R.id.button_sign_in);
        button_Sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserPanel();
            }
        });

        button_Register = findViewById(R.id.button_register);
        button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationWindow();
            }
        });
    }

    public void openUserPanel(){

        String loggin = logginTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        setUserData(loggin, password);

        //Intent intent = new Intent(this, user_panel.class);
        //startActivity(intent);
    }

    public void openRegistrationWindow(){
        Intent intent = new Intent(this, RegistrationWindow.class);
        startActivity(intent);
    }

  /*  public void setUserData2( String login, String Password){


        String urlKey = "http://10.0.2.2:8080/app/getKey/name="+ login;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new  JsonObjectRequest(Request.Method.GET,urlKey,
                new JSONObject(),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // textView.setText("start");
                        try {
                            if(response != null)
                            {

                                RSAkey rsaKey = new RSAkey(response.getString("n"), response.getString("e"), response.getInt("random"));
                                String password = Password;
                                byte[] encryptedMessageB = rsaKey.encryptMessage(password.getBytes());
                                String  encryptedMessage = rsaKey.bytesToString(encryptedMessageB);
                                // textView.setText(response.getString("e") + ">>>>>"+response.getString("n") + ">>>>>>>>>>>>>" + encryptedMessage);


                                //******************************


                                //("users/random={random}&name={name}&password={password}")
                                String url ="http://10.0.2.2:8080/app/users/random=" + rsaKey.getRandom() + "&name=" + login + "&password=" + encryptedMessage;





                                JsonArrayRequest jsonArrayRequest = new  JsonArrayRequest(Request.Method.GET,url,
                                        new JSONArray(),
                                        new com.android.volley.Response.Listener<JSONArray>() {

                                            @Override
                                            public void onResponse(JSONArray response) {

                                                System.out.println(response);
                                                JSONObject jsonObject = null;
                                                try {
                                                    jsonObject = response.getJSONObject(0);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    System.out.println("4" + e);
                                                }
                                                System.out.println("response: " + response);
                                                try {
                                                    if(response != null)
                                                    {

                                                        //long id, String gender, int age, int weight, int height, String login, String password
                                                       //user = new User(jsonObject.getLong("id"), jsonObject.getString("gender"), jsonObject.getInt("age"), jsonObject.getInt("weight"), jsonObject.getInt("height"), jsonObject.getString("name"), jsonObject.getString("password"));

                                                       System.out.println(user.toString());

                                                    }else
                                                    {


                                                    }


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    System.out.println("3" + e);

                                                }

                                            }
                                        }, new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        System.out.println("2" + error);


                                    }
                                });

                                queue.add(jsonArrayRequest);






                                //******************************

                            }else
                            {


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("1" + e.toString());

                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        queue.add(jsonObjectRequest);



    }*/


    public void setUserData( String login, String Password){
        Intent intent = new Intent(this, WaitingForSignInActivity.class);
        intent.putExtra("name", login);
        intent.putExtra("password", Password);
        startActivityForResult(intent, SIGNING_UP_ACTIVITY_REQUEST_CODE);
    }

    //waiting for open activitys data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SIGNING_UP_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra(Intent.EXTRA_TEXT);

                try {
                    JSONArray jsonArray = new JSONArray(returnString);
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                    user = jsonObject;
                   // user = new User(jsonObject.getLong("id"), jsonObject.getString("gender"), jsonObject.getInt("age"), jsonObject.getInt("weight"), jsonObject.getInt("height"), jsonObject.getString("name"), jsonObject.getString("password"));

                    Intent intent = new Intent(this, user_panel.class);
                    intent.putExtra("user", user.toString());
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}