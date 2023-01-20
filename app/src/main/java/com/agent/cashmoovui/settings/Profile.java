package com.agent.cashmoovui.settings;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.NotificationList;
import com.agent.cashmoovui.activity.ShowProfileQr;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.cashout.CashOutAgent;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.model.CountryCurrencyInfoModel;
import com.agent.cashmoovui.payments.Payments;
import com.agent.cashmoovui.pin_change.ChangePin;
import com.agent.cashmoovui.servicecharge.ServiceCharge;
import com.agent.cashmoovui.transactionhistory_walletscreen.TransactionHistoryMainPage;
import com.agent.cashmoovui.transactionhistory_walletscreen.WalletScreen;
import com.agent.cashmoovui.transfer_float.TransferFloats;
import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    public static Profile profileC;
    ImageView imgBack,imgHome;
    ImageView imgNotification,imgQR;
    TextView tvBadge;
    SmoothBottomBar bottomBar;

    TextView spinner_currency;
    LinearLayout linGloballimitcount,linServiceCharge,linBeneficiary,linChangeLang,linConfidentiality,linShareApp,
            linTermCondition,linAbout,linChangePin,linEditProfile,linReset;

    TextView currency,number,etAddress,name;
    CircleImageView profile_img;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int READ_EXTERNAL_STORAGE = 201;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    String currencyName_from_currency="",profiletypecode="",currencycode="",walletownercode="",walletOwnerCode_destination="",walletOwnerCode_source="";
    String countryCurrencyCode_from_currency="";
    ArrayList<String> arrayList_currecnyName = new ArrayList<String>();
    ArrayList<String> arrayList_currecnyCode = new ArrayList<String>();
    ArrayList<String> arrayList_currencySymbol = new ArrayList<String>();
    ArrayList<String> arrayList_desWalletOwnerCode = new ArrayList<String>();
    private SpinnerDialog spinnerDialogImstitute,spinnerDialogCurrency;
    private TextView paymonthcountText,paymonthlimitAccountText;
    private long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        applicationComponentClass = (MyApplication) getApplicationContext();

        try {

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
            setContentView(R.layout.activity_profile);


            profileC = this;
            //  setBackMenu();


            getIds();

        } catch (Exception e) {
            Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.hideKeyboard(profileC);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profil)
                .error(R.drawable.profil);
        String ImageName=MyApplication.getSaveString("ImageName", profileC);
        if(ImageName!=null&&ImageName.length()>1) {
            Glide.with(this).load(ImageName).apply(options).into(profile_img);

        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setBackMenu() {
//        imgBack = findViewById(R.id.imgBack);
//        imgHome = findViewById(R.id.imgHome);
//
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
//        imgHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        MyApplication.hideKeyboard(profileC);
        bottomBar.setItemActiveIndex(2);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        if(MyApplication.isNotification&&MyApplication.getSaveInt("NOTIFICATIONCOUNTCURR",profileC)!=0){
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText(String.valueOf(MyApplication.getSaveInt("NOTIFICATIONCOUNTCURR",profileC)));
        }else{
            tvBadge.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.hideKeyboard(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.hideKeyboard(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.hideKeyboard(this);
    }


    private void getIds() {
        imgNotification = findViewById(R.id.imgNotification);
        tvBadge = findViewById(R.id.tvBadge);
        imgQR = findViewById(R.id.imgQR);

        bottomBar = findViewById(R.id.bottomBar);
        linServiceCharge = findViewById(R.id.linServiceCharge);
        linBeneficiary = findViewById(R.id.linBeneficiary);
        linChangeLang = findViewById(R.id.linChangeLang);
        linConfidentiality = findViewById(R.id.linConfidentiality);
        linShareApp = findViewById(R.id.linShareApp);
        linTermCondition = findViewById(R.id.linTermCondition);
        linAbout = findViewById(R.id.linAbout);
        linChangePin = findViewById(R.id.linChangePin);
        linEditProfile = findViewById(R.id.linEditProfile);
        linReset = findViewById(R.id.linReset);
        linGloballimitcount = findViewById(R.id.linGloballimitcount);
        linGloballimitcount.setOnClickListener(this);
        currency = findViewById(R.id.currency);
        number = findViewById(R.id.number);
        etAddress = findViewById(R.id.etAddress);
        name = findViewById(R.id.name);
        profile_img = findViewById(R.id.profile_img);





        profiletypecode= MyApplication.getSaveString("profiletypecode",profileC);


        //String add= MyApplication.getSaveString("issuingCountryName",profileC);
        //String num= MyApplication.getSaveString("mobile",profileC);
        String firstName= MyApplication.getSaveString("firstName",profileC);
       // number.setText(num);


        if(firstName.equalsIgnoreCase(""))
        {
            name.setText(firstName+"");
        }
        else if(firstName.equalsIgnoreCase("null"))
        {
            name.setText(firstName+"");
        }
        else if(firstName==null)
        {
            name.setText(firstName+"");
        }

        else {
            name.setText(firstName);

        }
//
//
//        etAddress.setText(add);

        MyApplication.hideKeyboard(profileC);
        bottomBar.setItemActiveIndex(2);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));

        if(MyApplication.isNotification&&MyApplication.getSaveInt("NOTIFICATIONCOUNTCURR",profileC)!=0){
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText(String.valueOf(MyApplication.getSaveInt("NOTIFICATIONCOUNTCURR",profileC)));
        }else{
            tvBadge.setVisibility(View.GONE);
        }

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                if (bottomId == 0) {
                    Intent i = new Intent(profileC, MainActivity.class);
                    startActivity(i);
                    //  finish();
                }
                if (bottomId == 1) {
                    Intent i = new Intent(profileC, TransactionHistoryMainPage.class);
                    startActivity(i);
                    //finish();
                }
                if (bottomId == 2) {
//                    Intent i = new Intent(profileC, Profile.class);
//                    startActivity(i);
                    //  finish();
                }
                return true;
            }
        });

        callAPIWalletOwnerDetails();

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        imgNotification.setOnClickListener(profileC);
        imgQR.setOnClickListener(profileC);
        linServiceCharge.setOnClickListener(profileC);
        linBeneficiary.setOnClickListener(profileC);
        linChangeLang.setOnClickListener(profileC);
        linConfidentiality.setOnClickListener(profileC);
        linShareApp.setOnClickListener(profileC);
        linTermCondition.setOnClickListener(profileC);
        linAbout.setOnClickListener(profileC);
        linChangePin.setOnClickListener(profileC);
        linEditProfile.setOnClickListener(profileC);
        linReset.setOnClickListener(profileC);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.imgNotification:
                intent = new Intent(profileC, NotificationList.class);
                startActivity(intent);
                break;

            case R.id.imgQR:
                intent = new Intent(profileC, ShowProfileQr.class);
                startActivity(intent);
                break;

            case R.id.linServiceCharge:
                intent = new Intent(profileC, ServiceCharge.class);
                startActivity(intent);
                break;

            case R.id.linBeneficiary:
//                intent = new Intent(profileC, AddBeneficiary.class);
//                startActivity(intent);
                Toast.makeText(profileC,"Coming Soon.....", Toast.LENGTH_SHORT).show();
                break;

            case R.id.linChangeLang:
                intent = new Intent(profileC, ChangeLanguage.class);
                startActivity(intent);
                break;

            case R.id.linConfidentiality:
                intent = new Intent(profileC, Confidentiality.class);
                startActivity(intent);
                break;

            case R.id.linShareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.linTermCondition:
                Toast.makeText(profileC,"Coming Soon.....", Toast.LENGTH_SHORT).show();
                break;

            case R.id.linAbout:
                intent = new Intent(profileC, About.class);
                startActivity(intent);
                break;


            case R.id.linChangePin:
                intent = new Intent(profileC, ChangePin.class);
                startActivity(intent);
                break;


            case R.id.linGloballimitcount:

                alertdialougeGlobalLimit();
                break;

            case R.id.linEditProfile:


                if(checkPermission_camera())
                {

                    if(checkPermission_read_external_storage())
                    {

                        intent = new Intent(profileC, EditProfile.class);
                        startActivity(intent);
                    }
                    else {
                        requestPermission_read_external_storage();
                    }



                }
                else {
                    requestPermission();
                }


                break;

            case R.id.linReset:
                intent = new Intent(profileC, Logout.class);
                startActivity(intent);
                break;

        }
    }


    public void callAPIWalletOwnerDetails(){
        API.GET("ewallet/api/v1/walletOwner/"+MyApplication.getSaveString("walletOwnerCode", profileC), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    //name.setText(jsonObject.optJSONObject("walletOwner").optString("ownerName","N/A"));
                    etAddress.setText(jsonObject.optJSONObject("walletOwner").optString("registerCountryName","N/A"));
                    number.setText(jsonObject.optJSONObject("walletOwner").optString("mobileNumber","N/A"));



                    walletownercode = jsonObject.optJSONObject("walletOwner").optString("walletOwnerCategoryCode");


                    System.out.println("get walletcode"+walletownercode);
                    callApiFromCurrency(jsonObject.optJSONObject("walletOwner").optString("registerCountryCode"));
                }else{
                    MyApplication.showToast(profileC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(profileC,aFalse);
            }
        });
    }

    private void callApiFromCurrency(String code) {
        try {

            API.GET("ewallet/api/v1/countryCurrency/country/"+code,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode", "  ").equalsIgnoreCase("0")){
                                    currency.setText(jsonObject.optJSONObject("country").optString("currencyCode"));
                                    //fromCurrencySymbol = jsonObject.optJSONObject("country").optString("currencySymbol");


                                } else {
                                    MyApplication.showToast(profileC,jsonObject.optString("resultDescription", "  "));
                                }
                            }

                            // callApiBenefiCurrency();
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }



    private boolean checkPermission_camera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestPermission_read_external_storage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
    }

    private boolean checkPermission_read_external_storage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


    public void alertdialougeGlobalLimit() {

        try {


            Dialog operationDialog = new Dialog(Profile.this);
            operationDialog.setContentView(R.layout.dialog_global_limit);

            Button closeButton;
            closeButton = operationDialog.findViewById(R.id.closeButton);
             spinner_currency= operationDialog.findViewById(R.id.spinner_currency);
            paymonthcountText=operationDialog.findViewById(R.id.paymonthcountText);
            paymonthlimitAccountText=operationDialog.findViewById(R.id.paymonthlimitAccountText);


            spinner_currency.setText(getString(R.string.select_currency));

            spinner_currency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    if (spinnerDialogCurrency!=null)
                        spinnerDialogCurrency.showSpinerDialog();
                }
            });


            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    operationDialog.dismiss();
                }
            });
            //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            operationDialog.show();

            apicurrency();

        } catch (Exception e) {

            Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_LONG).show();

        }

    }
    private void apicurrency() {

        String userCode_agentCode_from_mssid =  MyApplication.getSaveString("USERCODE", Profile.this);

        API.GET_TRANSFER_DETAILS("ewallet/api/v1/walletOwnerCountryCurrency/"+userCode_agentCode_from_mssid,languageToUse,new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {


                    String resultCode =  jsonObject.getString("resultCode");
                    String resultDescription =  jsonObject.getString("resultDescription");

                    if(resultCode.equalsIgnoreCase("0")) {


                      /*  arrayList_currecnyName.add(0,getString(R.string.select_currency_star));
                        arrayList_currecnyCode.add(0,getString(R.string.select_currency_star));
                       arrayList_currencySymbol.add(0,getString(R.string.select_currency_star));
                       arrayList_desWalletOwnerCode.add(0,getString(R.string.select_currency_star));

*/

                        JSONArray jsonArray = jsonObject.getJSONArray("walletOwnerCountryCurrencyList");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                            currencyName_from_currency = jsonObject3.getString("currencyName");
                            countryCurrencyCode_from_currency = jsonObject3.getString("currencyCode");
                            walletOwnerCode_destination = jsonObject3.getString("walletOwnerCode");

                            String currencySymbol = jsonObject3.getString("currencySymbol");




                            if(jsonObject3.has("currencyName")) {

                                String  currencyName_subscriber_temp = jsonObject3.getString("currencyName");

                            }

                            arrayList_currecnyName.add(currencyName_from_currency);
                            arrayList_currecnyCode.add(countryCurrencyCode_from_currency);
                            arrayList_currencySymbol.add(currencySymbol);
                            arrayList_desWalletOwnerCode.add(walletOwnerCode_destination);


                        }


                      /*  CommonBaseAdapterSecond arraadapter2 = new CommonBaseAdapterSecond(TransferFloats.this, arrayList_currecnyName);
                        spinner_currency.setAdapter(arraadapter2);
*/

                        spinnerDialogCurrency = new SpinnerDialog(Profile.this, arrayList_currecnyName, getString(R.string.select_currency), R.style.DialogAnimations_SmileWindow, getString(R.string.cancel));// With 	Animation

                        spinnerDialogCurrency.setCancellable(true); // for cancellable
                        spinnerDialogCurrency.setShowKeyboard(false);// for open keyboard by default
                        spinnerDialogCurrency.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {


                                //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                setSelctionCurrency(position);
                                // spBusinessType.setTag(position);

                            }
                        });
                    }

                    else {
                        Toast.makeText(Profile.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(Profile.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(Profile.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }
    public void setSelctionCurrency(int pos){
        spinner_currency.setText(arrayList_currecnyName.get(pos));

        currencycode = arrayList_currecnyCode.get(pos);

        callAPIGloballimitCount();

      /*  tvAmtCurr.setText(arrayList_currencySymbol.get(pos));
        selectCurrecnyName = arrayList_currecnyName.get(pos);
        selectCurrecnyCode = arrayList_currecnyCode.get(pos);

        currencySymbol_receiver = arrayList_currencySymbol.get(pos);
        walletOwnerCode_destination = arrayList_desWalletOwnerCode.get(pos);
        spinner_currency.setText(arrayList_currecnyName.get(pos));*/
        // edittext_amount.requestFocus();

    }

    private void callAPIGloballimitCount() {
        try {

            API.GET("ewallet/api/v1/globallimitconfiguration/getProfileAndWltOwrCat?profileTypeCode="+profiletypecode+"&wltOwrCatCode="+walletownercode+"&currencyCode="+currencycode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            try {

                                if (jsonObject != null) {

                                System.out.println("get json"+jsonObject);


                                    String resultCode =  jsonObject.getString("resultCode");

                                    System.out.println("get code"+resultCode);

                                    if (jsonObject.has("globalLimitConfiguration")) {
                                        paymonthcountText.setText(jsonObject.optJSONObject("globalLimitConfiguration").optString("perMonthLimitCount"));

                                        paymonthlimitAccountText.setText(MyApplication.addDecimal(jsonObject.optJSONObject("globalLimitConfiguration").optString("perMonthLimitAmount")));

                                    }
                                    else
                                    {
                                        paymonthcountText.setText("0");
                                        paymonthlimitAccountText.setText("0.00");

                                    }








                                    // callApiRecCountry();

                                } else {
                                    MyApplication.showToast(Profile.this,jsonObject.optString("resultDescription", "  "));
                                }

                            } catch (Exception e) {

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




}