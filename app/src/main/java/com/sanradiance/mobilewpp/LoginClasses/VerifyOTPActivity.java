package com.sanradiance.mobilewpp.LoginClasses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyOTPActivity extends AppCompatActivity {

    EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4,otpDigit5,otpDigit6;
    Button verifyOtpButton;
    private static StringBuilder otpEntered = new StringBuilder();
    private String userDetailsString;
    private Long userMobileNumber;
    SharedPreferences sharedPre;
    private ProgressBar spinner;
     String mobile_number;



    private static String Verifyotp_URL ="https://domytaxonline.com.au/shuddha-neeru-demo/public/api/auth/verifyOTP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        Bundle bundle = getIntent().getExtras();
          mobile_number = bundle.getString("mobile_number");

        otpDigit1 = findViewById(R.id.otpDigit1);

        TextView operatorMobileNumberTv = findViewById(R.id.operatormobile_value);
        operatorMobileNumberTv.setText(mobile_number);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        sharedPre= getSharedPreferences("user_save", Context.MODE_PRIVATE);
        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final String fullOTP = otpDigit1.getText().toString();
                    if (fullOTP.length() < 1) {
                        Toast.makeText(getApplicationContext(), "Please enter verification code", Toast.LENGTH_LONG).show();
                    }else if(fullOTP.length() < 6){
                        Toast.makeText(getApplicationContext(), "Please enter valid verification code", Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("verify","success8528");
                        verifyOtpButton.setEnabled(false);
                        verifyOtpButton.setBackgroundColor(Color.parseColor("#F4F4F4"));
                        spinner.setVisibility(View.VISIBLE);
                        try {
                            JSONObject paramJson = new JSONObject();
                            paramJson.put("mobile", mobile_number);
                            paramJson.put("otp", fullOTP);
                            Log.i("verify","calling success8528");
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Verifyotp_URL, paramJson, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.d("Response", response.toString());
                                    Log.i("verify","before try");
                                    try {
                                        Log.i("verify","inside try");
                                        JSONObject loginResponse = new JSONObject(response.toString());
                                        String success = loginResponse.getString("success");
                                        Log.i("verify",success);
                                        if (success.equals("true")) {
                                            String userAccessToken = loginResponse.getString("access_token");
                                            JSONObject userDetails = loginResponse.getJSONObject("user");

                                            String accountType = userDetails.getString("account_type");
                                            if (accountType.equals("operator")) {
                                                try {
                                                    SharedPreferences.Editor editor = sharedPre.edit();
                                                    editor.putString("user_session_save", response.toString());
                                                    editor.commit();
                                                    Intent intent = new Intent(VerifyOTPActivity.this, OperatorDashboardActivity.class);
                                                    intent.putExtra("userdetails", response.toString());
                                                    startActivity(intent);
                                                    finish();
                                                }catch(Exception e){
                                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                                }
                                                //end session

                                            } else if (accountType.equals("admin")) {
                                                SharedPreferences.Editor editor = sharedPre.edit();
                                                editor.putString("user_session_save", response.toString());
                                                editor.commit();
                                                Intent intent = new Intent(VerifyOTPActivity.this, CommissionerDashboard.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (accountType.equals("commissioner")) {
                                                SharedPreferences.Editor editor = sharedPre.edit();
                                                editor.putString("user_session_save", response.toString());
                                                editor.commit();
                                                Intent intent = new Intent(VerifyOTPActivity.this, CommissionerDashboard.class);
                                               // intent.putExtra("userdetails", response.toString());
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                verifyOtpButton.setEnabled(true);
                                                verifyOtpButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                                spinner.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Invalid member", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            verifyOtpButton.setEnabled(true);
                                            verifyOtpButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            spinner.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                                        e.printStackTrace();

                                    }
                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error", error.toString());
                                    verifyOtpButton.setEnabled(true);
                                    verifyOtpButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    spinner.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),  error.toString(), Toast.LENGTH_LONG).show();

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

                            RequestQueue requestQueue = Volley.newRequestQueue(VerifyOTPActivity.this);
                            requestQueue.add(jsonObjectRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        });

    }
    public void onBackPressed() {

        Intent intent = new Intent(VerifyOTPActivity.this,LoginActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
