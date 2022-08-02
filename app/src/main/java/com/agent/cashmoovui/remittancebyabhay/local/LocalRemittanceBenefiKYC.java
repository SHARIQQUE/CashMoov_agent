package com.agent.cashmoovui.remittancebyabhay.local;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.CityInfoModel;
import com.agent.cashmoovui.model.CountryRemittanceInfoModel;
import com.agent.cashmoovui.model.GenderModel;
import com.agent.cashmoovui.model.IDProofTypeModel;
import com.agent.cashmoovui.model.RegionInfoModel;
import com.agent.cashmoovui.model.SubscriberInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class LocalRemittanceBenefiKYC extends AppCompatActivity implements View.OnClickListener {
    public static LocalRemittanceBenefiKYC localremitbenefikycC;
    ImageView imgBack, imgHome;
    boolean isCustomerData;
    public static final int REQUEST_CODE = 1;
    DatePickerDialog picker;
    private TextView spinner_destination_gender, spinner_destination_country, spinner_destination_region,
            spinner_destination_idprooftype, spinner_destination_issuingCountry, tvNext;
    private AutoCompleteTextView et_destination_mobileNumber;
    private EditText et_destination_firstName, et_destination_lastName, edittext_email_destination,
             et_destination_address, et_destination_city, et_destination_idproofNumber,
            et_destination_idproof_expiry;
    public static EditText etComment;
    private static EditText et_destination_dob;
    public static TextView mDobText;
    private ImageView mCalenderIcon_Image;

    private ArrayList<String> benefiGenderList = new ArrayList<>();
    private ArrayList<GenderModel.Gender> benefiGenderModelList=new ArrayList<>();
    private ArrayList<String> regionList = new ArrayList<>();
    private ArrayList<RegionInfoModel.Region> regionModelList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<CityInfoModel.City> cityModelList = new ArrayList<>();
    private ArrayList<String> idProofTypeList = new ArrayList<>();
    private ArrayList<String> issuingCountryList = new ArrayList<>();
    private ArrayList<CountryRemittanceInfoModel.RemitCountry> issuingCountryModelList = new ArrayList<>();
    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList=new ArrayList<>();
    private SpinnerDialog spinnerDialogBenefiGender,spinnerDialogBenefiRegion,
            spinnerDialogIssuingCountry,spinnerDialogBenefiIdProofType;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_remittance_benefi_kyc);
        localremitbenefikycC = this;
        setBackMenu();
        getIds();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(localremitbenefikycC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(localremitbenefikycC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public boolean isSet=false;
    public static JSONObject walletOwner = new JSONObject();



    private void getIds() {
        spinner_destination_gender = findViewById(R.id.spinner_destination_gender);
        spinner_destination_country = findViewById(R.id.spinner_destination_country);
        spinner_destination_region = findViewById(R.id.spinner_destination_region);
        spinner_destination_idprooftype = findViewById(R.id.spinner_destination_idprooftype);
        spinner_destination_issuingCountry = findViewById(R.id.spinner_destination_issuingCountry);
        et_destination_mobileNumber = findViewById(R.id.et_destination_mobileNumber);
        et_destination_firstName = findViewById(R.id.et_destination_firstName);
        et_destination_lastName = findViewById(R.id.et_destination_lastName);
        edittext_email_destination = findViewById(R.id.edittext_email_destination);
        et_destination_dob = findViewById(R.id.et_destination_dob);
        et_destination_address = findViewById(R.id.et_destination_address);
        et_destination_city = findViewById(R.id.et_destination_city);
        et_destination_idproofNumber = findViewById(R.id.et_destination_idproofNumber);
        et_destination_idproof_expiry = findViewById(R.id.et_destination_idproof_expiry);
        etComment = findViewById(R.id.et_fp_reason_sending);
        tvNext = findViewById(R.id.tvNext);
        mDobText=findViewById(R.id.dobText);

        mCalenderIcon_Image=findViewById(R.id.calenderIcon_Image);
        // etDob.setInputType(InputType.TYPE_NULL);
        mCalenderIcon_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogTheme();

                dialogfragment.show(getSupportFragmentManager(), "");

                // ffffff

            }
        });

        spinner_destination_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBenefiGender != null)
                    spinnerDialogBenefiGender.showSpinerDialog();
            }
        });

        spinner_destination_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBenefiRegion != null)
                    spinnerDialogBenefiRegion.showSpinerDialog();
            }
        });


        spinner_destination_country.setText(LocalRemittanceActivity.recCountryName);

        spinner_destination_idprooftype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBenefiIdProofType != null)
                    spinnerDialogBenefiIdProofType.showSpinerDialog();
            }
        });

        spinner_destination_issuingCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogIssuingCountry != null)
                    spinnerDialogIssuingCountry.showSpinerDialog();
            }
        });

        et_destination_mobileNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_destination_mobileNumber.getRight() - et_destination_mobileNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here


                        Intent intent = new Intent(localremitbenefikycC, AddContact.class);
                        startActivityForResult(intent , REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });

        et_destination_mobileNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.contains(",")) {
                    String[] list = value.split(",");
                    isSet = true;
                    if (list.length == 3) {
                        et_destination_mobileNumber.setText(list[0]);
                        et_destination_firstName.setText(list[1]);
                        et_destination_lastName.setText(list[2]);
//                        etComment.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        et_destination_mobileNumber.setText(list[0]);
                        et_destination_firstName.setText(list[1]);
//                        etLname.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(etLname, InputMethodManager.SHOW_IMPLICIT);
                    }
                }else{
                    et_destination_firstName.setText("");
                    et_destination_lastName.setText("");
                    edittext_email_destination.setText("");
                    et_destination_dob.setText("");
                    spinner_destination_gender.setText(getString(R.string.valid_select_gender));
                    et_destination_city.setText("");
                    et_destination_address.setText("");
                    spinner_destination_region.setText(getString(R.string.val_select_region));
                    spinner_destination_idprooftype.setText(getString(R.string.val_select_id_proof));
                    et_destination_idproofNumber.setText("");
                    et_destination_idproof_expiry.setText("");
                    spinner_destination_issuingCountry.setText(getString(R.string.val_select_issuing_country));
                    etComment.setText("");
                }

            }
        });

        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);


        et_destination_mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(localremitbenefikycC)) {

                    Matcher m = p.matcher(s);
                    if(s.length()>=9 && m.matches()){
                        if(isSet) {
                            isSet=false;
                        }else{
                            et_destination_firstName.setText("");
                            et_destination_lastName.setText("");
                            edittext_email_destination.setText("");
                            et_destination_dob.setText("");
                            spinner_destination_gender.setText(getString(R.string.valid_select_gender));
                            et_destination_city.setText("");
                            et_destination_address.setText("");
                            spinner_destination_region.setText(getString(R.string.val_select_region));
                            spinner_destination_idprooftype.setText(getString(R.string.val_select_id_proof));
                            et_destination_idproofNumber.setText("");
                            et_destination_idproof_expiry.setText("");
                            spinner_destination_issuingCountry.setText(getString(R.string.val_select_issuing_country));
                            etComment.setText("");
                            callApiSubsriberList();
                        }
                    }else{
                        et_destination_firstName.setText("");
                        et_destination_lastName.setText("");
                        edittext_email_destination.setText("");
                        et_destination_dob.setText("");
                        spinner_destination_gender.setText(getString(R.string.valid_select_gender));
                        et_destination_city.setText("");
                        et_destination_address.setText("");
                        spinner_destination_region.setText(getString(R.string.val_select_region));
                        spinner_destination_idprooftype.setText(getString(R.string.val_select_id_proof));
                        et_destination_idproofNumber.setText("");
                        et_destination_idproof_expiry.setText("");
                        spinner_destination_issuingCountry.setText(getString(R.string.val_select_issuing_country));
                        etComment.setText("");
                    }

                } else {
                    Toast.makeText(localremitbenefikycC, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
        });


        et_destination_idproof_expiry.setInputType(InputType.TYPE_NULL);
        et_destination_idproof_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog

                picker = new DatePickerDialog(localremitbenefikycC,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_destination_idproof_expiry.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis());
                picker.show();
            }
        });

        callApigenderType();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(localremitbenefikycC);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNext:

                if (et_destination_mobileNumber.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.val_phone));
                    return;
                }
                if (et_destination_mobileNumber.getText().toString().trim().length() < 9) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.enter_phone_no_val));
                    return;
                }
                if (et_destination_firstName.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.val_fname));
                    return;
                }
                if (et_destination_firstName.getText().toString().trim().length() < 3) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.val_fname_len));
                    return;
                }
                if (!et_destination_lastName.getText().toString().trim().isEmpty()&&et_destination_lastName.getText().toString().trim().length() < 3) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.val_lname_len));
                    return;
                }
//        if(edittext_email_destination.getText().toString().trim().isEmpty()) {
//            MyApplication.showErrorToast(internationalremitsenderkycC,getString(R.string.val_email));
//            return;
//        }
                if (!edittext_email_destination.getText().toString().trim().isEmpty()&& (!MyApplication.isEmail(edittext_email_destination.getText().toString()))) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.val_email_valid));
                    return;
                }
                if (spinner_destination_gender.getText().toString().equals(getString(R.string.valid_select_gender))) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.val_select_gender));
                    return;
                }
                if (et_destination_dob.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.val_dob));
                    return;
                }
                if (spinner_destination_country.getText().toString().equals(getString(R.string.valid_select_country))) {
                    MyApplication.showErrorToast(localremitbenefikycC, getString(R.string.val_select_country));
                    return;
                }
                if (LocalRemittanceSenderKYC.et_sender_phoneNumber.getText().toString().trim().equalsIgnoreCase(et_destination_mobileNumber.getText().toString().trim())) {
                    MyApplication.showToast(localremitbenefikycC,getString(R.string.both_msisdn_not_same));
                    return;
                }
                callApiPostReceiver();

                break;

        }
    }



    private void callApigenderType() {
        try {

            API.GET("ewallet/api/v1/master/GENDERTYPE",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                benefiGenderList.clear();
                                benefiGenderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("genderTypeList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        benefiGenderModelList.add(new GenderModel.Gender(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("creationDate"),
                                                data.optString("status"),
                                                data.optString("type")
                                        ));

                                        benefiGenderList.add(data.optString("type").trim());

                                    }

                                    spinnerDialogBenefiGender = new SpinnerDialog(localremitbenefikycC, benefiGenderList, "Select Gender", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

                                    spinnerDialogBenefiGender.setCancellable(true); // for cancellable
                                    spinnerDialogBenefiGender.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogBenefiGender.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_destination_gender.setText(item);
                                            spinner_destination_gender.setTag(position);
                                        }
                                    });

                                    callApiRegions();

                                } else {
                                    MyApplication.showToast(localremitbenefikycC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiRegions() {
        try {
            API.GET_PUBLIC("ewallet/public/region/country/"+LocalRemittanceActivity.recCountryCode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //   MyApplication.hideLoader();

                            if (jsonObject != null) {
                                regionList.clear();
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

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City","CANCEL");// With No Animation
                                    spinnerDialogBenefiRegion = new SpinnerDialog(localremitbenefikycC, regionList, "Select Region", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogBenefiRegion.setCancellable(true); // for cancellable
                                    spinnerDialogBenefiRegion.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogBenefiRegion.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_destination_region.setText(item);
                                            spinner_destination_region.setTag(position);
                                            //  spCity.setText(getString(R.string.valid_select_city));

                                            //callApiCity(regionModelList.get(position).getCode());
                                        }
                                    });

                                    callApiIdproofType();

                                } else {
                                    MyApplication.showToast(localremitbenefikycC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiIdproofType() {
        try {

            API.GET("ewallet/api/v1/master/IDPROOFTYPE",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                idProofTypeList.clear();
                                idProofTypeModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("idProffTypeList");
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

                                    spinnerDialogBenefiIdProofType = new SpinnerDialog(localremitbenefikycC, idProofTypeList, "Select Id Proof", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

                                    spinnerDialogBenefiIdProofType.setCancellable(true); // for cancellable
                                    spinnerDialogBenefiIdProofType.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogBenefiIdProofType.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_destination_idprooftype.setText(item);
                                            spinner_destination_idprooftype.setTag(position);
                                        }
                                    });

                                    callApiIssueCountry();

                                } else {
                                    MyApplication.showToast(localremitbenefikycC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiIssueCountry() {
        try {

            API.GET("ewallet/api/v1/countryRemittance/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                issuingCountryList.clear();
                                issuingCountryModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryRemittanceList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        issuingCountryModelList.add(new CountryRemittanceInfoModel.RemitCountry(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("countryCode"),
                                                data.optString("countryIsoCode"),
                                                data.optString("countryName"),
                                                data.optString("createdBy"),
                                                data.optString("creationDate"),
                                                data.optString("currencyCode"),
                                                data.optString("currencySymbol"),
                                                data.optString("dialCode"),
                                                data.optString("mobileLength"),
                                                data.optString("modificationDate"),
                                                data.optString("modifiedBy"),
                                                data.optString("state"),
                                                data.optString("status"),
                                                data.optBoolean("remitReceiving"),
                                                data.optBoolean("remitSending")
                                        ));

                                        issuingCountryList.add(data.optString("countryName").trim());


                                    }

                                    spinnerDialogIssuingCountry= new SpinnerDialog(localremitbenefikycC, issuingCountryList, "Select Country", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogIssuingCountry.setCancellable(true); // for cancellable
                                    spinnerDialogIssuingCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogIssuingCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_destination_issuingCountry.setText(item);
                                            spinner_destination_issuingCountry.setTag(position);

                                        }
                                    });


                                } else {
                                    MyApplication.showToast(localremitbenefikycC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiSubsriberList() {
        try {

            // MyApplication.showloader(TransferToAccountActivity.this, "Please wait!");
            API.GET("ewallet/api/v1/customer/allByCriteria?mobileNumber="+et_destination_mobileNumber.getText().toString()+"&countryCode="+
                            LocalRemittanceActivity.recCountryCode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                subscriberList.clear();
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    walletOwner = jsonObject;
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("customerList");
                                    int pcount=0;
                                    int index=0;
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject jsonObjectSubscriber = walletOwnerListArr.optJSONObject(i);
                                        Iterator<?> keys = jsonObjectSubscriber.keys();
                                        JSONObject countObj=new JSONObject();
                                        int count=0;
                                        while( keys.hasNext() ) {
                                            count++;
                                            String key = (String) keys.next();

                                        }
                                        if(count>pcount){
                                            index=i;
                                            pcount=count;
                                        }

                                    }


                                    JSONObject jsonObjectSubscriber =  walletOwnerListArr.optJSONObject(index);

                                    SubscriberInfoModel.Customer customer = new SubscriberInfoModel.Customer(
                                                jsonObjectSubscriber.optInt("id"),
                                                jsonObjectSubscriber.optString("code", "N/A"),
                                                jsonObjectSubscriber.optString("firstName"),
                                                jsonObjectSubscriber.optString("lastName"),
                                                jsonObjectSubscriber.optString("email"),
                                                jsonObjectSubscriber.optString("mobileNumber", "N/A"),
                                                jsonObjectSubscriber.optString("gender", "N/A"),
                                                jsonObjectSubscriber.optString("idProofTypeCode", "N/A"),
                                                jsonObjectSubscriber.optString("idProofTypeName", "N/A"),
                                                jsonObjectSubscriber.optString("idProofNumber", "N/A"),
                                                jsonObjectSubscriber.optString("idExpiryDate", "N/A"),
                                                jsonObjectSubscriber.optString("dateOfBirth", "N/A"),
                                                jsonObjectSubscriber.optString("countryCode", "N/A"),
                                                jsonObjectSubscriber.optString("countryName", "N/A"),
                                                jsonObjectSubscriber.optString("regionCode", "N/A"),
                                                jsonObjectSubscriber.optString("regionName", "N/A"),
                                                jsonObjectSubscriber.optString("city", "N/A"),
                                                jsonObjectSubscriber.optString("address", "N/A"),
                                                jsonObjectSubscriber.optString("issuingCountryCode", "N/A"),
                                                jsonObjectSubscriber.optString("issuingCountryName", "N/A"),
                                                jsonObjectSubscriber.optString("idProofUrl", "N/A"),
                                                jsonObjectSubscriber.optString("status", "N/A"),
                                                jsonObjectSubscriber.optString("creationDate", "N/A"),
                                                jsonObjectSubscriber.optString("createdBy", "N/A"),
                                                jsonObjectSubscriber.optString("modificationDate", "N/A"),
                                                jsonObjectSubscriber.optString("modifiedBy", "N/A")
                                        );


                                        SubscriberInfoModel subscriberInfoModel = new SubscriberInfoModel(
                                                jsonObject.optString("transactionId", "N/A"),
                                                jsonObject.optString("requestTime", "N/A"),
                                                jsonObject.optString("responseTime", "N/A"),
                                                jsonObject.optString("resultCode", "N/A"),
                                                jsonObject.optString("resultDescription", "N/A"),
                                                customer
                                        );

                                        setSubscriberdata(subscriberInfoModel);


                                } else {
                                    setSubscriberdataf("No Data");
                                    // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
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

    String code,idprooftypecode="",regioncode="",gendercode="",issuingcountrycode="";
    private ArrayList<String> subscriberList = new ArrayList<String>();

    private ArrayAdapter<String> adapter;
    private void setSubscriberdataf(String subscriberInfoModel) {
        isCustomerData = false;
        subscriberList.clear();

        subscriberList.add(""+""+subscriberInfoModel+""+"");
        adapter = new ArrayAdapter<String>(localremitbenefikycC,R.layout.item_select, subscriberList);
        et_destination_mobileNumber.setAdapter(adapter);
//        et_destination_mobileNumber.setThreshold(9);
//        et_destination_mobileNumber.showDropDown();

        et_destination_firstName.setText("");
        et_destination_lastName.setText("");
        edittext_email_destination.setText("");
        et_destination_dob.setText("");
        spinner_destination_gender.setText(getString(R.string.valid_select_gender));
        et_destination_city.setText("");
        et_destination_address.setText("");
        spinner_destination_region.setText(getString(R.string.val_select_region));
        spinner_destination_idprooftype.setText(getString(R.string.val_select_id_proof));
        et_destination_idproofNumber.setText("");
        et_destination_idproof_expiry.setText("");
        spinner_destination_issuingCountry.setText(getString(R.string.val_select_issuing_country));
        etComment.setText("");
        code = "";
    }

    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        isCustomerData = true;
        SubscriberInfoModel.Customer data = subscriberInfoModel.getCustomer();

        subscriberList.add(data.getMobileNumber() + "," + data.getFirstName() + "," + data.getLastName());
        adapter = new ArrayAdapter<String>(localremitbenefikycC, R.layout.item_select, subscriberList);
        et_destination_mobileNumber.setAdapter(adapter);
//        et_destination_mobileNumber.setThreshold(9);
//        et_destination_mobileNumber.showDropDown();

        et_destination_firstName.setText(data.getFirstName());
        et_destination_lastName.setText(data.getLastName());
        edittext_email_destination.setText(data.getEmail());
        if(data.getGender().equalsIgnoreCase("M"))
        {
            spinner_destination_gender.setText("Male");
            gendercode = "M";
        } else if(data.getGender().equalsIgnoreCase("F")) {
            spinner_destination_gender.setText("Female");
            gendercode = "F";
        } else{
            spinner_destination_gender.setText("Other");
            gendercode = "O";
        }
        et_destination_dob.setText(data.getDateOfBirth());
        et_destination_address.setText(data.getAddress());
        spinner_destination_region.setText(data.getRegionName());
        et_destination_city.setText(data.getCity());
        spinner_destination_idprooftype.setText(data.getIdProofTypeName());
        et_destination_idproofNumber.setText(data.getIdProofNumber());
        et_destination_idproof_expiry.setText(data.getIdExpiryDate());
        spinner_destination_issuingCountry.setText(data.getIssuingCountryName());
        idprooftypecode = data.getIdProofTypeCode();
        regioncode = data.getRegionCode();
        issuingcountrycode = data.getIssuingCountryCode();
        code = data.getCode();


    }

    public static JSONObject benefiCustomerJsonObj = new JSONObject();

    public void callApiPostReceiver(){

        JSONObject benefiJson=new JSONObject();
        try {

            benefiJson.put("firstName",et_destination_firstName.getText().toString().trim());
            benefiJson.put("lastName",et_destination_lastName.getText().toString().trim());
            benefiJson.put("email",edittext_email_destination.getText().toString().trim());
            benefiJson.put("mobileNumber",et_destination_mobileNumber.getText().toString().trim());
            if(spinner_destination_region.getTag()!=null){
                benefiJson.put("idProofTypeCode",idProofTypeModelList.get((Integer) spinner_destination_idprooftype.getTag()).getCode());
            }else{
                benefiJson.put("idProofTypeCode",idprooftypecode);
            }
            benefiJson.put("idProofNumber",et_destination_idproofNumber.getText().toString().trim());
            benefiJson.put("idExpiryDate",et_destination_idproof_expiry.getText().toString().trim());
            benefiJson.put("dateOfBirth",et_destination_dob.getText().toString().trim());
            benefiJson.put("countryCode",LocalRemittanceActivity.recCountryCode);
            if(spinner_destination_region.getTag()!=null){
                benefiJson.put("regionCode",regionModelList.get((Integer) spinner_destination_region.getTag()).getCode());
            }else{
                benefiJson.put("regionCode",regioncode);
            }

            benefiJson.put("city",et_destination_city.getText().toString().trim());
            benefiJson.put("address",et_destination_address.getText().toString().trim());
            if(spinner_destination_issuingCountry.getTag()!=null){
                benefiJson.put("issuingCountryCode",issuingCountryModelList.get((Integer) spinner_destination_issuingCountry.getTag()).getCountryCode());
            }else{
                benefiJson.put("issuingCountryCode",issuingcountrycode);
            }
            if(spinner_destination_gender.getTag()!=null){
                benefiJson.put("gender",benefiGenderModelList.get((Integer) spinner_destination_gender.getTag()).getCode());
            }else{
                benefiJson.put("gender",gendercode);
            }

            if (code.isEmpty()||code==null) {
            } else{
                benefiJson.put("code",code);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(isCustomerData) {
            MyApplication.showloader(localremitbenefikycC,"Please Wait...");
            API.PUT("ewallet/api/v1/customer/receiver",benefiJson,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                benefiCustomerJsonObj = jsonObject;
                                Intent i = new Intent(localremitbenefikycC, LocalRemittanceConfirmScreen.class);
                                startActivity(i);

                            }else{
                                MyApplication.showToast(localremitbenefikycC,jsonObject.optString("resultDescription"));
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });
        }else{
            MyApplication.showloader(localremitbenefikycC,"Please Wait...");
            API.POST_REQEST_WH_NEW("ewallet/api/v1/customer/receiver",benefiJson,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                benefiCustomerJsonObj = jsonObject;
                                Intent i = new Intent(localremitbenefikycC, LocalRemittanceConfirmScreen.class);
                                startActivity(i);

                            }else{
                                MyApplication.showToast(localremitbenefikycC,jsonObject.optString("resultDescription"));
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });
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

            et_destination_dob.setText(year + "-" + (month+1) + "-" + day);
            mDobText.setVisibility(View.VISIBLE);
            // etDob.setText(year + "-" + (month+1) + "-" + day);

        }
    }


}

