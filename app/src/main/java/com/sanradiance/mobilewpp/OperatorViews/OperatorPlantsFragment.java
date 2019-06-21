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
import android.widget.Toast;

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
                String district = plantDetails.getString("district");
                String taluk = plantDetails.getString("taluk");
                String panchayath = plantDetails.getString("panchayath");
                String village = plantDetails.getString("village");
                String address = plantDetails.getString("address");
                String latitude = plantDetails.getString("latitude");
                String longitude = plantDetails.getString("longitude");
                String wpp_status = plantDetails.getString("wpp_status");
                String dateOfInstallation = plantDetails.getString("date_of_installation");
                String schemeinstalled = plantDetails.getString("scheme_under_which_installed");
                String plantCapacity = plantDetails.getString("plant_capacity");
                String plantSupplier = plantDetails.getString("plant_supplier");
                String aAndmagency = plantDetails.getString("o_and_m_agency");
                String serviceProvider = plantDetails.getString("service_provider");
                String omforservice = plantDetails.getString("o_and_m_or_amc_for_service");
                String createdAt = plantDetails.getString("created_at");
                String updatedAt = plantDetails.getString("updated_at");

                Log.d("JsonParsing plantId", plantId);
                PlantDataModel plantDataModel = new PlantDataModel(id,plantId, district, taluk, panchayath,
                        village, address, latitude, longitude,wpp_status,dateOfInstallation,schemeinstalled,
                        plantCapacity,plantSupplier,aAndmagency, serviceProvider, omforservice,createdAt, updatedAt);

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
