package com.agent.cashmoovui.remittancebyabhay;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.CityInfoModel;
import com.agent.cashmoovui.model.GenderModel;
import com.agent.cashmoovui.model.IDProofTypeModel;
import com.agent.cashmoovui.model.RegionInfoModel;
import com.agent.cashmoovui.model.SubscriberInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class InternationalRemittanceSenderKYC extends AppCompatActivity implements View.OnClickListener {
    public static InternationalRemittanceSenderKYC internationalremitsenderkycC;
    ImageView imgBack,imgHome;
    boolean isCustomerData;
    public static final int REQUEST_CODE = 1;
    private TextView spinner_sender_gender,spinner_sender_region,spinner_sender_idprooftype,
            spinner_sender_issuingCountry,tvNext;
    public static AutoCompleteTextView et_sender_phoneNumber;
    private EditText et_sender_firstName,et_sender_lastname,et_sender_email,
            et_sender_dob,et_sender_address,et_sender_city,et_sender_idproofNumber,et_sender_idproof_expiry,etFront,etBack;
    private ImageButton btnFront,btnBack;

    private ArrayList<String> senderGenderList = new ArrayList<>();
    private ArrayList<GenderModel.Gender> senderGenderModelList=new ArrayList<>();
    private ArrayList<String> regionList = new ArrayList<>();
    private ArrayList<RegionInfoModel.Region> regionModelList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<CityInfoModel.City> cityModelList = new ArrayList<>();
    private ArrayList<String> idProofTypeList = new ArrayList<>();
    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList=new ArrayList<>();
    private SpinnerDialog spinnerDialogSenderGender,spinnerDialogSenderRegion,
            spinnerDialogIssuingCountry,spinnerDialogSenderIdProofType;
    static final int REQUEST_IMAGE_CAPTURE_ONE = 1;
    static final int REQUEST_IMAGE_CAPTURE_TWO = 2;
    public static final int RESULT_CODE_FAILURE = 10;
    private Intent Data;
    Uri tempUriFront,tempUriBack;
    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international_remittance_sender_kyc);
        internationalremitsenderkycC=this;
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
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public boolean isSet=false;
    public static JSONObject walletOwner = new JSONObject();

    private void getIds() {
        spinner_sender_gender = findViewById(R.id.spinner_sender_gender);
        spinner_sender_region = findViewById(R.id.spinner_sender_region);
        spinner_sender_idprooftype = findViewById(R.id.spinner_sender_idprooftype);
        spinner_sender_issuingCountry = findViewById(R.id.spinner_sender_issuingCountry);
        et_sender_phoneNumber = findViewById(R.id.et_sender_phoneNumber);
        et_sender_firstName = findViewById(R.id.et_sender_firstName);
        et_sender_lastname = findViewById(R.id.et_sender_lastname);
        et_sender_email = findViewById(R.id.et_sender_email);
        et_sender_dob = findViewById(R.id.et_sender_dob);
        et_sender_address = findViewById(R.id.et_sender_address);
        et_sender_city = findViewById(R.id.et_sender_city);
        et_sender_idproofNumber = findViewById(R.id.et_sender_idproofNumber);
        et_sender_idproof_expiry = findViewById(R.id.et_sender_idproof_expiry);
        etFront = findViewById(R.id.etFront);
        btnFront = findViewById(R.id.btnFront);
        etBack = findViewById(R.id.etBack);
        btnBack = findViewById(R.id.btnBack);
        tvNext = findViewById(R.id.tvNext);


        spinner_sender_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSenderGender!=null)
                    spinnerDialogSenderGender.showSpinerDialog();
            }
        });

        spinner_sender_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSenderRegion!=null)
                    spinnerDialogSenderRegion.showSpinerDialog();
            }
        });


        spinner_sender_issuingCountry.setText(InternationalRemittanceActivity.sendCountryName);

        spinner_sender_idprooftype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSenderIdProofType!=null)
                    spinnerDialogSenderIdProofType.showSpinerDialog();
            }
        });

        et_sender_phoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_sender_phoneNumber.getRight() - et_sender_phoneNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here


                        Intent intent = new Intent(internationalremitsenderkycC, AddContact.class);
                        startActivityForResult(intent , REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });

        et_sender_phoneNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.contains(",")) {
                    String[] list = value.split(",");
                    isSet = true;
                    if (list.length == 3) {
                        et_sender_phoneNumber.setText(list[0]);
                        et_sender_firstName.setText(list[1]);
                        et_sender_lastname.setText(list[2]);
//                        etComment.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        et_sender_phoneNumber.setText(list[0]);
                        et_sender_firstName.setText(list[1]);
//                        etLname.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(etLname, InputMethodManager.SHOW_IMPLICIT);
                    }
                }else{
                    et_sender_firstName.setText("");
                    et_sender_lastname.setText("");
                    et_sender_email.setText("");
                    et_sender_dob.setText("");
                    spinner_sender_gender.setText(getString(R.string.valid_select_gender));
                    et_sender_city.setText("");
                    et_sender_address.setText("");
                    spinner_sender_region.setText(getString(R.string.valid_select_region));
                    spinner_sender_idprooftype.setText(getString(R.string.valid_select_id_proof));
                    et_sender_idproofNumber.setText("");
                    et_sender_idproof_expiry.setText("");
                }

            }
        });

        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);


        et_sender_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(internationalremitsenderkycC)) {

                    Matcher m = p.matcher(s);
                    if(s.length()>=9 && m.matches()){
                        if(isSet) {
                            isSet=false;
                        }else{
                            et_sender_firstName.setText("");
                            et_sender_lastname.setText("");
                            et_sender_email.setText("");
                            et_sender_dob.setText("");
                            spinner_sender_gender.setText(getString(R.string.valid_select_gender));
                            et_sender_city.setText("");
                            et_sender_address.setText("");
                            spinner_sender_region.setText(getString(R.string.valid_select_region));
                            spinner_sender_idprooftype.setText(getString(R.string.valid_select_id_proof));
                            et_sender_idproofNumber.setText("");
                            et_sender_idproof_expiry.setText("");
                            callApiSubsriberList();
                        }
                    }else{
                        et_sender_firstName.setText("");
                        et_sender_lastname.setText("");
                        et_sender_email.setText("");
                        et_sender_dob.setText("");
                        spinner_sender_gender.setText(getString(R.string.valid_select_gender));
                        et_sender_city.setText("");
                        et_sender_address.setText("");
                        spinner_sender_region.setText(getString(R.string.valid_select_region));
                        spinner_sender_idprooftype.setText(getString(R.string.valid_select_id_proof));
                        et_sender_idproofNumber.setText("");
                        et_sender_idproof_expiry.setText("");
                    }

                } else {
                    Toast.makeText(internationalremitsenderkycC, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
        });

        et_sender_dob.setInputType(InputType.TYPE_NULL);
        et_sender_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(internationalremitsenderkycC,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_sender_dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, 1960, 01, 00);
                picker.getDatePicker().setMaxDate(System.currentTimeMillis()-568025136000L);
                picker.show();
            }
        });

        et_sender_idproof_expiry.setInputType(InputType.TYPE_NULL);
        et_sender_idproof_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog

                picker = new DatePickerDialog(internationalremitsenderkycC,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_sender_idproof_expiry.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
        btnFront.setOnClickListener(internationalremitsenderkycC);
        tvNext.setOnClickListener(internationalremitsenderkycC);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFront:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_ONE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
                break;
            case R.id.tvNext:

                if (et_sender_phoneNumber.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_phone));
                    return;
                }
                if (et_sender_phoneNumber.getText().toString().trim().length() < 9) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.enter_phone_no_val));
                    return;
                }
                if (et_sender_firstName.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_fname));
                    return;
                }
                if (et_sender_firstName.getText().toString().trim().length() < 3) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_fname_len));
                    return;
                }
                if (!et_sender_lastname.getText().toString().trim().isEmpty()&&et_sender_lastname.getText().toString().trim().length() < 3) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_lname_len));
                    return;
                }
//        if(et_sender_email.getText().toString().trim().isEmpty()) {
//            MyApplication.showErrorToast(internationalremitsenderkycC,getString(R.string.val_email));
//            return;
//        }
                if (!et_sender_email.getText().toString().trim().isEmpty()&& (!MyApplication.isEmail(et_sender_email.getText().toString()))) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_email_valid));
                    return;
                }
                if (spinner_sender_gender.getText().toString().equals(getString(R.string.valid_select_gender))) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_select_gender));
                    return;
                }
                if (et_sender_dob.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_dob));
                    return;
                }
                if (et_sender_address.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_address));
                    return;
                }
                if (spinner_sender_region.getText().toString().equals(getString(R.string.valid_select_region))) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_select_region));
                    return;
                }
                if (et_sender_city.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_city));
                    return;
                }
                if (spinner_sender_idprooftype.getText().toString().equals(getString(R.string.valid_select_id_proof))) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_select_id_proof));
                    return;
                }
                if (et_sender_idproofNumber.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_proof_no));
                    return;
                }
                if (et_sender_idproof_expiry.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_id_proof_expiryDate));
                    return;
                }
                if (!isFrontUpload) {
                    MyApplication.showErrorToast(this, "please upload front Image");
                    return;
                }
                if (spinner_sender_issuingCountry.getText().toString().equals(getString(R.string.valid_select_issuing_country))) {
                    MyApplication.showErrorToast(internationalremitsenderkycC, getString(R.string.val_select_issuing_country));
                    return;
                }

                callApiPostSender();

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
                                senderGenderList.clear();
                                senderGenderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("genderTypeList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        senderGenderModelList.add(new GenderModel.Gender(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("creationDate"),
                                                data.optString("status"),
                                                data.optString("type")
                                        ));

                                        senderGenderList.add(data.optString("type").trim());

                                    }

                                    spinnerDialogSenderGender = new SpinnerDialog(internationalremitsenderkycC, senderGenderList, "Select Gender", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

                                    spinnerDialogSenderGender.setCancellable(true); // for cancellable
                                    spinnerDialogSenderGender.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSenderGender.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_sender_gender.setText(item);
                                            spinner_sender_gender.setTag(position);
                                        }
                                    });

                                    callApiRegions();

                                } else {
                                    MyApplication.showToast(internationalremitsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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
            API.GET_PUBLIC("ewallet/public/region/country/"+InternationalRemittanceActivity.sendCountryCode,
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
                                    spinnerDialogSenderRegion = new SpinnerDialog(internationalremitsenderkycC, regionList, "Select Region", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogSenderRegion.setCancellable(true); // for cancellable
                                    spinnerDialogSenderRegion.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSenderRegion.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_sender_region.setText(item);
                                            spinner_sender_region.setTag(position);
                                          //  spCity.setText(getString(R.string.valid_select_city));

                                            //callApiCity(regionModelList.get(position).getCode());
                                        }
                                    });

                                    callApiIdproofType();

                                } else {
                                    MyApplication.showToast(internationalremitsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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

                                    spinnerDialogSenderIdProofType = new SpinnerDialog(internationalremitsenderkycC, idProofTypeList, "Select Id Proof", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

                                    spinnerDialogSenderIdProofType.setCancellable(true); // for cancellable
                                    spinnerDialogSenderIdProofType.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSenderIdProofType.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_sender_idprooftype.setText(item);
                                            spinner_sender_idprooftype.setTag(position);
                                        }
                                    });


                                } else {
                                    MyApplication.showToast(internationalremitsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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
            API.GET("ewallet/api/v1/customer/allByCriteria?mobileNumber="+et_sender_phoneNumber.getText().toString()+"&countryCode="+
                            InternationalRemittanceActivity.sendCountryCode,
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

    String code,idprooftypecode="",regioncode="",gendercode="";
    private ArrayList<String> subscriberList = new ArrayList<String>();

    private ArrayAdapter<String> adapter;
    private void setSubscriberdataf(String subscriberInfoModel) {
        isCustomerData = false;
        subscriberList.clear();

        subscriberList.add(""+""+subscriberInfoModel+""+"");
        adapter = new ArrayAdapter<String>(internationalremitsenderkycC,R.layout.item_select, subscriberList);
        et_sender_phoneNumber.setAdapter(adapter);
//        et_sender_phoneNumber.setThreshold(9);
//        et_sender_phoneNumber.showDropDown();

        et_sender_firstName.setText("");
        et_sender_lastname.setText("");
        et_sender_email.setText("");
        et_sender_dob.setText("");
        spinner_sender_gender.setText(getString(R.string.valid_select_gender));
        et_sender_city.setText("");
        et_sender_address.setText("");
        spinner_sender_region.setText(getString(R.string.valid_select_region));
        spinner_sender_idprooftype.setText(getString(R.string.valid_select_id_proof));
        et_sender_idproofNumber.setText("");
        et_sender_idproof_expiry.setText("");

        code = "";
    }

    private void setSubscriberdata(SubscriberInfoModel subscriberInfoModel) {
        isCustomerData = true;
        SubscriberInfoModel.Customer data = subscriberInfoModel.getCustomer();

        subscriberList.add(data.getMobileNumber() + "," + data.getFirstName() + "," + data.getLastName());
        adapter = new ArrayAdapter<String>(internationalremitsenderkycC, R.layout.item_select, subscriberList);
        et_sender_phoneNumber.setAdapter(adapter);
//        et_sender_phoneNumber.setThreshold(9);
//        et_sender_phoneNumber.showDropDown();

        et_sender_firstName.setText(data.getFirstName());
        et_sender_lastname.setText(data.getLastName());
        et_sender_email.setText(data.getEmail());
        if(data.getGender().equalsIgnoreCase("M"))
        {
            spinner_sender_gender.setText("Male");
            gendercode = "M";
        } else if(data.getGender().equalsIgnoreCase("F")) {
            spinner_sender_gender.setText("Female");
            gendercode = "F";
        } else{
            spinner_sender_gender.setText("Other");
            gendercode = "O";
        }
        et_sender_dob.setText(data.getDateOfBirth());
        et_sender_address.setText(data.getAddress());
        spinner_sender_region.setText(data.getRegionName());
        et_sender_city.setText(data.getCity());
        spinner_sender_idprooftype.setText(data.getIdProofTypeName());
        et_sender_idproofNumber.setText(data.getIdProofNumber());
        et_sender_idproof_expiry.setText(data.getIdExpiryDate());
        spinner_sender_issuingCountry.setText(data.getIssuingCountryName());
        idprooftypecode = data.getIdProofTypeCode();
        regioncode = data.getRegionCode();
        code = data.getCode();


    }

    File fileFront,fileBack;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE_ONE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Data = data;

                tempUriFront = getImageUri(getApplicationContext(), imageBitmap);

                etFront.setText(tempUriFront.getLastPathSegment());

                fileFront = new File(getRealPathFromURI(tempUriFront).toString());
                int file_size = Integer.parseInt(String.valueOf(fileFront.length() / 1024));     //calculate size of image in KB
                if (file_size <= 100){
                    isFrontUpload=true;
                }else {
                    MyApplication.showErrorToast(internationalremitsenderkycC,"File size exceeds");
                }
                //  btnFrontUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                File file = new File(getRealPathFromURI(tempUriFront));
//                System.out.println(file);

            } else if (resultCode == RESULT_CANCELED) {
                MyApplication.showToast(internationalremitsenderkycC,"User Canceled");
            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(internationalremitsenderkycC,"Failed");
            }

        }
        if (requestCode == REQUEST_IMAGE_CAPTURE_TWO) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Data = data;

                tempUriBack = getImageUri(getApplicationContext(), imageBitmap);

                etBack.setText(tempUriBack.getLastPathSegment());

                fileBack = new File(getRealPathFromURI(tempUriBack).toString());
                int file_size = Integer.parseInt(String.valueOf(fileBack.length() / 1024));     //calculate size of image in KB
                if (file_size <= 100){
                    isBackUpload=true;
                }else {
                    MyApplication.showErrorToast(internationalremitsenderkycC,"File size exceeds");
                }
                //btnBackUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                // File file = new File(getRealPathFromURI(tempUri));

            } else if (resultCode == RESULT_CANCELED) {
                MyApplication.showToast(internationalremitsenderkycC,"User Canceled");
            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(internationalremitsenderkycC,"Failed");
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title"+ Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public boolean isFrontUpload=false;
    public boolean isBackUpload=false;
    public File filesUploadFront() {
        File file = new File(getRealPathFromURI(tempUriFront).toString());
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));     //calculate size of image in KB
        if (file_size <= 100){
            isFrontUpload=true;

            callupload(file,"100012");
        }else {
            MyApplication.showErrorToast(internationalremitsenderkycC,"File size exceeds");
        }
        return file;
    }

    public File filesUploadBack() {
        File file = new File(getRealPathFromURI(tempUriBack).toString());
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));     //calculate size of image in KB
        if (file_size <= 100){
            isBackUpload=true;
            callupload(file,"100013");
        }else {
            MyApplication.showErrorToast(internationalremitsenderkycC,"File size exceeds");
        }
        return file;
    }


    JSONObject documentUploadJsonObj;
    String idprooftypecodefile="";
    private void callupload(File file,String Code) {
        if(spinner_sender_idprooftype.getTag()!=null){
            idprooftypecodefile = idProofTypeModelList.get((Integer) spinner_sender_idprooftype.getTag()).getCode();
        }else{
            idprooftypecodefile = idprooftypecode;
        }
        MyApplication.showloader(internationalremitsenderkycC, "uploading file...");
        //idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode()
        API.Upload_REQUEST_WH("ewallet/api/v1/customer/fileUpload",fileFront,idprooftypecodefile,
                sendorCustomerJsonObj.optJSONObject("customer").optString("code"), new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                    //MyApplication.showToast(getString(R.string.document_upload_msg));
                                    documentUploadJsonObj=jsonObject;
                                    MyApplication.showToast(internationalremitsenderkycC,"upload success");
                                    // callApiUpdateDataApproval();
                                    Intent i = new Intent(internationalremitsenderkycC, InternationalRemittanceBenefiKYC.class);
                                    startActivity(i);

                                } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                                    MyApplication.showToast(internationalremitsenderkycC,getString(R.string.technical_failure));
                                } else {
                                    MyApplication.showToast(internationalremitsenderkycC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }


                        }

                        @Override
                        public void failure(String aFalse) {

                        }
                    });
        }

    public static JSONObject sendorCustomerJsonObj = new JSONObject();

    public void callApiPostSender(){

        JSONObject senderJson=new JSONObject();
        try {

            senderJson.put("firstName",et_sender_firstName.getText().toString().trim());
            senderJson.put("lastName",et_sender_lastname.getText().toString().trim());
            senderJson.put("email",et_sender_email.getText().toString().trim());
            senderJson.put("mobileNumber",et_sender_phoneNumber.getText().toString().trim());
            if(spinner_sender_idprooftype.getTag()!=null){
                senderJson.put("idProofTypeCode",idProofTypeModelList.get((Integer) spinner_sender_idprooftype.getTag()).getCode());
            }else{
                senderJson.put("idProofTypeCode",idprooftypecode);
            }

            senderJson.put("idProofNumber",et_sender_idproofNumber.getText().toString().trim());
            senderJson.put("idExpiryDate",et_sender_idproof_expiry.getText().toString().trim());
            senderJson.put("dateOfBirth",et_sender_dob.getText().toString().trim());
            senderJson.put("countryCode",InternationalRemittanceActivity.sendCountryCode);
            if(spinner_sender_idprooftype.getTag()!=null){
                senderJson.put("idProofTypeCode",idProofTypeModelList.get((Integer) spinner_sender_idprooftype.getTag()).getCode());
            }else{
                senderJson.put("idProofTypeCode",idprooftypecode);
            }
            if(spinner_sender_region.getTag()!=null){
                senderJson.put("regionCode",regionModelList.get((Integer) spinner_sender_region.getTag()).getCode());
            }else{
                senderJson.put("regionCode",regioncode);
            }

            senderJson.put("city",et_sender_city.getText().toString().trim());
            senderJson.put("address",et_sender_address.getText().toString().trim());
            senderJson.put("issuingCountryCode",InternationalRemittanceActivity.sendCountryCode);
            if(spinner_sender_gender.getTag()!=null){
                senderJson.put("gender",senderGenderModelList.get((Integer) spinner_sender_gender.getTag()).getCode());
            }else{
                senderJson.put("gender",gendercode);
            }
            if (code.isEmpty()||code==null) {
            } else{
                senderJson.put("code",code);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(isCustomerData) {
            MyApplication.showloader(internationalremitsenderkycC,"Please Wait...");
            API.PUT("ewallet/api/v1/customer/sender",senderJson,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                sendorCustomerJsonObj = jsonObject;
                                filesUploadFront();
                            }else{
                                MyApplication.showToast(internationalremitsenderkycC,jsonObject.optString("resultDescription"));
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        }else{
            MyApplication.showloader(internationalremitsenderkycC,"Please Wait...");
            API.POST_REQEST_WH_NEW("ewallet/api/v1/customer/sender",senderJson,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                sendorCustomerJsonObj = jsonObject;
                                filesUploadFront();
                            }else{
                                MyApplication.showToast(internationalremitsenderkycC,jsonObject.optString("resultDescription"));
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });
        }


    }

}
