package com.agent.cashmoovui.wallet_owner.outlet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.agent.cashmoovui.model.BusinessTypeModel;
import com.agent.cashmoovui.model.CityInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.GenderModel;
import com.agent.cashmoovui.model.IDProofTypeModel;
import com.agent.cashmoovui.model.RegionInfoModel;
import com.agent.cashmoovui.model.RegistrationModel;
import com.agent.cashmoovui.model.UserDetailAgent;
import com.agent.cashmoovui.wallet_owner.branch.BranchKYC;
import com.agent.cashmoovui.wallet_owner.branch.BranchKYCAttached;
import com.agent.cashmoovui.wallet_owner.branch.BranchSignature;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class OutletKYC extends AppCompatActivity implements View.OnClickListener {
    public static OutletKYC outletkycC;
    DatePickerDialog picker;
    TextView spAccType, agentType, spBusinessType, spCountry, spRegion, spCity, spGender, spIdProof, tvNext;
    public static EditText etOutletName, etLname, etEmail, etPhone, etAddress, etDob_outlet, etProofNo;
    private ArrayList<String> businessTypeList = new ArrayList<>();
    private ArrayList<BusinessTypeModel.BusinessType> businessTypeModelList = new ArrayList<>();
    private ArrayList<String> idProofTypeList = new ArrayList<>();
    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList = new ArrayList<>();
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> countryModelList = new ArrayList<>();
    private ArrayList<String> regionList = new ArrayList<>();
    private ArrayList<RegionInfoModel.Region> regionModelList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<CityInfoModel.City> cityModelList = new ArrayList<>();
    private ArrayList<String> genderList = new ArrayList<>();
    private ArrayList<GenderModel.Gender> genderModelList = new ArrayList<>();

    private SpinnerDialog spinnerDialogAgentType, spinnerDialogBusinessType, spinnerDialogIdProofType, spinnerDialogCountry,
            spinnerDialogRegion, spinnerDialogCity, spinnerDialogGender;
    public static String idProofTypeCode, outletWalletOwnerCode;
    private SwitchButton sbLoginwithotp;
    private boolean loginwithOtp = false;
    private ImageView mCalenderIcon_Image;
    public static TextView mDobText;
    LinearLayout agentTypeLay;

    MyApplication applicationComponentClass;
    String languageToUse = "";
    public static final int REQUEST_CODE = 1;
    private boolean isFormatting;
    String walletOwnerCode, status;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {


            String requiredValue = data.getStringExtra("PHONE");


            MyApplication.contactValidation(requiredValue, etPhone);

            //etPhone.setText(requiredValue);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setContentView(R.layout.activity_addoutlet_one);
        outletkycC = this;
        getIds();


    }

    private void getIds() {

        mDobText = findViewById(R.id.dobText);

        spAccType = findViewById(R.id.spAccType);
        spBusinessType = findViewById(R.id.spBusinessType);
        spCountry = findViewById(R.id.spCountry);
        spRegion = findViewById(R.id.spRegion);
        spCity = findViewById(R.id.spCity);
        spGender = findViewById(R.id.spGender);
        spIdProof = findViewById(R.id.spIdProof);
        etOutletName = findViewById(R.id.etOutletName);
        etLname = findViewById(R.id.etLname);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etDob_outlet = findViewById(R.id.etDob_outlet);
        etProofNo = findViewById(R.id.etProofNo);
        sbLoginwithotp = findViewById(R.id.sbLoginwithotp);
        tvNext = findViewById(R.id.tvNext);
        mCalenderIcon_Image = findViewById(R.id.calenderIcon_Image);

        agentTypeLay = findViewById(R.id.agentTypeLay);

        // callApiCurrencyListnew1();


        String mobilelength = MyApplication.getSaveString("MobileLength", MyApplication.appInstance);

        etPhone.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Integer.parseInt(mobilelength))});

    /*    if (MyApplication.getSaveString("walletOwnerCategoryCode", OutletKYC.this).equalsIgnoreCase(MyApplication.AgentCode)) {
            agentTypeLay.setVisibility(View.GONE);
        } else {
            agentTypeLay.setVisibility(View.VISIBLE);
        }*/
        // etDob.setInputType(InputType.TYPE_NULL);
        mCalenderIcon_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogFragment dialogfragment = new OutletKYC.DatePickerDialogTheme();

                dialogfragment.show(getSupportFragmentManager(), "");

                // ffffff

            }
        });
        sbLoginwithotp.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    loginwithOtp = true;
                    // MyApplication.showToast(branchkycC, String.valueOf(loginwithOtp));
                } else {
                    loginwithOtp = false;
                    // MyApplication.showToast(branchkycC, String.valueOf(loginwithOtp));
                }
            }
        });

        // etDob.setInputType(InputType.TYPE_NULL);
/*
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(branchkycC,
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

        agentType = findViewById(R.id.agentType);
        agentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogAgentType != null)
                    spinnerDialogAgentType.showSpinerDialog();
            }
        });

        spBusinessType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBusinessType != null)
                    spinnerDialogBusinessType.showSpinerDialog();
            }
        });

        spBusinessType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBusinessType != null)
                    spinnerDialogBusinessType.showSpinerDialog();
            }
        });

        spBusinessType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBusinessType != null)
                    spinnerDialogBusinessType.showSpinerDialog();
            }
        });

        spCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogCountry != null)
                    spinnerDialogCountry.showSpinerDialog();
            }
        });

        spRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogRegion != null) {
                    spinnerDialogRegion.showSpinerDialog();
                }

            }
        });

        spCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogCity != null) {
                    spinnerDialogCity.showSpinerDialog();
                }

            }
        });
        spGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogGender != null)
                    spinnerDialogGender.showSpinerDialog();
            }
        });

        etPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPhone.getRight() - etPhone.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here


                        Intent intent = new Intent(OutletKYC.this,
                                AddContact.class);
                        startActivityForResult(intent, REQUEST_CODE);

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
                if (s.length() < Integer.parseInt(mobilelength)) {
                    clearData();
                    tvNext.setVisibility(View.VISIBLE);
                }
                isFormatting = false;


            }
        });

        spIdProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogIdProofType != null)
                    spinnerDialogIdProofType.showSpinerDialog();
            }
        });

        api_transactionHistory_agent();

        setOnCLickListener();


    }


    private void clearData() {
        etOutletName.setText("");
        etLname.setText((""));
        etEmail.setText("");
        Agentcode = "";
        tvNext.setVisibility(View.VISIBLE);


    }

    public String Agentcode = "";

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


                        if (json2.getString("status").equalsIgnoreCase("Active") && json2.getString("state").equalsIgnoreCase("Approved")) {
                            Toast.makeText(applicationComponentClass, getString(R.string.useralreadyexists), Toast.LENGTH_SHORT).show();
                            tvNext.setVisibility(View.GONE);

                            return;

                        }

                        etOutletName.setText(json2.optString("ownerName"));

                        if (json2.getString("walletOwnerCatName").equalsIgnoreCase("Agent")) {
                            Toast.makeText(applicationComponentClass, getString(R.string.mobile_number_exits_with_agent), Toast.LENGTH_SHORT).show();
                            tvNext.setVisibility(View.GONE);
                            return;
                        }
                        if (json2.getString("walletOwnerCatName").equalsIgnoreCase("Subscriber")) {
                            Toast.makeText(applicationComponentClass, getString(R.string.mobile_number_exits_with_subscriber), Toast.LENGTH_SHORT).show();
                            tvNext.setVisibility(View.GONE);

                            return;
                        }

                       /* if (json2.getString("stage").equalsIgnoreCase("Address") || json2.getString("stage").equalsIgnoreCase("Bank")) {
                            outletWalletOwnerCode = json2.optString("walletOwnerCode");
                            Intent i = new Intent(OutletKYC.this, BranchKYCAttached.class);
                            startActivity(i);
                            return;
                        }*/

                        if (json2.getString("stage").equalsIgnoreCase("Document")) {
                            outletWalletOwnerCode = json2.optString("walletOwnerCode");
                            Intent i = new Intent(outletkycC, OutletSignature.class);
                            startActivity(i);
                            return;
                        }

                        outletWalletOwnerCode = json2.optString("walletOwnerCode");


                        tvNext.setVisibility(View.VISIBLE);


                        RegistrationModel registrationModel = new RegistrationModel(
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


                        Agentcode = registrationModel.getWalletOwnerCode();


                        etOutletName.setText(registrationModel.getOwnerName());
                        etLname.setText(registrationModel.getLastName());
                        etEmail.setText(registrationModel.getEmail());
                        //spGender.setText(registrationModel.getGender());
                        //etDob.setText(registrationModel.getDateOfBirth());
                        // spCountry.setText(registrationModel.getRegisterCountryName());
                        // spBusinessType.setText(registrationModel.getBusinessTypeName());


                        //  Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();


                        // api_currency_sender();

                    } else {
                        clearData();

                        //  Toast.makeText(BranchKYC.this, resultDescription, Toast.LENGTH_LONG).show();
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
                Toast.makeText(OutletKYC.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void setOnCLickListener() {
        tvNext.setOnClickListener(outletkycC);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:


             /*   if (MyApplication.getSaveString("walletOwnerCategoryCode", OutletKYC.this).equalsIgnoreCase(MyApplication.AgentCode)) {

                } else {
                    if (agentType.getText().toString().equalsIgnoreCase(getString(R.string.select_agent))) {
                        MyApplication.showTipError(this, getString(R.string.please_select_agent), agentType);
                        MyApplication.hideKeyboard(outletkycC);
                        return;
                    }
                }*/


                if (etPhone.getText().toString().trim().isEmpty()) {
                    //MyApplication.showErrorToast(branchkycC,getString(R.string.val_phone));
                    MyApplication.showTipError(this, getString(R.string.enter_phone_no), etPhone);
                    MyApplication.hideKeyboard(outletkycC);
                    return;

                }
                if (etPhone.getText().toString().trim().length() < 9) {
                    //MyApplication.showErrorToast(branchkycC,getString(R.string.val_phone));
                    MyApplication.showTipError(this, getString(R.string.enter_phone_no_val), etPhone);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (etOutletName.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(branchkycC,getString(R.string.val_fname));
                    MyApplication.showTipError(this, getString(R.string.valid_outlet_name_new_empyty_error), etOutletName);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }


                if (etOutletName.getText().toString().trim().length() < 3) {
                    // MyApplication.showErrorToast(branchkycC,getString(R.string.val_fname));
                    MyApplication.showTipError(this, getString(R.string.val_outlet_name_len_new), etOutletName);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (etLname.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(branchkycC,getString(R.string.val_lname));
                    MyApplication.showTipError(this, getString(R.string.surname_star), etLname);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (etLname.getText().toString().trim().length() < 3) {
                    // MyApplication.showErrorToast(branchkycC,getString(R.string.val_lname));
                    MyApplication.showTipError(this, getString(R.string.surname_star_error_length), etLname);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (etEmail.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(branchkycC,getString(R.string.val_email));
                    MyApplication.showTipError(this, getString(R.string.val_email_valid), etEmail);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (!MyApplication.isEmail(etEmail.getText().toString())) {
                    MyApplication.showTipError(this, getString(R.string.val_email_valid), etEmail);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }

                if (spBusinessType.getText().toString().equals(getString(R.string.valid_select_business_type))) {
                    //MyApplication.showErrorToast(branchkycC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this, getString(R.string.val_select_business_type), spBusinessType);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (spCountry.getText().toString().equals(getString(R.string.valid_select_country))) {
                    //MyApplication.showErrorToast(branchkycC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this, getString(R.string.val_select_country), spCountry);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (spRegion.getText().toString().equals(getString(R.string.valid_select_region))) {
                    //MyApplication.showErrorToast(branchkycC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this, getString(R.string.val_select_region), spRegion);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (spCity.getText().toString().equals(getString(R.string.valid_select_city))) {
                    //MyApplication.showErrorToast(registersteponeC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this, getString(R.string.val_select_city), spCity);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (etAddress.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(branchkycC,getString(R.string.val_address));
                    MyApplication.showTipError(this, getString(R.string.val_address), etAddress);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (spGender.getText().toString().equals(getString(R.string.valid_select_gender))) {
                    //MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this, getString(R.string.val_select_gender), spGender);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (etDob_outlet.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(subscriberkycC,getString(R.string.val_dob));
                    MyApplication.showTipError(this, getString(R.string.val_dob), etDob_outlet);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (spIdProof.getText().toString().equals(getString(R.string.valid_select_id_proof))) {
                    MyApplication.showTipError(this, getString(R.string.val_select_id_proof), spIdProof);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }
                if (etProofNo.getText().toString().trim().isEmpty()) {
                    MyApplication.showTipError(this, getString(R.string.val_proof_no), etProofNo);
                    MyApplication.hideKeyboard(outletkycC);
                    return;
                }

                if (!MyApplication.isConnectingToInternet(OutletKYC.this)) {
                    Toast.makeText(OutletKYC.this, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("code", Agentcode);
                        jsonObject.put("ownerName", etOutletName.getText().toString());
                        jsonObject.put("lastName", etLname.getText().toString());
                        jsonObject.put("dateOfBirth", etDob_outlet.getText().toString().trim());
                        jsonObject.put("idExpiryDate", "");

                        jsonObject.put("walletExists", true);
                        jsonObject.put("walletOwnerParentCode", MyApplication.getSaveString("walletOwnerCode", outletkycC));

                        jsonObject.put("email", etEmail.getText().toString().trim());
                        jsonObject.put("gender", genderModelList.get((Integer) spGender.getTag()).getCode());
                        jsonObject.put("mobileNumber", etPhone.getText().toString().trim());
                        jsonObject.put("businessTypeCode", businessTypeModelList.get((Integer) spBusinessType.getTag()).getCode());
                        jsonObject.put("businessName", "");
                        jsonObject.put("lineOfBusiness", "");
                        jsonObject.put("groupCode", "");
                        jsonObject.put("idProofNumber", etProofNo.getText().toString().trim());
                        jsonObject.put("idProofTypeCode", idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode());
                        jsonObject.put("issuingCountryCode", "100092");
                        jsonObject.put("walletCurrencyList", new JSONArray(walletCurrencyListnew));
                        jsonObject.put("registerCountryCode", countryModelList.get((Integer) spCountry.getTag()).getCode());
//                    jsonObject.put("regionCode",regionModelList.get((Integer) spRegion.getTag()).getCode());
//                    jsonObject.put("addressLine1",etAddress.getText().toString().trim());
//                    jsonObject.put("city",etCity.getText().toString().trim());
                        jsonObject.put("notificationLanguage", MyApplication.getSaveString("Locale", outletkycC));
                        jsonObject.put("notificationTypeCode", "100002");
                        jsonObject.put("profileTypeCode", "100000");
                        if (MyApplication.getSaveString("walletOwnerCategoryCode", OutletKYC.this).equalsIgnoreCase(MyApplication.OutletCode)) {
                            jsonObject.put("walletOwnerParentCode", MyApplication.getSaveString("walletOwnerCode", outletkycC));
                        } else {
//                            jsonObject.put("walletOwnerParentCode", arrayList_modalDetailsnew.get((Integer) agentType.getTag()).getWalletOwnerCode());
                        }

                        jsonObject.put("walletOwnerCategoryCode", MyApplication.OutletCode);
                        jsonObject.put("loginWithOtpRequired", loginwithOtp);

                        System.out.println("get json" + jsonObject);

                        System.out.println("get val" + new JSONArray(walletCurrencyListnew));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callRegisterApi(jsonObject);

                 /*   if (MyApplication.getSaveString("walletOwnerCategoryCode", OutletKYC.this).equalsIgnoreCase(MyApplication.AgentCode)) {
                        callApiCurrencyListnew(MyApplication.getSaveString("walletOwnerCode", outletkycC), jsonObject);

                    } else {
                        callApiCurrencyListnew(MyApplication.getSaveString("walletOwnerCode", outletkycC), jsonObject);

                    }*/


                }
        }


    }

    ArrayList<UserDetailAgent> arrayList_modalDetailsnew;
    ArrayList<String> arraylistAgentStr;

    private void api_transactionHistory_agent() {

        // http://202.131.144.130:8081/ewallet/api/v1/transaction/all?srcWalletOwnerCode=1000002692&resultCode=0&offset=0&limit=5000

        if (MyApplication.getSaveString("walletOwnerCategoryCode", OutletKYC.this).equalsIgnoreCase(MyApplication.MerchatCode)) {
            callBusinessTypeList();
        } else {
            String usercode_from_msis = MyApplication.getSaveString("USERCODE", OutletKYC.this);

            API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwner/all/parent/" + MyApplication.getSaveString("walletOwnerCode", OutletKYC.this), languageToUse, new Api_Responce_Handler() {

                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {


                        System.out.println("get jsonob" + jsonObject);

                        MyApplication.saveString("JsonObject", jsonObject.toString(), OutletKYC.this);
                        arrayList_modalDetailsnew = new ArrayList<>();

                        arraylistAgentStr = new ArrayList<>();
                        UserDetailAgent userDetailAgent;// = new UserDetailAgent();

                        arrayList_modalDetailsnew.clear();
                        arraylistAgentStr.clear();

                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {


                            if (jsonObject.has("walletOwner")) {

                                JSONObject jsonObject1_walletOwner = jsonObject.getJSONObject("walletOwner");


                                if (jsonObject1_walletOwner.has("walletOwnerChildList")) {

                                    JSONArray jsonArray = jsonObject1_walletOwner.getJSONArray("walletOwnerChildList");


                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        if (MyApplication.AgentCode.equalsIgnoreCase(jsonObject1.getString("walletOwnerCategoryCode"))) {
                                            userDetailAgent = new UserDetailAgent();

                                            String state = jsonObject1.getString("state");

                                            if (state.equalsIgnoreCase("Created")) {
                                                String ownerName = jsonObject1.getString("ownerName");
                                                userDetailAgent.setOwnerName(ownerName);
                                                arraylistAgentStr.remove(ownerName);
                                            } else {

                                                if (jsonObject1.has("ownerName")) {

                                                    String ownerName = jsonObject1.getString("ownerName");
                                                    userDetailAgent.setOwnerName(ownerName);
                                                    arraylistAgentStr.add(ownerName);

                                                }
                                            }

                                            if (jsonObject1.has("mobileNumber")) {
                                                String mobileNumber = jsonObject1.getString("mobileNumber");

                                                userDetailAgent.setMobileNumber(mobileNumber);

                                            }

                                            if (jsonObject1.has("email")) {
                                                String email = jsonObject1.getString("email");

                                                userDetailAgent.setEmail(email);
                                            }
                                            if (jsonObject1.has("issuingCountryName")) {
                                                String issuingCountryName = jsonObject1.getString("issuingCountryName");
                                                userDetailAgent.setIssuingCountryName(issuingCountryName);


                                            }

                                            if (jsonObject1.has("walletOwnerCode")) {

                                                String walletOwnerCode = jsonObject1.getString("walletOwnerCode");
                                                userDetailAgent.setWalletOwnerCode(walletOwnerCode);

                                            }

                                            if (jsonObject1.has("registerCountryCode")) {

                                                String registerCountryCode = jsonObject1.getString("registerCountryCode");
                                                userDetailAgent.setRegisterCountryCode(registerCountryCode);

                                            }

                                            arrayList_modalDetailsnew.add(userDetailAgent);

                                            System.out.println("get list" + arrayList_modalDetailsnew);

                                        }


                                    }
                                    spinnerDialogAgentType = new SpinnerDialog(outletkycC, arraylistAgentStr, getString(R.string.select_agent), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation

                                    spinnerDialogAgentType.setCancellable(true); // for cancellable
                                    spinnerDialogAgentType.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogAgentType.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            agentType.setText(item);
                                            agentType.setTag(position);

                                        }
                                    });
                                    callBusinessTypeList();

                                } else {
                                    MyApplication.showToast(OutletKYC.this, getString(R.string.please_add_agent_createbranch));
                                    finish();
                                }


                            }


                        } else {
                            Toast.makeText(OutletKYC.this, resultDescription, Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception e) {
                        Toast.makeText(OutletKYC.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        finish();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();
                    Toast.makeText(OutletKYC.this, aFalse, Toast.LENGTH_SHORT).show();
                    finish();

                }
            });

        }


    }

    private void callBusinessTypeList() {
        try {

            API.GET("ewallet/api/v1/master/BUSINESSTYPE",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();


                            if (jsonObject != null) {
                                businessTypeList.clear();
                                businessTypeModelList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    JSONArray businessTypeListArr = jsonObject.optJSONArray("businessTypeList");
                                    for (int i = 0; i < businessTypeListArr.length(); i++) {
                                        JSONObject data = businessTypeListArr.optJSONObject(i);
                                        businessTypeModelList.add(new BusinessTypeModel.BusinessType(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("creationDate"),
                                                data.optString("type")

                                        ));
                                        businessTypeList.add(data.optString("type").trim());
                                    }

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City",getString(R.string.cancel));// With No Animation
                                    spinnerDialogBusinessType = new SpinnerDialog(outletkycC, businessTypeList, getString(R.string.val_select_business_type), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation

                                    spinnerDialogBusinessType.setCancellable(true); // for cancellable
                                    spinnerDialogBusinessType.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogBusinessType.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spBusinessType.setText(item);
                                            spBusinessType.setTag(position);

                                        }
                                    });

                                } else {
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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

        callApiCountry();

    }

    private void callApiCountry() {
        try {

            API.GET("ewallet/api/v1/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            // MyApplication.hideLoader();


                            if (jsonObject != null) {
                                countryList.clear();
                                countryModelList.clear();
                                countryList.clear();
                                countryModelList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        if (data.optString("code").equalsIgnoreCase(MyApplication.getSaveString("COUNTRYCODE_AGENT", outletkycC))) {

                                            countryModelList.add(new CountryInfoModel.Country(
                                                    data.optInt("id"), data.optInt("id"),
                                                    data.optString("code"),
                                                    data.optString("isoCode"),
                                                    data.optString("name"),
                                                    data.optString("countryCode"),
                                                    data.optString("status"),
                                                    data.optString("dialCode"),
                                                    data.optString("currencyCode"),
                                                    data.optString("currencySymbol"),
                                                    data.optString("creationDate"),
                                                    data.optBoolean("subscriberAllowed")

                                            ));

                                            countryList.add(data.optString("name").trim());
                                        }
                                    }

                                    spinnerDialogCountry = new SpinnerDialog(outletkycC, countryList, getString(R.string.valid_select_country), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogCountry.setCancellable(true); // for cancellable
                                    spinnerDialogCountry.setShowKeyboard(false);// for open keyboard by default

                                    if (countryList.size() == 1) {
                                        spCountry.setText(countryModelList.get(0).name);
                                        spCountry.setTag(0);
                                        callApiCurrencyList(MyApplication.getSaveString("COUNTRYCODE_AGENT", outletkycC));
                                    }
                                    spinnerDialogCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spCountry.setText(item);
                                            spCountry.setTag(position);
                                            //  callApiRegions();
                                            // callApiCurrencyList(countryModelList.get(position).getCode());
                                            callApiCurrencyList(MyApplication.getSaveString("COUNTRYCODE_AGENT", outletkycC));


                                        }
                                    });

                                } else {
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiRegions(String code) {
        try {

            API.GET("ewallet/api/v1/region/country/" + code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //
                            // MyApplication.hideLoader();

                            if (jsonObject != null) {
                                regionList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
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
                                    spinnerDialogRegion = new SpinnerDialog(outletkycC, regionList, getString(R.string.select_region), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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
            API.GET_PUBLIC("ewallet/public/city/region/" + code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //   MyApplication.hideLoader();

                            if (jsonObject != null) {

                                cityList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
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
                                    spinnerDialogCity = new SpinnerDialog(outletkycC, cityList, getString(R.string.val_select_city), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogCity.setCancellable(true); // for cancellable
                                    spinnerDialogCity.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogCity.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spCity.setText(item);
                                            spCity.setTag(position);
                                        }
                                    });

                                    callApiGenderType();

                                } else {
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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


    ArrayList<String> walletCurrencyList;
    JSONObject currenyList;

    private void callApiCurrencyList(String code) {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/" + code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //
                            // MyApplication.hideLoader();
                            walletCurrencyList = new ArrayList<>();
                            walletCurrencyList.clear();
                            currenyList = new JSONObject();
                            if (jsonObject != null) {
                                regionList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    JSONObject jsonObjectRegions = jsonObject.optJSONObject("country");
                                    JSONArray walletOwnerListArr = jsonObjectRegions.optJSONArray("countryCurrencyList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        walletCurrencyList.add(data.optString("currencyCode"));
//"walletCurrencyList":["100106","100062","100003","100004"]


                                    }

                                    System.out.println("LISTTTT  " + walletCurrencyList.toString());

                                    callApiRegions(code);

                                } else {
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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


    private void callApiCurrencyListnew1() {
        try {


            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/walletOwnerparentCurrency/" + MyApplication.getSaveString("walletOwnerCode", outletkycC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //
                            // MyApplication.hideLoader();
                            walletCurrencyListnew = new ArrayList<>();
                            // walletCurrencyListnew.clear();
                            currenyListnew = new JSONObject();

                            System.out.println("get response" + jsonObject.toString());
                            if (jsonObject != null) {
                                //  regionList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    //JSONObject jsonObjectRegions = jsonObject.optJSONObject("country");
                                    //   JSONArray walletOwnerListArr = jsonObjectRegions.optJSONArray("countryCurrencyList");
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletOwnerCountryCurrencyList");


                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        walletCurrencyListnew.add(data.optString("currencyCode"));
//"walletCurrencyList":["100106","100062","100003","100004"]


                                    }

                                    System.out.println("get data  " + walletCurrencyListnew.toString());


                                } else {
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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


    ArrayList<String> walletCurrencyListnew;
    JSONObject currenyListnew;

    // http://180.179.201.109:8081/ewallet/api/v1/walletOwnerCountryCurrency/walletOwnerparent/1000005350
    private void callApiCurrencyListnew(String code, JSONObject jsonObjectnew123) {
        try {


            API.GET("ewallet/api/v1/walletOwnerCountryCurrency/walletOwnerparentCurrency/" + MyApplication.getSaveString("walletOwnerCode", outletkycC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //
                            // MyApplication.hideLoader();
                            walletCurrencyListnew = new ArrayList<>();
                            // walletCurrencyListnew.clear();
                            currenyListnew = new JSONObject();

                            System.out.println("get response" + jsonObject.toString());
                            if (jsonObject != null) {
                                //  regionList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    //JSONObject jsonObjectRegions = jsonObject.optJSONObject("country");
                                    //   JSONArray walletOwnerListArr = jsonObjectRegions.optJSONArray("countryCurrencyList");
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletOwnerCountryCurrencyList");


                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        walletCurrencyListnew.add(data.optString("currencyCode"));
//"walletCurrencyList":["100106","100062","100003","100004"]


                                    }

                                    System.out.println("get data  " + walletCurrencyListnew.toString());

                                    System.out.println("get jsonObjectnew123  " + jsonObjectnew123);

                                    callRegisterApi(jsonObjectnew123);


                                } else {
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
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

                                    spinnerDialogGender = new SpinnerDialog(outletkycC, genderList, getString(R.string.select_gender), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
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

                                    spinnerDialogIdProofType = new SpinnerDialog(outletkycC, idProofTypeList, getString(R.string.valid_select_id_proofnew), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogIdProofType.setCancellable(true); // for cancellable
                                    spinnerDialogIdProofType.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogIdProofType.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spIdProof.setText(item);
                                            spIdProof.setTag(position);
                                            idProofTypeCode = idProofTypeModelList.get(position).getCode();
                                        }
                                    });

                                } else {
                                    MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
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

    public static JSONObject walletOwnerJson = new JSONObject();

    public void callRegisterApi(JSONObject jsonObject) {

        MyApplication.showloader(outletkycC, getString(R.string.pleasewait));
        API.POST_REQEST_REGISTER("ewallet/api/v1/walletOwner/outlet", jsonObject, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                System.out.println("OutLetKYC response=======" + jsonObject.toString());
                if (jsonObject != null) {
                    if (jsonObject.optString("resultCode").equalsIgnoreCase("0")) {
                        outletWalletOwnerCode = jsonObject.optString("walletOwnerCode");
                        MyApplication.UserMobile = etPhone.getText().toString().trim();
                        callApiAddBranchAddress(outletWalletOwnerCode);
                    } else {
                        MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription"));
                    }

                }
            }

            @Override
            public void failure(String aFalse) {
                MyApplication.hideLoader();
                MyApplication.showToast(outletkycC, aFalse);
            }

        });
    }

    private void callApiAddBranchAddress(String branchWalletOwnerCode) {
        try {
            JSONObject jsonObjectadd = new JSONObject();
            JSONObject addSubscriberJson = new JSONObject();
            try {
                addSubscriberJson.put("walletOwnerCode", branchWalletOwnerCode);

                jsonObjectadd.put("addTypeCode", "");
                jsonObjectadd.put("addressLine1", etAddress.getText().toString().trim());
                jsonObjectadd.put("addressLine2", "");
                jsonObjectadd.put("countryCode", countryModelList.get((Integer) spCountry.getTag()).getCode());
                jsonObjectadd.put("city", cityModelList.get((Integer) spCity.getTag()).getCode());
                jsonObjectadd.put("regionCode", regionModelList.get((Integer) spRegion.getTag()).getCode());
                jsonObjectadd.put("location", "");

                JSONArray jsonArray = new JSONArray();

                jsonArray.put(jsonObjectadd);
                addSubscriberJson.put("addressList", jsonArray);

            } catch (Exception e) {

            }

            MyApplication.showloader(outletkycC, "Please wait!");
            API.POST_REQEST_REGISTER("ewallet/api/v1/address", addSubscriberJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    if (jsonObject != null) {
                        if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                            //MyApplication.showToast(getString(R.string.address_add_msg));
                            Intent i = new Intent(outletkycC, OutletSignature.class);
                            startActivity(i);
                            finish();

                        } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                            MyApplication.showToast(outletkycC, getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(outletkycC, jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {

                }
            });

        } catch (Exception e) {

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

            etDob_outlet.setText(year + "-" + (month + 1) + "-" + day);
            mDobText.setVisibility(View.VISIBLE);
            // etDob.setText(year + "-" + (month+1) + "-" + day);

        }
    }

}