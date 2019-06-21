package com.sanradiance.mobilewpp.LoginClasses;

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
    SharedPreferences pref;
    private ProgressBar spinner;



    private static String Verifyotp_URL ="https://domytaxonline.com.au/shuddha-neeru-demo/public/api/auth/verifyOTP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);


        Bundle bundle = getIntent().getExtras();
        final String mobile_number = bundle.getString("mobilenumber");

//          userDetailsString = getIntent().getStringExtra("userdetails");
//        userMobileNumber = getIntent().getLongExtra("mobilenumber",0);
//        Log.d("User details:",userDetailsString);

        otpDigit1 = findViewById(R.id.otpDigit1);
        otpDigit2 = findViewById(R.id.otpDigit2);
        otpDigit3 = findViewById(R.id.otpDigit3);
        otpDigit4 = findViewById(R.id.otpDigit4);
        otpDigit5 = findViewById(R.id.otpDigit5);
        otpDigit6 = findViewById(R.id.otpDigit6);
//
        TextView operatorMobileNumberTv = findViewById(R.id.operatormobile_value);
////        String existingText = operatorMobileNumberTv.getText().toString();
////        existingText+= String.valueOf(userMobileNumber);
        operatorMobileNumberTv.setText(mobile_number);
//
        verifyOtpButton = findViewById(R.id.verifyOtpButton);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        otpDigit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==1){
                    otpEntered.deleteCharAt(0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==0&&otpDigit1.length()==1){
                    otpEntered.append(charSequence);
//                    otpDigit1.clearFocus();
                    otpDigit2.requestFocus();
                    otpDigit2.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(otpEntered.length()==0)
                    otpDigit1.requestFocus();
            }
        });

        otpDigit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==2){
                    otpEntered.deleteCharAt(1);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==1&&otpDigit1.length()==1){
                    otpEntered.append(charSequence);
//                    otpDigit2.clearFocus();
                    otpDigit3.requestFocus();
                    otpDigit3.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(otpEntered.length()==1)
                    otpDigit2.requestFocus();
            }
        });

        otpDigit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==3){
                    otpEntered.deleteCharAt(2);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==2&&otpDigit1.length()==1){
                    otpEntered.append(charSequence);
//                    otpDigit3.clearFocus();
                    otpDigit4.requestFocus();
                    otpDigit4.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(otpEntered.length()==2)
                    otpDigit3.requestFocus();
            }
        });

        otpDigit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==4){
                    otpEntered.deleteCharAt(3);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==3&&otpDigit1.length()==1){
                    otpEntered.append(charSequence);
                   // otpDigit3.clearFocus();
                    otpDigit5.requestFocus();
                    otpDigit5.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(otpEntered.length()==3)
                    otpDigit4.requestFocus();
            }
        });



        otpDigit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==5){
                    otpEntered.deleteCharAt(4);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==4&&otpDigit1.length()==1){
                    otpEntered.append(charSequence);
//                   otpDigit3.clearFocus();
                    otpDigit6.requestFocus();
                    otpDigit6.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(otpEntered.length()==4)
                    otpDigit4.requestFocus();
            }
        });


        otpDigit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==6){
                    otpEntered.deleteCharAt(5);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpEntered.length()==5&&otpDigit1.length()==1){
                    otpEntered.append(charSequence);
                   otpDigit5.clearFocus();
//                    otpDigit6.requestFocus();
//                    otpDigit6.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(otpEntered.length()==5)
                    otpDigit5.requestFocus();
            }
        });



        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullOTP = otpEntered.toString();
                if (fullOTP.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter verification code", Toast.LENGTH_LONG).show();
                } else {
                    verifyOtpButton.setEnabled(false);
                    verifyOtpButton.setBackgroundColor(Color.parseColor("#F4F4F4"));
                    spinner.setVisibility(View.VISIBLE);
                    try {

                        JSONObject paramJson = new JSONObject();

                        paramJson.put("mobile", mobile_number);
                        paramJson.put("otp", otpEntered);

                        // paramJson.put("remember_me", true);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Verifyotp_URL, paramJson, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("Response", response.toString());
                                try {
                                    JSONObject loginResponse = new JSONObject(response.toString());
                                    String success = loginResponse.getString("success");

                                    if (success.contains("true")) {
                                        String userAccessToken = loginResponse.getString("access_token");
                                        JSONObject userDetails = loginResponse.getJSONObject("user");

                                        String accountType = userDetails.getString("account_type");
                                        if (accountType.contains("operator")) {
                                            Intent intent = new Intent(VerifyOTPActivity.this, OperatorDashboardActivity.class);
                                            intent.putExtra("userdetails", response.toString());
                                            startActivity(intent);
                                            finish();
                                        } else if (accountType.contains("admin")) {
                                            Intent intent = new Intent(VerifyOTPActivity.this, CommissionerDashboard.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (accountType.contains("commissioner")) {
                                            Intent intent = new Intent(VerifyOTPActivity.this, CommissionerDashboard.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            verifyOtpButton.setEnabled(true);
                                            verifyOtpButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                            spinner.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "invalid operator", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        verifyOtpButton.setEnabled(true);
                                        verifyOtpButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                        spinner.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", error.toString());
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
}




//                String fullOTP = otpEntered.toString();
//                if (fullOTP.equals("1725")) {
//                    Intent intent = new Intent(VerifyOTPActivity.this, OperatorDashboardActivity.class);
//                    intent.putExtra("userdetails", userDetailsString);
//                    startActivity(intent);
//                    finish();
//                } else if (fullOTP.length() < 1) {
//                    Toast.makeText(getApplicationContext(), "Please enter verification code", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Invalid OTP! Try again!", Toast.LENGTH_SHORT).show();
//                }
//            }