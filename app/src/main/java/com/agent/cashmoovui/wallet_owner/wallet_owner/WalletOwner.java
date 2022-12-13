package com.agent.cashmoovui.wallet_owner.wallet_owner;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.wallet_owner.branch.BranchKYC;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class WalletOwner extends AppCompatActivity {
    public static WalletOwner walletownerC;
    ImageView imgBack,imgHome;
    MyApplication applicationComponentClass;
    CircleImageView profile_img;
    String languageToUse = "";
    TextView tvName,tvAddress,tvPhone,tvCurrency,tvCountry,tvProofTypename,tvLanguage,tvEmail,tvIdProofType,tvIdProofNo,tvCashmoovId,tvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_abonne);
        walletownerC = this;
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
                MyApplication.hideKeyboard(walletownerC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(walletownerC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
        String ImageName=MyApplication.getSaveString("ImageName", WalletOwner.this);
        if(ImageName!=null&&ImageName.length()>1) {
            Glide.with(this).load(ImageName).apply(options).into(profile_img);

        }
    }

    private void getIds() {
        profile_img= findViewById(R.id.profile_img);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvCountry = findViewById(R.id.tvCountry);
        tvProofTypename=findViewById(R.id.tvProofTypename);
        tvLanguage = findViewById(R.id.tvLanguage);
        tvEmail = findViewById(R.id.tvEmail);
        tvIdProofType = findViewById(R.id.tvIdProofType);
        tvIdProofNo = findViewById(R.id.tvIdProofNo);
        tvCashmoovId = findViewById(R.id.tvCashmoovId);
        tvState = findViewById(R.id.tvState);
        applicationComponentClass = (MyApplication) getApplicationContext();

        try {

            languageToUse = applicationComponentClass.getmSharedPreferences().getString("languageToUse", "");

            if (languageToUse.trim().length() == 0) {
                languageToUse = "en";
                tvLanguage.setText("English");
            }
            if(languageToUse.equalsIgnoreCase("en")){
                tvLanguage.setText("English");
            }
            if(languageToUse.equalsIgnoreCase("fr")){
                tvLanguage.setText("French");
            }

            Locale locale = new Locale(languageToUse);

            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        } catch (Exception e) {
            Toast.makeText(walletownerC, e.toString(), Toast.LENGTH_LONG).show();

        }

        callAPIWalletOwnerDetails();

    }

    public void callAPIWalletOwnerDetails(){
        API.GET("ewallet/api/v1/walletOwner/"+MyApplication.getSaveString("walletOwnerCode", walletownerC), new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                    tvName.setText(jsonObject.optJSONObject("walletOwner").optString("ownerName","N/A"));
                    tvAddress.setText(jsonObject.optJSONObject("walletOwner").optString("registerCountryName","N/A"));
                    tvPhone.setText(jsonObject.optJSONObject("walletOwner").optString("mobileNumber","N/A"));
                    //tvCurrency.setText(jsonObject.optJSONObject("walletOwner").optString("registerCountryCode","N/A"));
                    tvCountry.setText(jsonObject.optJSONObject("walletOwner").optString("registerCountryName","N/A"));
                    //tvLanguage.setText(jsonObject.optJSONObject("walletOwner").optString("registerCountryCode","N/A"));
                    tvEmail.setText(jsonObject.optJSONObject("walletOwner").optString("email","N/A"));


                    if(MyApplication.getSaveString("walletOwnerCategoryCode", WalletOwner.this).equalsIgnoreCase(MyApplication.AgentCode)) {
                        tvProofTypename.setText("Golden");

                    }else if(MyApplication.getSaveString("walletOwnerCategoryCode", WalletOwner.this).equalsIgnoreCase(MyApplication.BranchCode)) {
                        tvProofTypename.setText("Standard");


                }else if(MyApplication.getSaveString("walletOwnerCategoryCode", WalletOwner.this).equalsIgnoreCase(MyApplication.InstituteCode)) {
                    tvProofTypename.setText(getString(R.string.tier));
                }else {

                    tvProofTypename.setText(getString(R.string.all));
                }

                  //  tvProofTypename.setText(jsonObject.optJSONObject("walletOwner").optString("profileTypeName","N/A"));



                    tvIdProofType.setText(jsonObject.optJSONObject("walletOwner").optString("idProofTypeName","N/A"));
                    tvIdProofNo.setText(jsonObject.optJSONObject("walletOwner").optString("idProofNumber","N/A"));
                    tvCashmoovId.setText(jsonObject.optJSONObject("walletOwner").optString("createdBy","N/A"));
                    tvState.setText(jsonObject.optJSONObject("walletOwner").optString("state","N/A"));


                    callApiFromCurrency(jsonObject.optJSONObject("walletOwner").optString("registerCountryCode"));
                }else{
                    MyApplication.showToast(walletownerC,jsonObject.optString("resultDescription"));
                }

            }

            @Override
            public void failure(String aFalse) {
                MyApplication.showToast(walletownerC,aFalse);
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
                                    tvCurrency.setText(jsonObject.optJSONObject("country").optString("currencyCode"));
                                    //fromCurrencySymbol = jsonObject.optJSONObject("country").optString("currencySymbol");


                                } else {
                                    MyApplication.showToast(walletownerC,jsonObject.optString("resultDescription", "  "));
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

}
