package com.agent.cashmoovui.wallet_owner.outlet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.wallet_owner.outlet.OutletKYC;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class OutletSignature extends AppCompatActivity implements View.OnClickListener{
    public static OutletSignature agentsignatureC;
    private SignaturePad signaturePad_electricityBill;
    InputStream imageStream, imageStreamCamera;
    Bitmap signatureBitmapBillElectricity;

    Button btnClear,btnSave;
    boolean isphotoSigntature=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_subscriber_signature);
        agentsignatureC=this;
        getIds();

    }

    private void getIds() {
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);

        signaturePad_electricityBill = findViewById(R.id.signaturePad_electricityBill);
        signaturePad_electricityBill.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                isphotoSigntature=true;

                signatureBitmapBillElectricity = signaturePad_electricityBill.getSignatureBitmap();
                System.out.println(signatureBitmapBillElectricity);

            }

            @Override
            public void onSigned() {
                isphotoSigntature=true;

                btnClear.setEnabled(true);
                btnSave.setEnabled(true);
            }

            @Override
            public void onClear() {
                photoSend=null;
                btnClear.setEnabled(true);  // change all page
                btnClear.setEnabled(false);
                isphotoSigntature=false;
            }
        });

        setOnCLickListener();


    }



    private void setOnCLickListener() {
        btnClear.setOnClickListener(agentsignatureC);
        btnSave.setOnClickListener(agentsignatureC);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signaturePad_electricityBill:
                System.out.println();
                break;

            case R.id.btnClear: {
                photoSend=null;
                signaturePad_electricityBill.clear();

            }
            break;

            case R.id.btnSave: {
                if(isphotoSigntature){
                    signatureBitmapBillElectricity = signaturePad_electricityBill.getSignatureBitmap();
                    addJpgSignatureToGallery(signatureBitmapBillElectricity, "BillElectricity");

                    if(photoSend!=null){
                        callupload(photoSend, "100044", OutletKYC.outletWalletOwnerCode);
                    }else{
                        MyApplication.showToast(OutletSignature.this,getString(R.string.please_enter_signature));
                    }

                }
            else{
                MyApplication.showToast(OutletSignature.this,getString(R.string.please_enter_signature));
            }

            }
            break;
        }

    }

    File photoSend=null;
    public boolean addJpgSignatureToGallery(Bitmap signature, String selectionType) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            signSignatureHuman(photo, selectionType);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }
    void signSignatureHuman(File photo, String selectionType) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(photo);


        try {
            imageStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


        ColorMatrix matrix = new ColorMatrix();   // color to black in white
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        //  imageview_preview_mpinPage.setColorFilter(filter);

        Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
        selectedImage = resizeBmp;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] imageBytes = baos.toByteArray();


        Uri photoSend1 = getImageUri(getApplicationContext(), selectedImage);


        photoSend = new File(getRealPathFromURI(photoSend1).toString());


        String  uploadGalleryStringElectricity = Base64.encodeToString(imageBytes, Base64.DEFAULT);

       // Toast.makeText(SelfSignature.this,uploadGalleryStringElectricity,Toast.LENGTH_LONG).show();
        //   imageview_bill_electricityBill.setImageBitmap(selectedImage);
        //  imageview_preview_drawHome.setImageDrawable(null);

        System.out.println();

        mediaScanIntent.setData(uri);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "sele"+ Calendar.getInstance().getTime(), null);
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

    private void callupload(File file, String idProofTypeCode, String subscriberWalletOwnerCode) {

        MyApplication.showloader(agentsignatureC, "uploading file...");
        //idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode()
        API.Upload_REQEST_WH_NEW("ewallet/api/v1/fileUpload",file,idProofTypeCode,subscriberWalletOwnerCode,
                 new Api_Responce_Handler() {

                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if (jsonObject != null) {
                            if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                //MyApplication.showToast(getString(R.string.document_upload_msg));

                               // MyApplication.showToast(agentsignatureC,"upload success");
                                // callApiUpdateDataApproval();

                                callApiWalletList();


                            } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                                MyApplication.showToast(agentsignatureC,getString(R.string.technical_failure));
                            } else {
                                MyApplication.showToast(agentsignatureC,jsonObject.optString("resultDescription", "N/A"));
                            }
                        }


                    }

                    @Override
                    public void failure(String aFalse) {

                    }
                });
    }




    private void callApiAddAgentDataApproval() {
        try {
            JSONObject jsonObjectn = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonObjectn.put("actionType", "Created");
            jsonObjectn.put("assignTo", "");
            jsonObjectn.put("comments", "");
            jsonObjectn.put("entityCode", OutletKYC.outletWalletOwnerCode);
            jsonObjectn.put("entityName", OutletKYC.etOutletName.getText().toString());
            jsonObjectn.put("featureCode", "100006");
            jsonObjectn.put("status", "U");
            JSONObject ja=new JSONObject();
            jsonObjectn.put("updatedInformation", ja);

            jsonArray.put(jsonObjectn);
            JSONObject data = new JSONObject();
            data.put("dataApprovalList", jsonArray);

            System.out.println("test "+data.toString());

            // MyApplication.showloader(walletowneradduserC,"Please wait!");
            API.POST_REQEST_WH_NEW("ewallet/api/v1/dataApproval", data, new Api_Responce_Handler() {
                @Override
                public void success(JSONObject jsonObject) {
                    System.out.println("BranchDataApproval Bresponse======="+jsonObject.toString());

                    if (jsonObject != null) {
                        if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                            //MyApplication.showToast(agentkycattachedC,jsonObject.optString("resultDescription", "N/A"));
                            MyApplication.showToast(agentsignatureC,getString(R.string.addoutlet_added));
                            Intent intent = new Intent(agentsignatureC, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                            MyApplication.showToast(agentsignatureC,getString(R.string.technical_failure));
                        } else {
                            MyApplication.showToast(agentsignatureC,jsonObject.optString("resultDescription", "N/A"));
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


    private void callApiWalletList() {
        try {
            // MyApplication.showloader(MainActivity.this,"Please wait!");
                API.GET("ewallet/api/v1/walletOwner/merchant/"+OutletKYC.outletWalletOwnerCode,
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();
                            System.out.println("{\"id\":114838,\"code\":\"1000007270\",\"walletOwnerParentCode\":\"1000007190\",\"walletOwnerCategoryCode\":\"100012\",\"ownerName\":\"ccvgbhh\",\"mobileNumber\":\"111114477\",\"businessTypeCode\":\"100000\",\"businessTypeName\":\"Grocerry\",\"idProofNumber\":\"vvvvv\",\"email\":\"sonu.dubey@esteltelecom.com\",\"status\":\"N\",\"state\":\"U\",\"stage\":\"Document\",\"idProofTypeCode\":\"100006\",\"idProofTypeName\":\"OTHER\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100002\",\"notificationName\":\"Both\",\"gender\":\"M\",\"dateOfBirth\":\"2005-01-26\",\"lastName\":\"fffggg\",\"issuingCountryCode\":\"100092\",\"issuingCountryName\":\"Guinea\",\"registerCountryCode\":\"100092\",\"registerCountryName\":\"Guinea\",\"createdBy\":\"104780\",\"creationDate\":\"2023-01-26T11:42:47.105+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"Standard\",\"currencyCode\":\"GNF\",\"walletOwnerCatName\":\"Outlet\",\"requestedSource\":\"AGENT\",\"regesterCountryDialCode\":\"+224\",\"issuingCountryDialCode\":\"+224\",\"walletOwnerCode\":\"1000007270\",\"hasChild\":\"false\",\"currencySymbol\":\"GNF\",\"currencyName\":\"Guinean franc\",\"loginWithOtpRequired\":false,\"timeZone\":\"(UTC+00:00)\",\"associatedWalletOwnerCode\":\"1000007190\",\"associatedWalletOwnerName\":\"new merchant\",\"associatedWalletOwnerNumber\":\"807644460\"}"+jsonObject.toString());
                            // walletownercode=MyApplication.getSaveString("walletOwnerCode", getApplicationContext());


                            try {


                                //    JSONObject jsonObject1 = new JSONObject("{\"transactionId\":\"1927802\",\"requestTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"responseTime\":\"Tue Nov 02 13:03:30 IST 2021\",\"resultCode\":\"0\",\"resultDescription\":\"Transaction Successful\",\"walletOwner\":{\"id\":110679,\"code\":\"1000002785\",\"walletOwnerCategoryCode\":\"100000\",\"ownerName\":\"sharique agent\",\"mobileNumber\":\"9990063618\",\"businessTypeCode\":\"100008\",\"businessTypeName\":\"Goldsmith\",\"lineOfBusiness\":\"gffg\",\"idProofNumber\":\"trt465656\",\"email\":\"sharique9718@gmail.com\",\"status\":\"Active\",\"state\":\"Approved\",\"stage\":\"Document\",\"idProofTypeCode\":\"100005\",\"idProofTypeName\":\"COMPANY REGISTRATION NUMBER\",\"idExpiryDate\":\"2021-10-22\",\"notificationLanguage\":\"en\",\"notificationTypeCode\":\"100000\",\"notificationName\":\"EMAIL\",\"issuingCountryCode\":\"100102\",\"issuingCountryName\":\"India\",\"registerCountryCode\":\"100102\",\"registerCountryName\":\"India\",\"createdBy\":\"100250\",\"modifiedBy\":\"100308\",\"creationDate\":\"2021-10-19T22:38:48.969+0530\",\"modificationDate\":\"2021-11-01T13:49:14.892+0530\",\"walletExists\":true,\"profileTypeCode\":\"100000\",\"profileTypeName\":\"tier1\",\"walletCurrencyList\":[\"100018\",\"100017\",\"100069\",\"100020\",\"100004\",\"100029\",\"100062\",\"100003\"],\"walletOwnerCatName\":\"Institute\",\"requestedSource\":\"ADMIN\",\"regesterCountryDialCode\":\"+91\",\"issuingCountryDialCode\":\"+91\",\"walletOwnerCode\":\"1000002785\"}}");


                                String resultCode = jsonObject.getString("resultCode");
                                String resultDescription = jsonObject.getString("resultDescription");

                                if (resultCode.equalsIgnoreCase("0")) {

                                    JSONObject jsonObject_walletOwner = jsonObject.getJSONObject("walletOwner");


                                    JSONObject jsonObject1=jsonObject_walletOwner;
                                    jsonObject1.put("state","U");
                                    jsonObject1.put("status","N");

                                    callApiWalletListput(jsonObject1);


                                    System.out.println("get json soneeeee"+jsonObject1);


                                } else {
                                    Toast.makeText(OutletSignature.this, resultDescription, Toast.LENGTH_LONG).show();
                                }


                            } catch (Exception e) {
                                Toast.makeText(OutletSignature.this, e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                finish();

                            }





                                            /*   tvName.setText(data.optString("walletOwnerName"));
                                                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
                                                    DecimalFormat df = new DecimalFormat("0.00",symbols);
                                                    tvBalance.setText(	MyApplication.addDecimal(""+data.optDouble("value")) + " " + data.optString("currencySymbol"));
                                                    System.out.println("get value"+data.optString("value"));
*/







                        }





                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();


                        }
                    });

        } catch (Exception e) {

        }

    }


    private void callApiWalletListput(JSONObject jsonObject) {



        API.PUT("ewallet/api/v1/walletOwner/employer/"+ OutletKYC.outletWalletOwnerCode, jsonObject, new Api_Responce_Handler() {
            @Override
            public void success(JSONObject jsonObject) {
                // MyApplication.hideLoader();

                System.out.println("final response======="+jsonObject.toString());

                if (jsonObject != null) {
                    if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                        //MyApplication.showToast(getString(R.string.address_add_msg));

                        callApiAddAgentDataApproval();

                    }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
                        MyApplication.showToast(OutletSignature.this,getString(R.string.technical_failure));
                    } else {
                        MyApplication.showToast(OutletSignature.this,jsonObject.optString("resultDescription", "N/A"));
                    }
                }
            }

            @Override
            public void failure(String aFalse) {

            }
        });



    }

}