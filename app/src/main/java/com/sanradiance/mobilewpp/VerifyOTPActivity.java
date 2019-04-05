package com.sanradiance.mobilewpp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerifyOTPActivity extends AppCompatActivity {

    EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4;
    Button verifyOtpButton;
    private static StringBuilder otpEntered = new StringBuilder();
    private String userDetailsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        userDetailsString = getIntent().getStringExtra("userdetails");
        Log.d("User details:",userDetailsString);

        otpDigit1 = findViewById(R.id.otpDigit1);
        otpDigit2 = findViewById(R.id.otpDigit2);
        otpDigit3 = findViewById(R.id.otpDigit3);
        otpDigit4 = findViewById(R.id.otpDigit4);

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
                if(fullOTP.equals("1725")){
                    Intent intent = new Intent(VerifyOTPActivity.this,OperatorDashboardActivity.class);
                    intent.putExtra("userdetails",userDetailsString);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Invalid OTP! Try again!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
