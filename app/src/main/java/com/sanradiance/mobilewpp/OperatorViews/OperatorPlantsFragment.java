package com.sanradiance.mobilewpp.OperatorViews;

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

import com.sanradiance.mobilewpp.DataModels.PlantDataModel;
import com.sanradiance.mobilewpp.DataModels.UserDataModel;
import com.sanradiance.mobilewpp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OperatorPlantsFragment extends Fragment {
    public RecyclerView plantRecycler;
    public List<PlantDataModel> plantsList;

    OperatorPlantsAdapter operatorPlantsAdapter;
    String bearerToken;
    UserDataModel userDetail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.operator_plants_layout, container, false);
//        testPlants = view.findViewById(R.id.testPlants);

        plantRecycler = view.findViewById(R.id.plants_recycler);

        plantsList = new ArrayList<>();

        String plantData = this.getArguments().getString("userDetails").toString();
        Log.d("plantsJson", plantData);

        try {
            JSONObject loginResponse = new JSONObject(plantData);
            bearerToken = loginResponse.getString("access_token");

            JSONObject userDetails = loginResponse.getJSONObject("user");
            int userId = userDetails.getInt("id");
            String userName = userDetails.getString("name");
            long userMobile = userDetails.getLong("mobile");
            userDetail = new UserDataModel(bearerToken,userName,userId,userMobile);

            JSONArray assignedPlantList = loginResponse.getJSONArray("assigned_plant_list");
            for (int i = 0; i < assignedPlantList.length(); i++) {
//                JSONArray plantDetailsArray = assignedPlantList.getJSONObject(i).getJSONArray("plant_details");
                JSONObject plantDetails = assignedPlantList.getJSONObject(i);
                int id = plantDetails.getInt("id");
                String plantId = plantDetails.getString("plant_id");
                String installedVendor = plantDetails.getString("installed_by_vendor");
                String district = plantDetails.getString("district");
                String taluk = plantDetails.getString("taluk");
                String panchayath = plantDetails.getString("panchayath");
                String village = plantDetails.getString("village");
                String habitation = plantDetails.getString("habitation");
                String plantCapacity = plantDetails.getString("plant_capacity_lph");
                String eControllerMake = plantDetails.getString("e_controller_make");
                String panelId = plantDetails.getString("panel_id");
                String latitude = plantDetails.getString("latitude");
                String longitude = plantDetails.getString("longitude");
                String mobileNumber = plantDetails.getString("mobile_number");
                String serviceProvider = plantDetails.getString("service_provider");
                String createdAt = plantDetails.getString("created_at");
                String updatedAt = plantDetails.getString("updated_at");

                Log.d("JsonParsing plantId", plantId);
                PlantDataModel plantDataModel = new PlantDataModel(id,plantId, installedVendor, district, taluk, panchayath,
                        village, habitation, plantCapacity, eControllerMake, panelId, latitude, longitude,
                        mobileNumber, serviceProvider, createdAt, updatedAt);

                plantsList.add(plantDataModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        plantRecycler.setLayoutManager(linearLayoutManager);

        operatorPlantsAdapter = new OperatorPlantsAdapter(this.getContext(),plantsList,userDetail);

        plantRecycler.setAdapter(operatorPlantsAdapter);

        return view;
    }
}
