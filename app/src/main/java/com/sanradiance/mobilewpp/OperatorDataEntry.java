package com.sanradiance.mobilewpp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class OperatorDataEntry extends Fragment implements View.OnClickListener {
    Button voltageButton1, voltageButton2, voltageButton3;
    Button rwTankLevelButton1, rwTankLevelButton2, rwTanklevelButton3;
    Button twTankLevelButton1, twTankLevelButton2, twTankLevelButton3;
    Button twTdsButton1, twTdsButton2, twTdsButton3;
    Button operatorSubmitButton;
    EditText plantCapacityEditText, volumeDispensedEditText, electricityEditText;
    Spinner rwFlowRateSpinner, twFlowRateSpinner;
    TextView labelPlantCapacity, labelVoltage, labelRWTankLevel, labelRWFlowRate, labelTWFlowRate, labelTWTankLevel, labelVolumeDispensed, labelTWTDS, labelElectricityMeter;
    TextView operatorNameTv, operatorIdTv, operatorMobileNumberTv,plantLatLongTv;
    ImageView rwTankLevelCamera, rwFlowRateCamera, twFlowRateCamera, volumeDispensedCamera, twTDSCamera, electricityMeterCamera;

    String plantCapacity, plantVoltage, rwTankLevel, rwFlowRate, twFlowRate, twTankLevel, volumeDispensed, twTDS, electricityMeter;

    int plantId;

    Boolean valuesSetFlag = true;

    String accessToken;

    private static final String dataUploadURL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/user/survey-details/upload";
    private static final String fileUploadURL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/upload/file";
    private Uri picUri;

    int CAPTURE_IMAGE = 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.operator_data_entry, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        plantCapacity = plantVoltage = rwTankLevel = rwFlowRate = twFlowRate = twTankLevel = volumeDispensed = twTDS = electricityMeter = "";

        operatorNameTv = view.findViewById(R.id.operatorname_value);
        operatorIdTv = view.findViewById(R.id.operatorid_value);
        operatorMobileNumberTv = view.findViewById(R.id.operatormobile_value);
        plantLatLongTv = view.findViewById(R.id.plant_location);

        voltageButton1 = view.findViewById(R.id.voltage_button1);
        voltageButton2 = view.findViewById(R.id.voltage_button2);
        voltageButton3 = view.findViewById(R.id.voltage_button3);

        rwTankLevelButton1 = view.findViewById(R.id.rwtank_level1);
        rwTankLevelButton2 = view.findViewById(R.id.rwtank_level2);
        rwTanklevelButton3 = view.findViewById(R.id.rwtank_level3);

        twTankLevelButton1 = view.findViewById(R.id.twtanklevel1);
        twTankLevelButton2 = view.findViewById(R.id.twtanklevel2);
        twTankLevelButton3 = view.findViewById(R.id.twtanklevel3);

        twTdsButton1 = view.findViewById(R.id.twtds_button1);
        twTdsButton2 = view.findViewById(R.id.twtds_button2);
        twTdsButton3 = view.findViewById(R.id.twtds_button3);

        plantCapacityEditText = view.findViewById(R.id.plant_capacity_value);
        volumeDispensedEditText = view.findViewById(R.id.volumedisposed_value);
        electricityEditText = view.findViewById(R.id.electricity_meter_value);

        rwFlowRateSpinner = view.findViewById(R.id.rwflowrate_spinner);
        twFlowRateSpinner = view.findViewById(R.id.twflowrate_spinner);

        labelPlantCapacity = view.findViewById(R.id.label_plantcapacity);
        labelVoltage = view.findViewById(R.id.label_plantvoltage);
        labelRWTankLevel = view.findViewById(R.id.label_rwtanklevel);
        labelRWFlowRate = view.findViewById(R.id.label_rwflowrate);
        labelTWFlowRate = view.findViewById(R.id.label_twflowrate);
        labelTWTankLevel = view.findViewById(R.id.label_twtanklevel);
        labelVolumeDispensed = view.findViewById(R.id.label_volumedispensed);
        labelTWTDS = view.findViewById(R.id.label_twtds);
        labelElectricityMeter = view.findViewById(R.id.label_electricitymeter);


        rwTankLevelCamera = view.findViewById(R.id.rwtanklevelCamera);
        rwFlowRateCamera = view.findViewById(R.id.rwflowrateCamera);
        twFlowRateCamera = view.findViewById(R.id.twflowrateCamera);
        volumeDispensedCamera = view.findViewById(R.id.volumedispensedCamera);
        twTDSCamera = view.findViewById(R.id.twtdsCamera);
        electricityMeterCamera = view.findViewById(R.id.electricitymeterCamera);

        operatorSubmitButton = view.findViewById(R.id.operator_submit_button);


        String loginResponse = this.getArguments().getString("accessToken").toString();
        String operatorName = this.getArguments().getString("operatorName").toString();
        String operatorId = String.valueOf(this.getArguments().getInt("operatorId"));
        Long operatorMobile = Long.valueOf(this.getArguments().getLong("operatorMobile"));
        String capacity = String.valueOf(this.getArguments().getString("plantCapacity"));
        String latitude = String.valueOf(this.getArguments().getString("plantLatitude"));
        String longitude = String.valueOf(this.getArguments().getString("plantLongitude"));
        plantId = Integer.valueOf(this.getArguments().getInt("plantId"));
        plantCapacity = capacity;
        accessToken = loginResponse;

        plantCapacityEditText.setText(plantCapacity);
        operatorIdTv.setText(operatorId);
        operatorNameTv.setText(operatorName);
        operatorMobileNumberTv.setText(operatorMobile.toString());
        plantLatLongTv.setText(latitude+","+longitude);

        Log.d("loginResponse", loginResponse);

        voltageButton1.setOnClickListener(this);
        voltageButton2.setOnClickListener(this);
        voltageButton3.setOnClickListener(this);

        rwTankLevelButton1.setOnClickListener(this);
        rwTankLevelButton2.setOnClickListener(this);
        rwTanklevelButton3.setOnClickListener(this);

        twTankLevelButton1.setOnClickListener(this);
        twTankLevelButton2.setOnClickListener(this);
        twTankLevelButton3.setOnClickListener(this);

        twTdsButton1.setOnClickListener(this);
        twTdsButton2.setOnClickListener(this);
        twTdsButton3.setOnClickListener(this);

        rwTankLevelCamera.setOnClickListener(this);

        operatorSubmitButton.setOnClickListener(this);
        return view;


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voltage_button1:
                modifyButtons(view, R.id.voltage_button1, R.id.voltage_button2, R.id.voltage_button3);
                plantVoltage = voltageButton1.getText().toString();
                break;
            case R.id.voltage_button2:
                modifyButtons(view, R.id.voltage_button2, R.id.voltage_button1, R.id.voltage_button3);
                plantVoltage = voltageButton2.getText().toString();
                break;
            case R.id.voltage_button3:
                modifyButtons(view, R.id.voltage_button3, R.id.voltage_button2, R.id.voltage_button1);
                plantVoltage = voltageButton3.getText().toString();
                break;

            case R.id.rwtank_level1:
                modifyButtons(view, R.id.rwtank_level1, R.id.rwtank_level2, R.id.rwtank_level3);
                rwTankLevel = rwTankLevelButton1.getText().toString();
                break;
            case R.id.rwtank_level2:
                modifyButtons(view, R.id.rwtank_level2, R.id.rwtank_level1, R.id.rwtank_level3);
                rwTankLevel = rwTankLevelButton2.getText().toString();
                break;
            case R.id.rwtank_level3:
                modifyButtons(view, R.id.rwtank_level3, R.id.rwtank_level1, R.id.rwtank_level2);
                rwTankLevel = rwTanklevelButton3.getText().toString();
                break;
            case R.id.twtanklevel1:
                modifyButtons(view, R.id.twtanklevel1, R.id.twtanklevel2, R.id.twtanklevel3);
                twTankLevel = twTankLevelButton1.getText().toString();
                break;
            case R.id.twtanklevel2:
                modifyButtons(view, R.id.twtanklevel2, R.id.twtanklevel1, R.id.twtanklevel3);
                twTankLevel = twTankLevelButton2.getText().toString();
                break;
            case R.id.twtanklevel3:
                modifyButtons(view, R.id.twtanklevel3, R.id.twtanklevel2, R.id.twtanklevel2);
                twTankLevel = twTankLevelButton3.getText().toString();
                break;
            case R.id.twtds_button1:
                modifyButtons(view, R.id.twtds_button1, R.id.twtds_button2, R.id.twtds_button3);
                twTDS = twTdsButton1.getText().toString();
                break;
            case R.id.twtds_button2:
                modifyButtons(view, R.id.twtds_button2, R.id.twtds_button1, R.id.twtds_button3);
                twTDS = twTdsButton2.getText().toString();
                break;
            case R.id.twtds_button3:
                modifyButtons(view, R.id.twtds_button3, R.id.twtds_button2, R.id.twtds_button1);
                twTDS = twTdsButton3.getText().toString();
                break;

            case R.id.rwtanklevelCamera:
                openCamera();
                break;
            case R.id.operator_submit_button:
                validateData();
                break;
        }
    }

    private void validateData() {
//        plantCapacity = plantCapacityEditText.getText().toString();
//        if (plantCapacity.length() <= 0) {
//            labelPlantCapacity.setTextColor(Color.RED);
//            valuesSetFlag = false;
//        }

        if (plantVoltage.length() <= 0) {
            labelVoltage.setTextColor(Color.RED);
            valuesSetFlag = false;
        }

        if (rwTankLevel.length() <= 0) {
            labelRWTankLevel.setTextColor(Color.RED);
            valuesSetFlag = false;
        }

        rwFlowRate = rwFlowRateSpinner.getSelectedItem().toString();
        if (rwFlowRate.length() <= 0) {
            labelRWFlowRate.setTextColor(Color.RED);
            valuesSetFlag = false;
        }

        twFlowRate = twFlowRateSpinner.getSelectedItem().toString();
        if (twFlowRate.length() <= 0) {
            labelTWFlowRate.setTextColor(Color.RED);
            valuesSetFlag = false;
        }

        if (twTankLevel.length() <= 0) {
            labelTWTankLevel.setTextColor(Color.RED);
            valuesSetFlag = false;
        }

        volumeDispensed = volumeDispensedEditText.getText().toString();
        if (volumeDispensed.length() <= 0) {
            labelVolumeDispensed.setTextColor(Color.RED);
            valuesSetFlag = false;
        }

        if (twTDS.length() <= 0) {
            labelTWTDS.setTextColor(Color.RED);
            valuesSetFlag = false;
        }

        electricityMeter = electricityEditText.getText().toString();
        if (electricityMeter.length() <= 0) {
            labelElectricityMeter.setTextColor(Color.RED);
            valuesSetFlag = false;
        }

        if (valuesSetFlag) {
            uploadData();
        }
    }

    private void uploadData() {
        try {
            JSONObject paramJson = new JSONObject();
            Log.d("plantId", String.valueOf(plantId));
            paramJson.put("plant_id", plantId);
            paramJson.put("plant_capacity", plantCapacity);
            paramJson.put("voltage", plantVoltage);
            paramJson.put("rw_tank_level", rwTankLevel);
            paramJson.put("rw_flow_rate_in_lph", rwFlowRate);
            paramJson.put("rw_flow_rate_in_lph_image_id", 1);
            paramJson.put("tw_flow_rate_in_lph", twFlowRate);
            paramJson.put("tw_flow_rate_in_lph_image_id", 2);
            paramJson.put("tw_tank_level", twTankLevel);
            paramJson.put("volume_dispensed_in_ltr", volumeDispensed);
            paramJson.put("volume_dispensed_in_ltr_image_id", 3);
            paramJson.put("tw_tds_ppm", twTDS);
            paramJson.put("tw_tds_ppm_image_id", 4);
            paramJson.put("electricity_meter_kwh_or_units", electricityMeter);
            paramJson.put("electricity_meter_kwh_or_units_image_id", 5);
            paramJson.put("plant_working_status", 1);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, dataUploadURL, paramJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                }
            }) {

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    Log.d("Status code", String.valueOf(mStatusCode));
                    if (mStatusCode == 200) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getContext(), "Data Uploaded Successfully!", Toast.LENGTH_LONG).show();
                            }
                        });
                        if (getFragmentManager().getBackStackEntryCount() != 0) {
                            getFragmentManager().popBackStack();
                        }
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
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void modifyButtons(View view, int voltage_button1, int voltage_button2, int voltage_button3) {
        Button button1 = view.findViewById(voltage_button1);
        button1.setBackgroundResource(R.drawable.edittext_selected_bg);
        button1.setTextColor(Color.WHITE);

        view = getView();
        Button button2 = view.findViewById(voltage_button2);
        button2.setBackgroundResource(R.drawable.edittext_bg);
        button2.setTextColor(Color.BLACK);

        view = getView();
        Button button3 = view.findViewById(voltage_button3);
        button3.setBackgroundResource(R.drawable.edittext_bg);
        button3.setTextColor(Color.BLACK);

    }

    public void openCamera() {
        Log.d("CAMERA", "called function");
        Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                            File file = getOutputMediaFile(1);
                            picUri = Uri.fromFile(file); // create
                            i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file

                            startActivityForResult(i, CAPTURE_IMAGE);
                        } else {
                            Toast.makeText(getContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = picUri;
            Log.d("selectedImage", picUri.toString());

            String picturePath = getRealPathFromURI(selectedImage);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                String image = getStringImage(lastBitmap);
                Log.d("image", image);
                sendImage(image);
            } catch (IOException e) {

            }

            Log.d("picturePath", picturePath);
        }

    }

    private void sendImage(final String image) {
        Response.Listener<String> jsonListerner = new Response.Listener<String>() {
            @Override
            public void onResponse(String list) {

            }
        };

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, fileUploadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("fileUploadError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();

                params.put("image_file", image);
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "multipart/form-data");
                params.put("X-Requested-With", "XMLHttpRequest");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }

//            @Override
//            public String getBodyContentType() {
//                return "multipart/form-data";
//            }
        };
        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }
}
