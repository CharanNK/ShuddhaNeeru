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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class PlantFailureFragment extends Fragment implements View.OnClickListener {
    TextView plantIdTextView;
    RadioButton noPowerButton, noWaterButton, plantBreakdownButton;
    RadioGroup failureRadioGroup;
    Button rwTankLevelLow, rwTankLevelHalf, rwTankLevelFull;
    Button twTankLevelLow, twTankLevelHalf, twTankLevelFull;
    EditText volumeDispensedEditText;
    EditText twTDSEdiText;
    ImageView volumeDispensedCamera;
    Button submitButton;

    int plantId, volumeDispensedImageId;

    ConstantValues constantValues = new ConstantValues();

    int CAPTURE_IMAGE = 1000;
    private Uri fileUri;
    FileService fileService;

    String failureReason, rwTankLevel, twTankLevel, volumeDispensed, twTdsValue;
    private String dataUploadURL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/user/plant/not-working/status/update";
    private String accessToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plant_failure_layout, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        plantIdTextView = view.findViewById(R.id.label_plantid);

        rwTankLevelLow = view.findViewById(R.id.rwtank_level1);
        rwTankLevelHalf = view.findViewById(R.id.rwtank_level2);
        rwTankLevelFull = view.findViewById(R.id.rwtank_level3);

        twTankLevelLow = view.findViewById(R.id.twtanklevel1);
        twTankLevelHalf = view.findViewById(R.id.twtanklevel2);
        twTankLevelFull = view.findViewById(R.id.twtanklevel3);

        volumeDispensedEditText = view.findViewById(R.id.volumedisposed_value);
        twTDSEdiText = view.findViewById(R.id.twtds_value);

        volumeDispensedCamera = view.findViewById(R.id.volumedispensedCamera);

        noPowerButton = view.findViewById(R.id.radio_nopower);
        noWaterButton = view.findViewById(R.id.radio_nowater);
        plantBreakdownButton = view.findViewById(R.id.radio_breakdown);

        failureRadioGroup = view.findViewById(R.id.failure_radiogroup);

        submitButton = view.findViewById(R.id.operator_submit_button);

        accessToken = this.getArguments().getString("accessToken").toString();

        plantId = Integer.valueOf(this.getArguments().getInt("plant_id"));

        initViews();

        fileService = APIUtils.getFileService();

        return view;
    }

    private void initViews() {

        String plantIdLabel = plantIdTextView.getText().toString();
        plantIdTextView.setText(plantIdLabel + " " + String.valueOf(plantId));

        failureRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_nopower:
                        failureReason = "no_power";
                        enableButtons(rwTankLevelLow, rwTankLevelHalf, rwTankLevelFull);
                        break;
                    case R.id.radio_nowater:
                        failureReason = "no_water";
                        enableButtons(rwTankLevelLow, rwTankLevelHalf, rwTankLevelFull);
                        break;
                    case R.id.radio_breakdown:
                        failureReason = "break_down";
                        enableButtons(rwTankLevelLow, rwTankLevelHalf, rwTankLevelFull);
                        break;
                }
//                enableButtons(rwTankLevelLow,rwTankLevelHalf,rwTankLevelFull);
            }
        });

        twTDSEdiText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submitButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        volumeDispensedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                volumeDispensedCamera.setEnabled(true);
                volumeDispensedCamera.setImageResource(R.drawable.ic_camera);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        disableButtons(rwTankLevelLow, rwTankLevelHalf, rwTankLevelFull);

        disableButtons(twTankLevelLow, twTankLevelHalf, twTankLevelFull);

        volumeDispensedEditText.setEnabled(false);

        volumeDispensedCamera.setEnabled(false);
        volumeDispensedCamera.setImageResource(R.drawable.ic_camera_grey);

        twTDSEdiText.setEnabled(false);

        submitButton.setEnabled(false);

        rwTankLevelLow.setOnClickListener(this);
        rwTankLevelHalf.setOnClickListener(this);
        rwTankLevelFull.setOnClickListener(this);

        twTankLevelLow.setOnClickListener(this);
        twTankLevelHalf.setOnClickListener(this);
        twTankLevelFull.setOnClickListener(this);

        volumeDispensedCamera.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    private void disableButtons(Button button1, Button button2, Button button3) {
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
    }

    private void enableButtons(Button button1, Button button2, Button button3) {
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
    }


    @Override
    public void onClick(View view) {
        Log.d("clicked item", String.valueOf(view.getId()));
        switch (view.getId()) {
            case R.id.rwtank_level1:
                rwTankLevel = constantValues.CONSTANT_LOW;
                modifyButtons(rwTankLevelLow, rwTankLevelHalf, rwTankLevelFull);
                enableButtons(twTankLevelLow, twTankLevelHalf, twTankLevelFull);
                break;
            case R.id.rwtank_level2:
                rwTankLevel = constantValues.CONSTANT_HALF;
                modifyButtons(rwTankLevelHalf, rwTankLevelLow, rwTankLevelFull);
                enableButtons(twTankLevelLow, twTankLevelHalf, twTankLevelFull);
                break;
            case R.id.rwtank_level3:
                rwTankLevel = constantValues.CONSTANT_FULL;
                modifyButtons(rwTankLevelFull, rwTankLevelLow, rwTankLevelHalf);
                enableButtons(twTankLevelLow, twTankLevelHalf, twTankLevelFull);
                break;
            case R.id.twtanklevel1:
                twTankLevel = constantValues.CONSTANT_LOW;
                modifyButtons(twTankLevelLow, twTankLevelHalf, twTankLevelFull);
                volumeDispensedEditText.setEnabled(true);
                break;
            case R.id.twtanklevel2:
                twTankLevel = constantValues.CONSTANT_HALF;
                modifyButtons(twTankLevelHalf, twTankLevelLow, twTankLevelFull);
                volumeDispensedEditText.setEnabled(true);
                break;
            case R.id.twtanklevel3:
                twTankLevel = constantValues.CONSTANT_FULL;
                modifyButtons(twTankLevelFull, twTankLevelLow, twTankLevelHalf);
                volumeDispensedEditText.setEnabled(true);
                break;
            case R.id.volumedispensedCamera:
                openCamera();
                break;
            case R.id.operator_submit_button:
                twTdsValue = twTDSEdiText.getText().toString();
                uploadData();
                break;
        }
    }

    private void uploadData() {
        try {
            JSONObject paramJson = new JSONObject();
            paramJson.put("plant_id", plantId);
            paramJson.put("plant_working_status", 0);
            paramJson.put("plant_failure_reason", failureReason);
            paramJson.put("rw_tank_level", rwTankLevel);
            paramJson.put("tw_tank_level", twTankLevel);
            paramJson.put("volume_dispensed_in_ltr", volumeDispensed);
            paramJson.put("volume_dispensed_in_ltr_image_id", volumeDispensedImageId);
            paramJson.put("tw_tds_ppm", twTdsValue);

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading data to server..");
//            progressDialog.setMessage("Please wait..");
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
        } catch (Exception e) {
            Log.e("JsonError", e.getMessage());
        }
    }

    private void modifyButtons(Button button1, Button button2, Button button3) {
        button1.setBackgroundResource(R.drawable.edittext_selected_bg);
        button1.setTextColor(Color.WHITE);

        button2.setEnabled(false);

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
                imagePath = imagePath.replace("file://", "");
                uploadMedia(imagePath);
            }
        }

    }

    private void uploadMedia(String imageFilePath) {

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
                    Log.d("fileId", String.valueOf(fileId));
                    volumeDispensedImageId = fileId;
                    volumeDispensedCamera.setEnabled(false);
                    volumeDispensedCamera.setImageResource(R.drawable.ic_camera_grey);
                    volumeDispensed = volumeDispensedEditText.getText().toString();
                    volumeDispensedEditText.setEnabled(false);

                    twTDSEdiText.setEnabled(true);
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
