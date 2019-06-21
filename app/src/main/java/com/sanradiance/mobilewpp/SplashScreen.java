package com.sanradiance.mobilewpp;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.sanradiance.mobilewpp.LoginClasses.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 6000;  //splash screen timeout milliseconds

    Spinner spinner;
    Locale myLocale;
    String currentLanguage = "en", currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);





         currentLanguage = getIntent().getStringExtra(currentLang);

         spinner = (Spinner) findViewById(R.id.spinner);

         List<String> list = new ArrayList<String>();

        list.add("Select language");
        list.add("English");
        list.add("Kannada");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
@Override
public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
        case 0:
        break;
        case 1:
        setLocale("en");
        break;
        case 2:
        setLocale("kn");
        break;
//                    case 3:
//                        setLocale("fr");
//                        break;
//                    case 4:
//                        setLocale("hi");
//                        break;
        }
        }

@Override
public void onNothingSelected(AdapterView<?> adapterView) {
        }
        });
        }

public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SplashScreen.class);
        refresh.putExtra(currentLang, localeName);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainActivityIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(mainActivityIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    //close this Activity
                    finish();
                }
            },SPLASH_TIME_OUT);

            startActivity(refresh);
        } else {
        Toast.makeText(SplashScreen.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
        }

public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);



    }




        //splash screen start



        //end splash screen
    }

// currentLanguage = getIntent().getStringExtra(currentLang);
//
//         spinner = (Spinner) findViewById(R.id.spinner);
//
//         List<String> list = new ArrayList<String>();
//
//        list.add("Select language");
//        list.add("English");
//        list.add("Kannada");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//@Override
//public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//        switch (position) {
//        case 0:
//        break;
//        case 1:
//        setLocale("en");
//        break;
//        case 2:
//        setLocale("kn");
//        break;
////                    case 3:
////                        setLocale("fr");
////                        break;
////                    case 4:
////                        setLocale("hi");
////                        break;
//        }
//        }
//
//@Override
//public void onNothingSelected(AdapterView<?> adapterView) {
//        }
//        });
//        }
//
//public void setLocale(String localeName) {
//        if (!localeName.equals(currentLanguage)) {
//        myLocale = new Locale(localeName);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(this, SplashScreen.class);
//        refresh.putExtra(currentLang, localeName);
//        startActivity(refresh);
//        } else {
//        Toast.makeText(SplashScreen.this, "Language already selected!", Toast.LENGTH_SHORT).show();
//        }
//        }
//
//public void onBackPressed() {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//        System.exit(0);