package com.agent.cashmoovui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.agent.cashmoovui.activity.MoreActivity;
import com.agent.cashmoovui.activity.NotificationList;
import com.agent.cashmoovui.location.Constants;
import com.agent.cashmoovui.location.FetchAddressIntentServices;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.payments.Payments;
import com.agent.cashmoovui.activity.ShowProfileQr;
import com.agent.cashmoovui.airtime_purchase.AirtimePurchases;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.cashout.CashOutOpt;
import com.agent.cashmoovui.remittancebyabhay.RemittanceOption;
import com.agent.cashmoovui.settings.Profile;
import com.agent.cashmoovui.transactionhistory_walletscreen.TransactionHistoryMainPage;
import org.json.JSONArray;
import org.json.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SmoothBottomBar bottomBar;
    ImageView imgNotification,imgQR;
    CircleImageView imgProfile;
    TextView tvClick,tvBalance,tvName;
    LinearLayout linClick,ll_cashIn,ll_cashout,ll_remitence,ll_payment,
            ll_walletowner,ll_transfer,ll_credit,ll_overdraft,ll_more;

    MyApplication applicationComponentClass;
    String languageToUse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultReceiver = new AddressResultReceiver(new Handler());
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
        tvName = findViewById(R.id.tvName);

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

//        ll_transfer= (LinearLayout) findViewById(R.id.ll_transfer);
//        ll_transfer.setOnClickListener(this);

        ll_credit= (LinearLayout) findViewById(R.id.ll_credit);
        ll_credit.setOnClickListener(this);

//        ll_overdraft= (LinearLayout) findViewById(R.id.ll_overdraft);
//        ll_overdraft.setOnClickListener(this);


//        ll_walletowner= (LinearLayout) findViewById(R.id.ll_walletowner);
//        ll_walletowner.setOnClickListener(this);

         ll_more= (LinearLayout) findViewById(R.id.ll_more);
         ll_more.setOnClickListener(this);
        MyApplication.hideKeyboard(this);
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
        MyApplication.hideKeyboard(this);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profil)
                .error(R.drawable.profil);
        String ImageName=MyApplication.getSaveString("ImageName", MainActivity.this);
        if(ImageName!=null&&ImageName.length()>1) {
            Glide.with(this).load(ImageName).apply(options).into(imgProfile);
           /* String[] url = ImageName.split(":");
            if (url[0].equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode", MainActivity.this))) {
                String image_url = MyApplication.ImageURL + url[1];
                Glide.with(this).load(image_url).apply(options).into(imgProfile);
            }*/
        }
        callApiWalletList();


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyApplication.hideKeyboard(this);
        bottomBar.setItemActiveIndex(0);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tvClick.setVisibility(View.VISIBLE);
        tvBalance.setVisibility(View.GONE);
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

//            case R.id.ll_walletowner:
//                i = new Intent(MainActivity.this, WalletOwnerMenu.class);
//                startActivity(i);
//                break;

//            case R.id.ll_transfer:
//                i = new Intent(MainActivity.this, TransferOption.class);
//                startActivity(i);
//                break;

            case R.id.ll_credit:
                i = new Intent(MainActivity.this, AirtimePurchases.class);
                startActivity(i);
                break;

//            case R.id.ll_overdraft:
//                i = new Intent(MainActivity.this, OverdraftLimit.class);
//                startActivity(i);
//                break;
            case R.id.ll_more:
                i = new Intent(MainActivity.this, MoreActivity.class);
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
                                                    tvName.setText(data.optString("walletOwnerName"));
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
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ResultReceiver resultReceiver;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String transactionCoordinate;
    public static String transactionArea;
    private void getCurrentLocation() {
        // progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                            System.out.println("loc========="+ lati+""+ longi);

                            transactionCoordinate=lati+","+longi;
                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            fetchaddressfromlocation(location);

                        } else {
                            //progressBar.setVisibility(View.GONE);

                        }
                    }
                }, Looper.getMainLooper());

    }
    String textLatLong, address, postcode, locaity, state, district, country;
    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                address=(resultData.getString(Constants.ADDRESS));
                locaity=(resultData.getString(Constants.LOCAITY));
                state=(resultData.getString(Constants.STATE));
                district=(resultData.getString(Constants.DISTRICT));
                country=(resultData.getString(Constants.COUNTRY));
                postcode=(resultData.getString(Constants.POST_CODE));

                System.out.println("loc========="+address);
                System.out.println("loc=========locaity"+locaity);
                System.out.println("loc=========state"+state);
                System.out.println("loc=========district"+district);
                System.out.println("loc=========country"+country);
                System.out.println("loc=========postcode"+postcode);
                transactionArea=district+","+state+","+country+","+postcode;

            } else {
                Toast.makeText(MainActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
            //  progressBar.setVisibility(View.GONE);
        }


    }

    private void fetchaddressfromlocation(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentServices.class);
        intent.putExtra(Constants.RECEVIER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);


    }




}
