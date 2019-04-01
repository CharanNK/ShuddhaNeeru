package com.sanradiance.mobilewpp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phoneNumberField,passwordField;
    TextView errorMessage;
    Button loginButton;
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
                String phoneNumber = phoneNumberField.getText().toString();
                String password = passwordField.getText().toString();

                if(phoneNumber.length()!=10){
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Invalid Phone Number. Please try again!");
                }else{
                    Intent intent = new Intent(LoginActivity.this,VerifyOTPActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
