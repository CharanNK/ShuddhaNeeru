package com.sanradiance.mobilewpp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView monitoringSysTextView, govtDeptTextView;
    private Button operatorButton, supervisorButton, dashboardsButton;
    private Dialog dashboardTypeDialog;
    final String[] additionalTypes = {"Technician", "Manager", "PDO", "JE", "SE-Circle", "SE-Head Office", "Commissioner"};
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monitoringSysTextView = findViewById(R.id.monitr_system_tv); //WPP Status Monitoring System text
        govtDeptTextView = findViewById(R.id.govt_kar_tv);  //RWD and SD text

        operatorButton = findViewById(R.id.operator_button);
        supervisorButton = findViewById(R.id.supervisor_button);
        dashboardsButton = findViewById(R.id.dashboards_button);

        //custom font typeface
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/linottesemibold.otf");
        monitoringSysTextView.setTypeface(typeface);
        govtDeptTextView.setTypeface(typeface);

        //listening for button clicks
        operatorButton.setOnClickListener(this);
        supervisorButton.setOnClickListener(this);
        dashboardsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.operator_button:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.supervisor_button:
                break;
            case R.id.dashboards_button:
                populateLoginRadioButtons();
                break;
        }
    }

    private void populateLoginRadioButtons() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Login Type");

        builder.setSingleChoiceItems(additionalTypes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                switch (item) {
                    case 0:
                        Toast.makeText(MainActivity.this, additionalTypes[item], Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, additionalTypes[item], Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, additionalTypes[item], Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, additionalTypes[item], Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, additionalTypes[item], Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this, additionalTypes[item], Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, additionalTypes[item], Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                Intent intent = new Intent(MainActivity.this,Dashboard.class);
                startActivity(intent);
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                Toast.makeText(MainActivity.this, "Cancel button clicked!!", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }
}
