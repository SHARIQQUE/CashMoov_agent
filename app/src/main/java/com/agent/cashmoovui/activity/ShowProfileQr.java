package com.agent.cashmoovui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.LogoutAppCompactActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.bumptech.glide.Glide;
import org.json.JSONObject;

public class ShowProfileQr extends LogoutAppCompactActivity {
    public static ShowProfileQr showprofileqrcodeC;
    ImageView imgBack,imgHome,imgQR;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profil_qr);
        showprofileqrcodeC=this;
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
                MyApplication.hideKeyboard(showprofileqrcodeC);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(showprofileqrcodeC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        tvName = findViewById(R.id.tvName);
        imgQR = findViewById(R.id.imgQR);

        String fName = MyApplication.getSaveString("firstName", getApplicationContext());
        String lName = MyApplication.getSaveString("lastName", getApplicationContext());
        //  String mobile = MyApplication.getSaveString("mobile", getApplicationContext());
        //String email = MyApplication.getSaveString("email", getApplicationContext());

        String name = null;
        if (fName != null && !fName.isEmpty() && !fName.equals("null")) {
            name = fName;
        }
        if (lName != null && !lName.isEmpty() && !lName.equals("null")) {
            name=fName+" "+lName;
        }
        tvName.setText(getString(R.string.hello)+" "+name);

//        if (mobile != null && !mobile.isEmpty() && !mobile.equals("null")) {
//            txt_mobile.setText(mobile);
//        }

        if(MyApplication.getSaveString("qrcode",showprofileqrcodeC).equalsIgnoreCase("")){
            callApiGenerateQR();
        }else{
            displayQRCode(MyApplication.getSaveString("qrcode",showprofileqrcodeC));
        }



    }

    private void callApiGenerateQR() {
        try{

            JSONObject qrJson=new JSONObject();
            qrJson.put("walletOwnerCode", MyApplication.getSaveString("walletOwnerCode", showprofileqrcodeC));

            System.out.println("QR request"+qrJson.toString());

            MyApplication.showloader(showprofileqrcodeC,"Please wait!");
            API.POST_REQEST_WH_NEW("ewallet/api/v1/qrCode", qrJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    MyApplication.hideLoader();
                    System.out.println("qrJson response======="+jsonObject.toString());

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            MyApplication.saveString("qrcode",jsonObject.optString("qrCode"),ShowProfileQr.this);
                            displayQRCode(jsonObject.optString("qrCode"));
                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(showprofileqrcodeC,getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(showprofileqrcodeC,jsonObject.optString("resultDescription", "N/A"));
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

    private void displayQRCode(String qrCode){
        byte[] qrByteArray = Base64.decode(qrCode, Base64.DEFAULT);
        Glide.with(this)
                .asBitmap()
                .load(qrByteArray)
                .into(imgQR);

    }

}