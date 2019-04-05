package com.sanradiance.mobilewpp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phoneNumberField,passwordField;
    TextView errorMessage;
    Button loginButton;

    private static String LOGIN_URL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumberField = findViewById(R.id.phoneNumberField);
        passwordField = findViewById(R.id.passwordField);

        errorMessage = findViewById(R.id.errorMessage);

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginButton :
                if(!isNetworkAvailable(getApplicationContext())){
                    Snackbar.make(findViewById(R.id.loginLayout),"No Internet!",Snackbar.LENGTH_LONG).show();
                }
                String phoneNumber = phoneNumberField.getText().toString();
                String password = passwordField.getText().toString();

                if(phoneNumber.length()!=10){
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Invalid Phone Number. Please try again!");
                }else if(password.length()<1){
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Please enter the password & try again!");
                }
                else{
                    performLogin(phoneNumber,password);
                }
                break;
        }
    }

    private Boolean isNetworkAvailable(Context applicationContext) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void performLogin(final String phoneNumber, String password) {
        try {

            JSONObject paramJson = new JSONObject();
            paramJson.put("mobile", phoneNumber);
            paramJson.put("password", phoneNumber);
            paramJson.put("remember_me",true);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL,paramJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("Response", response.toString());
                    Intent intent = new Intent(LoginActivity.this,VerifyOTPActivity.class);
                    intent.putExtra("userdetails",response.toString());
                    startActivity(intent);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                    errorMessage.setVisibility(View.VISIBLE);
                    if(errorMessage.toString().contains("NoConnectionError")){
                        Snackbar.make(findViewById(R.id.loginLayout),"No Internet!",Snackbar.LENGTH_LONG).show();
                    }else
                    errorMessage.setText("Invalid login details. Please try again!");
                }
            }) {

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    Log.d("Status code",String.valueOf(mStatusCode));
                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("X-Requested-With", "XMLHttpRequest");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
