package com.sanradiance.mobilewpp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class VerifyOTPActivity extends AppCompatActivity {

    EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4;
    Button verifyOtpButton;
    private static StringBuilder otpEntered = new StringBuilder();
    private String userDetailsString;
    private Long userMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        userDetailsString = getIntent().getStringExtra("userdetails");
        userMobileNumber = getIntent().getLongExtra("mobilenumber",0);
        Log.d("User details:",userDetailsString);

        otpDigit1 = findViewById(R.id.otpDigit1);
        otpDigit2 = findViewById(R.id.otpDigit2);
        otpDigit3 = findViewById(R.id.otpDigit3);
        otpDigit4 = findViewById(R.id.otpDigit4);

        TextView operatorMobileNumberTv = findViewById(R.id.operatormobile_value);
        String existingText = operatorMobileNumberTv.getText().toString();
        existingText+= String.valueOf(userMobileNumber);
        operatorMobileNumberTv.setText(existingText);

        verifyOtpButton = findViewById(R.id.verifyOtpButton);






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
                    otpDigit3.clearFocus();
//                    otpDigit4.requestFocus();
//                    otpDigit4.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(otpEntered.length()==3)
                    otpDigit4.requestFocus();
            }
        });

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullOTP = otpEntered.toString();
                if (fullOTP.equals("1725")) {
                    Intent intent = new Intent(VerifyOTPActivity.this, OperatorDashboardActivity.class);
                    intent.putExtra("userdetails", userDetailsString);
                    startActivity(intent);
                    finish();
                } else if (fullOTP.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter verification code", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid OTP! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
