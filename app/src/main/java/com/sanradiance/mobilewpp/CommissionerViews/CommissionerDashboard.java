package com.sanradiance.mobilewpp.CommissionerViews;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.sanradiance.mobilewpp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommissionerDashboard extends AppCompatActivity implements View.OnClickListener {
    TextView today, plantcount, working, notworking, notreported, waterdispense, waterforday;
    PieChart nchart;
    String dateString, accessToken;
    long date = System.currentTimeMillis();
    LinearLayout plantsNotReported;

    AlertDialog alertDialog;

    String notReportedPlantDetails;

    Boolean serverResponse = false;
    SharedPreferences prf;


    final String Details_URL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/dashboard/count/details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commissioner_dashboard);

       // prf = getSharedPreferences("user_details", MODE_PRIVATE);


        nchart = findViewById(R.id.commissionerpiechart);
        today = findViewById(R.id.todaydate);

        String userDetails = getIntent().getStringExtra("userdetails");

        try {
            JSONObject jsonObject = new JSONObject(userDetails);
            accessToken = jsonObject.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        plantcount = findViewById(R.id.totalplant);
        working = findViewById(R.id.plantworking);
        notworking = findViewById(R.id.plantnotworking);
        notreported = findViewById(R.id.reporteddata);
        waterdispense = findViewById(R.id.volumedispense);
        waterforday = findViewById(R.id.volumeperday);

        plantsNotReported = findViewById(R.id.plantsNotReported);

        plantsNotReported.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        dateString = sdf.format(date);
        today.setText(dateString);
        updateFields();
    }

    private void updateFields() {

        try {
            JSONObject paramJson = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            paramJson.put("date", sdf.format(date));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Details_URL, paramJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", response.toString());
                    notReportedPlantDetails = response.toString();
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(response));
                        JSONObject employee = obj.getJSONObject("data");

                        int plant = Integer.valueOf(employee.getInt("total_plant_count"));
                        int totalworkingplant = Integer.valueOf(employee.getInt("total_working_plant"));
                        int notworkingplant = Integer.valueOf(employee.getInt("total_not_working_plant"));
                        int reportedplant = Integer.valueOf(employee.getInt("total_not_reported_plant"));
                        int dispenseplant = Integer.valueOf(employee.getInt("total_water_dispensed_cumulative"));
                        int fordayplant = Integer.valueOf(employee.getInt("total_water_dispensed_for_the_day"));

                        setInitValues(totalworkingplant, notworkingplant, reportedplant);

                        plantcount.setText(String.valueOf(plant));
                        working.setText(String.valueOf(totalworkingplant));
                        notworking.setText(String.valueOf(notworkingplant));
                        notreported.setText(String.valueOf(reportedplant));
                        waterdispense.setText(String.valueOf(dispenseplant));
                        waterforday.setText(String.valueOf(fordayplant));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    start pie chart code
    private void setInitValues(int totalworkingplant, int notworkingplant, int reportedplant) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(totalworkingplant, "Working"));
        entries.add(new PieEntry(notworkingplant,"Not Working"));
        entries.add(new PieEntry(reportedplant,"Not Reported"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(dataSet);


        dataSet.setColors(new int[]{Color.parseColor("#4caf50"), Color.parseColor("#f44336"), Color.parseColor("#ff9800")});
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextSize(10f);
        nchart.setUsePercentValues(false);
        nchart.animateXY(1500, 1500);

//        pieData.setValueFormatter(new PercentFormatter());

        nchart.setCenterText("Plant\nStatistics");
        nchart.setCenterTextSize(20f);

//        nchart.animateY(3000, Easing.EasingOption.Linear);
//        nchart.animateY(3000, Easing.EasingOption.EaseOutBack);
        nchart.setDrawHoleEnabled(true);
        nchart.setData(pieData);
        nchart.setDrawSliceText(false); // To remove slice text

        nchart.setDrawMarkers(false); // To remove markers when click
        nchart.setDrawEntryLabels(false); // To remove labels from piece of pie
        nchart.getDescription().setEnabled(false);
        nchart.getData().setDrawValues(false);
        nchart.setTouchEnabled(false);
        nchart.setRotationEnabled(false);

        nchart.setUsePercentValues(true);

        Legend legend = nchart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(11);
        legend.setXEntrySpace(5f);
        legend.setDrawInside(false);
    }

    @Override
    public void onClick(View view) {
        if(serverResponse){
            switch (view.getId()){
                case R.id.plantsNotReported :
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommissionerDashboard.this);
                    builder.setTitle("Do you want to view not reported plants?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Bundle bundle = new Bundle();
                            bundle.putString("notReportedPlants",notReportedPlantDetails);
                            PlantNotReportedFragment plantFailureFragment = new PlantNotReportedFragment();
                            plantFailureFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.commissionercontainer, plantFailureFragment,plantFailureFragment.getClass().getSimpleName()).commit();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.cancel();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
            }
        }
    }
//end pie chart code
}


