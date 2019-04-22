package com.sanradiance.mobilewpp;

import android.app.Activity;
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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.ViewHolder> {

    private Context mContext;
    private List<PlantDataModel> plantsList;
    private String[] plantStatus = {"Working", "Not working"};
    private String[] failureReasons = {"No power", "No Water", "Breakdown"};
    private AlertDialog alertDialog;
    private Boolean workingStatus;
    private UserDataModel userDetail;
    private String reasonOfFailure;

    private String failureStatusURL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/user/plant/not-working/status/update";

    public PlantsAdapter(Context mContext, List<PlantDataModel> plantsList, UserDataModel userDetail) {
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
                        if (workingStatus) {
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            OperatorDataEntry operatorDataEntry = new OperatorDataEntry();

                            Bundle bundle = new Bundle();
                            bundle.putString("accessToken", userDetail.getAccessToken());
                            bundle.putString("operatorName", userDetail.getUserName());
                            bundle.putInt("operatorId", userDetail.getUserId());
                            bundle.putLong("operatorMobile", userDetail.getUserMobile());
                            bundle.putInt("plantId", currentPlant.getId());
                            bundle.putString("plantCapacity", currentPlant.getPlantCapacityLPH());
                            bundle.putString("plantLatitude", currentPlant.getLatitude());
                            bundle.putString("plantLongitude", currentPlant.getLongitude());
                            operatorDataEntry.setArguments(bundle);

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, operatorDataEntry).addToBackStack(null).commit();
                        } else {
                            Log.d("testing", "true");
//                            dialogInterface.dismiss();
                            alertDialog.dismiss();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Select Reason");
                            builder.setSingleChoiceItems(failureReasons, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position) {
                                    switch (position) {
                                        case 0:
                                            reasonOfFailure = "no_power";
                                            break;
                                        case 1:
                                            reasonOfFailure = "no_water";
                                            break;
                                        case 2:
                                            reasonOfFailure = "break_down";
                                            break;
                                        default:
                                            reasonOfFailure = "no_power";
                                            break;
                                    }
                                }
                            })
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            updateFailureReason(currentPlant.getId(), reasonOfFailure);
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();
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
        });
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
            panelIdTextView = itemView.findViewById(R.id.panelIdTv);
        }
    }
}
