package com.agent.cashmoovui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agent.cashmoovui.activity.NotificationList;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.servicecharge.ServiceCharge;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.payments.Payments;
import com.agent.cashmoovui.activity.ShowProfileQr;
import com.agent.cashmoovui.airtime_purchase.AirtimePurchases;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.cashout.CashOutOpt;
import com.agent.cashmoovui.overdraft.OverdraftLimit;
import com.agent.cashmoovui.remmetience.RemittanceOption;
import com.agent.cashmoovui.settings.Profile;
import com.agent.cashmoovui.transactionhistory_walletscreen.TransactionHistoryMainPage;
import com.agent.cashmoovui.transfer_float.TransferOption;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SmoothBottomBar bottomBar;
    ImageView imgNotification,imgQR;
    CircleImageView imgProfile;
    TextView tvClick,tvBalance;
    LinearLayout linClick,ll_cashIn,ll_cashout,ll_remitence,ll_payment,ll_walletowner,ll_transfer,ll_credit,ll_overdraft,ll_serviceCharge;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        setContentView(R.layout.activity_main);


        String firstRunApp = applicationComponentClass.getmSharedPreferences().getString("isFirstRun", "");

        if (firstRunApp.trim().length() == 0) {
            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO").commit();
        }
        else if (firstRunApp.equalsIgnoreCase("YES"))
        {
            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO").commit();
        }
        else if (firstRunApp.equalsIgnoreCase("NO_LOGINPIN"))
        {
            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO_LOGINPIN").commit();
        }
        else {
            applicationComponentClass.getmSharedPreferences().edit().putString("isFirstRun", "NO").commit();
        }

        bottomBar = findViewById(R.id.bottomBar);
        imgNotification = findViewById(R.id.imgNotification);
        imgNotification.setOnClickListener(this);
        imgQR = findViewById(R.id.imgQR);
        imgQR.setOnClickListener(this);

        imgProfile = findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);

        linClick = findViewById(R.id.linClickn);
        linClick.setOnClickListener(this);
        tvClick = findViewById(R.id.tvClick);
       // tvClick.setOnClickListener(this);

        tvBalance = findViewById(R.id.tvBalance);
        //tvBalance.setOnClickListener(this);

        ll_cashIn = (LinearLayout) findViewById(R.id.ll_cashIn);
        ll_cashIn.setOnClickListener(this);

        ll_cashout = (LinearLayout) findViewById(R.id.ll_cashout);
        ll_cashout.setOnClickListener(this);

        ll_remitence= (LinearLayout) findViewById(R.id.ll_remitence);
        ll_remitence.setOnClickListener(this);

        ll_payment= (LinearLayout) findViewById(R.id.ll_payment);
        ll_payment.setOnClickListener(this);

        ll_transfer= (LinearLayout) findViewById(R.id.ll_transfer);
        ll_transfer.setOnClickListener(this);

        ll_credit= (LinearLayout) findViewById(R.id.ll_credit);
        ll_credit.setOnClickListener(this);

        ll_overdraft= (LinearLayout) findViewById(R.id.ll_overdraft);
        ll_overdraft.setOnClickListener(this);


        ll_serviceCharge= (LinearLayout) findViewById(R.id.ll_serviceCharge);
        ll_serviceCharge.setOnClickListener(this);

        ll_walletowner= (LinearLayout) findViewById(R.id.ll_walletowner);
        ll_walletowner.setOnClickListener(this);

        bottomBar.setItemActiveIndex(0);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));


        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                Log.e("PositionMain--",""+bottomId);

                if (bottomId == 0) {
//                    Intent i = new Intent(mainC, MainActivity.class);
//                    startActivity(i);
//                    finish();
                }
                if (bottomId == 1) {



                    Intent i = new Intent(MainActivity.this, TransactionHistoryMainPage.class);
                    startActivity(i);

                }

                if (bottomId == 2) {
                    Intent i = new Intent(MainActivity.this, Profile.class);
                    startActivity(i);
                    // finish();
                }

                return false;

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profil)
                .error(R.drawable.profil);
        String ImageName=MyApplication.getSaveString("ImageName", MainActivity.this);
        if(ImageName!=null&&ImageName.length()>1) {
            String[] url = ImageName.split(":");
            if (url[0].equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode", MainActivity.this))) {
                String image_url = MyApplication.ImageURL + url[1];
                Glide.with(this).load(image_url).apply(options).into(imgProfile);
            }
        }
        callApiWalletList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(0);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tvClick.setVisibility(View.VISIBLE);
        tvBalance.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.imgNotification:
                i = new Intent(MainActivity.this, NotificationList.class);
                startActivity(i);
                break;
            case R.id.imgQR:
                i = new Intent(MainActivity.this, ShowProfileQr.class);
                startActivity(i);
                break;
            case R.id.imgProfile:
                i = new Intent(MainActivity.this, Profile.class);
                startActivity(i);
                break;
            case R.id.linClickn:
                if(tvClick.isShown()) {
                    tvClick.setVisibility(View.GONE);
                    tvBalance.setVisibility(View.VISIBLE);
                }
                else{
                    tvClick.setVisibility(View.VISIBLE);
                    tvBalance.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_cashIn:
                i = new Intent(MainActivity.this, CashIn.class);
                startActivity(i);
                break;

            case R.id.ll_cashout:
                i = new Intent(MainActivity.this, CashOutOpt.class);
                startActivity(i);
                // finish();
                break;

            case R.id.ll_remitence:
                i = new Intent(MainActivity.this, RemittanceOption.class);
                startActivity(i);
                break;

            case R.id.ll_payment:
                i = new Intent(MainActivity.this, Payments.class);
                startActivity(i);
                break;

            case R.id.ll_walletowner:
                i = new Intent(MainActivity.this, WalletOwnerMenu.class);
                startActivity(i);
                break;

            case R.id.ll_transfer:
                i = new Intent(MainActivity.this, TransferOption.class);
                startActivity(i);
                break;

            case R.id.ll_credit:
                i = new Intent(MainActivity.this, AirtimePurchases.class);
                startActivity(i);
                break;

            case R.id.ll_overdraft:
                i = new Intent(MainActivity.this, OverdraftLimit.class);
                startActivity(i);
                break;

            case R.id.ll_serviceCharge:
                i = new Intent(MainActivity.this, ServiceCharge.class);
                startActivity(i);
                break;

        }
    }
    int doubleBackToExitPressed = 1;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }

    private void callApiWalletList() {
        try {
           // MyApplication.showloader(MainActivity.this,"Please wait!");
            API.GET("ewallet/api/v1/wallet/walletOwner/"+ MyApplication.getSaveString("walletOwnerCode", getApplicationContext()),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            System.out.println("MiniStatement response======="+jsonObject.toString());
                            if (jsonObject != null) {

                                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                                    if(jsonObject.has("walletList") &&jsonObject.optJSONArray("walletList")!=null){
                                        JSONArray walletOwnerListArr = jsonObject.optJSONArray("walletList");
                                        for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                            JSONObject data = walletOwnerListArr.optJSONObject(i);
                                            if(data.optString("walletTypeCode").equalsIgnoreCase("100008")){
                                                if(data.optString("currencyCode").equalsIgnoreCase(MyApplication.getSaveString("countryCode_Loginpage",MainActivity.this))) {
                                                    tvBalance.setText(data.optString("value") + " " + data.optString("currencySymbol"));
                                                }
                                            }

                                        }

                                    }


                                } else {
                                    MyApplication.showToast(MainActivity.this,jsonObject.optString("resultDescription"));
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


}
