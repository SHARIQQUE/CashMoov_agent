package com.agent.cashmoovui.wallet_owner.agent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.model.BusinessTypeModel;
import com.agent.cashmoovui.model.CityInfoModel;
import com.agent.cashmoovui.model.CountryInfoModel;
import com.agent.cashmoovui.model.IDProofTypeModel;
import com.agent.cashmoovui.model.RegionInfoModel;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberKYCAttached;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class AgentKYC extends AppCompatActivity implements View.OnClickListener {
    public static AgentKYC agentkycC;
    TextView spAccType,spBusinessType,spCountry,spRegion,spIdProof,tvNext;
    public static EditText etAgentName,etLname,etEmail,etPhone,etCity,etAddress,etProofNo;
    private ArrayList<String> businessTypeList = new ArrayList<>();
    private ArrayList<BusinessTypeModel.BusinessType> businessTypeModelList = new ArrayList<>();
    private ArrayList<String> idProofTypeList = new ArrayList<>();
    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList=new ArrayList<>();
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<CountryInfoModel.Country> countryModelList = new ArrayList<>();
    private ArrayList<String> regionList = new ArrayList<>();
    private ArrayList<RegionInfoModel.Region> regionModelList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<CityInfoModel.City> cityModelList = new ArrayList<>();

    private SpinnerDialog spinnerDialogBusinessType,spinnerDialogIdProofType,spinnerDialogCountry,
            spinnerDialogRegion,spinnerDialogCity;
    public static String idProofTypeCode,agentWalletOwnerCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_agent);
        agentkycC=this;
        getIds();

    }

    private void getIds() {
        spAccType = findViewById(R.id.spAccType);
        spBusinessType = findViewById(R.id.spBusinessType);
        spCountry = findViewById(R.id.spCountry);
        spRegion = findViewById(R.id.spRegion);
        etCity = findViewById(R.id.etCity);
        spIdProof = findViewById(R.id.spIdProof);
        etAddress = findViewById(R.id.etAddress);
        etAgentName = findViewById(R.id.etAgentName);
        etLname = findViewById(R.id.etLname);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etCity = findViewById(R.id.etCity);
        etAddress = findViewById(R.id.etAddress);
        etProofNo = findViewById(R.id.etProofNo);
        tvNext = findViewById(R.id.tvNext);


        spBusinessType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogBusinessType!=null)
                    spinnerDialogBusinessType.showSpinerDialog();
            }
        });

        spCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogCountry!=null)
                    spinnerDialogCountry.showSpinerDialog();
            }
        });

        spRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogRegion!=null){
                    spinnerDialogRegion.showSpinerDialog();
                }

            }
        });

        spIdProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDialogIdProofType!=null)
                    spinnerDialogIdProofType.showSpinerDialog();
            }
        });

        callBusinessTypeList();

        setOnCLickListener();


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(agentkycC);
    }

        @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                if(etAgentName.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(agentkycC,getString(R.string.val_fname));
                    MyApplication.showTipError(this,getString(R.string.val_agent_name),etAgentName);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(etAgentName.getText().toString().trim().length()<3) {
                    // MyApplication.showErrorToast(agentkycC,getString(R.string.val_fname));
                    MyApplication.showTipError(this,getString(R.string.val_agent_name_len),etAgentName);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }

                if(etLname.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(agentkycC,getString(R.string.val_lname));
                    MyApplication.showTipError(this,getString(R.string.val_lname),etLname);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(etLname.getText().toString().trim().length()<3) {
                    // MyApplication.showErrorToast(agentkycC,getString(R.string.val_lname));
                    MyApplication.showTipError(this,getString(R.string.val_lname_len),etLname);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(etEmail.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(agentkycC,getString(R.string.val_email));
                    MyApplication.showTipError(this,getString(R.string.val_email_valid),etEmail);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(!MyApplication.isEmail(etEmail.getText().toString())){
                    MyApplication.showTipError(this,getString(R.string.val_email_valid),etEmail);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(etPhone.getText().toString().trim().isEmpty()) {
                    //MyApplication.showErrorToast(agentkycC,getString(R.string.val_phone));
                    MyApplication.showTipError(this,getString(R.string.enter_phone_no),etPhone);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(etPhone.getText().toString().trim().length()<9) {
                    //MyApplication.showErrorToast(agentkycC,getString(R.string.val_phone));
                    MyApplication.showTipError(this,getString(R.string.enter_phone_no_val),etPhone);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(spBusinessType.getText().toString().equals(getString(R.string.valid_select_business_type))) {
                    //MyApplication.showErrorToast(agentkycC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this,getString(R.string.val_select_business_type),spBusinessType);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(spCountry.getText().toString().equals(getString(R.string.valid_select_country))) {
                    //MyApplication.showErrorToast(agentkycC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this,getString(R.string.val_select_country),spCountry);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(spRegion.getText().toString().equals(getString(R.string.valid_select_region))) {
                    //MyApplication.showErrorToast(agentkycC,getString(R.string.val_select_gender));
                    MyApplication.showTipError(this,getString(R.string.val_select_region),spRegion);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(etCity.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(agentkycC,getString(R.string.val_city));
                    MyApplication.showTipError(this,getString(R.string.val_city),etCity);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(etAddress.getText().toString().trim().isEmpty()) {
                    // MyApplication.showErrorToast(agentkycC,getString(R.string.val_address));
                    MyApplication.showTipError(this,getString(R.string.val_address),etAddress);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(spIdProof.getText().toString().equals(getString(R.string.valid_select_id_proof))) {
                    MyApplication.showTipError(this,getString(R.string.val_select_id_proof),spIdProof);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                if(etProofNo.getText().toString().trim().isEmpty()) {
                    MyApplication.showTipError(this,getString(R.string.val_proof_no),etProofNo);
                    MyApplication.hideKeyboard(agentkycC);
                    return;
                }
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("ownerName",etAgentName.getText().toString().trim());
                    jsonObject.put("lastName",etLname.getText().toString().trim());
                    jsonObject.put("dateOfBirth","");
                    jsonObject.put("idExpiryDate","");
                    jsonObject.put("email",etEmail.getText().toString().trim());
                    jsonObject.put("gender","");
                    jsonObject.put("mobileNumber",etPhone.getText().toString().trim());
                    jsonObject.put("businessTypeCode",businessTypeModelList.get((Integer) spBusinessType.getTag()).getCode());
                    jsonObject.put("idProofNumber",etProofNo.getText().toString().trim());
                    jsonObject.put("idProofTypeCode",idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode());
                    jsonObject.put("issuingCountryCode","");
                    jsonObject.put("registerCountryCode",countryModelList.get((Integer) spCountry.getTag()).getCode());
                    jsonObject.put("regionCode",regionModelList.get((Integer) spRegion.getTag()).getCode());
                    jsonObject.put("addressLine1",etAddress.getText().toString().trim());
                    jsonObject.put("city",etCity.getText().toString().trim());
                    jsonObject.put("notificationLanguage",MyApplication.getSaveString("Locale", agentkycC));
                    jsonObject.put("notificationTypeCode","");
                    jsonObject.put("occupationTypeCode","");
                    jsonObject.put("walletOwnerCategoryCode",MyApplication.AgentCode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callRegisterApi(jsonObject);

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
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
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

                                    //  spinnerDialog=new SpinnerDialog(selltransferC,instituteList,"Select or Search City","CANCEL");// With No Animation
                                    spinnerDialogBusinessType = new SpinnerDialog(agentkycC, businessTypeList, "Select Business Type", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

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
                                    MyApplication.showToast(agentkycC,jsonObject.optString("resultDescription", "N/A"));
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
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("countryList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        countryModelList.add(new CountryInfoModel.Country(
                                                data.optInt("id"),
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

                                    spinnerDialogCountry= new SpinnerDialog(agentkycC,countryList, "Select Country", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogCountry.setCancellable(true); // for cancellable
                                    spinnerDialogCountry.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogCountry.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spCountry.setText(item);
                                            spCountry.setTag(position);
                                            //  callApiRegions();
                                            callApiRegions(countryModelList.get(position).getCode());


                                        }
                                    });

                                } else {
                                    MyApplication.showToast(agentkycC,jsonObject.optString("resultDescription", "N/A"));
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

            API.GET("ewallet/api/v1/region/country/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //
                            // MyApplication.hideLoader();

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
                                    spinnerDialogRegion = new SpinnerDialog(agentkycC, regionList, "Select Region", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogRegion.setCancellable(true); // for cancellable
                                    spinnerDialogRegion.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogRegion.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spRegion.setText(item);
                                            spRegion.setTag(position);
                                           // spCity.setText("Select");

                                           // callApiCity(regionModelList.get(position).getCode());
                                        }
                                    });

                                } else {
                                    MyApplication.showToast(agentkycC,jsonObject.optString("resultDescription", "N/A"));
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
        
        callApiIdProofType();

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

                                    spinnerDialogIdProofType = new SpinnerDialog(agentkycC, idProofTypeList, "Select Id Proof", R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
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
                                    MyApplication.showToast(agentkycC,jsonObject.optString("resultDescription", "N/A"));
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

        MyApplication.showloader(agentkycC,"Please wait...");
        API.POST_REQEST_REGISTER("ewallet/api/v1/walletOwner/agent", jsonObject, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                System.out.println("AgentKYC response======="+jsonObject.toString());
                if(jsonObject!=null){
                    if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                        agentWalletOwnerCode = jsonObject.optString("walletOwnerCode");
                        MyApplication.UserMobile=etPhone.getText().toString().trim();
                        Intent i = new Intent(agentkycC,AgentKYCAttached.class);
                        startActivity(i);
                        finish();
                    }else{
                        MyApplication.showToast(agentkycC,jsonObject.optString("resultDescription"));
                    }

                }
            }

            @Override
            public void failure(String aFalse) {
                MyApplication.hideLoader();
                MyApplication.showToast(agentkycC,aFalse);
            }

        });




    }

}