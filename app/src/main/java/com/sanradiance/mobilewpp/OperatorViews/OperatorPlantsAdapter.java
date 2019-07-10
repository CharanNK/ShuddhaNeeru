package com.sanradiance.mobilewpp.OperatorViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sanradiance.mobilewpp.DataModels.PlantDataModel;
import com.sanradiance.mobilewpp.DataModels.UserDataModel;
import com.sanradiance.mobilewpp.CommissionerViews.PlantFailureFragment;
import com.sanradiance.mobilewpp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperatorPlantsAdapter extends RecyclerView.Adapter<OperatorPlantsAdapter.ViewHolder> {

    private Context mContext;
    private List<PlantDataModel> plantsList;
    private String[] plantStatus = {"Working", "Not working"};
    private String[] failureReasons = {"No power", "No Water", "Breakdown"};
    private AlertDialog alertDialog;
    private Boolean workingStatus;
    private UserDataModel userDetail;
    private String reasonOfFailure;

    private String failureStatusURL = "https://domytaxonline.com.au/shuddha-neeru-test/public/api/auth/user/plant/not-working/status/update";
    private String operatorSurveyStatusURL = "https://domytaxonline.com.au/shuddha-neeru-test/public/api/auth/check/opertor-survey/status";

    public OperatorPlantsAdapter(Context mContext, List<PlantDataModel> plantsList, UserDataModel userDetail) {
        this.mContext = mContext;
        this.plantsList = plantsList;
        this.userDetail = userDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plant_detail_cardview, viewGroup, false);
        mContext = viewGroup.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final PlantDataModel currentPlant = plantsList.get(position);

        Typeface roundsTypeFace = Typeface.createFromAsset(mContext.getAssets(),"fonts/ttrounds.ttf");
        Typeface linottesemibold = Typeface.createFromAsset(mContext.getAssets(),"fonts/linottesemibold.otf");

        viewHolder.plantAddressTextView.setTypeface(linottesemibold);
        String completeAddress = currentPlant.getVillage() +  "," + currentPlant.getPanchayat()
                + ", " + currentPlant.getTaluk() + ", \n" + currentPlant.getDistrict();
        viewHolder.plantAddressTextView.setText(completeAddress);

        viewHolder.plantIdTextView.setTypeface(roundsTypeFace);
        viewHolder.plantIdTextView.setText("Plant ID : " + currentPlant.getPlantId());


        viewHolder.plantCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("cardview","clicked card");
                getPermissionStatus(currentPlant.getId(),currentPlant);
            }
        });
    }

    private void getPermissionStatus(int id, final PlantDataModel currentPlant) {
        Log.d(getClass().getName(),"inside getStatus method");
        try{
            JSONObject paramJson = new JSONObject();
            paramJson.put("plant_id",id);

            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Checking Permission to Upload..");
            progressDialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, operatorSurveyStatusURL, paramJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        Boolean permission = response.getBoolean("permission");
                        Log.d(getClass().getName(),"permission to upload:"+permission);
                        if(permission){
                            displayEntryPrompts(currentPlant);
                        } else {
                            Toast.makeText(mContext,"You uploaded data in the last 24 hours!",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                    progressDialog.dismiss();
                    Toast.makeText(mContext,"Oops! Something went wrong.",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    Log.d("Status code", String.valueOf(mStatusCode));
                    if (mStatusCode == 200) {
                        progressDialog.dismiss();
                    }
                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("X-Requested-With", "XMLHttpRequest");
                    params.put("Authorization", "Bearer " + userDetail.getAccessToken());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void displayEntryPrompts(final PlantDataModel currentPlant){
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
                if (workingStatus) {
                    AppCompatActivity activity = (AppCompatActivity)mContext;
                    OperatorDataEntry operatorDataEntry = new OperatorDataEntry();

                    Bundle bundle = new Bundle();
                    bundle.putString("accessToken", userDetail.getAccessToken());
                    bundle.putString("operatorName", userDetail.getUserName());
                    bundle.putInt("operatorId", userDetail.getUserId());
                    bundle.putLong("operatorMobile", userDetail.getUserMobile());
                    bundle.putInt("plantId", currentPlant.getId());
                    bundle.putString("plantdisplayId" ,currentPlant.getPlantId());
                    bundle.putString("plantVillage" ,currentPlant.getVillage());
                    bundle.putString("plantCapacity", currentPlant.getPlantCapacity());
                    bundle.putString("plantLatitude", currentPlant.getLatitude());
                    bundle.putString("plantLongitude", currentPlant.getLongitude());

                    operatorDataEntry.setArguments(bundle);

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, operatorDataEntry).addToBackStack(null).commit();
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putInt("plant_id", currentPlant.getId());
                    bundle.putString("accessToken", userDetail.getAccessToken());
                    bundle.putString("plantdisplayId" ,currentPlant.getPlantId());
                    bundle.putString("plantVillage" ,currentPlant.getVillage());

                    AppCompatActivity activity = (AppCompatActivity) mContext;
                    PlantFailureFragment plantFailureFragment = new PlantFailureFragment();
                    plantFailureFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, plantFailureFragment).addToBackStack(null).commit();
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                alertDialog.cancel();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateFailureReason(int id, String reasonOfFailure) {
        try {
            JSONObject paramJson = new JSONObject();
            paramJson.put("plant_id",id);
            paramJson.put("plant_working_status", "0");
            paramJson.put("plant_failure_reason", reasonOfFailure);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, failureStatusURL, paramJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    Log.d("Status code", String.valueOf(mStatusCode));
                    if (mStatusCode == 200) {
                        Activity currentActivity = (Activity) mContext;
                        currentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Updated to server!", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("X-Requested-With", "XMLHttpRequest");
                    params.put("Authorization", "Bearer " + userDetail.getAccessToken());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        }
    }
}
