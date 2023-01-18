package com.agent.cashmoovui.remittancebyabhay.cashtowallet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
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
import androidx.fragment.app.DialogFragment;

import com.agent.cashmoovui.AddContact;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.model.CityInfoModel;
import com.agent.cashmoovui.model.CountryCurrencyInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.GenderModel;
import com.agent.cashmoovui.model.IDProofTypeModel;
import com.agent.cashmoovui.model.RegionInfoModel;
import com.agent.cashmoovui.model.ServiceProviderModel;
import com.agent.cashmoovui.model.SubscriberInfoModel;
import com.agent.cashmoovui.wallet_owner.agent.AgentKYC;

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

    public class CashtoWalletSenderKYC extends AppCompatActivity implements View.OnClickListener {
    public static CashtoWalletSenderKYC cashtowalletsenderkycC;
    ImageView imgBack,imgHome;
    boolean isCustomerData;
    public static final int REQUEST_CODE = 1;
    private TextView spinner_provider,spinner_sender_gender,spinner_senderCountry,spinner_sender_region,spinner_sender_idprooftype,
            spinner_issuingCountry,tvNext;
    public static AutoCompleteTextView et_sender_phoneNumber;
    public static EditText et_sender_firstName,et_sender_lastname,et_sender_email,
            et_sender_dob,et_sender_address,et_sender_city,et_sender_idproofNumber,et_sender_idproof_expiry,etFront,etBack;
    private ImageButton btnFront,btnBack;

    private ArrayList<String> serviceProviderList = new ArrayList<>();
    private ArrayList<ServiceProviderModel.ServiceProvider> serviceProviderModelList = new ArrayList<>();

    private ArrayList<String> sendingCountryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> sendCountryModelList = new ArrayList<>();

    private ArrayList<String> sendCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> sendCurrencyModelList = new ArrayList<>();

    private ArrayList<String> recCountryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> recCountryModelList = new ArrayList<>();

    private ArrayList<String> recCurrencyList = new ArrayList<>();
    private ArrayList<CountryCurrencyInfoModel.CountryCurrency> recCurrencyModelList = new ArrayList<>();
    private ArrayList<String> senderGenderList = new ArrayList<>();
    private ArrayList<GenderModel.Gender> senderGenderModelList=new ArrayList<>();
    private ArrayList<String> regionList = new ArrayList<>();
    private ArrayList<RegionInfoModel.Region> regionModelList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<CityInfoModel.City> cityModelList = new ArrayList<>();
    private ArrayList<String> idProofTypeList = new ArrayList<>();
    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList=new ArrayList<>();
    private SpinnerDialog spinnerDialogSerProvider,spinnerDialogSenderGender,spinnerDialogSendingCountry,
            spinnerDialogSenderRegion, spinnerDialogIssuingCountry,spinnerDialogSenderIdProofType;
    static final int REQUEST_IMAGE_CAPTURE_ONE = 4;
    static final int REQUEST_IMAGE_CAPTURE_TWO = 2;
    public static final int RESULT_CODE_FAILURE = 10;
    private Intent Data;
    Uri tempUriFront,tempUriBack;
    public static TextView mDobText,mValidIdproffText,mDobidproffText;
    private static  EditText etDob;
    private ImageView mCalenderIcon_Image,calenderIconidproff_Image;

    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_to_wallet_sender_kyc);
        cashtowalletsenderkycC=this;
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
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(cashtowalletsenderkycC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(cashtowalletsenderkycC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public static String serviceProvider,sendCountryCode,sendCountryName,recCountryCode,recCountryName;
    public static int receiverFee,receiverTax;
    public boolean isSet=false;
    public static JSONObject walletOwner = new JSONObject();
    public static JSONObject serviceCategory = new JSONObject();


    private void getIds() {
        spinner_provider = findViewById(R.id.spinner_provider);
        spinner_sender_gender = findViewById(R.id.spinner_sender_gender);
        spinner_senderCountry = findViewById(R.id.spinner_senderCountry);
        spinner_sender_region = findViewById(R.id.spinner_sender_region);
        spinner_sender_idprooftype = findViewById(R.id.spinner_sender_idprooftype);
        spinner_issuingCountry = findViewById(R.id.spinner_sender_issuingCountry);
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
        mDobText=findViewById(R.id.dobText);
        mDobidproffText=findViewById(R.id.dobidproffText);




        calenderIconidproff_Image = findViewById(R.id.calenderIconidproff_Image);
        mCalenderIcon_Image = findViewById(R.id.calenderIcon_Image);
        mCalenderIcon_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogfragment = new DatePickerDialogTheme();

                dialogfragment.show(getSupportFragmentManager(), "");

                // ffffff

            }
        });
        calenderIconidproff_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogThemeidproff();

                dialogfragment.show(getSupportFragmentManager(), "");

                // ffffff

            }
        });

        spinner_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSerProvider!=null)
                    spinnerDialogSerProvider.showSpinerDialog();
            }
        });

        spinner_senderCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSendingCountry!=null)
                    spinnerDialogSendingCountry.showSpinerDialog();
            }
        });

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

        spinner_issuingCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogIssuingCountry!=null)
                    spinnerDialogIssuingCountry.showSpinerDialog();
            }
        });

        //spinner_sender_issuingCountry.setText(InternationalRemittanceActivity.sendCountryName);

        spinner_sender_idprooftype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogSenderIdProofType!=null)
                    spinnerDialogSenderIdProofType.showSpinerDialog();
            }
        });


        String mobilelength=MyApplication.getSaveString("MobileLength",MyApplication.appInstance);

        et_sender_phoneNumber.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(Integer.parseInt(mobilelength))});


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


                        Intent intent = new Intent(cashtowalletsenderkycC, AddContact.class);
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

        et_sender_phoneNumber.setThreshold(9);

        et_sender_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(cashtowalletsenderkycC)) {

                    Matcher m = p.matcher(s);
                    if(s.length()>=Integer.parseInt(mobilelength) && m.matches()){
                        if(isSet) {
                            isSet=false;
                        }else{
                            et_sender_firstName.setText("");
                            et_sender_lastname.setText("");
                            et_sender_email.setText("");
                            et_sender_dob.setText("");
                            spinner_sender_gender.setText(getString(R.string.select_gender));
                            et_sender_city.setText("");
                            et_sender_address.setText("");
                            spinner_sender_region.setText(getString(R.string.select_region));
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
                        spinner_sender_gender.setText(getString(R.string.select_gender));
                        et_sender_city.setText("");
                        et_sender_address.setText("");
                        spinner_sender_region.setText(getString(R.string.select_region));
                        spinner_sender_idprooftype.setText(getString(R.string.valid_select_id_proof));
                        et_sender_idproofNumber.setText("");
                        et_sender_idproof_expiry.setText("");
                    }

                } else {
                    Toast.makeText(cashtowalletsenderkycC, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
        });



        callApiserviceProvider();
        //callApigenderType();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnFront.setOnClickListener(cashtowalletsenderkycC);
        tvNext.setOnClickListener(cashtowalletsenderkycC);
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
                if (spinner_provider.getText().toString().equals(getString(R.string.valid_select_provider))) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.plz_select_provider));
                    return;
                }
                if (et_sender_phoneNumber.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_phone));
                    return;
                }
                if (et_sender_phoneNumber.getText().toString().trim().length() < 9) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.enter_phone_no_val));
                    return;
                }
                if (et_sender_firstName.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_fname));
                    return;
                }
                if (et_sender_firstName.getText().toString().trim().length() < 3) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_fname_len));
                    return;
                }
                if (!et_sender_lastname.getText().toString().trim().isEmpty()&&et_sender_lastname.getText().toString().trim().length() < 3) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_lname_len));
                    return;
                }
//        if(et_sender_email.getText().toString().trim().isEmpty()) {
//            MyApplication.showErrorToast(cashtowalletsenderkycC,getString(R.string.val_email));
//            return;
//        }
                if (!et_sender_email.getText().toString().trim().isEmpty()&& (!MyApplication.isEmail(et_sender_email.getText().toString()))) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_email_valid));
                    return;
                }
              /*  if (spinner_sender_gender.getText().toString().equals(getString(R.string.valid_select_gender))) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_select_gender));
                    return;
                }*/
                if (et_sender_dob.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_dob));
                    return;
                }
                /*if (et_sender_address.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_address));
                    return;
                }*/
                /*if (spinner_senderCountry.getText().toString().equals(getString(R.string.sending_country_star))) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_select_country));
                    return;
                }
                if (spinner_sender_region.getText().toString().equals(getString(R.string.valid_select_region))) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_select_region));
                    return;
                }*/
              /*  if (et_sender_city.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_city));
                    return;
                }*/
                if (spinner_sender_idprooftype.getText().toString().equals(getString(R.string.valid_select_id_proof))) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_select_id_proof));
                    return;
                }
                if (et_sender_idproofNumber.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_proof_no));
                    return;
                }
                if (et_sender_idproof_expiry.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_id_proof_expiryDate));
                    return;
                }
                if (!isFrontUpload) {
                    MyApplication.showErrorToast(this, "please upload front Image");
                    return;
                }
                /*if (spinner_issuingCountry.getText().toString().equals(getString(R.string.valid_select_issuing_country))) {
                    MyApplication.showErrorToast(cashtowalletsenderkycC, getString(R.string.val_select_issuing_country));
                    return;
                }*/
                senderNumber=et_sender_phoneNumber.getText().toString().trim();
                callApiPostSender();

                break;

        }
    }

    public static String senderNumber;

    private void callApiserviceProvider() {
        try {

            API.GET("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100002&serviceCategoryCode=100061&status=Y",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                serviceProviderList.clear();
                                serviceProviderModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    serviceCategory = jsonObject;
                                    serviceProvider = serviceCategory.optJSONArray("serviceProviderList").optJSONObject(0).optString("name");

                                    JSONArray walletOwnerListArr = serviceCategory.optJSONArray("serviceProviderList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        serviceProviderModelList.add(new ServiceProviderModel.ServiceProvider(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("creationDate"),
                                                data.optString("name"),
                                                data.optString("serviceCategoryCode"),
                                                data.optString("serviceCategoryName"),
                                                data.optString("serviceCode"),
                                                data.optString("serviceName"),
                                                data.optString("serviceProviderMasterCode"),
                                                data.optString("status")

                                        ));
                                        serviceProviderList.add(data.optString("name").trim());
                                        spinner_provider.setText(data.optString("name"));


                                    }

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City","CANCEL");// With No Animation
                                    spinnerDialogSerProvider = new SpinnerDialog(cashtowalletsenderkycC, serviceProviderList, getString(R.string.val_select_service_provider), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation


                                    spinnerDialogSerProvider.setCancellable(true); // for cancellable
                                    spinnerDialogSerProvider.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSerProvider.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_provider.setText(item);
                                            spinner_provider.setTag(position);

                                        }
                                    });

                                    callApigenderType();

                                } else {
                                    MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiCountry() {
        try {

            API.GET("ewallet/api/v1/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                sendingCountryList.clear();
                                sendCountryModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){

                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        //  if (data.optString("code").equalsIgnoreCase(MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletsenderkycC))) {
                                        sendCountryModelList.add(new CountryInfoModel.Country(
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

                                        sendingCountryList.add(data.optString("name").trim());

                                    }
                                    //}

                                    for(int i=0;i<sendCountryModelList.size();i++){
                                        if(MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletsenderkycC).equalsIgnoreCase(
                                                sendCountryModelList.get(i).getCode()
                                        )){
                                            spinner_senderCountry.setText(sendCountryModelList.get(i).getName() );
                                            spinner_senderCountry.setTag(i);
                                            sendCountryCode = sendCountryModelList.get(i).getCode();
                                            sendCountryName = sendCountryModelList.get(i).getName();
                                            //  spinner_senderCurrency.setText(getString(R.string.sending_currencey_star));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            //callApiSendCurrency(sendCountryModelList.get(i).getCode());

                                        }
                                    }

                                    spinnerDialogSendingCountry= new SpinnerDialog(cashtowalletsenderkycC, sendingCountryList, getString(R.string.valid_select_country), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel)); // With 	Animation
                                    spinnerDialogSendingCountry.setCancellable(true); // for cancellable
                                    spinnerDialogSendingCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogSendingCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_senderCountry.setText(item);
                                            spinner_senderCountry.setTag(position);
                                            sendCountryCode = sendCountryModelList.get(position).getCode();
                                            sendCountryName = sendCountryModelList.get(position).getName();
                                            //  spinner_senderCurrency.setText(getString(R.string.sending_currencey_star));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            //callApiSendCurrency(sendCountryModelList.get(position).getCode());
                                        }
                                    });

                                    callApiRegions(sendCountryCode);
                                    // callApiSendCurrency(MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletsenderkycC));

                                } else {
                                    MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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

//    private void callApiSendCurrency(String code) {
//        try {
//
//            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
//                    new Api_Responce_Handler() {
//                        @Override
//                        public void success(JSONObject jsonObject) {
//                            MyApplication.hideLoader();
//
//                            if (jsonObject != null) {
//                                sendCurrencyList.clear();
//                                sendCurrencyModelList.clear();
//                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
//                                    JSONObject countryCurrObj = jsonObject.optJSONObject("country");
//                                    JSONArray countryCurrencyListArr = countryCurrObj.optJSONArray("countryCurrencyList");
//                                    for (int i = 0; i < countryCurrencyListArr.length(); i++) {
//                                        JSONObject data = countryCurrencyListArr.optJSONObject(i);
//                                        sendCurrencyModelList.add(new CountryCurrencyInfoModel.CountryCurrency(
//                                                data.optInt("id"),
//                                                data.optString("code"),
//                                                data.optString("countryCode"),
//                                                data.optString("countryName"),
//                                                data.optString("createdBy"),
//                                                data.optString("creationDate"),
//                                                data.optString("currCode"),
//                                                data.optString("currencyCode"),
//                                                data.optString("currencyName"),
//                                                data.optString("currencySymbol"),
//                                                data.optString("dialCode"),
//                                                data.optInt("mobileLength"),
//                                                data.optString("modificationDate"),
//                                                data.optString("modifiedBy"),
//                                                data.optString("state"),
//                                                data.optString("status"),
//                                                data.optBoolean("inBound"),
//                                                data.optBoolean("outBound")
//
//                                        ));
//
//                                        sendCurrencyList.add(data.optString("currCode").trim());
//
//                                    }
//
//                                   // tvAmtCurr.setText("");
//                                    for(int i=0;i<sendCurrencyModelList.size();i++){
//                                        if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
//                                                sendCurrencyModelList.get(i).getCurrencySymbol()
//                                        )){
//                                            //spinner_senderCurrency.setText(sendCurrencyModelList.get(i).getCurrCode() );
//                                            //spinner_senderCurrency.setTag(i);
//                                            fromCurrency = sendCurrencyModelList.get(i).getCurrCode();
//                                            fromCurrencySymbol = sendCurrencyModelList.get(i).getCurrencySymbol();
//                                            fromCurrencyCode = sendCurrencyModelList.get(i).getCurrencyCode();
//                                           // tvAmtCurr.setText(fromCurrencySymbol);
//                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
//                                            //edittext_amount.getText().clear();
//                                            //edittext_amount_pay.getText().clear();
//
//                                        }
//                                    }
//
////                                    spinnerDialogSendingCurr = new SpinnerDialog(cashtowalletsenderkycC, sendCurrencyList, "Select Currency", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
////                                    spinnerDialogSendingCurr.setCancellable(true); // for cancellable
////                                    spinnerDialogSendingCurr.setShowKeyboard(false);// for open keyboard by default
////                                    spinnerDialogSendingCurr.bindOnSpinerListener(new OnSpinerItemClick() {
////                                        @Override
////                                        public void onClick(String item, int position) {
////                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
////                                            spinner_senderCurrency.setText(item);
////                                            spinner_senderCurrency.setTag(position);
////                                            fromCurrency = sendCurrencyModelList.get(position).getCurrCode();
////                                            fromCurrencySymbol = sendCurrencyModelList.get(position).getCurrencySymbol();
////                                            fromCurrencyCode = sendCurrencyModelList.get(position).getCurrencyCode();
////                                            tvAmtCurr.setText(fromCurrencySymbol);
////                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
////                                            edittext_amount.getText().clear();
////                                            edittext_amount_pay.getText().clear();
////                                        }
////                                    });
//
//                                    callApiRecCountry();
//
//                                } else {
//                                    MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "  "));
//                                }
//                            }
//
//
//                        }
//
//                        @Override
//                        public void failure(String aFalse) {
//                            MyApplication.hideLoader();
//
//                        }
//                    });
//
//        } catch (Exception e) {
//
//        }
//
//    }

    private void callApiRecCountry() {
        try {

            API.GET("ewallet/api/v1/country/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if (jsonObject != null) {
                                recCountryList.clear();
                                recCountryModelList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        // if (!MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletsenderkycC).equalsIgnoreCase(data.optString("countryCode"))) {
                                        recCountryModelList.add(new CountryInfoModel.Country(
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

                                        recCountryList.add(data.optString("name").trim());

                                    }

                                    // }

                                    for(int i=0;i<recCountryModelList.size();i++){
                                        if(MyApplication.getSaveString("COUNTRYCODE_AGENT", cashtowalletsenderkycC).equalsIgnoreCase(
                                                recCountryModelList.get(i).getCode()
                                        )){
                                            spinner_issuingCountry.setText(recCountryModelList.get(i).getName() );
                                            spinner_issuingCountry.setTag(i);
                                            //spinner_issuingCountry.setText(getString(R.string.valid_beneficiary_currency));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            recCountryCode = recCountryModelList.get(i).getCode();
                                            recCountryName = recCountryModelList.get(i).getName();
                                            // callApiRecCurrency(recCountryCode);

                                        }
                                    }

                                    spinnerDialogIssuingCountry= new SpinnerDialog(cashtowalletsenderkycC, recCountryList, getString(R.string.valid_select_country), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
                                    spinnerDialogIssuingCountry.setCancellable(true); // for cancellable
                                    spinnerDialogIssuingCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogIssuingCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spinner_issuingCountry.setText(item);
                                            spinner_issuingCountry.setTag(position);
                                            //spinner_issuingCountry.setText(getString(R.string.valid_beneficiary_currency));
                                            //   txt_benefi_phone.setText(benefiCountryModelList.get(position).dialCode);
                                            recCountryCode = recCountryModelList.get(position).getCode();
                                            recCountryName = recCountryModelList.get(position).getName();
                                            //  callApiRecCurrency(recCountryCode);
                                        }
                                    });


                                } else {
                                    MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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

//    private void callApiRecCurrency(String code) {
//        try {
//
//            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
//                    new Api_Responce_Handler() {
//                        @Override
//                        public void success(JSONObject jsonObject) {
//                            MyApplication.hideLoader();
//
//                            if (jsonObject != null) {
//                                recCurrencyList.clear();
//                                recCurrencyModelList.clear();
//                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
//                                    JSONObject countryCurrObj = jsonObject.optJSONObject("country");
//                                    JSONArray countryCurrencyListArr = countryCurrObj.optJSONArray("countryCurrencyList");
//                                    for (int i = 0; i < countryCurrencyListArr.length(); i++) {
//                                        JSONObject data = countryCurrencyListArr.optJSONObject(i);
//                                        recCurrencyModelList.add(new CountryCurrencyInfoModel.CountryCurrency(
//                                                data.optInt("id"),
//                                                data.optString("code"),
//                                                data.optString("countryCode"),
//                                                data.optString("countryName"),
//                                                data.optString("createdBy"),
//                                                data.optString("creationDate"),
//                                                data.optString("currCode"),
//                                                data.optString("currencyCode"),
//                                                data.optString("currencyName"),
//                                                data.optString("currencySymbol"),
//                                                data.optString("dialCode"),
//                                                data.optInt("mobileLength"),
//                                                data.optString("modificationDate"),
//                                                data.optString("modifiedBy"),
//                                                data.optString("state"),
//                                                data.optString("status"),
//                                                data.optBoolean("inBound"),
//                                                data.optBoolean("outBound")
//
//                                        ));
//
//                                        recCurrencyList.add(data.optString("currCode").trim());
//
//                                    }
//
//                                    //tvAmtPaidCurr.setText("");
//                                    for(int i=0;i<recCurrencyModelList.size();i++){
//                                        if(countryCurrObj.optString("currencySymbol").equalsIgnoreCase(
//                                                recCurrencyModelList.get(i).getCurrencySymbol()
//                                        )){
//                                            //spinner_receiverCurrency.setText(recCurrencyModelList.get(i).getCurrCode() );
//                                            //spinner_receiverCurrency.setTag(i);
//                                            toCurrency = recCurrencyModelList.get(i).getCurrCode();
//                                            toCurrencySymbol = recCurrencyModelList.get(i).getCurrencySymbol();
//                                            toCurrencyCode = recCurrencyModelList.get(i).getCurrencyCode();
//                                            //tvAmtPaidCurr.setText(toCurrencySymbol);
//                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
//                                           // edittext_amount.getText().clear();
//                                            //edittext_amount_pay.getText().clear();
//
//                                        }
//                                    }
//
////                                    spinnerDialogRecCurr = new SpinnerDialog(cashtowalletsenderkycC, recCurrencyList, "Select Currency", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
////                                    spinnerDialogRecCurr.setCancellable(true); // for cancellable
////                                    spinnerDialogRecCurr.setShowKeyboard(false);// for open keyboard by default
////                                    spinnerDialogRecCurr.bindOnSpinerListener(new OnSpinerItemClick() {
////                                        @Override
////                                        public void onClick(String item, int position) {
////                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
////                                            spinner_receiverCurrency.setText(item);
////                                            spinner_receiverCurrency.setTag(position);
////                                            toCurrency = recCurrencyModelList.get(position).getCurrCode();
////                                            toCurrencySymbol = recCurrencyModelList.get(position).getCurrencySymbol();
////                                            toCurrencyCode = recCurrencyModelList.get(position).getCurrencyCode();
////                                            tvAmtPaidCurr.setText(toCurrencySymbol);
////                                            // txt_curr_symbol_paid.setText(benefiCurrencyModelList.get(position).currencySymbol);
////                                            edittext_amount.getText().clear();
////                                            edittext_amount_pay.getText().clear();
////                                        }
////                                    });
//
//                                  //  callApiRecCountry();
//                                    callApiRegions();
//
//                                } else {
//                                    MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "  "));
//                                }
//                            }
//
//
//                        }
//
//                        @Override
//                        public void failure(String aFalse) {
//                            MyApplication.hideLoader();
//
//                        }
//                    });
//
//        } catch (Exception e) {
//
//        }
//    }

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

                                    spinnerDialogSenderGender = new SpinnerDialog(cashtowalletsenderkycC, senderGenderList, getString(R.string.select_gender), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation

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

                                    callApiCountry();


                                } else {
                                    MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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

    private void callApiRegions(String countryCode) {
        try {
            API.GET_PUBLIC("ewallet/public/region/country/"+ countryCode,
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
                                    spinnerDialogSenderRegion = new SpinnerDialog(cashtowalletsenderkycC, regionList, getString(R.string.select_region), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation
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
                                    MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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

                                    spinnerDialogSenderIdProofType = new SpinnerDialog(cashtowalletsenderkycC, idProofTypeList, getString(R.string.valid_select_id_proofnew), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation

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

                                    callApiRecCountry();

                                } else {
                                    MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "N/A"));
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

            // MyApplication.showloader(TransferToAccountActivity.this, "getString(R.string.pleasewait)!");
            API.GET("ewallet/api/v1/customer/allByCriteria?mobileNumber="+et_sender_phoneNumber.getText().toString()+"&countryCode="+
                            sendCountryCode,
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
        adapter = new ArrayAdapter<String>(cashtowalletsenderkycC,R.layout.item_select, subscriberList);
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
        adapter = new ArrayAdapter<String>(cashtowalletsenderkycC, R.layout.item_select, subscriberList);
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
        spinner_issuingCountry.setText(data.getIssuingCountryName());
        idprooftypecode = data.getIdProofTypeCode();
        regioncode = data.getRegionCode();
        code = data.getCode();


    }

    File fileFront,fileBack;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String requiredValue = data.getStringExtra("PHONE");

            MyApplication.contactValidation(requiredValue,et_sender_phoneNumber);

            et_sender_firstName.requestFocus();
        }
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
                    MyApplication.showErrorToast(cashtowalletsenderkycC,"File size exceeds");
                }
                //  btnFrontUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                File file = new File(getRealPathFromURI(tempUriFront));
//                System.out.println(file);

            } else if (resultCode == RESULT_CANCELED) {
                MyApplication.showToast(cashtowalletsenderkycC,"User Canceled");
            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(cashtowalletsenderkycC,"Failed");
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
                    MyApplication.showErrorToast(cashtowalletsenderkycC,"File size exceeds");
                }
                //btnBackUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                // File file = new File(getRealPathFromURI(tempUri));

            } else if (resultCode == RESULT_CANCELED) {
                MyApplication.showToast(cashtowalletsenderkycC,"User Canceled");
            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(cashtowalletsenderkycC,"Failed");
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
            MyApplication.showErrorToast(cashtowalletsenderkycC,"File size exceeds");
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
            MyApplication.showErrorToast(cashtowalletsenderkycC,"File size exceeds");
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
        //  MyApplication.showloader(cashtowalletsenderkycC, "uploading file...");
        //idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode()
        API.Upload_REQUEST_WH("ewallet/api/v1/customer/fileUpload",fileFront,idprooftypecodefile,
                sendorCustomerJsonObj.optJSONObject("customer").optString("code"), new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                //MyApplication.showToast(getString(R.string.document_upload_msg));
                                documentUploadJsonObj=jsonObject;
                                MyApplication.showToast(cashtowalletsenderkycC,getString(R.string.upload_success_toast));
                                // callApiUpdateDataApproval();
                                Intent i = new Intent(cashtowalletsenderkycC, CashtoWalletReceiverKYC.class);
                                i.putExtra("mobileNumber", et_sender_phoneNumber.getText().toString());

                                startActivity(i);

                            } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                                MyApplication.showToast(cashtowalletsenderkycC,getString(R.string.technical_failure));
                            } else {
                                MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription", "N/A"));
                            }
                        }


                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();

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
            senderJson.put("countryCode",sendCountryCode);
            if(spinner_sender_region.getTag()!=null){
                senderJson.put("regionCode","");
            }else{
                senderJson.put("regionCode","");
            }

            senderJson.put("city",et_sender_city.getText().toString().trim());
            senderJson.put("address","");
            senderJson.put("issuingCountryCode",recCountryCode);
            if(spinner_sender_gender.getTag()!=null){
                senderJson.put("gender","Male");
            }else{
                senderJson.put("gender","Male");
            }
            if (code==null) {
                senderJson.put("code","");

            } else{
                senderJson.put("code",code);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(isCustomerData) {
            MyApplication.showloader(cashtowalletsenderkycC,getString(R.string.pleasewait));
            API.PUT("ewallet/api/v1/customer/sender",senderJson,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                sendorCustomerJsonObj = jsonObject;
                                filesUploadFront();
                            }else{
                                MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription"));
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        }else{
            MyApplication.showloader(cashtowalletsenderkycC,getString(R.string.pleasewait));
            API.POST_REQEST_WH_NEW("ewallet/api/v1/customer/sender",senderJson,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                sendorCustomerJsonObj = jsonObject;
                                filesUploadFront();
                            }else{
                                MyApplication.showToast(cashtowalletsenderkycC,jsonObject.optString("resultDescription"));
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

            et_sender_dob.setText(year + "-" + (month+1) + "-" + day);
            mDobText.setVisibility(View.VISIBLE);
            // etDob.setText(year + "-" + (month+1) + "-" + day);

        }
    }
    public static class DatePickerDialogThemeidproff extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //calendar.add(Calendar.YEAR, -18);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_TRADITIONAL, this, year, month, day);

           // datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            et_sender_idproof_expiry.setText(year + "-" + (month+1) + "-" + day);
           // mDobidproffText.setVisibility(View.VISIBLE);
            // etDob.setText(year + "-" + (month+1) + "-" + day);

        }
    }

}
