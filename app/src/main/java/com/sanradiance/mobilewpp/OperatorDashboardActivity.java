package com.sanradiance.mobilewpp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OperatorDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String userDetails = getIntent().getStringExtra("userdetails");

        try {
            JSONObject jsonObject = new JSONObject(userDetails);
            String accessToken = jsonObject.getString("access_token");
            JSONObject userData = jsonObject.getJSONObject("user");
            String userName = userData.getString("name");
            int userNumber = userData.getInt("id");
            int mobileNumber = userData.getInt("mobile");

            UserDataModel user = new UserDataModel(accessToken,userName,userNumber,mobileNumber);

            View headerView = navigationView.getHeaderView(0);
            TextView navHeaderUserName = headerView.findViewById(R.id.nav_header_userName);
            TextView navHeaderUserId = headerView.findViewById(R.id.nav_header_userId);

            navHeaderUserName.setText("Username : "+userName);
            navHeaderUserId.setText("User ID : "+userNumber);

            Log.d("UserInfoName",userName);
            Log.d("UserInfoNumber", String.valueOf(userNumber));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString("userDetails",getIntent().getStringExtra("userdetails"));

        OperatorPlantsFragment plantsFragment = new OperatorPlantsFragment();
        plantsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,plantsFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.action_settings) {
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
            bundle.putString("userDetails",getIntent().getStringExtra("userdetails"));

            OperatorPlantsFragment plantsFragment = new OperatorPlantsFragment();
            plantsFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.container,plantsFragment).commit();
        } else if (id == R.id.nav_reports) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new OperatorReportsFragment()).commit();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
