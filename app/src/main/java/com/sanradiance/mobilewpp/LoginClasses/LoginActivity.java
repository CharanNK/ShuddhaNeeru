package com.sanradiance.mobilewpp.LoginClasses;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sanradiance.mobilewpp.CommissionerViews.CommissionerDashboard;
import com.sanradiance.mobilewpp.R;

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
        passwordField = findViewById(R.id.passwordField);
        phoneNumberField.setOnClickListener(this);

        errorMessage = findViewById(R.id.errorMessage);

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {



        switch (view.getId()) {
            case R.id.loginButton:
                if (!isNetworkAvailable(getApplicationContext())) {
                    Snackbar.make(findViewById(R.id.loginLayout), "No Internet!", Snackbar.LENGTH_LONG).show();
                }
                String phoneNumber = phoneNumberField.getText().toString();
                String password = passwordField.getText().toString();
                if (phoneNumber.length() < 1) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Please enter a phone number & try again!");
                }else if (phoneNumber.length() != 10) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Please enter valid phone number & try again!");
                } else if (password.length() < 1) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Please enter a password & try again!");
                } else {
                    performLogin(phoneNumber, password);
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
            paramJson.put("password", password);
            paramJson.put("remember_me", true);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, paramJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("Response", response.toString());
                    try {
                        JSONObject loginResponse = new JSONObject(response.toString());
                        JSONObject userDetails = loginResponse.getJSONObject("user");
                        Long mobileNumber = userDetails.getLong("mobile");
                        String accountType = userDetails.getString("account_type");
                        if (accountType.contains("operator")) {
                            Intent intent = new Intent(LoginActivity.this, VerifyOTPActivity.class);
                            intent.putExtra("userdetails", response.toString());
                            intent.putExtra("mobilenumber",mobileNumber);
                            startActivity(intent);
                            finish();
                        }else if(accountType.contains("admin")) {
                            Intent intent = new Intent(LoginActivity.this, CommissionerDashboard.class);
                            intent.putExtra("userdetails", response.toString());
                            intent.putExtra("mobilenumber",mobileNumber);
                            startActivity(intent);

                            finish();
                        }else if(accountType.contains("commissioner")){
                            Intent intent = new Intent(LoginActivity.this,CommissionerDashboard.class);
                            intent.putExtra("userdetails",response.toString());
                            intent.putExtra("mobilenumber",mobileNumber);
                            startActivity(intent);
                        }else{
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("Not an member!");
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
