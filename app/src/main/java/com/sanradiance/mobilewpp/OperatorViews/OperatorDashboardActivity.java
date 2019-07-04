package com.sanradiance.mobilewpp.OperatorViews;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.sanradiance.mobilewpp.ConstantValues;
import com.sanradiance.mobilewpp.DataModels.UserDataModel;
import com.sanradiance.mobilewpp.LoginClasses.LoginActivity;
import com.sanradiance.mobilewpp.LoginClasses.VerifyOTPActivity;
import com.sanradiance.mobilewpp.R;
import com.sanradiance.mobilewpp.SplashScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OperatorDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreference;
    Intent intent;
    Boolean serverResponse = false;
    String accessToken;
    String updated_response = "";
    final String Details_URL = "https://domytaxonline.com.au/shuddha-neeru-test/public/api/auth/loggedUserDetails";
    NavigationView navigationView;

    ConstantValues constantValues = new ConstantValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_dashboard);

        sharedPreference = getSharedPreferences(constantValues.PROJECT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        accessToken = sharedPreference.getString(constantValues.USER_ACCESS_TOKEN,null);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(OperatorDashboardActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        try {
            JSONObject paramJson = new JSONObject();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Details_URL, paramJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", response.toString());

                    try {
                        updated_response = response.toString();
                        JSONObject obj = new JSONObject(String.valueOf(response));
                        String accessToken = obj.getString("access_token");
                        JSONObject userData = obj.getJSONObject("user");
                        String userName = userData.getString("name");
                        int userNumber = userData.getInt("id");
                        int mobileNumber = userData.getInt("mobile");

                        UserDataModel user = new UserDataModel(accessToken, userName, userNumber, mobileNumber);
                        View headerView = navigationView.getHeaderView(0);
                        TextView navHeaderUserName = headerView.findViewById(R.id.nav_header_userName);

                        navHeaderUserName.setText("Name : " + userName);

                        Log.d("UserInfoName", userName);
                        Log.d("UserInfoNumber", String.valueOf(userNumber));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("userDetails", response.toString());

                    OperatorPlantsFragment plantsFragment = new OperatorPlantsFragment();
                    plantsFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, plantsFragment).commit();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("Error", error.toString());
                }
            }) {

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    Log.d("Status code", String.valueOf(mStatusCode));
                    if (mStatusCode == 200) {
//                        if (getFragmentManager().getBackStackEntryCount() != 0) {
//                            getFragmentManager().popBackStack();
//                        }
                        serverResponse = true;
                    }
                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("X-Requested-With", "XMLHttpRequest");
                    params.put("Authorization", "Bearer " + accessToken);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.operator_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.clear();
            editor.commit();
            String action;
            intent = new Intent(OperatorDashboardActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_plants) {
            Bundle bundle = new Bundle();
            bundle.putString("userDetails", updated_response);

            OperatorPlantsFragment plantsFragment = new OperatorPlantsFragment();
            plantsFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.container, plantsFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
