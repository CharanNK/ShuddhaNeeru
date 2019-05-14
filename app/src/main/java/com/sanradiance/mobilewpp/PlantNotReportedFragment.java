package com.sanradiance.mobilewpp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantNotReportedFragment extends Fragment {

    public RecyclerView plantRecycler;
    public List<PlantDataModel> plantsList;

    NotWorkingPlantsAdapter notWorkingPlantsAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.operator_plants_layout,container,false);

        plantRecycler = view.findViewById(R.id.plants_recycler);

        plantsList = new ArrayList<>();

        String notReportedPlants = this.getArguments().getString("notReportedPlants");

        Log.d("plantsJson",notReportedPlants);

        try {
            JSONObject plantsJsonObject = new JSONObject(notReportedPlants);
            JSONObject dataObject = plantsJsonObject.getJSONObject("data");
            JSONArray plantsJsonArray = dataObject.getJSONArray("total_not_reported_plant_details");
            for(int i=0;i<plantsJsonArray.length();i++){
                JSONObject individualPlant = plantsJsonArray.getJSONObject(i);
                int id = individualPlant.getInt("id");
                String plantId = individualPlant.getString("display_plant_id");
                String operatorName = individualPlant.getString("operator_name");
                Long operatorMobile = individualPlant.getLong("operator_mobile");
                String district = individualPlant.getString("district");

                Log.d("planid",String.valueOf(id));

                PlantDataModel plantDataModel = new PlantDataModel(id,plantId,district,operatorName,operatorMobile);
                plantsList.add(plantDataModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        notWorkingPlantsAdapter = new NotWorkingPlantsAdapter(plantsList,this.getContext());

        plantRecycler.setAdapter(notWorkingPlantsAdapter);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        plantRecycler.setLayoutManager(linearLayoutManager);

        Log.d("size",String.valueOf(plantsList.size()));



        return view;
    }
}
