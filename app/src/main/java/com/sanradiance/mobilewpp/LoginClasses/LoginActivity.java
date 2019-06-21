package com.sanradiance.mobilewpp.LoginClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sanradiance.mobilewpp.CommissionerViews.CommissionerDashboard;
import com.sanradiance.mobilewpp.OperatorViews.OperatorDashboardActivity;
import com.sanradiance.mobilewpp.R;
import com.sanradiance.mobilewpp.SplashScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phoneNumberField, passwordField;
    TextView errorMessage;
    Button loginButton;
    String dateString;
    SharedPreferences pref;
    private ProgressBar spinner;
    LinearLayout main_layout;


    private static String Sendotp_URL ="https://domytaxonline.com.au/shuddha-neeru-demo/public/api/auth/sendOTP";
    private static String LOGIN_URL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/login";
    final String Details_URL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/dashboard/count/details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateString = sdf.format(date);

        phoneNumberField = findViewById(R.id.phoneNumberField);
        main_layout =(LinearLayout)findViewById(R.id.loginLayout);


        phoneNumberField = findViewById(R.id.phoneNumberField);
       // passwordField = findViewById(R.id.passwordField);
        phoneNumberField.setOnClickListener(this);

        errorMessage = findViewById(R.id.errorMessage);

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.loginButton:
                if (!isNetworkAvailable(getApplicationContext())) {
                    Snackbar.make(findViewById(R.id.loginLayout), "No Internet!", Snackbar.LENGTH_LONG).show();
                }
                String phoneNumber = phoneNumberField.getText().toString();
               // String password = passwordField.getText().toString();
                if (phoneNumber.length() < 1) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Please enter a phone number & try again!");
                }else if (phoneNumber.length() != 10) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Please enter valid phone number & try again!");
//                } else if (password.length() < 1) {
//                    errorMessage.setVisibility(View.VISIBLE);
//                    errorMessage.setText("Please enter a password & try again!");
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundColor(Color.parseColor("#F4F4F4"));
                    phoneNumberField.setEnabled(false);
                    spinner.setVisibility(View.VISIBLE);
                    performLogin(phoneNumber);
                }
                break;
        }
    }

    private Boolean isNetworkAvailable(Context applicationContext) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void performLogin(final String phoneNumber) {
        try {

            JSONObject paramJson = new JSONObject();
            paramJson.put("mobile", phoneNumber);
          //  paramJson.put("password", password);
           // paramJson.put("remember_me", true);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Sendotp_URL, paramJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("Response", response.toString());
                    try {
                        String phoneNumber = phoneNumberField.getText().toString();
                        JSONObject loginResponse = new JSONObject(response.toString());
                        String success = loginResponse.getString("success");
                        if(success.contains("true")){
                            Intent intent = new Intent(LoginActivity.this, VerifyOTPActivity.class);
                            intent.putExtra("mobilenumber",phoneNumber);
                            startActivity(intent);
                            finish();
                        }else{
                            errorMessage.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                            phoneNumberField.setEnabled(true);
                            loginButton.setEnabled(true);
                            loginButton.setBackgroundColor(Color.parseColor("#3F51B5"));

                            errorMessage.setText("Please enter valid phone number");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                    errorMessage.setVisibility(View.VISIBLE);
                    if (errorMessage.toString().contains("NoConnectionError")) {
                        Snackbar.make(findViewById(R.id.loginLayout), "No Internet!", Snackbar.LENGTH_LONG).show();
                    } else
                        errorMessage.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    phoneNumberField.setEnabled(true);
                    loginButton.setEnabled(true);
                    loginButton.setBackgroundColor(Color.parseColor("#3F51B5"));
                        errorMessage.setText("Invalid login details. Please try again!");
                }
            }) {

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    Log.d("Status code", String.valueOf(mStatusCode));
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}