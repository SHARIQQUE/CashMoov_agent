package com.agent.cashmoovui.wallet_owner.subscriber;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.model.CityInfoModel;
import com.agent.cashmoovui.model.GenderModel;
import com.agent.cashmoovui.model.IDProofTypeModel;
import com.agent.cashmoovui.model.OccupationTypeModel;
import com.agent.cashmoovui.model.RegionInfoModel;
import com.agent.cashmoovui.model.RegistrationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SubscriberKYC extends AppCompatActivity implements View.OnClickListener {

    public static SubscriberKYC subscriberkycC;
    DatePickerDialog picker;
    public static EditText etFname,etLname,etPhone,etEmail,etAddress,etDob,etProofNo;
    TextView tvNext,spGender,spIdProof,spOccupation,spRegion,spCity;
    SpinnerDialog spinnerDialogRegion,spinnerDialogCity,spinnerDialogGender,spinnerDialogIdProofType,spinnerDialogOccupation;

    private ArrayList<String> regionList = new ArrayList<>();
    private ArrayList<RegionInfoModel.Region> regionModelList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<CityInfoModel.City> cityModelList = new ArrayList<>();

    private ArrayList<String> genderList = new ArrayList<>();
    private ArrayList<GenderModel.Gender> genderModelList=new ArrayList<>();

    private ArrayList<String> idProofTypeList = new ArrayList<>();
    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList=new ArrayList<>();
    public static String idProofTypeCode,subscriberWalletOwnerCode;

    private ArrayList<String> occupationTypeList = new ArrayList<>();
    private ArrayList<OccupationTypeModel.OccupationType> occupationTypeModelList=new ArrayList<>();

    public static final int REQUEST_CODE = 1;
    private ImageView mCalenderIcon_Image;
    public static TextView mDobText;
    private boolean isFormatting;
    private String walletOwnerCode,status;
    String languageToUse = "";
    MyApplication applicationComponentClass;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {


            String requiredValue = data.getStringExtra("PHONE");
            MyApplication.contactValidation(requiredValue,etPhone);

           // etPhone.setText(requiredValue);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applicationComponentClass = (MyApplication) getApplicationContext();

        languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");

        if (languageToUse.trim().length() == 0) {
            languageToUse = "en";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_abonne_one);
        subscriberkycC=this;
        getIds();

    }

    private void getIds() {
        mDobText=findViewById(R.id.dobText);

        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        spRegion = findViewById(R.id.spRegion);
        spCity = findViewById(R.id.spCity);
        etAddress = findViewById(R.id.etAddress);
        spGender = findViewById(R.id.spGender);
        etDob = findViewById(R.id.etDob);
        spIdProof = findViewById(R.id.spIdProof);
        etProofNo = findViewById(R.id.etProofNo);
        spOccupation = findViewById(R.id.spOccupation);
        tvNext = findViewById(R.id.tvNext);
        mCalenderIcon_Image=findViewById(R.id.calenderIcon_Image);


        String mobilelength=MyApplication.getSaveString("MobileLength",MyApplication.appInstance);

        etPhone.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(Integer.parseInt(mobilelength))});


        mCalenderIcon_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogFragment dialogfragment = new DatePickerDialogTheme();

                dialogfragment.show(getSupportFragmentManager(), "");

                // ffffff

            }
        });


        etPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etPhone.getRight() - etPhone.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here


                        Intent intent = new Intent(SubscriberKYC.this,
                                AddContact.class);
                        startActivityForResult(intent , REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });


        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isFormatting) {
                    return;
                }

                if (s.length() >= 9) {
                    saveMobilenoInfoAPI();


                }
                if(s.length()<Integer.parseInt(mobilelength)){
                    clearData();
                    tvNext.setVisibility(View.VISIBLE);
                }

                isFormatting = false;


            }
        });

        //  etDob.setInputType(InputType.TYPE_NULL);
/*
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(subscriberkycC,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etDob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, 1960, 01, 00);
                picker.show();
            }
        });
*/

        spRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if (spinnerDialogRegion!=null){
                    spinnerDialogRegion.showSpinerDialog();
                }

            }
        });
        spCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if (spinnerDialogCity!=null){
                    spinnerDialogCity.showSpinerDialog();
                }

            }
        });

        spGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if (spinnerDialogGender!=null)
                    spinnerDialogGender.showSpinerDialog();
            }
        });
        spIdProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if (spinnerDialogIdProofType!=null)
                    spinnerDialogIdProofType.showSpinerDialog();
            }
        });
        spOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if (spinnerDialogOccupation!=null)
                    spinnerDialogOccupation.showSpinerDialog();
            }
        });


        callApiRegions();

        setOnCLickListener();




    }

    private void clearData() {
        etFname.setText("");
        etLname.setText((""));
        etEmail.setText("");
        Agentcode="";
        tvNext.setVisibility(View.VISIBLE);


    }


    @Override
    protected void onStart() {
        super.onStart();




    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(subscriberkycC);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.tvNext:
                //

                if (etPhone.getText().toString().trim().isEmpty()) {
                    //MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_phone));
                    MyApplication.showTipError(this, getString(R.string.enter_phone_no), etPhone);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (etPhone.getText().toString().trim().length() < 9) {
                    //MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_phone));
                    MyApplication.showTipError(this, getString(R.string.enter_phone_no_val), etPhone);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (etFname.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_fname));
                    MyApplication.showTipError(this, getString(R.string.val_fname), etFname);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (etFname.getText().toString().trim().length() < 3) {
                    // MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_fname));
                    MyApplication.showTipError(this, getString(R.string.val_fname_len), etFname);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }

                if (etLname.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_lname));
                    MyApplication.showTipError(this, getString(R.string.val_lname), etLname);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (etLname.getText().toString().trim().length() < 3) {
                    // MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_lname));
                    MyApplication.showTipError(this, getString(R.string.val_lname_len), etLname);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }

              /*  if (etEmail.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_email));
                    MyApplication.showTipError(this, getString(R.string.val_email_valid), etEmail);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (!MyApplication.isEmail(etEmail.getText().toString())) {
                    MyApplication.showTipError(this, getString(R.string.val_email_valid), etEmail);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }*/

               /* if (!MyApplication.email_validation(etEmail.getText().toString())) {
                    MyApplication.showTipError(this, getString(R.string.val_email_valid), etEmail);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }*/
                if (spRegion.getText().toString().equals(getString(R.string.valid_select_region))) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this, getString(R.string.val_select_region), spRegion);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (spCity.getText().toString().equals(getString(R.string.valid_select_city))) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this, getString(R.string.val_select_city), spCity);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (etAddress.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_address));
                    MyApplication.showTipError(this, getString(R.string.val_address), etAddress);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (spGender.getText().toString().equals(getString(R.string.valid_select_gender))) {
                    //MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this, getString(R.string.val_select_gender), spGender);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (etDob.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_dob));
                    MyApplication.showTipError(this, getString(R.string.val_dob), etDob);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (spIdProof.getText().toString().equals(getString(R.string.valid_select_id_proof))) {
                    MyApplication.showTipError(this, getString(R.string.val_select_id_proof), spIdProof);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (etProofNo.getText().toString().trim().isEmpty()) {
                    MyApplication.showTipError(this, getString(R.string.val_proof_no), etProofNo);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (spOccupation.getText().toString().equals(getString(R.string.valid_select_occupation))) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_select_occupation));
                    MyApplication.showTipError(this, getString(R.string.val_select_occupation), spOccupation);
                    MyApplication.hideKeyboard(subscriberkycC);
                    return;
                }
                if (!MyApplication.isConnectingToInternet(SubscriberKYC.this)) {
                    Toast.makeText(SubscriberKYC.this, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                } else {


                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("code", Agentcode);

                            jsonObject.put("ownerName", etFname.getText().toString().trim());
                            jsonObject.put("lastName", etLname.getText().toString().trim());
                            jsonObject.put("dateOfBirth", etDob.getText().toString().trim());
                            jsonObject.put("idExpiryDate", "2025-12-12T00:00:00.000Z");
                            if(etEmail.getText().toString().equalsIgnoreCase("")){
                                jsonObject.put("email", "");

                            }else{
                                jsonObject.put("email", etEmail.getText().toString().trim());

                            }
                            jsonObject.put("gender", genderModelList.get((Integer) spGender.getTag()).getCode());
                            jsonObject.put("mobileNumber", etPhone.getText().toString().trim());
                            jsonObject.put("idProofNumber", etProofNo.getText().toString().trim());
                            jsonObject.put("idProofTypeCode", idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode());
                            jsonObject.put("issuingCountryCode", "100092");
                            jsonObject.put("registerCountryCode", "100092");
//                    jsonObject.put("addressLine1",etAddress.getText().toString().trim());
//                    jsonObject.put("city",etCity.getText().toString().trim());
                            jsonObject.put("notificationLanguage", "fr");
                            jsonObject.put("notificationTypeCode", "100002");
                            jsonObject.put("profileTypeCode", "100000");
                            jsonObject.put("occupationTypeCode", occupationTypeModelList.get((Integer) spOccupation.getTag()).getCode());
                        /////Sandep will remove 2 things 10-01-23
                          /*  jsonObject.put("state", "U");
                            jsonObject.put("status", "N");*/
                            jsonObject.put("walletOwnerCategoryCode", MyApplication.SubscriberCode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callRegisterApi(jsonObject);



                }
        }
    }

    private void callApiRegions() {
        try {
//http://202.131.144.130:8081/ewallet/public/region/country/100092
            API.GET_PUBLIC("ewallet/public/region/country/100092",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //   MyApplication.hideLoader();

                            if (jsonObject != null) {
                                regionList.clear();

                                generateNoteOnSD(SubscriberKYC.this,jsonObject.toString());
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectRegions = jsonObject.optJSONObject("country");
                                    JSONArray walletOwnerListArr = jsonObjectRegions.optJSONArray("regionList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        regionModelList.add(new RegionInfoModel.Region(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("countryCode"),
                                                data.optString("countryName"),
                                                data.optString("creationDate"),
                                                data.optString("name"),
                                                data.optString("state"),
                                                data.optString("status")

                                        ));

                                        regionList.add(data.optString("name").trim());

                                    }

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City",getString(R.string.cancel));// With No Animation
                                    spinnerDialogRegion = new SpinnerDialog(subscriberkycC, regionList, getString(R.string.select_region), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogRegion.setCancellable(true); // for cancellable
                                    spinnerDialogRegion.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogRegion.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spRegion.setText(item);
                                            spRegion.setTag(position);
                                            spCity.setText(getString(R.string.valid_select_city));

                                            callApiCity(regionModelList.get(position).getCode());
                                        }
                                    });


                                } else {
                                    MyApplication.showToast(subscriberkycC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }

    private void callApiCity(String code) {
        try {
//http://202.131.144.130:8081/ewallet/public/city/region/100068
            API.GET_PUBLIC("ewallet/public/city/region/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //   MyApplication.hideLoader();

                            if (jsonObject != null) {

                                cityList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONObject jsonObjectRegions = jsonObject.optJSONObject("region");
                                    JSONArray walletOwnerListArr = jsonObjectRegions.optJSONArray("cityList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        cityModelList.add(new CityInfoModel.City(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("creationDate"),
                                                data.optString("modificationDate"),
                                                data.optString("name"),
                                                data.optString("regionCode"),
                                                data.optString("regionName"),
                                                data.optString("state"),
                                                data.optString("status")

                                        ));

                                        cityList.add(data.optString("name").trim());

                                    }

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City",getString(R.string.cancel));// With No Animation
                                    spinnerDialogCity = new SpinnerDialog(subscriberkycC, cityList, getString(R.string.val_select_city), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogCity.setCancellable(true); // for cancellable
                                    spinnerDialogCity.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogCity.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spCity.setText(item);
                                            spCity.setTag(position);
                                            etAddress.requestFocus();
                                        }
                                    });

                                    callApiGenderType();

                                } else {
                                    MyApplication.showToast(subscriberkycC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }

    private void callApiGenderType() {
        try {
            API.GET_PUBLIC("ewallet/public/gender/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                genderList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("genderTypeList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        genderModelList.add(new GenderModel.Gender(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("type"),
                                                data.optString("status"),
                                                data.optString("creationDate")

                                        ));

                                        genderList.add(data.optString("type").trim());

                                    }

                                    spinnerDialogGender = new SpinnerDialog(subscriberkycC, genderList, getString(R.string.select_gender), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogGender.setCancellable(true); // for cancellable
                                    spinnerDialogGender.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogGender.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spGender.setText(item);
                                            spGender.setTag(position);
                                        }
                                    });

                                    callApiIdProofType();

                                } else {
                                    MyApplication.showToast(subscriberkycC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }


    }

    private void callApiIdProofType() {
        try {
            API.GET_PUBLIC("ewallet/public/idProofType/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                idProofTypeList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("idProofTypeList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        idProofTypeModelList.add(new IDProofTypeModel.IDProofType(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("type"),
                                                data.optString("status"),
                                                data.optString("creationDate")

                                        ));

                                        idProofTypeList.add(data.optString("type").trim());

                                    }

                                    spinnerDialogIdProofType = new SpinnerDialog(subscriberkycC, idProofTypeList, getString(R.string.valid_select_id_proofnew), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogIdProofType.setCancellable(true); // for cancellable
                                    spinnerDialogIdProofType.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogIdProofType.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spIdProof.setText(item);
                                            spIdProof.setTag(position);
                                            idProofTypeCode = idProofTypeModelList.get(position).getCode();
                                            etProofNo.requestFocus();
                                        }
                                    });

                                    callApiOccupationType();

                                } else {
                                    MyApplication.showToast(subscriberkycC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });


        } catch (Exception e) {

        }

    }

    private void callApiOccupationType() {
        try {
            API.GET_PUBLIC("ewallet/public/occupationType/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                occupationTypeList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("occupationTypeList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        occupationTypeModelList.add(new OccupationTypeModel.OccupationType(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("type"),
                                                data.optString("status"),
                                                data.optString("creationDate")

                                        ));

                                        occupationTypeList.add(data.optString("type").trim());

                                    }
                                    spinnerDialogOccupation = new SpinnerDialog(subscriberkycC, occupationTypeList, getString(R.string.select_occupation), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogOccupation.setCancellable(true); // for cancellable
                                    spinnerDialogOccupation.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogOccupation.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spOccupation.setText(item);
                                            spOccupation.setTag(position);
                                        }
                                    });

                                } else {
                                    MyApplication.showToast(subscriberkycC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }

    public static JSONObject walletOwnerJson=new JSONObject();
    public void callRegisterApi(JSONObject jsonObject){

        MyApplication.showloader(subscriberkycC,getString(R.string.pleasewait));
        API.POST_REQEST_REGISTER("ewallet/api/v1/walletOwner/subscriber", jsonObject, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                System.out.println("SubscriberKYC response======="+jsonObject.toString());
                if(jsonObject!=null){
                    if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                        subscriberWalletOwnerCode = jsonObject.optString("walletOwnerCode");
                        MyApplication.UserMobile=etPhone.getText().toString().trim();
                        callApiAddSubscriberAddress(subscriberWalletOwnerCode);

                    }else{
                        MyApplication.showToast(subscriberkycC,jsonObject.optString("resultDescription"));
                    }

                }
            }

            @Override
            public void failure(String aFalse) {
                MyApplication.hideLoader();
                MyApplication.showToast(subscriberkycC,aFalse);
            }

        });

    }

    private void callApiAddSubscriberAddress(String subscriberWalletOwnerCode) {
        try{
            JSONObject jsonObjectadd=new JSONObject();
            JSONObject addSubscriberJson=new JSONObject();
            try {
                addSubscriberJson.put("walletOwnerCode",subscriberWalletOwnerCode);

                jsonObjectadd.put("addTypeCode","");
                jsonObjectadd.put("addressLine1",etAddress.getText().toString().trim());
                jsonObjectadd.put("addressLine2","");
                jsonObjectadd.put("countryCode","100092");
                jsonObjectadd.put("city",cityModelList.get((Integer) spCity.getTag()).getCode());
                jsonObjectadd.put("regionCode",regionModelList.get((Integer) spRegion.getTag()).getCode());
                jsonObjectadd.put("location","");

                JSONArray jsonArray=new JSONArray();

                jsonArray.put(jsonObjectadd);
                addSubscriberJson.put("addressList",jsonArray);

            }catch (Exception e){

            }

            MyApplication.showloader(subscriberkycC,"Please wait!");
            API.POST_REQEST_REGISTER("ewallet/api/v1/address", addSubscriberJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            //MyApplication.showToast(getString(R.string.address_add_msg));
                            Intent i = new Intent(subscriberkycC, SubscriberKYCAttached.class);
                            startActivity(i);
                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(subscriberkycC,getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(subscriberkycC,jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {

                }
            });

        }catch (Exception e){

        }

    }
    public static class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.YEAR, -18);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_TRADITIONAL, this, year, month, day);

            datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());


            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            etDob.setText(year + "-" + (month+1) + "-" + day);
            mDobText.setVisibility(View.VISIBLE);
            // etDob.setText(year + "-" + (month+1) + "-" + day);

        }
    }

    public void generateNoteOnSD(Context context, String sFileName) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Login Request");

            if (!root.exists()) {
                root.mkdirs();
            }
            File txt = new File(root, "json.txt");
            FileWriter fw = new FileWriter(txt);

            File gpxfile = new File(root, sFileName);
            //FileWriter writer = new FileWriter(gpxfile);
            fw.append(sFileName);
            fw.flush();
            fw.close();
            //Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String Agentcode="";

    private void saveMobilenoInfoAPI() {


        API.GET_CASHIN_DETAILS("ewallet/api/v1/walletOwner/mobileNumber/" + etPhone.getText().toString(), languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    System.out.println("get moble response" + jsonObject.toString());

                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789327","requestTime":"Wed Oct 20 15:55:16 IST 2021","responseTime":"Wed Oct 20 15:55:16 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","pageable":{"limit":500,"offset":0,"totalRecords":1},"walletOwnerList":[{"id":110382,"code":"1000002488","walletOwnerCategoryCode":"100010","ownerName":"Kundan","mobileNumber":"118110111","idProofNumber":"vc12345","email":"kundan.kumar@esteltelecom.com","status":"Active","state":"Approved","stage":"Document","idProofTypeCode":"100006","idProofTypeName":"OTHER","idExpiryDate":"2021-09-29","notificationLanguage":"en","notificationTypeCode":"100000","notificationName":"EMAIL","gender":"M","dateOfBirth":"1960-01-26","lastName":"New","issuingCountryCode":"100092","issuingCountryName":"Guinea","registerCountryCode":"100092","registerCountryName":"Guinea","createdBy":"100375","modifiedBy":"100322","creationDate":"2021-09-16T17:08:49.796+0530","modificationDate":"2021-09-16T17:10:17.009+0530","walletExists":true,"profileTypeCode":"100001","profileTypeName":"tier2","walletOwnerCatName":"Subscriber","occupationTypeCode":"100002","occupationTypeName":"Others","requestedSource":"ADMIN","regesterCountryDialCode":"+224","issuingCountryDialCode":"+224","walletOwnerCode":"1000002488"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");


                    if (resultCode.equalsIgnoreCase("0")) {
                        JSONObject json2 = jsonObject.getJSONObject("walletOwner");



                        if(json2.getString("status").equalsIgnoreCase("Active") && json2.getString("state").equalsIgnoreCase("Approved")){
                            Toast.makeText(applicationComponentClass, getString(R.string.useralreadyexists), Toast.LENGTH_SHORT).show();
                            tvNext.setVisibility(View.GONE);

                            return;
                        }

                        etFname.setText(json2.optString("ownerName"));


                        if(json2.getString("walletOwnerCatName").equalsIgnoreCase("Agent")){
                            Toast.makeText(applicationComponentClass, getString(R.string.mobile_number_exits_with_agent), Toast.LENGTH_SHORT).show();
                            tvNext.setVisibility(View.GONE);
                           return;
                        }
                        if(json2.getString("walletOwnerCatName").equalsIgnoreCase("Branch")){
                            Toast.makeText(applicationComponentClass, getString(R.string.mobile_number_exits_with_branch), Toast.LENGTH_SHORT).show();
                            tvNext.setVisibility(View.GONE);

                            return;
                        }
                        if(json2.getString("stage").equalsIgnoreCase("Address")|| json2.getString("stage").equalsIgnoreCase("Bank")){
                            subscriberWalletOwnerCode=json2.optString("walletOwnerCode");
                            Intent i = new Intent(subscriberkycC, SubscriberKYCAttached.class);
                            startActivity(i);
                            return;
                        }

                        if(json2.getString("stage").equalsIgnoreCase("Document")){
                            subscriberWalletOwnerCode=json2.optString("walletOwnerCode");
                            Intent i = new Intent(subscriberkycC, SubscriberOtpActivity.class);
                            startActivity(i);
                            return;
                        }

                        subscriberWalletOwnerCode=json2.optString("walletOwnerCode");


                        tvNext.setVisibility(View.VISIBLE);

                        RegistrationModel registrationModel=new RegistrationModel(
                                    json2.getInt("id"),
                                    json2.optString("code"),
                                    json2.optString("walletOwnerParentCode"),
                                    json2.optString("walletOwnerCategoryCode"),
                                    json2.optString("ownerName"),
                                    json2.optString("mobileNumber"),
                                    json2.optString("businessTypeCode"),
                                    json2.optString("businessTypeName"),
                                    json2.optString("idProofNumber"),
                                    json2.optString("email"),
                                    json2.optString("status"),
                                    json2.optString("state"),
                                    json2.optString("stage"),
                                    json2.optString("idProofTypeCode"),
                                    json2.optString("idProofTypeName"),
                                    json2.optString("notificationLanguage"),
                                    json2.optString("notificationTypeCode"),
                                    json2.optString("notificationName"),
                                    json2.optString("gender"),
                                    json2.optString("dateOfBirth"),
                                    json2.optString("lastName"),
                                    json2.optString("issuingCountryCode"),
                                    json2.optString("issuingCountryName"),
                                    json2.optString("registerCountryCode"),
                                    json2.optString("registerCountryName"),
                                    json2.optString("createdBy"),
                                    json2.getBoolean("walletExists"),
                                    json2.optString("profileTypeCode"),
                                    json2.optString("profileTypeName"),
                                    json2.optString("currencyCode"),
                                    json2.optString("walletOwnerCatName"),
                                    json2.optString("requestedSource"),
                                    json2.optString("regesterCountryDialCode"),
                                    json2.optString("issuingCountryDialCode"),
                                    json2.optString("walletOwnerCode"),
                                    json2.optBoolean("hasChild"),
                                    json2.optString("currencyName"),
                                    json2.optBoolean("loginWithOtpRequired"),
                                    json2.optString("timeZone"));



                            Agentcode=registrationModel.getWalletOwnerCode();



                            etFname.setText(registrationModel.getOwnerName());
                            etLname.setText(registrationModel.getLastName());
                            etEmail.setText(registrationModel.getEmail());

                            //spGender.setText(registrationModel.getGender());
                          //  etDob.setText(registrationModel.getDateOfBirth());
                            // spCountry.setText(registrationModel.getRegisterCountryName());
                            // spBusinessType.setText(registrationModel.getBusinessTypeName());




                        //  Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();


                        // api_currency_sender();

                    } else {
                        clearData();

                        if(resultCode.equalsIgnoreCase("1354")) {
                            Toast.makeText(SubscriberKYC.this, resultDescription, Toast.LENGTH_LONG).show();
                        }
                        //  finish();
                    }


                } catch (Exception e) {
                    clearData();
                    // Toast.makeText(AgentKYC.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {
                clearData();

                MyApplication.hideLoader();
                Toast.makeText(SubscriberKYC.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


}
