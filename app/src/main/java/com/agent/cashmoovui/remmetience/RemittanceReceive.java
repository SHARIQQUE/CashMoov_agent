package com.agent.cashmoovui.remmetience;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.agent.cashmoovui.HiddenPassTransformationMethod;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.OtherOption;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.cashout.CashOutAgent;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.model.IDProofTypeModel;
import com.agent.cashmoovui.otp.VerifyLoginAccountScreen;
import com.agent.cashmoovui.remittancebyabhay.cashtowallet.CashtoWalletSenderKYC;
import com.agent.cashmoovui.remittancebyabhay.local.LocalRemittanceActivity;
import com.agent.cashmoovui.remittancebyabhay.local.LocalRemittanceConfirmScreen;
import com.agent.cashmoovui.set_pin.AESEncryption;
import com.agent.cashmoovui.wallet_owner.agent.AgentKYC;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RemittanceReceive extends AppCompatActivity implements View.OnClickListener {

    String currencySymbol_sender = "";
    String currencySymbol_receiver = "";

    TextView tvContinue, receiptPage_tv_receiver_emailid, receiptPage_tv_receiver_gender, receiptPage_tv_receiver_country, receiptPage_tv_receiver_address, receiptPage_tv_receiver_dob, receiptPage_tv_receiver_idproofType, receiptPage_tv_receiver_idproofNumber, receiptPage_tv_receiver_idproofExpirayDate, receiptPage_tv_sender_idproofType, receiptPage_tv_sender_idproofExpirayDate, receiptPage_tv_sender_idproofNumber, receiptPage_tv_sender_emailid, receiptPage_tv_sender_gender, receiptPage_tv_sender_country, receiptPage_tv_sender_address, receiptPage_tv_sender_dob;

    public static LoginPin loginpinC;
    String buttonClick = "0";
    boolean isPasswordVisible;

    String emailId_from_api = "";

    LinearLayout otp_new_l, ll_resendOtp;
    EditText et_otp, edittext_comment;


    ImageButton qrCode_imageButton;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ImageView imgBack, imgHome;

    TextView exportReceipt_textview, spinner_sender_idprooftype;
    String countryName_from_confcode = "", currencyName_from_confcode = "", countryCode_from_confcode = "", firstName_from_confcode = "", lastName_from_confcode = "", gender_from_confcode = "", currencyCode_from_confcode = "";
    View rootView;

    TextView tv_nextClick, rp_tv_senderName, receiptPage_sender_mssidn, receiptPage_sbenificairay_mssidn, receiptPage_conversion_rate, receiptPage_confirmationCode, rp_tv_mobileNumber, rp_tv_businessType, rp_tv_email, rp_tv_country, rp_tv_receiverName, rp_tv_transactionAmount, rp_tv_fees_reveiewPage, receiptPage_tv_stransactionType, receiptPage_tv_dateOfTransaction, receiptPage_tv_transactionAmount,
            receiptPage_tv_amount_to_be_charged, receiptPage_amount_to_paid_receiptpage, receiptPage_tv_fee, receiptPage_tv_financialtax, receiptPage_tv_transaction_receiptNo, receiptPage_tv_sender_name,
            receiptPage_tv_sender_phoneNo,
            receiptPage_tv_receiver_name, receiptPage_tv_receiver_phoneNo, close_receiptPage_textview, rp_tv_financialTax, rp_tv_amount_to_be_charge, rp_tv_amount_to_be_credit, previous_reviewClick_textview;
    LinearLayout amoutnpaidLinear,ll_page_1, ll_reviewPage, ll_receiptPage, ll_pin, ll_successPage;
    MyApplication applicationComponentClass;
    String languageToUse = "";
    EditText et_sender_idproofNumber, etFront, edittext_email, edittext_mobileNuber, edittext_amount, et_mpin, edittext_confirmationCode, edittext_countryName,
            edittext_currencyName, edittext_firstName, edittext_gender;
    public static EditText et_sender_dob, et_sender_idproof_expiry;
    public static TextView mDobText, mDobidproffText;
    String mobileNoStr = "", amountstr = "", confirmationCodeStr,amountfr="";
    String walletOwnerCode_mssis_agent = "", walletOwnerCode_subs, senderNameAgent = "";
    String currencyCode_agent = "", countryCode_agent = "", currencyName_agent = "";
    String tax_financial = "", fees_amount, totalAmount_str, senderName_susbcriber = "";
    Double tax_financial_double = 0.0, amountstr_double = 0.0, fees_amount_double = 0.0, totalAmount_double = 0.0;
    private Intent Data;
    Uri tempUriFront;
    public boolean isFrontUpload = false;
    private String mInternational, mLocal;

    String mpinStr = "";
    private ImageButton btnFront;
    private LinearLayout dobLinear;

    private ArrayList<String> idProofTypeList = new ArrayList<>();
    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList = new ArrayList<>();
    static final int REQUEST_IMAGE_CAPTURE_ONE = 1;
    File fileFront;

    private ImageView mCalenderIcon_Image, calenderIconidproff_Image;
    LinearLayout et_sender_idproof_expiry_lay;

    private SpinnerDialog spinnerDialogSenderIdProofType;

    String serviceCode_from_serviceCategory = "", serviceCategoryCode_from_serviceCategory = "", serviceProviderCode_from_serviceCategory;


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

            setContentView(R.layout.remitance_receive);
            setBackMenu();

            rootView = getWindow().getDecorView().findViewById(R.id.main_layout);
            exportReceipt_textview = (TextView) findViewById(R.id.exportReceipt_textview);
            exportReceipt_textview.setOnClickListener(this);
            spinner_sender_idprooftype = findViewById(R.id.spinner_sender_idprooftype);

            et_sender_idproofNumber = findViewById(R.id.et_sender_idproofNumber);
            edittext_confirmationCode = (EditText) findViewById(R.id.edittext_confirmationCode);


            //     First page

            ll_page_1 = (LinearLayout) findViewById(R.id.ll_page_1);
            amoutnpaidLinear=findViewById(R.id.amoutnpaidLinear);

            tv_nextClick = (TextView) findViewById(R.id.tv_nextClick);
            edittext_mobileNuber = (EditText) findViewById(R.id.edittext_mobileNuber);
            edittext_email = (EditText) findViewById(R.id.edittext_email);
            edittext_amount = (EditText) findViewById(R.id.edittext_amount);
            edittext_comment = (EditText) findViewById(R.id.edittext_comment);
            btnFront = findViewById(R.id.btnFront);
            edittext_comment.setEnabled(false);
            btnFront.setOnClickListener(RemittanceReceive.this);
            etFront = findViewById(R.id.etFront);
            et_sender_dob = findViewById(R.id.et_sender_dob);
            et_sender_idproof_expiry = findViewById(R.id.et_sender_idproof_expiry);
            et_sender_idproof_expiry_lay = findViewById(R.id.et_sender_idproof_expiry_lay);
            mDobText = findViewById(R.id.dobText);
            dobLinear = findViewById(R.id.dobLinear);
            mDobidproffText = findViewById(R.id.dobidproffText);

            calenderIconidproff_Image = findViewById(R.id.calenderIconidproff_Image);
            mCalenderIcon_Image = findViewById(R.id.calenderIcon_Image);

            edittext_amount.setEnabled(false);
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


            et_otp = findViewById(R.id.et_otp);
            HiddenPassTransformationMethod hiddenPassTransformationMethod = new HiddenPassTransformationMethod();
            et_otp.setTransformationMethod(hiddenPassTransformationMethod);
            et_otp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int RIGHT = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (et_otp.getRight() - et_otp.getCompoundDrawables()[RIGHT].getBounds().width())) {
                            int selection = et_otp.getSelectionEnd();
                            if (isPasswordVisible) {
                                // set drawable image
                                et_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                                // hide Password
                                et_otp.setTransformationMethod(hiddenPassTransformationMethod);
                                isPasswordVisible = false;
                            } else {
                                // set drawable image
                                et_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                                // show Password
                                et_otp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                isPasswordVisible = true;
                            }
                            et_otp.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                }
            });


            //    Reveiw page

            ll_reviewPage = (LinearLayout) findViewById(R.id.ll_reviewPage);

            rp_tv_senderName = (TextView) findViewById(R.id.rp_tv_senderName);
            rp_tv_mobileNumber = (TextView) findViewById(R.id.rp_tv_mobileNumber);
            rp_tv_businessType = (TextView) findViewById(R.id.rp_tv_businessType);
            rp_tv_email = (TextView) findViewById(R.id.rp_tv_email);
            rp_tv_country = (TextView) findViewById(R.id.rp_tv_country);
            rp_tv_receiverName = (TextView) findViewById(R.id.rp_tv_receiverName);
            rp_tv_transactionAmount = (TextView) findViewById(R.id.rp_tv_transactionAmount);
            rp_tv_fees_reveiewPage = (TextView) findViewById(R.id.rp_tv_fees_reveiewPage);
            rp_tv_financialTax = (TextView) findViewById(R.id.rp_tv_financialTax);
            rp_tv_amount_to_be_charge = (TextView) findViewById(R.id.rp_tv_amount_to_be_charge);
            rp_tv_amount_to_be_credit = (TextView) findViewById(R.id.rp_tv_amount_to_be_credit);


            otp_new_l = findViewById(R.id.otp_new_l);
            ll_resendOtp = findViewById(R.id.ll_resendOtp);
            et_otp = findViewById(R.id.et_otp);
            ll_resendOtp.setOnClickListener(this);


            et_mpin = (EditText) findViewById(R.id.et_mpin);
            previous_reviewClick_textview = (TextView) findViewById(R.id.previous_reviewClick_textview);

            //    Receipt page

            ll_receiptPage = (LinearLayout) findViewById(R.id.ll_receiptPage);
            ll_pin = (LinearLayout) findViewById(R.id.ll_pin);


            receiptPage_conversion_rate = (TextView) findViewById(R.id.receiptPage_conversion_rate);
            receiptPage_sbenificairay_mssidn = (TextView) findViewById(R.id.receiptPage_sbenificairay_mssidn);
            receiptPage_confirmationCode = (TextView) findViewById(R.id.receiptPage_confirmationCode);
            receiptPage_sender_mssidn = (TextView) findViewById(R.id.receiptPage_sender_mssidn);
            receiptPage_tv_transaction_receiptNo = (TextView) findViewById(R.id.receiptPage_tv_transaction_receiptNo);
            receiptPage_tv_stransactionType = (TextView) findViewById(R.id.receiptPage_tv_stransactionType);
            receiptPage_tv_dateOfTransaction = (TextView) findViewById(R.id.receiptPage_tv_dateOfTransaction);
            receiptPage_tv_transactionAmount = (TextView) findViewById(R.id.receiptPage_tv_transactionAmount);
            receiptPage_tv_amount_to_be_charged = (TextView) findViewById(R.id.receiptPage_tv_amount_to_be_charged);
            receiptPage_amount_to_paid_receiptpage = (TextView) findViewById(R.id.receiptPage_amount_to_paid_receiptpage);
            receiptPage_tv_fee = (TextView) findViewById(R.id.receiptPage_tv_fee);
            receiptPage_tv_financialtax = (TextView) findViewById(R.id.receiptPage_tv_financialtax);
            receiptPage_tv_sender_name = (TextView) findViewById(R.id.receiptPage_tv_sender_name);
            receiptPage_tv_sender_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_sender_phoneNo);
            receiptPage_tv_receiver_name = (TextView) findViewById(R.id.receiptPage_tv_receiver_name);
            receiptPage_tv_receiver_phoneNo = (TextView) findViewById(R.id.receiptPage_tv_receiver_phoneNo);
            close_receiptPage_textview = (TextView) findViewById(R.id.close_receiptPage_textview);
            qrCode_imageButton = (ImageButton) findViewById(R.id.qrCode_imageButton);
            edittext_countryName = (EditText) findViewById(R.id.edittext_countryName);
            edittext_currencyName = findViewById(R.id.edittext_currencyName);
            edittext_gender = (EditText) findViewById(R.id.edittext_gender);
            edittext_firstName = (EditText) findViewById(R.id.edittext_firstName);

            qrCode_imageButton.setOnClickListener(this);
            tv_nextClick.setOnClickListener(this);
            previous_reviewClick_textview.setOnClickListener(this);
            close_receiptPage_textview.setOnClickListener(this);

            edittext_mobileNuber.setEnabled(true);


            walletOwnerCode_mssis_agent = MyApplication.getSaveString("walletOwnerCode", RemittanceReceive.this);

            ll_successPage = (LinearLayout) findViewById(R.id.ll_successPage);
            tvContinue = (TextView) findViewById(R.id.tvContinue);
            tvContinue.setOnClickListener(this);


            receiptPage_tv_sender_emailid = (TextView) findViewById(R.id.receiptPage_tv_sender_emailid);
            receiptPage_tv_sender_gender = (TextView) findViewById(R.id.receiptPage_tv_sender_gender);
            receiptPage_tv_sender_country = (TextView) findViewById(R.id.receiptPage_tv_sender_country);
            receiptPage_tv_sender_address = (TextView) findViewById(R.id.receiptPage_tv_sender_address);
            receiptPage_tv_sender_dob = (TextView) findViewById(R.id.receiptPage_tv_sender_dob);
            receiptPage_tv_sender_idproofType = (TextView) findViewById(R.id.receiptPage_tv_sender_idproofType);
            receiptPage_tv_sender_idproofNumber = (TextView) findViewById(R.id.receiptPage_tv_sender_idproofNumber);
            receiptPage_tv_sender_idproofExpirayDate = (TextView) findViewById(R.id.receiptPage_tv_sender_idproofExpirayDate);

            receiptPage_tv_receiver_emailid = (TextView) findViewById(R.id.receiptPage_tv_receiver_emailid);
            receiptPage_tv_receiver_gender = (TextView) findViewById(R.id.receiptPage_tv_receiver_gender);
            receiptPage_tv_receiver_country = (TextView) findViewById(R.id.receiptPage_tv_receiver_country);
            receiptPage_tv_receiver_address = (TextView) findViewById(R.id.receiptPage_tv_receiver_address);
            receiptPage_tv_receiver_dob = (TextView) findViewById(R.id.receiptPage_tv_receiver_dob);
            receiptPage_tv_receiver_idproofType = (TextView) findViewById(R.id.receiptPage_tv_receiver_idproofType);
            receiptPage_tv_receiver_idproofNumber = (TextView) findViewById(R.id.receiptPage_tv_receiver_idproofNumber);
            receiptPage_tv_receiver_idproofExpirayDate = (TextView) findViewById(R.id.receiptPage_tv_receiver_idproofExpirayDate);
            callApiIdproofType();

            spinner_sender_idprooftype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spinnerDialogSenderIdProofType != null)
                        spinnerDialogSenderIdProofType.showSpinerDialog();
                }
            });

            edittext_confirmationCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (new InternetCheck().isConnected(RemittanceReceive.this)) {

                        confirmationCodeStr = edittext_confirmationCode.getText().toString().trim();


                        if (confirmationCodeStr.length() == 11) {


                            api_confcode();
                        } else {

                            edittext_comment.setText("");
                            edittext_firstName.setText("");
                            edittext_countryName.setText("");
                            edittext_currencyName.setText("");
                            edittext_amount.setText("");
                            spinner_sender_idprooftype.setVisibility(View.GONE);
                            dobLinear.setVisibility(View.GONE);
                            et_sender_idproofNumber.setVisibility(View.GONE);
                            et_sender_idproof_expiry.setVisibility(View.GONE);
                            et_sender_idproof_expiry_lay.setVisibility(View.GONE);

                        }

                    } else {
                        Toast.makeText(RemittanceReceive.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                    }
                }
            });


            et_mpin.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if (s.length() >= 4)
                        MyApplication.hideKeyboard(RemittanceReceive.this);
                }
            });

            // HiddenPassTransformationMethod hiddenPassTransformationMethod=new HiddenPassTransformationMethod();
            et_mpin.setTransformationMethod(hiddenPassTransformationMethod);
            et_mpin.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int RIGHT = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (et_mpin.getRight() - et_mpin.getCompoundDrawables()[RIGHT].getBounds().width())) {
                            int selection = et_mpin.getSelectionEnd();
                            if (isPasswordVisible) {
                                // set drawable image
                                et_mpin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                                // hide Password
                                et_mpin.setTransformationMethod(hiddenPassTransformationMethod);
                                isPasswordVisible = false;
                            } else {
                                // set drawable image
                                et_mpin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                                // show Password
                                et_mpin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                isPasswordVisible = true;
                            }
                            et_mpin.setSelection(selection);
                            return true;
                        }
                    }
                    return false;
                }
            });


        } catch (Exception e) {
            Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();

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
                                if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
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

                                    spinnerDialogSenderIdProofType = new SpinnerDialog(RemittanceReceive.this, idProofTypeList, getString(R.string.valid_select_id_proofnew), R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation

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
                                    MyApplication.showToast(RemittanceReceive.this, jsonObject.optString("resultDescription", "N/A"));
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
                MyApplication.hideKeyboard(RemittanceReceive.this);
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(RemittanceReceive.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    boolean validation_confCode() {
        confirmationCodeStr = edittext_confirmationCode.getText().toString().trim();

        if (confirmationCodeStr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_confirmation_code));

            return false;
        } else if (confirmationCodeStr.trim().length() == 11) {

            //  MyApplication.showErrorToast(this, getString(R.string.plz_enter_confirmation_code));

            return true;
        } else {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_confirmation_code));

            return false;
        }
    }


    boolean validation_mobile_Details() {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("0.00",symbols);
        mobileNoStr = edittext_mobileNuber.getText().toString().trim();
        amountstr = edittext_amount.getText().toString().trim().replace(",", "");
        confirmationCodeStr = edittext_confirmationCode.getText().toString().trim();

        amountfr = edittext_amount.getText().toString().trim().replace(".","").replace(",",".");

        Double amountpay_temp_double = Double.parseDouble(amountfr);

        System.out.println("get amount fr"+df.format(amountpay_temp_double));
        System.out.println("get amount eng"+amountstr);

        // mpinStr = et_mpin.getText().toString();

        if (confirmationCodeStr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_confirmation_code));

            return false;
        } else if (confirmationCodeStr.trim().length() < 4) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_confirmation_code));

            return false;
        } else if (amountstr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        } else if (amountstr.trim().length() < 2) {

            MyApplication.showErrorToast(this, getString(R.string.plz_enter_amount));

            return false;
        } else if (edittext_countryName.getText().toString().trim().isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.val_select_country));

            return false;
        }
//        else if (edittext_currencyName.getText().toString().trim().isEmpty()) {
//
//            MyApplication.showErrorToast(this, getString(R.string.currency_Name));
//
//            return false;
//        }
        else if (edittext_firstName.getText().toString().trim().length() < 2) {

            MyApplication.showErrorToast(this, getString(R.string.val_fname));

            return false;
        } else if (mobileNoStr.isEmpty()) {

            MyApplication.showErrorToast(this, getString(R.string.val_phone));

            return false;
        } else if (mobileNoStr.length() < 9) {

            MyApplication.showErrorToast(this, getString(R.string.val_phone));

            return false;
        } else if (remitType.equalsIgnoreCase("Local Remit") ||remitType.equalsIgnoreCase("Wallet to Cash Local")) {
            return true;
        } else {
            //spinner_sender_idprooftype.setVisibility(View.GONE);
            //                            dobLinear.setVisibility(View.GONE);
            //                            et_sender_idproofNumber.setVisibility(View.GONE);
            //                            et_sender_idproof_expiry.setVisibility(View.GONE);
            //                            et_sender_idproof_expiry_lay.setVisibility(View.GONE);

            if (spinner_sender_idprooftype.getText().toString().equals(getString(R.string.valid_select_id_proof))) {
                MyApplication.showErrorToast(RemittanceReceive.this, getString(R.string.val_select_id_proof));
                return false;
            }
            if (et_sender_idproofNumber.getText().toString().trim().isEmpty()) {
                MyApplication.showErrorToast(RemittanceReceive.this, getString(R.string.val_proof_no));
                return false;
            }
            if (et_sender_idproof_expiry.getText().toString().trim().isEmpty()) {
                MyApplication.showErrorToast(RemittanceReceive.this, getString(R.string.val_id_proof_expiryDate));
                return false;
            }

            if (et_sender_dob.getText().toString().trim().isEmpty()) {
                MyApplication.showErrorToast(RemittanceReceive.this, getString(R.string.val_dob));
                return false;
            }
            return true;
        }


        //return true;
    }


    private void service_Provider_api() {

        // Hard Code Final Deepak

        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/serviceProvider/serviceCategory?serviceCode=100002&serviceCategoryCode=100018&status=Y\n", languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1812943\",\"requestTime\":\"Fri Oct 22 15:28:44 IST 2021\",\"responseTime\":\"Fri Oct 22 15:28:44 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"serviceProviderList\":[{\"id\":108,\"code\":\"100107\",\"serviceCode\":\"100003\",\"serviceName\":\"Cash\",\"serviceCategoryCode\":\"100012\",\"serviceCategoryName\":\"Cash Out\",\"name\":\"Cashmoov\",\"status\":\"Active\",\"serviceProviderMasterCode\":\"100004\",\"creationDate\":\"2021-06-07T19:11:29.944+0530\"}]}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("serviceProviderList");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            serviceCode_from_serviceCategory = jsonObject2.getString("serviceCode");
                            serviceCategoryCode_from_serviceCategory = jsonObject2.getString("serviceCategoryCode");
                            serviceProviderCode_from_serviceCategory = jsonObject2.getString("code");
                        }

                        api_walletOwnerUser();


                    } else {
                        Toast.makeText(RemittanceReceive.this, resultDescription, Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(RemittanceReceive.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    private void api_walletOwnerUser() {

        String USER_CODE_FROM_TOKEN_AGENTDETAILS = MyApplication.getSaveString("userCode", RemittanceReceive.this);


        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/walletOwnerUser/" + USER_CODE_FROM_TOKEN_AGENTDETAILS, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1789408\",\"requestTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"responseTime\":\"Wed Oct 20 16:05:19 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwnerUser\":{\"id\":2171,\"code\":\"101917\",\"firstName\":\"TATASnegal\",\"userName\":\"TATASnegal5597\",\"mobileNumber\":\"8888888882\",\"email\":\"kundan.kumar@esteltelecom.com\",\"walletOwnerUserTypeCode\":\"100000\",\"walletOwnerUserTypeName\":\"Supervisor\",\"walletOwnerCategoryCode\":\"100000\",\"walletOwnerCategoryName\":\"Institute\",\"userCode\":\"1000002606\",\"status\":\"Active\",\"state\":\"Approved\",\"gender\":\"M\",\"idProofTypeCode\":\"100004\",\"idProofTypeName\":\"MILITARY ID CARD\",\"idProofNumber\":\"44444444444\",\"creationDate\":\"2021-10-01T09:04:07.330+0530\",\"notificationName\":\"EMAIL\",\"notificationLanguage\":\"en\",\"createdBy\":\"100308\",\"modificationDate\":\"2021-10-20T14:59:00.791+0530\",\"modifiedBy\":\"100308\",\"addressList\":[{\"id\":3569,\"walletOwnerUserCode\":\"101917\",\"addTypeCode\":\"100001\",\"addTypeName\":\"Commercial\",\"regionCode\":\"100068\",\"regionName\":\"Boke\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"city\":\"100022\",\"cityName\":\"Dubreka\",\"addressLine1\":\"delhi\",\"status\":\"Inactive\",\"creationDate\":\"2021-10-01T09:04:07.498+0530\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-10-03T09:52:57.407+0530\",\"modifiedBy\":\"100308\"}],\"workingDaysList\":[{\"id\":3597,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100000\",\"weekdaysName\":\"Monday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.518+0530\"},{\"id\":3598,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100001\",\"weekdaysName\":\"Tuesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.528+0530\"},{\"id\":3599,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100002\",\"weekdaysName\":\"Wednesday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.538+0530\"},{\"id\":3600,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100003\",\"weekdaysName\":\"Thursday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.547+0530\"},{\"id\":3601,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100004\",\"weekdaysName\":\"Friday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.556+0530\"},{\"id\":3602,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100005\",\"weekdaysName\":\"Saturday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"6:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.565+0530\"},{\"id\":3603,\"walletOwnerUserCode\":\"101917\",\"weekdaysCode\":\"100006\",\"weekdaysName\":\"Sunday\",\"openingTime\":\"10:00 AM\",\"closingTime\":\"2:00 PM\",\"creationDate\":\"2021-10-01T09:04:07.573+0530\"}],\"macEnabled\":false,\"ipEnabled\":false,\"resetCredReqInit\":false,\"notificationTypeCode\":\"100000\"}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        JSONObject walletOwnerUser = jsonObject.getJSONObject("walletOwnerUser");

                        ll_pin.setVisibility(View.VISIBLE);

                        tv_nextClick.setText(getString(R.string.Submit));

                        buttonClick = "1";


                    } else {
                        MyApplication.hideLoader();

                        Toast.makeText(RemittanceReceive.this, resultDescription, Toast.LENGTH_LONG).show();
                        //  finish();
                    }


                } catch (Exception e) {
                    MyApplication.hideLoader();

                    Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(RemittanceReceive.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    JSONObject jsonObject_beneficiaryCustomer = null;
    JSONObject jsonObject_accountHolding = null;

    String benificiaryCode, remitType;

    private void api_confcode() {

        //   MyApplication.showloader(RemittanceReceive.this, getString(R.string.getting_user_info));


        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/holdingAccount/confirmationCode/" + confirmationCodeStr, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1813407\",\"requestTime\":\"Fri Oct 22 15:57:35 IST 2021\",\"responseTime\":\"Fri Oct 22 15:57:35 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"accountHolding\":{\"id\":1937,\"code\":\"1000002014\",\"walletOwnerCode\":\"1000002794\",\"transactionId\":116607,\"openingBalance\":47593,\"closingBalance\":45124,\"sendingAmount\":1000,\"beneficiaryAmount\":1000,\"fee\":1300,\"exchangeRateValue\":0,\"tax\":169,\"fromCurrencyCode\":\"100062\",\"toCurrencyCode\":\"100062\",\"confirmationCode\":\"MM*********\",\"dueDate\":\"2021-10-29T00:00:00.000+0530\",\"status\":\"In Transit\",\"creationDate\":\"2021-10-22 15:08:56\",\"createdBy\":\"102073\",\"sendingWalletOwnerCode\":\"1000002794\",\"beneficiaryWalletOwnerCode\":\"1000002785\",\"holdingAccountTypeCode\":\"100000\",\"holdingAccountTypeName\":\"R2R\",\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":169,\"taxAvailBy\":\"Fee Amount\"}],\"srcProviderWalletCode\":\"1000015411\",\"sendCountryCode\":\"100092\",\"receiveCountryCode\":\"100092\",\"walletToCash\":true}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");


                    if (resultCode.equalsIgnoreCase("0")) {

                        // ############################    accountHolding  ############################


                        if (jsonObject.has("accountHolding")) {

                            jsonObject_accountHolding = jsonObject.getJSONObject("accountHolding");


                       /* if (jsonObject_accountHolding.optBoolean("walletToCash")) {
                            Toast.makeText(RemittanceReceive.this, "Receive remittance is not allocated for", Toast.LENGTH_LONG).show();
                            return;
                        } else {

                        }*/


                        if (jsonObject_accountHolding.has("comments")) {

                            edittext_comment.setText(jsonObject_accountHolding.optString("comments", "N/A"));

                        } else {

                        }

                        if (jsonObject_accountHolding.has("remitType")) {

                            remitType = jsonObject_accountHolding.optString("remitType");
                            if (remitType.equalsIgnoreCase("Local Remit")||remitType.equalsIgnoreCase("Wallet to Cash Local")) {

                                spinner_sender_idprooftype.setVisibility(View.GONE);
                                dobLinear.setVisibility(View.GONE);
                                et_sender_idproofNumber.setVisibility(View.GONE);
                                et_sender_idproof_expiry.setVisibility(View.GONE);
                                et_sender_idproof_expiry_lay.setVisibility(View.GONE);


                            } else {
                                spinner_sender_idprooftype.setVisibility(View.VISIBLE);
                                dobLinear.setVisibility(View.VISIBLE);
                                et_sender_idproofNumber.setVisibility(View.VISIBLE);
                                et_sender_idproof_expiry.setVisibility(View.VISIBLE);
                                et_sender_idproof_expiry_lay.setVisibility(View.VISIBLE);

                            }

                        } else {


                        }

                        // ############################    beneficiaryCustomer  ############################


                        if (jsonObject_accountHolding.has("beneficiaryCustomer")) {
                            jsonObject_beneficiaryCustomer = jsonObject_accountHolding.getJSONObject("beneficiaryCustomer");
                        } else {

                        }


                        if (jsonObject_beneficiaryCustomer.has("code")) {
                            benificiaryCode = jsonObject_beneficiaryCustomer.getString("code");
                        } else {
                            benificiaryCode = "";
                        }

                        if (jsonObject_beneficiaryCustomer.has("lastName")) {
                            lastName_from_confcode = jsonObject_beneficiaryCustomer.getString("lastName");
                        } else {
                            lastName_from_confcode = "";
                        }


                        if (jsonObject_beneficiaryCustomer.has("firstName")) {
                            firstName_from_confcode = jsonObject_beneficiaryCustomer.getString("firstName");
                        } else {
                            firstName_from_confcode = "";
                        }

                        if (jsonObject_beneficiaryCustomer.has("countryName")) {
                            countryName_from_confcode = jsonObject_beneficiaryCustomer.getString("countryName");
                        } else {
                            countryName_from_confcode = "";
                        }
//
//                        if (jsonObject_beneficiaryCustomer.has("currencyName")) {
//                            currencyName_from_confcode = jsonObject_beneficiaryCustomer.getString("countryName");
//                        } else {
//                            currencyName_from_confcode = "";
//                        }

                        if (jsonObject_beneficiaryCustomer.has("gender")) {
                            gender_from_confcode = jsonObject_beneficiaryCustomer.getString("gender");
                        } else {
                            gender_from_confcode = "";
                        }


                        if (jsonObject_beneficiaryCustomer.has("countryCode")) {
                            countryCode_from_confcode = jsonObject_beneficiaryCustomer.getString("countryCode");
                        } else {
                            countryCode_from_confcode = "";
                        }

                        if(!countryCode_from_confcode.equalsIgnoreCase(MyApplication.getSaveString("userCountryCode",  RemittanceReceive.this))){
                            MyApplication.showToast(RemittanceReceive.this,"This Confirmation Code Not Allowed to Your Country ");
                            return;
                            }


                        if (jsonObject_beneficiaryCustomer.has("toCurrencyCode")) {
                            currencyCode_from_confcode = jsonObject_beneficiaryCustomer.getString("toCurrencyCode");
                        } else {
                            currencyCode_from_confcode = "";
                        }


                        if (jsonObject_beneficiaryCustomer.has("email")) {
                            emailId_from_api = jsonObject_beneficiaryCustomer.getString("email");

                            edittext_email.setEnabled(false);
                            edittext_email.setText(emailId_from_api);
                        } else {
                            emailId_from_api = "";
                            edittext_email.setEnabled(false);
                            edittext_email.setText(emailId_from_api);
                        }


                        edittext_firstName.setEnabled(false);
                        edittext_countryName.setEnabled(false);
                        edittext_currencyName.setEnabled(false);
                        edittext_gender.setEnabled(false);

                        edittext_firstName.setText(firstName_from_confcode);
                        edittext_countryName.setText(countryName_from_confcode);
                        edittext_currencyName.setText(currencyName_from_confcode);


                        if (jsonObject.has("accountHolding")) {

                            JSONObject jsonObject3 = jsonObject.getJSONObject("accountHolding");
                            amountstr =  jsonObject3.opt("beneficiaryAmount")+"";
                            edittext_amount.setText(MyApplication.addDecimal(amountstr));
                        } else {
                            //tt

                        }

                        if (gender_from_confcode.equalsIgnoreCase("M")) {
                            edittext_gender.setText("Male");
                        } else {
                            edittext_gender.setText("Female");
                        }

                        //  mpin_final_api();

                    }

                } else{
                    MyApplication.hideLoader();
                    edittext_comment.setText("");
                    edittext_firstName.setText("");
                    edittext_countryName.setText("");
                    edittext_currencyName.setText("");
                    edittext_amount.setText("");
                    spinner_sender_idprooftype.setVisibility(View.GONE);
                    dobLinear.setVisibility(View.GONE);
                    et_sender_idproofNumber.setVisibility(View.GONE);
                    et_sender_idproof_expiry.setVisibility(View.GONE);
                    et_sender_idproof_expiry_lay.setVisibility(View.GONE);

                    Toast.makeText(RemittanceReceive.this, resultDescription, Toast.LENGTH_LONG).show();
                }


            } catch(
            Exception e)

            {
                MyApplication.hideLoader();
                edittext_comment.setText("");
                edittext_firstName.setText("");
                edittext_countryName.setText("");
                edittext_currencyName.setText("");
                edittext_amount.setText("");
                spinner_sender_idprooftype.setVisibility(View.GONE);
                dobLinear.setVisibility(View.GONE);
                et_sender_idproofNumber.setVisibility(View.GONE);
                et_sender_idproof_expiry.setVisibility(View.GONE);
                et_sender_idproof_expiry_lay.setVisibility(View.GONE);

                Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }


        @Override
        public void failure (String aFalse){

            MyApplication.hideLoader();
            edittext_comment.setText("");
            edittext_firstName.setText("");
            edittext_countryName.setText("");
            edittext_currencyName.setText("");
            edittext_amount.setText("");
            spinner_sender_idprooftype.setVisibility(View.GONE);
            dobLinear.setVisibility(View.GONE);
            et_sender_idproofNumber.setVisibility(View.GONE);
            et_sender_idproof_expiry.setVisibility(View.GONE);
            et_sender_idproof_expiry_lay.setVisibility(View.GONE);

            Toast.makeText(RemittanceReceive.this, aFalse, Toast.LENGTH_SHORT).show();
            finish();

        }
    });


}

    private void api_remittance_getCustomernew() {


        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/customer/allByCriteria?firstName=" + edittext_firstName.getText().toString().trim() +
                "&mobileNumber=" + edittext_mobileNuber.getText().toString().toString()+"&confirmationCode="+edittext_confirmationCode.getText().toString().trim(), languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {

                MyApplication.hideLoader();

                try {

                    //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1813407\",\"requestTime\":\"Fri Oct 22 15:57:35 IST 2021\",\"responseTime\":\"Fri Oct 22 15:57:35 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"accountHolding\":{\"id\":1937,\"code\":\"1000002014\",\"walletOwnerCode\":\"1000002794\",\"transactionId\":116607,\"openingBalance\":47593,\"closingBalance\":45124,\"sendingAmount\":1000,\"beneficiaryAmount\":1000,\"fee\":1300,\"exchangeRateValue\":0,\"tax\":169,\"fromCurrencyCode\":\"100062\",\"toCurrencyCode\":\"100062\",\"confirmationCode\":\"MM*********\",\"dueDate\":\"2021-10-29T00:00:00.000+0530\",\"status\":\"In Transit\",\"creationDate\":\"2021-10-22 15:08:56\",\"createdBy\":\"102073\",\"sendingWalletOwnerCode\":\"1000002794\",\"beneficiaryWalletOwnerCode\":\"1000002785\",\"holdingAccountTypeCode\":\"100000\",\"holdingAccountTypeName\":\"R2R\",\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":169,\"taxAvailBy\":\"Fee Amount\"}],\"srcProviderWalletCode\":\"1000015411\",\"sendCountryCode\":\"100092\",\"receiveCountryCode\":\"100092\",\"walletToCash\":true}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        // JSONObject accountHolding=jsonObject.getJSONObject("accountHolding");
                        //   JSONObject beneficiaryCustomer=accountHolding.getJSONObject("beneficiaryCustomer");

                        if (remitType.equalsIgnoreCase("Local Remit")||remitType.equalsIgnoreCase("Wallet to Cash Local")) {

                            otp_generate_api(benificiaryCode);

                        } else {
                            api_remittance_getCustomer();
                            // service_Provider_api();

                        }

                        //


                    } else {
                        Toast.makeText(RemittanceReceive.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(RemittanceReceive.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }

    private void api_remittance_getCustomer() {


        API.GET_REMMITANCE_DETAILS("ewallet/api/v1/remittance/getCustomer?firstName=" + firstName_from_confcode + "&confirmationCode=" + confirmationCodeStr + "&toCurrencyCode=" + currencyCode_from_confcode, languageToUse, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {


                try {

                    //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1813407\",\"requestTime\":\"Fri Oct 22 15:57:35 IST 2021\",\"responseTime\":\"Fri Oct 22 15:57:35 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"accountHolding\":{\"id\":1937,\"code\":\"1000002014\",\"walletOwnerCode\":\"1000002794\",\"transactionId\":116607,\"openingBalance\":47593,\"closingBalance\":45124,\"sendingAmount\":1000,\"beneficiaryAmount\":1000,\"fee\":1300,\"exchangeRateValue\":0,\"tax\":169,\"fromCurrencyCode\":\"100062\",\"toCurrencyCode\":\"100062\",\"confirmationCode\":\"MM*********\",\"dueDate\":\"2021-10-29T00:00:00.000+0530\",\"status\":\"In Transit\",\"creationDate\":\"2021-10-22 15:08:56\",\"createdBy\":\"102073\",\"sendingWalletOwnerCode\":\"1000002794\",\"beneficiaryWalletOwnerCode\":\"1000002785\",\"holdingAccountTypeCode\":\"100000\",\"holdingAccountTypeName\":\"R2R\",\"taxConfigurationList\":[{\"taxTypeCode\":\"100133\",\"taxTypeName\":\"Financial Tax\",\"value\":169,\"taxAvailBy\":\"Fee Amount\"}],\"srcProviderWalletCode\":\"1000015411\",\"sendCountryCode\":\"100092\",\"receiveCountryCode\":\"100092\",\"walletToCash\":true}}");

                    String resultCode = jsonObject.getString("resultCode");
                    String resultDescription = jsonObject.getString("resultDescription");

                    if (resultCode.equalsIgnoreCase("0")) {


                        // JSONObject accountHolding=jsonObject.getJSONObject("accountHolding");
                        //   JSONObject beneficiaryCustomer=accountHolding.getJSONObject("beneficiaryCustomer");

                        service_Provider_api();


                    } else {
                        MyApplication.hideLoader();

                        Toast.makeText(RemittanceReceive.this, resultDescription, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    MyApplication.hideLoader();

                    Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(String aFalse) {

                MyApplication.hideLoader();
                Toast.makeText(RemittanceReceive.this, aFalse, Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }


    boolean validation_mpin_detail() {

        mpinStr = et_mpin.getText().toString();

        if (mpinStr.trim().isEmpty()) {
            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
            //tt
        } else if (mpinStr.trim().length() == 4) {


            return true;
        } else {

            MyApplication.showErrorToast(this, getString(R.string.please_enter_4_digit_mpin));

            return false;
        }
    }


    private void mpin_final_api() {

        try {

//            {
//                    "walletOwnerCode": "1000002846",
//                    "transactionType": "RECEIVEREMITTANCE",
//                    "confirmationCode": "RRMM248614949XX",
//                    "firstName": "abhey",
//                    "lastName": "thakur",
//                    "toCurrencyCode": "100018",
//                    "amount": "1500",
//                    "pin": "b9f5e1c17d8a5f873eb65e69a5150dde",
//                    "channelTypeCode": "100000",
//                    "serviceCode": "100002",
//                    "serviceCategoryCode": "100018",
//                    "serviceProviderCode": "100105"
//            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("walletOwnerCode", walletOwnerCode_mssis_agent);
            jsonObject.put("transactionType", "RECEIVEREMITTANCE"); // Hard Code according  to Deepak
            jsonObject.put("confirmationCode", confirmationCodeStr);
            jsonObject.put("firstName", firstName_from_confcode);
            jsonObject.put("lastName", lastName_from_confcode);
            jsonObject.put("toCurrencyCode", currencyCode_from_confcode);


            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {


                jsonObject.put("amount", amountfr);

            } else {
                jsonObject.put("amount", amountstr);

            }
            String encryptionDatanew = AESEncryption.getAESEncryption(mpinStr);
            jsonObject.put("pin", encryptionDatanew);


            // jsonObject.put("phoneNumber", mobileNoStr);
            //  jsonObject.put("toCurrencyCode", currencyCode_agent);         // source  srcWalletOwnerCode

            jsonObject.put("channelTypeCode", "100000");           // Hard Code according  to Deepak
            jsonObject.put("serviceCode", serviceCode_from_serviceCategory);
            jsonObject.put("serviceCategoryCode", serviceCategoryCode_from_serviceCategory);  // Hard Code according  to Deepak
            jsonObject.put("serviceProviderCode", serviceProviderCode_from_serviceCategory);  // Hard Code according  to Deepak

            System.out.println("Receive request "+jsonObject.toString());
            String requestNo = AESEncryption.getAESEncryption(jsonObject.toString());
            JSONObject jsonObjectA = null;
            try {
                jsonObjectA = new JSONObject();
                jsonObjectA.put("request", requestNo);
            } catch (Exception e) {

            }
            API.POST_TRANSFER("ewallet/api/v1/remittance/receive", jsonObjectA, languageToUse, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {


                    try {


                        // JSONObject jsonObject = new JSONObject("{\"transactionId\":\"115028\",\"requestTime\":\"Fri Nov 12 16:12:57 IST 2021\",\"responseTime\":\"Fri Nov 12 16:12:57 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"remittance\":{\"id\":1846,\"code\":\"1000001880\",\"walletOwnerCode\":\"1000002687\",\"transactionType\":\"RECEIVE REMITTANCE\",\"receiverCode\":\"1000001834\",\"fromCurrencyCode\":\"100062\",\"fromCurrencyName\":\"GNF\",\"fromCurrencySymbol\":\"Fr\",\"toCurrencyCode\":\"100062\",\"toCurrencyName\":\"GNF\",\"toCurrencySymbol\":\"Fr\",\"comments\":\"ok\",\"amount\":3000,\"amountToPaid\":3000,\"fee\":0,\"tax\":\"0.0\",\"conversionRate\":0,\"confirmationCode\":\"MM*********\",\"transactionReferenceNo\":\"115028\",\"transactionDateTime\":\"2021-11-12 16:12:57\",\"sender\":{\"id\":110581,\"code\":\"1000002687\",\"firstName\":\"Sharique Subs\",\"lastName\":\"Sharique Subs\",\"mobileNumber\":\"9015931368\",\"gender\":\"M\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idProofNumber\":\"id12345\",\"idExpiryDate\":\"2021-11-12\",\"dateOfBirth\":\"1960-01-22\",\"email\":\"sharique9718@gmail.com\",\"issuingCountryCode\":\"100195\",\"issuingCountryName\":\"Senegal\",\"status\":\"Active\",\"creationDate\":\"2021-11-10 21:07:00\",\"createdBy\":\"100250\",\"modificationDate\":\"2021-11-11 10:58:24\",\"modifiedBy\":\"100308\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"ownerName\":\"Sharique Subs\",\"regesterCountryDialCode\":\"+224\"},\"receiver\":{\"id\":1805,\"code\":\"1000001834\",\"firstName\":\"annu sender\",\"lastName\":\"last annu\",\"mobileNumber\":\"9990063618\",\"gender\":\"M\",\"email\":\"annu@gmail.com\",\"countryCode\":\"100092\",\"countryName\":\"Guinea\",\"status\":\"Active\",\"creationDate\":\"2021-11-12 15:58:41\",\"createdBy\":\"101975\",\"modificationDate\":\"2021-11-12 16:00:53\",\"modifiedBy\":\"101975\",\"dialCode\":\"+224\"},\"sendCountryCode\":\"100092\",\"receiveCountryCode\":\"100092\",\"sendCountryName\":\"Guinea\",\"receiveCountryName\":\"Guinea\",\"walletToCash\":true}}");


                        String resultCode = jsonObject.getString("resultCode");
                        String resultDescription = jsonObject.getString("resultDescription");

                        if (resultCode.equalsIgnoreCase("0")) {


                            ll_page_1.setVisibility(View.GONE);
                            ll_reviewPage.setVisibility(View.GONE);
                            ll_receiptPage.setVisibility(View.GONE);
                            ll_successPage.setVisibility(View.VISIBLE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ll_page_1.setVisibility(View.GONE);
                                    ll_reviewPage.setVisibility(View.GONE);
                                    ll_successPage.setVisibility(View.GONE);
                                    ll_receiptPage.setVisibility(View.VISIBLE);
                                }
                            }, 2000);

                            receiptPage_tv_stransactionType.setText(getString(R.string.receive_remittance));

                            receiptPage_sender_mssidn.setText(mobileNoStr);

                            receiptPage_sender_mssidn.setVisibility(View.GONE);

                            JSONObject remittance_object = jsonObject.getJSONObject("remittance");

                            TextView receiptPage_agentName = findViewById(R.id.receiptPage_agentName);
                            receiptPage_agentName.setText(remittance_object.getString("walletOwnerUserName"));


                            receiptPage_confirmationCode.setText(remittance_object.getString("confirmationCode"));
                            receiptPage_confirmationCode.setVisibility(View.GONE);
                            JSONObject remittance_sender = remittance_object.getJSONObject("sender");
                            JSONObject remittance_receiver = remittance_object.getJSONObject("receiver");


                            if (remittance_object.has("fromCurrencySymbol")) {
                                currencySymbol_sender = remittance_object.getString("fromCurrencySymbol");
                            }

                            if (remittance_object.has("toCurrencySymbol")) {
                                currencySymbol_receiver = remittance_object.getString("toCurrencySymbol");
                            }

                            TextView taxnew=findViewById(R.id.taxnew);

                            if(remittance_object.optBoolean("bearerSender")) {
                                if (Double.parseDouble(remittance_object.optString("tax")) > 0) {
                                    receiptPage_tv_financialtax.setVisibility(View.VISIBLE);
                                    receiptPage_tv_financialtax.setText(currencySymbol_sender + " " + MyApplication.addDecimal(remittance_object.optString("tax") + ""));
                                } else {
                                    receiptPage_tv_financialtax.setVisibility(View.VISIBLE);
                                    receiptPage_tv_financialtax.setText(currencySymbol_sender + " " + MyApplication.addDecimal("0.00"));
                                }
                            }else{
                                receiptPage_tv_financialtax.setVisibility(View.VISIBLE);
                                receiptPage_tv_financialtax.setText(currencySymbol_sender + " " + MyApplication.addDecimal("0.00"));
                            }


                            receiptPage_sbenificairay_mssidn.setText(MyApplication.getSaveString("USERNAME", RemittanceReceive.this));

                            receiptPage_sbenificairay_mssidn.setVisibility(View.GONE);

                  //        receiptPage_tv_amount_to_be_charged.setText(currencySymbol_receiver + " " + MyApplication.addDecimal(remittance_object.optDouble("amount") + ""));
                            receiptPage_amount_to_paid_receiptpage.setText(currencySymbol_receiver + " " + MyApplication.addDecimal(remittance_object.optDouble("amountToPaid") + ""));
                            receiptPage_tv_transactionAmount.setText(currencySymbol_sender + " " + MyApplication.addDecimal(remittance_object.optDouble("amount")+""));

                           /* Double tamount=Double.parseDouble(MyApplication.addDecimal(remittance_object.optDouble("amount")+""));
                            Double paid= Double.parseDouble(MyApplication.addDecimal(remittance_object.optDouble("amountToPaid") + ""));


                            if(tamount!=paid){
                                amoutnpaidLinear.setVisibility(View.VISIBLE);
                            }else{
                                amoutnpaidLinear.setVisibility(View.GONE);

                            }*/



                            if(remittance_object.optBoolean("bearerSender")) {
                                if (remittance_object.optDouble("fee") > 0) {
                                    receiptPage_tv_fee.setVisibility(View.VISIBLE);
                                    receiptPage_tv_fee.setText(currencySymbol_sender + " " + MyApplication.addDecimal(remittance_object.getInt("fee") + ""));
                                } else {
                                    receiptPage_tv_fee.setVisibility(View.VISIBLE);
                                    receiptPage_tv_fee.setText(currencySymbol_sender + " " + MyApplication.addDecimal("0.00"));
                                }
                            }else{
                                receiptPage_tv_fee.setVisibility(View.VISIBLE);
                                receiptPage_tv_fee.setText(currencySymbol_sender + " " + MyApplication.addDecimal("0.00"));
                            }
                            if (remittance_object.has("conversionRate")) {
                                receiptPage_conversion_rate.setVisibility(View.VISIBLE);
                                receiptPage_conversion_rate.setText(MyApplication.addDecimalfive(remittance_object.getString("conversionRate")));
                            } else {
                                receiptPage_conversion_rate.setVisibility(View.VISIBLE);
                                receiptPage_conversion_rate.setText(MyApplication.addDecimalfive("0.00"));
                            }


                            receiptPage_tv_transaction_receiptNo.setText(jsonObject.getString("transactionId"));
                            receiptPage_tv_dateOfTransaction.setText(MyApplication.convertUTCToLocaldate(remittance_object.getString("transactionDateTime")));

                            MyApplication.hideLoader();

                            ////////////////////////////// Sender Details ///////////

                            String senderFirstNameTemp = "", senderlastNameTemp = "";
                            if (remittance_sender.has("firstName")) {
                                senderFirstNameTemp = remittance_sender.getString("firstName");
                                receiptPage_tv_sender_name.setText(senderFirstNameTemp);
                            }

                            if (remittance_sender.has("lastName")) {
                                senderlastNameTemp = remittance_sender.getString("lastName");
                                receiptPage_tv_sender_name.setText(senderFirstNameTemp + " " + senderlastNameTemp);
                            }

                            if (remittance_sender.has("mobileNumber")) {
                                receiptPage_tv_sender_phoneNo.setText(remittance_sender.getString("mobileNumber"));
                            }

                            if (remittance_sender.has("email")) {
                                receiptPage_tv_sender_emailid.setText(remittance_sender.getString("email"));
                            }

                            if (remittance_sender.has("gender")) {

                                if (remittance_sender.getString("gender").equalsIgnoreCase("M")) {
                                    receiptPage_tv_sender_gender.setText("Male");
                                } else {
                                    receiptPage_tv_sender_gender.setText("Female");
                                }
                            }

                            if (remittance_sender.has("registerCountryName")) {
                                receiptPage_tv_sender_country.setText(remittance_sender.getString("registerCountryName"));
                            }
                            receiptPage_tv_sender_address.setText("");

                            if (remittance_sender.has("dateOfBirth")) {
                                receiptPage_tv_sender_dob.setText(remittance_sender.getString("dateOfBirth"));
                            }

                            if (remittance_sender.has("idProofTypeName")) {
                                receiptPage_tv_sender_idproofType.setText(remittance_sender.getString("idProofTypeName"));
                            }

                            if (remittance_sender.has("idProofNumber")) {
                                receiptPage_tv_sender_idproofNumber.setText(remittance_sender.getString("idProofNumber"));
                            }

                            if (remittance_sender.has("idExpiryDate")) {
                                receiptPage_tv_sender_idproofExpirayDate.setText(remittance_sender.getString("idExpiryDate"));
                            }

                            ////////////////////////////// Receiver Details ///////////


                            String receiveFirstNameTemp = "", receiverlastNameTemp = "";
                            if (remittance_receiver.has("firstName")) {
                                receiveFirstNameTemp = remittance_receiver.getString("firstName");
                                receiptPage_tv_receiver_name.setText(receiveFirstNameTemp);
                            }

                            if (remittance_receiver.has("lastName")) {
                                receiverlastNameTemp = remittance_receiver.getString("lastName");
                                receiptPage_tv_receiver_name.setText(receiveFirstNameTemp + " " + receiverlastNameTemp);

                            }

                            if (remittance_receiver.has("mobileNumber")) {
                                receiptPage_tv_receiver_phoneNo.setText(remittance_receiver.getString("mobileNumber"));
                            }

                            if (remittance_receiver.has("email")) {
                                receiptPage_tv_receiver_emailid.setText(remittance_receiver.getString("email"));
                            }

                            if (remittance_receiver.has("gender")) {

                                if (remittance_receiver.getString("gender").equalsIgnoreCase("M")) {
                                    receiptPage_tv_receiver_gender.setText("Male");
                                } else {
                                    receiptPage_tv_receiver_gender.setText("Female");
                                }
                            }


                            if (remittance_receiver.has("email")) {
                                receiptPage_tv_receiver_emailid.setText(remittance_receiver.getString("email"));
                            }


                            if (remittance_receiver.has("countryName")) {
                                receiptPage_tv_receiver_country.setText(remittance_receiver.getString("countryName"));
                            }

                            if (remittance_receiver.has("creationDate")) {
                                receiptPage_tv_receiver_dob.setText(remittance_receiver.getString("creationDate"));
                            }

                            if (remittance_receiver.has("idProofTypeName")) {
                                receiptPage_tv_receiver_idproofType.setText(remittance_receiver.getString("idProofTypeName"));
                            }

                            if (remittance_receiver.has("idProofNumber")) {
                                receiptPage_tv_receiver_idproofNumber.setText(remittance_receiver.getString("idProofNumber"));
                            }

                            if (remittance_receiver.has("modificationDate")) {
                                receiptPage_tv_receiver_idproofExpirayDate.setText(remittance_receiver.getString("modificationDate"));
                            }

                            receiptPage_tv_receiver_address.setText("");


                        } else {
                            MyApplication.hideLoader();

                            Toast.makeText(RemittanceReceive.this, resultDescription, Toast.LENGTH_LONG).show();
                            //  finish();
                        }


                    } catch (Exception e) {
                        MyApplication.hideLoader();

                        Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(RemittanceReceive.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {

                        Toast.makeText(RemittanceReceive.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnFront:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_ONE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
                break;

            case R.id.ll_resendOtp:
                otp_generate_api(benificiaryCode);
                break;

            case R.id.tv_nextClick: {


                if (buttonClick.equalsIgnoreCase("0")) {
                    if (validation_mobile_Details()) {

                        if (new InternetCheck().isConnected(RemittanceReceive.this)) {

                            MyApplication.showloader(RemittanceReceive.this, getString(R.string.please_wait));

                            if (tv_nextClick.getText().toString().equalsIgnoreCase(getString(R.string.otp_verify))) {
                                otp_verify_api();
                            } else {
                                api_remittance_getCustomernew();
                            }

                            //


                        } else {
                            Toast.makeText(RemittanceReceive.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {

                    if (validation_mpin_detail()) {

                        if (new InternetCheck().isConnected(RemittanceReceive.this)) {

                            MyApplication.showloader(RemittanceReceive.this, getString(R.string.please_wait));

                            mpin_final_api();


                        } else {
                            Toast.makeText(RemittanceReceive.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                        }
                    }
                }


            }

            break;

            case R.id.exportReceipt_textview: {
                close_receiptPage_textview.setVisibility(View.GONE);
                exportReceipt_textview.setVisibility(View.GONE);
                Bitmap bitmap = getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap, "test.jpg");
            }

            break;


            case R.id.confirm_reviewClick_textview: {

                Toast.makeText(RemittanceReceive.this, "------confirm_reviewClick_textview -----", Toast.LENGTH_LONG).show();


            }
            break;

            case R.id.previous_reviewClick_textview: {

                ll_page_1.setVisibility(View.VISIBLE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.GONE);
            }
            break;

            case R.id.qrCode_imageButton: {

                if (checkPermission()) {


                    IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                    intentIntegrator.setPrompt(getString(R.string.scanbarcode));
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.initiateScan();


                } else {
                    requestPermission();
                }


            }

            break;

            case R.id.tvContinue: {

                ll_page_1.setVisibility(View.GONE);
                ll_reviewPage.setVisibility(View.GONE);
                ll_successPage.setVisibility(View.GONE);
                ll_receiptPage.setVisibility(View.VISIBLE);

            }
            break;


            case R.id.close_receiptPage_textview: {


//                ll_page_1.setVisibility(View.VISIBLE);
//                ll_reviewPage.setVisibility(View.GONE);
//                ll_receiptPage.setVisibility(View.GONE);

                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();


            }

            break;


        }
    }

//    private void exchange_rate_api() {
//
//        // API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=100062&receiveCurrencyCode=100062&sendCountryCode=100092&receiveCountryCode=&currencyValue=1000&channelTypeCode=100000&serviceCode=100003&serviceCategoryCode=100011&serviceProviderCode=100106&walletOwnerCode=1000002606&remitAgentCode=1000002606&payAgentCode=1000002488",languageToUse,new Api_Responce_Handler() {
//
//
//        API.GET_CASHOUT_CONFCODE_DETAILS("ewallet/api/v1/exchangeRate/getAmountDetails?sendCurrencyCode=" + currencyCode_agent + "&receiveCurrencyCode=100062&sendCountryCode=" + countryCode_agent + "&receiveCountryCode=" +
//                "&currencyValue=" + amountstr + "&channelTypeCode=2&serviceCode=" + serviceCode_from_serviceCategory + "&serviceCategoryCode=" + serviceCategoryCode_from_serviceCategory + "&serviceProviderCode=" + serviceProviderCode_from_serviceCategory + "&walletOwnerCode=" + walletOwnerCode_mssis_agent + "&remitAgentCode=" + walletOwnerCode_mssis_agent + "&payAgentCode=1000002488", languageToUse, new Api_Responce_Handler() {
//            @Override
//            public void success(JSONObject jsonObject) {
//
//                MyApplication.hideLoader();
//
//                try {
//
//                    //JSONObject jsonObject = new JSONObject("{"transactionId":"1789322","requestTime":"Wed Oct 20 15:53:33 IST 2021","responseTime":"Wed Oct 20 15:53:33 IST 2021","resultCode":"0","resultDescription":"Transaction Successful","walletOwnerCountryCurrencyList":[{"id":7452,"code":"107451","walletOwnerCode":"1000002488","currencyCode":"100062","currencyName":"GNF","currencySymbol":"Fr","countryCurrencyCode":"100076","inBound":true,"outBound":true,"status":"Active"}]}");
//
//                    String resultCode = jsonObject.getString("resultCode");
//                    String resultDescription = jsonObject.getString("resultDescription");
//
//                    if (resultCode.equalsIgnoreCase("0")) {
//
//                        //Toast.makeText(CashIn.this, resultDescription, Toast.LENGTH_LONG).show();
//
//                        JSONObject exchangeRate = jsonObject.getJSONObject("exchangeRate");
//
//                        fees_amount = exchangeRate.getString("fee");
//                        rp_tv_fees_reveiewPage.setText("Fr " + fees_amount);
//
//                        //  credit_amount=exchangeRate.getString("currencyValue");
//
//                        JSONArray jsonArray = exchangeRate.getJSONArray("taxConfigurationList");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                            tax_financial = jsonObject2.getString("value");
//                        }
//
//                        rp_tv_financialTax.setText("Fr " + tax_financial);
//
//                        tax_financial_double = Double.parseDouble(tax_financial);
//                        fees_amount_double = Double.parseDouble(fees_amount);
//                        amountstr_double = Double.parseDouble(amountstr);
//
//                        totalAmount_double = tax_financial_double + amountstr_double + fees_amount_double;
//                        totalAmount_str = String.valueOf(totalAmount_double);
//                        rp_tv_amount_to_be_charge.setText("Fr " + totalAmount_str);
//
//                        amountstr = String.valueOf(amountstr_double);
//                        rp_tv_transactionAmount.setText("Fr " + amountstr);
//                        rp_tv_amount_to_be_credit.setText("Fr " + amountstr);
//
//                        allByCriteria_walletOwnerCode_api();
//
//                    } else {
//                        Toast.makeText(CashOutCodeSubscriber.this, resultDescription, Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//
//                } catch (Exception e) {
//                    Toast.makeText(CashOutCodeSubscriber.this, e.toString(), Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//
//            }
//
//
//            @Override
//            public void failure(String aFalse) {
//
//                MyApplication.hideLoader();
//                Toast.makeText(CashOutCodeSubscriber.this, aFalse, Toast.LENGTH_SHORT).show();
//                finish();
//
//            }
//        });
//
//
//    }


    private boolean checkPermission() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        try {


            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {

                System.out.println(resultCode);

                if (result.getContents() == null) {

                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {


                    String str = result.getContents();

                    if (str.equalsIgnoreCase("")) {
                        // 1000002786:TarunMwTest

                        Toast.makeText(this, "QR Code Not Valid", Toast.LENGTH_LONG).show();
                        edittext_mobileNuber.setEnabled(true);

                    } else {


                        String[] qrData = str.split("\\:");
                        mobileNoStr = qrData[0];
                        edittext_mobileNuber.setText(mobileNoStr);
                        edittext_mobileNuber.setEnabled(false);


                        // Toast.makeText(this, "QR Code  Valid", Toast.LENGTH_LONG).show();
                    }

                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }


        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            e.printStackTrace();
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
                if (file_size <= 100) {
                    isFrontUpload = true;
                } else {
                    MyApplication.showErrorToast(RemittanceReceive.this, "File size exceeds");
                }
            }

        }
    }


//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view;
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void createImageFile(Bitmap bm) {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(System.currentTimeMillis());
            File storageDir = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
            if (!storageDir.exists())
                storageDir.mkdirs();
            File image = File.createTempFile(
                    timeStamp,
                    ".jpeg",
                    storageDir
            );

            System.out.println(image.getAbsolutePath());
            if (image.exists()) image.delete();
            //   Log.i("LOAD", root + fname);
            try {
                FileOutputStream out = new FileOutputStream(image);
              //  bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareImage(image);
        } catch (Exception e) {

        }
    }

//    public  void store(Bitmap bm, String fileName){
//        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
//        File dir = new File(dirPath);
//        if(!dir.exists())
//            dir.mkdirs();
//        File file = new File(dirPath, fileName);
//        try {
//            FileOutputStream fOut = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//            fOut.flush();
//            fOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        shareImage(file);
//    }

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }


    private void otp_generate_api(String code) {
        try {

            JSONObject jsonObject = new JSONObject();


            jsonObject.put("transTypeCode", "101442");
            // jsonObject.put("transTypeCode","101813");
            jsonObject.put("customerCode", code);


            API.POST_GET_OTP("ewallet/api/v1/otp", jsonObject, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //  JSONObject jsonObject = new JSONObject("{\"transactionId\":\"1771883\",\"requestTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"responseTime\":\"Mon Oct 18 18:05:40 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\"}");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(RemittanceReceive.this, error_message, Toast.LENGTH_LONG).show();

                            if (error.equalsIgnoreCase("1251")) {

                                Intent i = new Intent(RemittanceReceive.this, VerifyLoginAccountScreen.class);
                                startActivity(i);

                                // finish();
                            }
                        } else {

                            String resultDescription = jsonObject.getString("resultDescription");
                            String resultCode = jsonObject.getString("resultCode");

                            if (resultCode.equalsIgnoreCase("0")) {


                                Toast.makeText(RemittanceReceive.this, getString(R.string.otp_has_send_sucessfully_subscriber_register), Toast.LENGTH_LONG).show();

                                otp_new_l.setVisibility(View.VISIBLE);
                                tv_nextClick.setText(getString(R.string.otp_verify));


                            } else {
                                Toast.makeText(RemittanceReceive.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(RemittanceReceive.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {

                        Toast.makeText(RemittanceReceive.this, aFalse, Toast.LENGTH_SHORT).show();
                    }

                    //  MyApplication.showToast(LoginPin.this,aFalse);

                }
            });

        } catch (Exception e) {

            MyApplication.hideLoader();

            MyApplication.showToast(RemittanceReceive.this, e.toString());

        }

    }


    private void otp_verify_api() {
        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("transTypeCode", "101442");      // Temporary Hard Code acording to Praveen
            jsonObject.put("otp", et_otp.getText().toString());
            jsonObject.put("customerCode", benificiaryCode);


            API.POST_REQUEST_VERIFY_OTP("ewallet/api/v1/otp/verify", jsonObject, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {

                    MyApplication.hideLoader();

                    try {

                        //JSONObject jsonObject = new JSONObject("");

                        if (jsonObject.has("error")) {

                            String error = jsonObject.getString("error");
                            String error_message = jsonObject.getString("error_message");

                            Toast.makeText(RemittanceReceive.this, error_message, Toast.LENGTH_LONG).show();

                            if (error.equalsIgnoreCase("1251")) {

                                Intent i = new Intent(RemittanceReceive.this, VerifyLoginAccountScreen.class);
                                startActivity(i);

                                // finish();
                            }
                        } else {
                            String resultDescription = jsonObject.getString("resultDescription");
                            String resultCode = jsonObject.getString("resultCode");


                            if (resultCode.equalsIgnoreCase("0")) {

                                api_remittance_getCustomer();
                                otp_new_l.setVisibility(View.GONE);


                            } else {
                                Toast.makeText(RemittanceReceive.this, resultDescription, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(RemittanceReceive.this, e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(String aFalse) {

                    MyApplication.hideLoader();

                    if (aFalse.equalsIgnoreCase("1251")) {
                        Intent i = new Intent(RemittanceReceive.this, VerifyLoginAccountScreen.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(RemittanceReceive.this, aFalse, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {

            MyApplication.hideLoader();
            MyApplication.showToast(RemittanceReceive.this, e.toString());
        }


    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
       // inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + Calendar.getInstance().getTime(), null);
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

        et_sender_dob.setText(year + "-" + (month + 1) + "-" + day);
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

      //  datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        return datepickerdialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        et_sender_idproof_expiry.setText(year + "-" + (month + 1) + "-" + day);
        //mDobidproffText.setVisibility(View.VISIBLE);
        // etDob.setText(year + "-" + (month+1) + "-" + day);

    }
}

}

