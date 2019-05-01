package com.sanradiance.mobilewpp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    TextView today;
    PieChart nchart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        nchart = (PieChart) findViewById(R.id.chart);
        today= (TextView) findViewById(R.id.todaydate);

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        today.setText("Date:" + dateString);


        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(30f,"Total Number of Plant"));
        entries.add(new PieEntry(56f, "Total Number of Working Plant"));
        entries.add(new PieEntry(66f, "Total Number of Not Working Plant  "));
        entries.add(new PieEntry(45f, "Total Number Plant Not Reported Data"));
//        entries.add(new PieEntry(23f,"Total Water Dispense (Ltr)"));


        PieDataSet dataSet = new PieDataSet(entries, "");
        PieData data = new PieData(dataSet);


//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setColors(new int[]{Color.parseColor("#32CD32"), Color.parseColor("#FF0000"), Color.parseColor("#FE9200")});
        dataSet.setSliceSpace(5f);
        dataSet.setValueTextSize(10f);
        nchart.setUsePercentValues(true);
        nchart.setDrawHoleEnabled(true);
        nchart.setData(data);
        nchart.setDrawSliceText(false); // To remove slice text

        nchart.setDrawMarkers(false); // To remove markers when click
        nchart.setDrawEntryLabels(false); // To remove labels from piece of pie
        nchart.getDescription().setEnabled(false);
        nchart.getData().setDrawValues(false);
        nchart.setTouchEnabled(false);
        nchart.setRotationEnabled(false);

        Legend legend = nchart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);
        legend.setYOffset(10f);

    }
}


