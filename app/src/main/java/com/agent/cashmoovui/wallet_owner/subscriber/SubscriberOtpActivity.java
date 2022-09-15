package com.agent.cashmoovui.wallet_owner.subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.agent.cashmoovui.wallet_owner.branch.BranchKYCAttached;

import org.json.JSONException;
import org.json.JSONObject;

public class SubscriberOtpActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    private EditText et_otp,et_new_pass,et_conf_new_pass,et_new_pin,et_conf_new_pin;
    private TextView txt_otp_msg,txt_resend,txt_otp_success_msg;
    private ImageView img_qr_code;
    private Button btn_verify_otp,btn_generate_pass,btn_generate_pin,btn_submit_gen_pass,btn_submit_gen_pin,btn_save;
    private CardView card_generate_via;
    private LinearLayout lin_resend,lin_header,lin_gen_pass,lin_gen_pin,lin_basic_info,lin_address,lin_bank_deatil,lin_document,lin_qr_code,lin_wallet,lin_template;
    private RadioGroup radio_group;
    private RadioButton rb_self,rb_agent;
    private String pattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*_;~]).{8,12}$";
    public static String value="Self";
    boolean isPasswordVisible,isPasswordVisibleOther;
    LinearLayout ll_resendOtp;

    public static SubscriberOtpActivity subscriberotpC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_otp);
        subscriberotpC = this;
        getIds();
    }


    private void getIds() {
        //lin_resend = findViewById(R.id.lin_resend);
        et_otp = findViewById(R.id.et_otp);
        txt_otp_msg = findViewById(R.id.txt_otp_msg);
       // txt_resend = findViewById(R.id.txt_resend);
        txt_otp_success_msg = findViewById(R.id.txt_otp_success_msg);
        card_generate_via = findViewById(R.id.card_generate_via);

        ll_resendOtp = findViewById(R.id.ll_resendOtp);

        ll_resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiOTP();
            }
        });

        radio_group = findViewById(R.id.radio_group);
        rb_self = findViewById(R.id.rb_self);
        rb_agent = findViewById(R.id.rb_agent);
        lin_header = findViewById(R.id.lin_header);
        btn_verify_otp = findViewById(R.id.btn_verify_otp);
       // btn_generate_pass = findViewById(R.id.btn_generate_pass);
        btn_generate_pin = findViewById(R.id.btn_generate_pin);
        //lin_gen_pass = findViewById(R.id.lin_gen_pass);
       // et_new_pass = findViewById(R.id.et_new_pass);
       // et_conf_new_pass = findViewById(R.id.et_conf_new_pass);
       // btn_submit_gen_pass = findViewById(R.id.btn_submit_gen_pass);
        lin_gen_pin = findViewById(R.id.lin_gen_pin);
        et_new_pin = findViewById(R.id.et_new_pin);
        et_conf_new_pin = findViewById(R.id.et_conf_new_pin);
        btn_submit_gen_pin = findViewById(R.id.btn_submit_gen_pin);
        btn_save = findViewById(R.id.btn_save);

       // lin_resend.setVisibility(View.GONE);

        HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
        et_new_pin.setTransformationMethod(hiddenPassTransformationMethod);
        et_new_pin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_new_pin.getRight() - et_new_pin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = et_new_pin.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            et_new_pin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            et_new_pin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            et_new_pin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            et_new_pin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        et_new_pin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        et_conf_new_pin.setTransformationMethod(hiddenPassTransformationMethod);
        et_conf_new_pin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_conf_new_pin.getRight() - et_conf_new_pin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = et_conf_new_pin.getSelectionEnd();
                        if (isPasswordVisibleOther) {
                            // set drawable image
                            et_conf_new_pin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            et_conf_new_pin.setTransformationMethod(hiddenPassTransformationMethod);
                            isPasswordVisibleOther = false;
                        } else  {
                            // set drawable image
                            et_conf_new_pin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            et_conf_new_pin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisibleOther = true;
                        }
                        et_conf_new_pin.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        callApiOTP();

        setOnCLickListener();

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_self:
                        value = "Self";
                        lin_header.setVisibility(View.GONE);
                       // lin_gen_pass.setVisibility(View.GONE);
                        lin_gen_pin.setVisibility(View.GONE);
                        Intent i = new Intent(subscriberotpC, SubscriberSignature.class);
                        startActivity(i);
                        finish();
                       // btn_save.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_agent:
                        value = "Agent";
                        lin_header.setVisibility(View.VISIBLE);
                       // btn_save.setVisibility(View.GONE);

                }
            }
        });

    }

    private void setOnCLickListener() {
        //txt_resend.setOnClickListener(subscriberotpC);
        btn_verify_otp.setOnClickListener(subscriberotpC);
        //btn_generate_pass.setOnClickListener(subscriberotpC);
        btn_generate_pin.setOnClickListener(subscriberotpC);
      //  btn_submit_gen_pass.setOnClickListener(subscriberotpC);
        btn_submit_gen_pin.setOnClickListener(subscriberotpC);
        btn_save.setOnClickListener(subscriberotpC);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
//            case R.id.txt_resend:
//                callApiOTP();
//                if(et_otp.getText().toString().isEmpty()) {
//                    MyApplication.showErrorToast(getString(R.string.val_otp));
//                    return;
//                }if(et_otp.getText().toString().length()<6){
//                    MyApplication.showErrorToast(getString(R.string.val_valid_otp));
//                    return;
//                }else{
//                    callApiOTP();
//                }
//
               // break;
            case R.id.btn_verify_otp:
                if(et_otp.getText().toString().isEmpty()){
                    MyApplication.showErrorToast(subscriberotpC,getString(R.string.please_enter_otp_code));
                    return;
                }
                if(et_otp.getText().toString().length()<6) {
                    MyApplication.showErrorToast(subscriberotpC,getString(R.string.plz_enter_6_digit_otp_code));
                    return;
                }else{

                    callApiVerifyOTP();
                }

                break;
//            case R.id.btn_generate_pass:
//                lin_gen_pass.setVisibility(View.VISIBLE);
//                lin_gen_pin.setVisibility(View.GONE);

               // break;
            case R.id.btn_generate_pin:
                lin_gen_pin.setVisibility(View.VISIBLE);
               // lin_gen_pass.setVisibility(View.GONE);

                break;
//            case R.id.btn_submit_gen_pass:
//                if (et_new_pass.getText().toString().trim().isEmpty()) {
//                    MyApplication.showErrorToast(getString(R.string.val_new_pass));
//                    return;
//                }
//                if (!et_new_pass.getText().toString().matches(pattern)) {
//                    MyApplication.showErrorToast(getString(R.string.val_pass_policy));
//                    return;
//                }
//                if (et_conf_new_pass.getText().toString().trim().isEmpty()) {
//                    MyApplication.showErrorToast(getString(R.string.val_conf_pass));
//                    return;
//                }
//
//                if (!et_new_pass.getText().toString().trim().equalsIgnoreCase(et_conf_new_pass.getText().toString().trim())) {
//                    MyApplication.showErrorToast(getString(R.string.val_new_conf_pass));
//                    return;
//                }
//
//                callPUTPass(et_conf_new_pass.getText().toString().trim());
//
//
//                break;
            case R.id.btn_submit_gen_pin:
                if (et_new_pin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(subscriberotpC,getString(R.string.val_new_pin));
                    return;
                }if (et_new_pin.getText().toString().trim().length()<4) {
                    MyApplication.showErrorToast(subscriberotpC,getString(R.string.val_valid_pin));
                    return;
                }
                if (et_conf_new_pin.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(subscriberotpC,getString(R.string.val_conf_pin));
                    return;
                }if (et_conf_new_pin.getText().toString().trim().length()<4) {
                    MyApplication.showErrorToast(subscriberotpC,getString(R.string.val_valid_conf_pin));
                    return;
                }
                if (!et_new_pin.getText().toString().trim().equalsIgnoreCase(et_conf_new_pin.getText().toString().trim())) {
                    MyApplication.showErrorToast(subscriberotpC,getString(R.string.val_new_conf_pin));
                    return;
                }

                callPUTPIN(et_conf_new_pin.getText().toString().trim());
                btn_submit_gen_pin.setVisibility(View.VISIBLE);

                break;
            case R.id.btn_save:

                Intent i = new Intent(subscriberotpC, SubscriberSignature.class);
                startActivity(i);
                finish();

                break;
        }
    }



    private void callApiOTP() {
        try{

            JSONObject genPinJson=new JSONObject();
            genPinJson.put("transTypeCode","106124");
            genPinJson.put("subscriberWalletOwnerCode",SubscriberKYC.subscriberWalletOwnerCode);

            MyApplication.showloader(subscriberotpC,"Please wait!");
            API.POST_REQEST_WH_NEW("ewallet/api/v1/otp", genPinJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            MyApplication.showToast(subscriberotpC,getString(R.string.otp_send_successfully));
                            txt_otp_msg.setVisibility(View.VISIBLE);
                            txt_otp_msg.setText(getString(R.string.verification_register_otp));
                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(subscriberotpC,getString(R.string.technical_failure));
                        } else {
                            txt_otp_msg.setText(jsonObject.optString("resultDescription", "N/A"));
                            txt_otp_msg.setTextColor(ContextCompat.getColor(subscriberotpC,R.color.red));
                            // callNodata(jsonObject.optString("resultDescription", "N/A"));
                        }
                    }
                }

                @Override
                public void failure(String aFalse) {
                    MyApplication.hideLoader();
                    MyApplication.showToast(subscriberotpC,aFalse);
                }
            });

        }catch (Exception e){

        }

    }

    private void callApiVerifyOTP() {
        try {

            JSONObject cashoutJson = new JSONObject();
            cashoutJson.put("transTypeCode", "106124");
            cashoutJson.put("walletOwnerCode", SubscriberKYC.subscriberWalletOwnerCode);
            cashoutJson.put("otp", et_otp.getText().toString().trim());

            //MyApplication.showloader(subscriberotpC, "Please wait!");
            API.PUT("ewallet/api/v1/otp/verify", cashoutJson, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    if (jsonObject != null) {
                        if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                            // MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
                            txt_otp_msg.setVisibility(View.VISIBLE);
                           // lin_resend.setVisibility(View.GONE);
                            btn_verify_otp.setVisibility(View.GONE);
                            txt_otp_success_msg.setVisibility(View.VISIBLE);
                            txt_otp_success_msg.setText(getString(R.string.otp_verified_msg));
                            card_generate_via.setVisibility(View.VISIBLE);

                        } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                            MyApplication.showToast(subscriberotpC,getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(subscriberotpC,jsonObject.optString("resultDescription", "N/A"));
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


//    public void callPUTPass(String pass){
//        //{"userName":"99108591851"}
//
//        JSONObject logiJson=new JSONObject();
//        try {
//
//
//            logiJson.put("password", AESEncryption.getAESEncryption(pass));
//            logiJson.put("walletOwnerCode",walletOwnerCode);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MyApplication.showloader(subscriberotpC,"Please Wait...");
//        API.PUT_SET_PASS("ewallet/api/v1/walletOwnerUser/setPassword",logiJson,
//                new Api_Responce_Handler() {
//                    @Override
//                    public void success(JSONObject jsonObject) {
//                        MyApplication.hideLoader();
//                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
//                            btn_submit_gen_pin.setSelected(true);
//                            lin_gen_pin.setVisibility(View.VISIBLE);
//                            lin_gen_pass.setVisibility(View.GONE);
//                            MyApplication.showToast("password generated successfully please create you pin");
//                        }else{
//                            MyApplication.showToast(jsonObject.optString("resultDescription"));
//                        }
//                    }
//
//                    @Override
//                    public void failure(String aFalse) {
//                        MyApplication.hideLoader();
//
//                    }
//                });
//    }

    public void callPUTPIN(String pass){
        //{"userName":"99108591851"}

        JSONObject logiJson=new JSONObject();
        JSONObject jsonObjectA=null;
        try {


            logiJson.put("pin", AESEncryption.getAESEncryption(pass));
            logiJson.put("walletOwnerCode",SubscriberKYC.subscriberWalletOwnerCode);


            String requestNo=AESEncryption.getAESEncryption(logiJson.toString());

            try{
                jsonObjectA=new JSONObject();
                jsonObjectA.put("request",requestNo);
            }catch (Exception e){

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyApplication.showloader(subscriberotpC,"Please Wait...");
        API.PUT_SET_PASS("ewallet/api/v1/walletOwnerUser/setPin",jsonObjectA,
                new Api_Responce_Handler() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if(jsonObject.optString("resultCode").equalsIgnoreCase("0")){
                           // btn_save.setVisibility(View.VISIBLE);
                           // MyApplication.showToast(subscriberotpC,"Pin generated successfully please click save to add signature");
                            Intent i = new Intent(subscriberotpC, SubscriberSignature.class);
                            startActivity(i);
                            finish();
                        }else{
                            MyApplication.showToast(subscriberotpC,jsonObject.optString("resultDescription"));
                        }
                    }

                    @Override
                    public void failure(String aFalse) {
                        MyApplication.hideLoader();

                    }
                });
    }

}