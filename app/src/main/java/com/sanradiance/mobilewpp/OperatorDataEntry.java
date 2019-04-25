package com.sanradiance.mobilewpp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

public class OperatorDataEntry extends Fragment implements View.OnClickListener {
    Button voltageButton1, voltageButton2, voltageButton3;
    Button rwTankLevelButton1, rwTankLevelButton2, rwTanklevelButton3;
    Button twTankLevelButton1, twTankLevelButton2, twTankLevelButton3;
    Button twTdsButton1, twTdsButton2, twTdsButton3;
    Button operatorSubmitButton;
    EditText volumeDispensedEditText, electricityEditText;
    Spinner rwFlowRateSpinner, twFlowRateSpinner;
    TextView plantCapacityTextView, labelPlantCapacity, labelVoltage, labelRWTankLevel, labelRWFlowRate, labelTWFlowRate, labelTWTankLevel, labelVolumeDispensed, labelTWTDS, labelElectricityMeter;
    TextView operatorNameTv, operatorIdTv, operatorMobileNumberTv, plantLatLongTv;
    ImageView rwFlowRateCamera, twFlowRateCamera, volumeDispensedCamera, twTDSCamera, electricityMeterCamera;

    int rwTankLevelImageId, rwFlowRateImageId, twFlowRateImageId, volumeDispensedImageId, twTDSImageId, electricityMeterImageId;

    String plantCapacity, plantVoltage, rwTankLevel, rwFlowRate, twFlowRate, twTankLevel, volumeDispensed, twTDS, electricityMeter;

    int plantId;

    Boolean valuesSetFlag = true;

    String accessToken;

    private static final String dataUploadURL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/user/survey-details/upload";
    private static final String fileUploadURL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/upload/file";

    int CAPTURE_IMAGE = 1000;
    private Uri fileUri;
    FileService fileService;

    private static String currentImageIdentifier;

    private String currentImagePath;

    final int currentItem = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.operator_data_entry, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        plantCapacity = plantVoltage = rwTankLevel = rwFlowRate = twFlowRate = twTankLevel = volumeDispensed = twTDS = electricityMeter = "";

        //operator fields
        operatorNameTv = view.findViewById(R.id.operatorname_value);
        operatorIdTv = view.findViewById(R.id.operatorid_value);
        operatorMobileNumberTv = view.findViewById(R.id.operatormobile_value);
        plantLatLongTv = view.findViewById(R.id.plant_location);

        //voltage buttons
        voltageButton1 = view.findViewById(R.id.voltage_button1);
        voltageButton2 = view.findViewById(R.id.voltage_button2);
        voltageButton3 = view.findViewById(R.id.voltage_button3);

        //raw water tank level buttons
        rwTankLevelButton1 = view.findViewById(R.id.rwtank_level1);
        rwTankLevelButton2 = view.findViewById(R.id.rwtank_level2);
        rwTanklevelButton3 = view.findViewById(R.id.rwtank_level3);

        //treated water tank level buttons
        twTankLevelButton1 = view.findViewById(R.id.twtanklevel1);
        twTankLevelButton2 = view.findViewById(R.id.twtanklevel2);
        twTankLevelButton3 = view.findViewById(R.id.twtanklevel3);

        //treated water TDS buttons
        twTdsButton1 = view.findViewById(R.id.twtds_button1);
        twTdsButton2 = view.findViewById(R.id.twtds_button2);
        twTdsButton3 = view.findViewById(R.id.twtds_button3);

        //plant capacity
        plantCapacityTextView = view.findViewById(R.id.plant_capacity_value);

        //volume and electricity entry fields
        volumeDispensedEditText = view.findViewById(R.id.volumedisposed_value);
        electricityEditText = view.findViewById(R.id.electricity_meter_value);

        //Raw Water and Treated water flow rate Dropdowns
        rwFlowRateSpinner = view.findViewById(R.id.rwflowrate_spinner);
        twFlowRateSpinner = view.findViewById(R.id.twflowrate_spinner);

        //Labels for all fields
        labelPlantCapacity = view.findViewById(R.id.label_plantcapacity);
        labelVoltage = view.findViewById(R.id.label_plantvoltage);
        labelRWTankLevel = view.findViewById(R.id.label_rwtanklevel);
        labelRWFlowRate = view.findViewById(R.id.label_rwflowrate);
        labelTWFlowRate = view.findViewById(R.id.label_twflowrate);
        labelTWTankLevel = view.findViewById(R.id.label_twtanklevel);
        labelVolumeDispensed = view.findViewById(R.id.label_volumedispensed);
        labelTWTDS = view.findViewById(R.id.label_twtds);
        labelElectricityMeter = view.findViewById(R.id.label_electricitymeter);


        //Camera buttons for required fields
        rwFlowRateCamera = view.findViewById(R.id.rwflowrateCamera);
        twFlowRateCamera = view.findViewById(R.id.twflowrateCamera);
        volumeDispensedCamera = view.findViewById(R.id.volumedispensedCamera);
        twTDSCamera = view.findViewById(R.id.twtdsCamera);
        electricityMeterCamera = view.findViewById(R.id.electricitymeterCamera);

        //Submit button
        operatorSubmitButton = view.findViewById(R.id.operator_submit_button);


        //Parsing the necessary operator details
        String userAccessToken = this.getArguments().getString("accessToken").toString();
        String operatorName = this.getArguments().getString("operatorName").toString();
        String operatorId = String.valueOf(this.getArguments().getInt("operatorId"));
        Long operatorMobile = Long.valueOf(this.getArguments().getLong("operatorMobile"));
        String capacity = String.valueOf(this.getArguments().getString("plantCapacity"));
        String latitude = String.valueOf(this.getArguments().getString("plantLatitude"));
        String longitude = String.valueOf(this.getArguments().getString("plantLongitude"));
        plantId = Integer.valueOf(this.getArguments().getInt("plantId"));
        plantCapacity = capacity;
        accessToken = userAccessToken;

        //set InitialValues
        setInitValues(operatorId,operatorName,operatorMobile,latitude,longitude);

        //instantiate the file upload Service
        fileService = APIUtils.getFileService();

        initViews();

        //set listeners for all buttons
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

        electricityMeterCamera.setOnClickListener(this);
        twTDSCamera.setOnClickListener(this);
        volumeDispensedCamera.setOnClickListener(this);
        twFlowRateCamera.setOnClickListener(this);
        rwFlowRateCamera.setOnClickListener(this);

        operatorSubmitButton.setOnClickListener(this);


        return view;
    }

    private void initViews() {
        rwTankLevelButton1.setEnabled(false);
        rwTankLevelButton2.setEnabled(false);
        rwTanklevelButton3.setEnabled(false);

        rwFlowRateSpinner.setEnabled(false);
        rwFlowRateCamera.setEnabled(false);
        rwFlowRateCamera.setImageResource(R.drawable.ic_camera_grey);

        twFlowRateSpinner.setEnabled(false);
        twFlowRateCamera.setEnabled(false);
        twFlowRateCamera.setImageResource(R.drawable.ic_camera_grey);

        twTankLevelButton1.setEnabled(false);
        twTankLevelButton2.setEnabled(false);
        twTankLevelButton3.setEnabled(false);

        volumeDispensedEditText.setEnabled(false);
        volumeDispensedCamera.setEnabled(false);
        volumeDispensedCamera.setImageResource(R.drawable.ic_camera_grey);

        twTdsButton1.setEnabled(false);
        twTdsButton2.setEnabled(false);
        twTdsButton3.setEnabled(false);
        twTDSCamera.setEnabled(false);
        twTDSCamera.setImageResource(R.drawable.ic_camera_grey);

        electricityEditText.setEnabled(false);
        electricityMeterCamera.setEnabled(false);
        electricityMeterCamera.setImageResource(R.drawable.ic_camera_grey);

        operatorSubmitButton.setEnabled(false);

        rwFlowRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentItem==i)
                    return;
                rwFlowRateCamera.setEnabled(true);
                rwFlowRateCamera.setImageResource(R.drawable.ic_camera);
                rwFlowRateSpinner.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner item listener
        twFlowRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentItem==i)
                    return;
                twFlowRateCamera.setEnabled(true);
                twFlowRateCamera.setImageResource(R.drawable.ic_camera);
                twFlowRateSpinner.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        volumeDispensedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enableCamera(volumeDispensedCamera);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        electricityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enableCamera(electricityMeterCamera);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void setInitValues(String operatorId, String operatorName, Long operatorMobile, String latitude, String longitude) {
        plantCapacityTextView.setText(plantCapacity);
        initializeFlowLevels(plantCapacity);
        operatorIdTv.setText(operatorId);
        operatorNameTv.setText(operatorName);
        operatorMobileNumberTv.setText(operatorMobile.toString());
        plantLatLongTv.setText(latitude + "," + longitude);
    }

    private void initializeFlowLevels(String plantCapacity) {
        List<String> rwFlowLevel = Arrays.asList(getResources().getStringArray(R.array.rw_flow_level_500));
        List<String> twFlowLevel = Arrays.asList(getResources().getStringArray(R.array.tw_flow_level_500));

        if (plantCapacity.equals("1000")) {
            rwFlowLevel = Arrays.asList(getResources().getStringArray(R.array.rw_flow_level_1000));
            twFlowLevel = Arrays.asList(getResources().getStringArray(R.array.tw_flow_level_1000));
        } else if (plantCapacity.equals("2000")) {
            rwFlowLevel = Arrays.asList(getResources().getStringArray(R.array.rw_flow_level_2000));
            twFlowLevel = Arrays.asList(getResources().getStringArray(R.array.tw_flow_level_2000));
        }

        ArrayAdapter<String> rwAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, rwFlowLevel);
        ArrayAdapter<String> twAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, twFlowLevel);

        rwFlowRateSpinner.setAdapter(rwAdapter);
        twFlowRateSpinner.setAdapter(twAdapter);
        rwAdapter.notifyDataSetChanged();
        twAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voltage_button1:
                modifyButtons(view, R.id.voltage_button1, R.id.voltage_button2, R.id.voltage_button3);
                plantVoltage = voltageButton1.getText().toString();
                enableButtons(rwTankLevelButton1,rwTankLevelButton2,rwTanklevelButton3);
                break;
            case R.id.voltage_button2:
                modifyButtons(view, R.id.voltage_button2, R.id.voltage_button1, R.id.voltage_button3);
                plantVoltage = voltageButton2.getText().toString();
                enableButtons(rwTankLevelButton1,rwTankLevelButton2,rwTanklevelButton3);
                break;
            case R.id.voltage_button3:
                modifyButtons(view, R.id.voltage_button3, R.id.voltage_button2, R.id.voltage_button1);
                plantVoltage = voltageButton3.getText().toString();
                enableButtons(rwTankLevelButton1,rwTankLevelButton2,rwTanklevelButton3);
                break;

            case R.id.rwtank_level1:
                modifyButtons(view, R.id.rwtank_level1, R.id.rwtank_level2, R.id.rwtank_level3);
                rwTankLevel = rwTankLevelButton1.getText().toString();
                rwFlowRateSpinner.setEnabled(true);
//                enableCamera(rwFlowRateCamera);
                break;
            case R.id.rwtank_level2:
                modifyButtons(view, R.id.rwtank_level2, R.id.rwtank_level1, R.id.rwtank_level3);
                rwTankLevel = rwTankLevelButton2.getText().toString();
                rwFlowRateSpinner.setEnabled(true);
//                enableCamera(rwFlowRateCamera);
                break;
            case R.id.rwtank_level3:
                modifyButtons(view, R.id.rwtank_level3, R.id.rwtank_level1, R.id.rwtank_level2);
                rwTankLevel = rwTanklevelButton3.getText().toString();
                rwFlowRateSpinner.setEnabled(true);
//                enableCamera(rwFlowRateCamera);
                break;
            case R.id.twtanklevel1:
                modifyButtons(view, R.id.twtanklevel1, R.id.twtanklevel2, R.id.twtanklevel3);
                twTankLevel = twTankLevelButton1.getText().toString();
                volumeDispensedEditText.setEnabled(true);
                break;
            case R.id.twtanklevel2:
                modifyButtons(view, R.id.twtanklevel2, R.id.twtanklevel1, R.id.twtanklevel3);
                twTankLevel = twTankLevelButton2.getText().toString();
                volumeDispensedEditText.setEnabled(true);
                break;
            case R.id.twtanklevel3:
                modifyButtons(view, R.id.twtanklevel3, R.id.twtanklevel1, R.id.twtanklevel2);
                twTankLevel = twTankLevelButton3.getText().toString();
                volumeDispensedEditText.setEnabled(true);
                break;
            case R.id.twtds_button1:
                modifyButtons(view, R.id.twtds_button1, R.id.twtds_button2, R.id.twtds_button3);
                twTDS = twTdsButton1.getText().toString();
                enableCamera(twTDSCamera);
                break;
            case R.id.twtds_button2:
                modifyButtons(view, R.id.twtds_button2, R.id.twtds_button1, R.id.twtds_button3);
                twTDS = twTdsButton2.getText().toString();
                enableCamera(twTDSCamera);
                break;
            case R.id.twtds_button3:
                modifyButtons(view, R.id.twtds_button3, R.id.twtds_button2, R.id.twtds_button1);
                twTDS = twTdsButton3.getText().toString();
                enableCamera(twTDSCamera);
                break;

            case R.id.twflowrateCamera:
                openCamera();
                currentImageIdentifier = "twflowrate";
                break;
            case R.id.electricitymeterCamera:
                openCamera();
                electricityEditText.setEnabled(false);
                currentImageIdentifier = "electricitymeter";
                break;
            case R.id.rwflowrateCamera:
                openCamera();
                currentImageIdentifier = "rwflowrate";
                break;
            case R.id.twtdsCamera:
                openCamera();
                currentImageIdentifier = "twtds";
                break;
            case R.id.volumedispensedCamera: {
                openCamera();
                volumeDispensedEditText.setEnabled(false);
                currentImageIdentifier = "volumedispnesed";
                break;
            }
            case R.id.operator_submit_button:
                validateData();
                break;
        }
    }

    private void enableCamera(ImageView cameraButton) {
        cameraButton.setEnabled(true);
        cameraButton.setImageResource(R.drawable.ic_camera);
    }

    private void enableButtons(Button button1, Button button2, Button button3) {
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
    }

    private void validateData() {


        if (plantVoltage.length() <= 0) {
            labelVoltage.setTextColor(Color.RED);
            valuesSetFlag = false;
        } else {
            labelVoltage.setTextColor(Color.BLACK);

        }

        if (rwTankLevel.length() <= 0) {
            labelRWTankLevel.setTextColor(Color.RED);
            valuesSetFlag = false;
        } else {
            labelRWTankLevel.setTextColor(Color.BLACK);

        }

        rwFlowRate = rwFlowRateSpinner.getSelectedItem().toString();
        if (rwFlowRate.length() <= 0 || rwFlowRate.contains("-Please select-")) {
            labelRWFlowRate.setTextColor(Color.RED);
            valuesSetFlag = false;
        } else {
            labelRWFlowRate.setTextColor(Color.BLACK);

        }

        twFlowRate = twFlowRateSpinner.getSelectedItem().toString();
        if (twFlowRate.length() <= 0 || twFlowRate.contains("-Please select-")) {
            labelTWFlowRate.setTextColor(Color.RED);
            valuesSetFlag = false;
        } else {
            labelTWFlowRate.setTextColor(Color.BLACK);

        }

        if (twTankLevel.length() <= 0) {
            labelTWTankLevel.setTextColor(Color.RED);
            valuesSetFlag = false;
        } else {
            labelTWTankLevel.setTextColor(Color.BLACK);

        }

        volumeDispensed = volumeDispensedEditText.getText().toString();
        if (Integer.valueOf(volumeDispensed) > Integer.valueOf(plantCapacity)) {
            labelVolumeDispensed.setTextColor(Color.RED);
            valuesSetFlag = false;
            Toast.makeText(getContext(), "Volume dispense value should be less than the plant capacity value", Toast.LENGTH_LONG).show();

        } else {
            labelVolumeDispensed.setTextColor(Color.BLACK);

        }

        if (twTDS.length() <= 0) {
            labelTWTDS.setTextColor(Color.RED);
            valuesSetFlag = false;
        } else {
            labelTWTDS.setTextColor(Color.BLACK);

        }

        electricityMeter = electricityEditText.getText().toString();
        if (electricityMeter.length() <= 0) {
            labelElectricityMeter.setTextColor(Color.RED);
            valuesSetFlag = false;
        } else {
            labelElectricityMeter.setTextColor(Color.BLACK);

        }

        if (valuesSetFlag) {
            uploadData();

        } else {
            Toast.makeText(getContext(), "Please fill all fields before Submit!", Toast.LENGTH_LONG).show();
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
            paramJson.put("rw_flow_rate_in_lph_image_id", rwFlowRateImageId);
            paramJson.put("tw_flow_rate_in_lph", twFlowRate);
            paramJson.put("tw_flow_rate_in_lph_image_id", twFlowRateImageId);
            paramJson.put("tw_tank_level", twTankLevel);
            paramJson.put("volume_dispensed_in_ltr", volumeDispensed);
            paramJson.put("volume_dispensed_in_ltr_image_id", volumeDispensedImageId);
            paramJson.put("tw_tds_ppm", twTDS);
            paramJson.put("tw_tds_ppm_image_id", twTDSImageId);
            paramJson.put("electricity_meter_kwh_or_units", electricityMeter);
            paramJson.put("electricity_meter_kwh_or_units_image_id", electricityMeterImageId);
            paramJson.put("plant_working_status", 1);

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading data to server..");
            progressDialog.show();
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
                        progressDialog.dismiss();
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
//        button2.setBackgroundResource(R.drawable.edittext_bg);
//        button2.setTextColor(Color.BLACK);
        button2.setEnabled(false);

        view = getView();
        Button button3 = view.findViewById(voltage_button3);
//        button3.setBackgroundResource(R.drawable.edittext_bg);
//        button3.setTextColor(Color.BLACK);
        button3.setEnabled(false);

    }

    public void openCamera() {
        Log.d("cameraintent", "called function");
        Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = getOutputMediaFile(1);
                            fileUri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                            // start the image capture Intent
                            startActivityForResult(intent, CAPTURE_IMAGE);
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
            Uri selectedImage = fileUri;
            Log.d("selectedImage", fileUri.toString());

            if (selectedImage != null) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                final String imageFilePath = cursor.getString(column_index_data);
                Log.d("imageURL", "image url : " + imageFilePath);

                String imagePath = selectedImage.toString();
                imagePath = imagePath.replace("file://","");
                uploadMedia(imagePath);
            }
        }

    }

    private void uploadMedia(String imageFilePath) {

        final int[] imageId = new int[1];
        File imageFile = new File(imageFilePath);
        Log.d("imageName", imageFile.getName());
        String mimeType = URLConnection.guessContentTypeFromName(imageFile.getName());
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), imageFile);
        final MultipartBody.Part body = MultipartBody.Part.createFormData("image_file", imageFile.getName(), requestBody);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading..");
        progressDialog.show();
        Call<ServerResponse> call = fileService.upload(body);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Log.d("response", "success");
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    int fileId = response.body().getFileId();
                    Log.d("fileId",String.valueOf(fileId));
                    Log.d("currentImage",currentImageIdentifier);
                    switch (currentImageIdentifier) {
                        case "twflowrate":
                            twFlowRateImageId = fileId;
                            twFlowRateCamera.setEnabled(false);
                            twFlowRateCamera.setImageResource(R.drawable.ic_camera_grey);
                            enableButtons(twTankLevelButton1,twTankLevelButton2,twTankLevelButton3);
                            break;
                        case  "electricitymeter":
                            electricityMeterImageId = fileId;
                            electricityMeterCamera.setEnabled(false);
                            electricityMeterCamera.setImageResource(R.drawable.ic_camera_grey);
                            operatorSubmitButton.setEnabled(true);
                            break;
                        case "rwflowrate" :
                            rwFlowRateImageId = fileId;
                            rwFlowRateCamera.setEnabled(false);
                            rwFlowRateCamera.setImageResource(R.drawable.ic_camera_grey);
                            twFlowRateSpinner.setEnabled(true);
                            break;
                        case "twtds" :
                            twTDSImageId = fileId;
                            twTDSCamera.setEnabled(false);
                            twTDSCamera.setImageResource(R.drawable.ic_camera_grey);
                            electricityEditText.setEnabled(true);
                            break;
                        case "volumedispnesed" :
                            volumeDispensedImageId = fileId;
                            volumeDispensedCamera.setEnabled(false);
                            volumeDispensedCamera.setImageResource(R.drawable.ic_camera_grey);
                            enableButtons(twTdsButton1,twTdsButton2,twTdsButton3);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("response", "failure");
                Toast.makeText(getContext(), "ERROR: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
