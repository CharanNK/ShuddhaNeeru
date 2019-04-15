package com.sanradiance.mobilewpp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.ViewHolder> {

    private Context mContext;
    private List<PlantDataModel> plantsList;
    private String[] plantStatus = {"Working", "Not working"};
    private AlertDialog alertDialog;
    private Boolean workingStatus;
    private UserDataModel userDetail;

    public PlantsAdapter(Context mContext, List<PlantDataModel> plantsList,UserDataModel userDetail) {
        this.mContext = mContext;
        this.plantsList = plantsList;
        this.userDetail = userDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plant_detail_cardview, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final PlantDataModel currentPlant = plantsList.get(position);

        viewHolder.panelIdTextView.setText("Panel ID :" + currentPlant.getPanelId());
        String completeAddress = currentPlant.getVillage() + ", " + currentPlant.getHabitation() + ", " + currentPlant.getPanchayat()
                + ", " + currentPlant.getTaluk() + ", " + currentPlant.getDistrict();
        viewHolder.plantAddressTextView.setText(completeAddress);
        viewHolder.plantIdTextView.setText("Plant ID :" + currentPlant.getPlantId());

        viewHolder.plantCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Select Plant Status");
                builder.setSingleChoiceItems(plantStatus, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        switch (item) {
                            case 1:
                                workingStatus = false;
                                break;
                            default:
                                workingStatus = true;
                                break;
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        if (workingStatus){
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            OperatorDataEntry operatorDataEntry = new OperatorDataEntry();

                            Bundle bundle = new Bundle();
                            bundle.putString("accessToken",userDetail.getAccessToken());
                            bundle.putString("operatorName",userDetail.getUserName());
                            bundle.putInt("operatorId",userDetail.getUserId());
                            bundle.putLong("operatorMobile",userDetail.getUserMobile());
                            bundle.putInt("plantId",currentPlant.getId());
                            bundle.putString("plantCapacity",currentPlant.getPlantCapacityLPH());
                            bundle.putString("plantLatitude",currentPlant.getLatitude());
                            bundle.putString("plantLongitude",currentPlant.getLongitude());
                            operatorDataEntry.setArguments(bundle);

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,operatorDataEntry).addToBackStack(null).commit();
                        }
                        Toast.makeText(mContext, "Okay button clicked!!", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Toast.makeText(mContext, "Cancel button clicked!!", Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView plantCardView;
        TextView plantIdTextView, plantAddressTextView, panelIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            plantCardView = itemView.findViewById(R.id.plantDetailCardView);

            plantIdTextView = itemView.findViewById(R.id.plantIdTv);
            plantAddressTextView = itemView.findViewById(R.id.plantAddressTv);
            panelIdTextView = itemView.findViewById(R.id.panelIdTv);
        }
    }
}
