package com.agent.cashmoovui.remittancebyabhay.local;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.LogoutAppCompactActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LocalRemittanceReceiptScreen extends LogoutAppCompactActivity implements View.OnClickListener {
    public static LocalRemittanceReceiptScreen localremitreceiptscreenC;
    private TextView tvTransRefNo,tvConfCode,tvTransType,tvDateOfTrans,tvSendCurrency,tvBenefiCurrency,
            tvRecCountry,tvSendCountry,tvTransAmount,tvConvRate,tvFee,tvAmountCharged,tvAmountPaid,
            tvSendName,tvSendPhoneNo,tvBenefiName,tvBenefiPhoneNo,
            tax1_lable,tax1_value,tax2_lable,tax2_value,agentName;
    private LinearLayout linConfCode,tax1_layout,tax2_layout,amoutnpaidLinear;
    private Button btnCloseReceipt,btnShareReceipt;
    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_remittance_receipt_screen);
        localremitreceiptscreenC=this;
        rootView = getWindow().getDecorView().findViewById(R.id.lin);
        getIds();
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view;
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public  void createImageFile(Bitmap bm)  {
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
               // bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareImage(image);
        }catch (Exception e){

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

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_screenshot)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_app_available), Toast.LENGTH_SHORT).show();
        }
    }

    private void getIds() {

        try {

            agentName= findViewById(R.id.agentName);
            String nameOwner=MyApplication.getSaveString("FIRSTNAME_USERINFO", localremitreceiptscreenC)+
                    MyApplication.getSaveString("LASTNAME_USERINFO", localremitreceiptscreenC);
            agentName.setText(getString(R.string.SendingAgentName)+nameOwner);
            tvTransRefNo = findViewById(R.id.tvTransRefNo);
            tvConfCode = findViewById(R.id.tvConfCode);
            tvTransType = findViewById(R.id.tvTransType);
            tvDateOfTrans = findViewById(R.id.tvDateOfTrans);
            tvSendCurrency = findViewById(R.id.tvSendCurrency);
            tvBenefiCurrency = findViewById(R.id.tvBenefiCurrency);
            tvSendCountry = findViewById(R.id.tvSendCountry);
            tvRecCountry = findViewById(R.id.tvRecCountry);
            tvTransAmount = findViewById(R.id.tvTransAmount);
            tvConvRate = findViewById(R.id.tvConvRate);
            tvFee = findViewById(R.id.tvFee);
            tvAmountCharged = findViewById(R.id.tvAmountCharged);
            tvAmountPaid = findViewById(R.id.tvAmountPaid);
            tvSendName = findViewById(R.id.tvSendName);
            tvSendPhoneNo = findViewById(R.id.tvSendPhoneNo);
            tvBenefiName = findViewById(R.id.tvBenefiName);
            tvBenefiPhoneNo = findViewById(R.id.tvBenefiPhoneNo);
            btnCloseReceipt = findViewById(R.id.btnCloseReceipt);
            btnShareReceipt = findViewById(R.id.btnShareReceipt);

            tax1_layout = findViewById(R.id.tax1_layout);
            tax2_layout = findViewById(R.id.tax2_layout);
            tax1_lable = findViewById(R.id.tax1_lable);
            tax1_value = findViewById(R.id.tax1_value);
            tax2_lable = findViewById(R.id.tax2_lable);
            tax2_value = findViewById(R.id.tax2_value);
            amoutnpaidLinear=findViewById(R.id.amoutnpaidLinear);

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
            DecimalFormat df = new DecimalFormat("0.00",symbols);
            tvTransRefNo.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("transactionReferenceNo"));
            tvConfCode.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("confirmationCode"));
            tvTransType.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("transactionType"));
            tvDateOfTrans.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("transactionDateTime"));
            tvSendCurrency.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencyName"));
            tvBenefiCurrency.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("toCurrencyName"));
            tvSendCountry.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("countryName"));
            tvRecCountry.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("countryName"));
            tvSendName.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("firstName") + " " +
                    LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("lastName"));
            tvSendPhoneNo.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("sender").optString("mobileNumber"));
            tvBenefiName.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("firstName") + " " +
                    LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("lastName"));
            tvBenefiPhoneNo.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optJSONObject("receiver").optString("mobileNumber"));
           /* String[] arr = LocalRemittanceConfirmScreen.tvTransAmount.getText().toString().split(" ");
            tvTransAmount.setText(MyApplication.addDecimal(arr[2]));
*/
            //tvTransAmount.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("amountToPaid"));


            tvTransAmount.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol") + " " +
                    MyApplication.addDecimal(""+LocalRemittanceActivity.amount));
            tvConvRate.setText(MyApplication.addDecimalfiveinternatonal(LocalRemittanceActivity.rate));
            tvFee.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol") + " "
                    + MyApplication.addDecimal(""+LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("fee")));




            tvAmountPaid.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("toCurrencySymbol") + " " + MyApplication.addDecimal(""+LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("amountToPaid")));



            String tamount=tvTransAmount.getText().toString();
            String paid=tvAmountPaid.getText().toString();
            System.out.println("get amount"+tamount);
            System.out.println("get paid"+paid);


            if(!tamount.equalsIgnoreCase(paid)){
                amoutnpaidLinear.setVisibility(View.VISIBLE);
            }else{
                amoutnpaidLinear.setVisibility(View.GONE);
            }
            tvAmountCharged.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol") + " " + MyApplication.addDecimal(""+LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optDouble("amount")));



            if (LocalRemittanceConfirmScreen.taxConfigList != null) {
                if (LocalRemittanceConfirmScreen.taxConfigList.length() == 1) {
                    tax1_layout.setVisibility(View.VISIBLE);
                    tax1_lable.setText(MyApplication.getTaxStringnew(LocalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName") )+ " :");
                    tax1_value.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol") + " " + MyApplication.addDecimal(""+LocalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));
                    // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
                }
                if (LocalRemittanceConfirmScreen.taxConfigList.length() == 2) {
                    tax1_layout.setVisibility(View.VISIBLE);
                    tax1_lable.setText(MyApplication.getTaxStringnew(LocalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optString("taxTypeName") )+ " :");
                    tax1_value.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol") + " " + MyApplication.addDecimal(""+LocalRemittanceConfirmScreen.taxConfigList.optJSONObject(0).optDouble("value")));

                    tax2_layout.setVisibility(View.VISIBLE);
                    tax2_lable.setText(MyApplication.getTaxStringnew(LocalRemittanceConfirmScreen.taxConfigList.optJSONObject(1).optString("taxTypeName")) + " :");
                    tax2_value.setText(LocalRemittanceConfirmScreen.receiptJson.optJSONObject("remittance").optString("fromCurrencySymbol") + " " + MyApplication.addDecimal(""+LocalRemittanceConfirmScreen.taxConfigList.optJSONObject(1).optDouble("value")));
                    // finalamount=Double.parseDouble(String.valueOf(ToSubscriber.fee))+Double.parseDouble(ToSubscriber.etAmount.getText().toString())+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"))+Double.parseDouble(ToSubscriber.taxConfigurationList.optJSONObject(0).optString("value"));
                }
            }


            setOnCLickListener();
        }catch (Exception e){
            MyApplication.showToast(LocalRemittanceReceiptScreen.this,e.getMessage());
        }

    }

    private void setOnCLickListener() {
        btnCloseReceipt.setOnClickListener(localremitreceiptscreenC);
        btnShareReceipt.setOnClickListener(localremitreceiptscreenC);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCloseReceipt:
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.btnShareReceipt:
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
               // btnShareReceipt.setVisibility(View.GONE);
                Bitmap bitmap=getScreenShot(rootView);
                createImageFile(bitmap);
                //store(bitmap,"test.jpg");
                break;

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }


}
