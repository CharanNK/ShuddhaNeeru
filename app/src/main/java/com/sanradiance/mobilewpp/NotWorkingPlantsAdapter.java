package com.sanradiance.mobilewpp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotWorkingPlantsAdapter extends RecyclerView.Adapter<NotWorkingPlantsAdapter.ViewHolder> {

    List<PlantDataModel> plantsList;
    Context mContext;

    public NotWorkingPlantsAdapter(List<PlantDataModel> plantsList, Context mContext) {
        this.plantsList = plantsList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(getClass().getName(),"onCreateViewHolder called");
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.not_reported_cardview,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotWorkingPlantsAdapter.ViewHolder viewHolder, int position) {
        Log.d(getClass().getName(),"onBindViewHolder called");
        final PlantDataModel currentPlant = plantsList.get(position);



        Typeface roundsTypeFace = Typeface.createFromAsset(mContext.getAssets(),"fonts/ttrounds.ttf");
        Typeface linottesemibold = Typeface.createFromAsset(mContext.getAssets(),"fonts/linottesemibold.otf");

        viewHolder.plantAddressTextView.setTypeface(linottesemibold);
        String completeAddress = currentPlant.getDistrict();
        viewHolder.plantAddressTextView.setText(completeAddress);

        viewHolder.plantIdTextView.setTypeface(roundsTypeFace);
        viewHolder.plantIdTextView.setText("Plant ID : " + currentPlant.getPlantId());

        viewHolder.operatorNameTextView.setTypeface(linottesemibold);
        viewHolder.operatorMobileTv.setTypeface(linottesemibold);
        viewHolder.operatorNameTextView.setText("Operator Name : "+currentPlant.getOperatorName());
        viewHolder.operatorMobileTv.setText("Operator Mobile : "+currentPlant.getOperatorMobile());
    }

    @Override
    public int getItemCount() {
        Log.i(getClass().getName(),"getItemCount called");
        Log.d("ItemCount",String.valueOf(plantsList.size()));
        return plantsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView plantCardView;
        public TextView plantIdTextView,plantAddressTextView,operatorNameTextView,operatorMobileTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            plantCardView = itemView.findViewById(R.id.notReportedCard);

            plantIdTextView = itemView.findViewById(R.id.plantIDTextView);
            plantAddressTextView = itemView.findViewById(R.id.plantAddressTextView);
            operatorNameTextView = itemView.findViewById(R.id.operatorNameTv);
            operatorMobileTv = itemView.findViewById(R.id.operatorMobileTv);
        }
    }
}
