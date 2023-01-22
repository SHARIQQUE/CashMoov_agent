package com.agent.cashmoovui.wallet_owner.branch;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;

import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class BranchKYCAttached extends AppCompatActivity implements View.OnClickListener {

    public static BranchKYCAttached branchkycattachedC;
    TextView spIdProof,tvNext;
    Button btnFrontUpload,btnBackUpload,btnOtherDocUpload;
    SpinnerDialog spinnerDialogIdProofType;
    ImageButton btnFront,btnBack,btnOtherDoc;
    EditText etFront,etBack,etOtherDoc,etProofNo;
//    private ArrayList<String> idProofTypeList = new ArrayList<>();
//    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList=new ArrayList<>();
    static final int REQUEST_IMAGE_CAPTURE_ONE = 1;
    static final int REQUEST_IMAGE_CAPTURE_TWO = 2;
    static final int REQUEST_IMAGE_CAPTURE_THREE = 3;
    public static final int RESULT_CODE_FAILURE = 10;
    private Intent Data;
    Uri tempUriFront,tempUriBack,tempUriOther;
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_branch_doc);
        branchkycattachedC = this;
        getIds();
    }

    private void getIds() {
        spIdProof = findViewById(R.id.spIdProof);
        tvNext = findViewById(R.id.tvNext);
        btnFront = findViewById(R.id.btnFront);
        etFront = findViewById(R.id.etFront);
        btnBack = findViewById(R.id.btnBack);
        etBack = findViewById(R.id.etBack);
        btnOtherDoc = findViewById(R.id.btnOtherDoc);
        etOtherDoc = findViewById(R.id.etOtherDoc);
        etProofNo = findViewById(R.id.etProofNo);
        btnFrontUpload = findViewById(R.id.btnFrontUpload);
        btnBackUpload = findViewById(R.id.btnBackUpload);
        btnOtherDocUpload = findViewById(R.id.btnOtherDocUpload);
        btnFrontUpload.setVisibility(View.GONE);
        btnBackUpload.setVisibility(View.GONE);
        btnOtherDocUpload.setVisibility(View.GONE);

//        spIdProof.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (spinnerDialogIdProofType!=null)
//                    spinnerDialogIdProofType.showSpinerDialog();
//            }
//        });

        setOnCLickListener();

       // callApiIdProofType();

    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(branchkycattachedC);
        btnFront.setOnClickListener(branchkycattachedC);
        btnBack.setOnClickListener(branchkycattachedC);
        btnOtherDoc.setOnClickListener(branchkycattachedC);
        btnFrontUpload.setOnClickListener(branchkycattachedC);
        btnBackUpload.setOnClickListener(branchkycattachedC);
        btnOtherDocUpload.setOnClickListener(branchkycattachedC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.btnFront:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_ONE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }

                break;

            case R.id.btnBack:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_TWO);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
                break;

            case R.id.btnOtherDoc:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_THREE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
                break;

            case R.id.btnFrontUpload:
                check = 1;
                filesUploadFront();
                break;

            case R.id.btnBackUpload:
                check = 2;
                filesUploadBack();
                break;

            case R.id.btnOtherDocUpload:
                check = 3;
                filesUploadOther();
                break;

            case R.id.tvNext:
//                if(spIdProof.getText().toString().equals(getString(R.string.valid_select_id_proof))) {
//                    MyApplication.showErrorToast(branchkycattachedC,getString(R.string.val_select_id_proof));
//                    return;
//                }
//                if(etProofNo.getText().toString().trim().isEmpty()) {
//                    MyApplication.showErrorToast(branchkycattachedC,getString(R.string.val_proof_no));
//                    return;
//                }

                if(!isFrontUpload){
                    MyApplication.showTipError(this,getString(R.string.uploadimage),btnFrontUpload);
                    MyApplication.hideKeyboard(branchkycattachedC);
                    return;
                }

                if(!isBackUpload){
                    MyApplication.showTipError(this,getString(R.string.uploadbackimage),btnBackUpload);
                    MyApplication.hideKeyboard(branchkycattachedC);
                    return;
                }

//                if(!isOtherUpload){
//                    MyApplication.showTipError(this,"Please upload other Image",btnOtherDocUpload);
//                    MyApplication.hideKeyboard(branchkycattachedC);
//                    return;
//                }

                intent = new Intent(branchkycattachedC, BranchSignature.class);
                //intent.putExtra("CHECKINTENTSETPIN","BranchKYC");
                startActivity(intent);
                finish();

                break;
        }
    }

    File fileFront,fileBack,fileOther;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE_ONE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Data = data;

                tempUriFront = getImageUri(getApplicationContext(), imageBitmap);

                etFront.setText(tempUriFront.getLastPathSegment());

                fileFront = new File(getRealPathFromURI(tempUriFront).toString());
                int file_size = Integer.parseInt(String.valueOf(fileFront.length() / 1024));     //calculate size of image in KB
                if (file_size <= 100){
                   // isFrontUpload=true;
                    btnFrontUpload.setVisibility(View.VISIBLE);
                }else {
                    MyApplication.showErrorToast(branchkycattachedC,"File size exceeds");
                }
                //  btnFrontUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                File file = new File(getRealPathFromURI(tempUriFront));
//                System.out.println(file);

            } else if (resultCode == RESULT_CANCELED) {
                MyApplication.showToast(branchkycattachedC,"User Canceled");
            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(branchkycattachedC,"Failed");
            }

        }
        if (requestCode == REQUEST_IMAGE_CAPTURE_TWO) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Data = data;

                tempUriBack = getImageUri(getApplicationContext(), imageBitmap);

                etBack.setText(tempUriBack.getLastPathSegment());

                fileBack = new File(getRealPathFromURI(tempUriBack).toString());
                int file_size = Integer.parseInt(String.valueOf(fileBack.length() / 1024));     //calculate size of image in KB
                if (file_size <= 100){
                    //isBackUpload=true;
                    btnBackUpload.setVisibility(View.VISIBLE);
                }else {
                    MyApplication.showErrorToast(branchkycattachedC,"File size exceeds");
                }
                //btnBackUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                // File file = new File(getRealPathFromURI(tempUri));

            } else if (resultCode == RESULT_CANCELED) {
                MyApplication.showToast(branchkycattachedC,"User Canceled");
            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(branchkycattachedC,"Failed");
            }

        }

        if (requestCode == REQUEST_IMAGE_CAPTURE_THREE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Data = data;

                tempUriOther = getImageUri(getApplicationContext(), imageBitmap);

                etOtherDoc.setText(tempUriOther.getLastPathSegment());

                fileOther = new File(getRealPathFromURI(tempUriOther).toString());
                int file_size = Integer.parseInt(String.valueOf(fileOther.length() / 1024));     //calculate size of image in KB
                if (file_size <= 100){
                    //isBackUpload=true;
                    btnOtherDocUpload.setVisibility(View.VISIBLE);
                }else {
                    MyApplication.showErrorToast(branchkycattachedC,"File size exceeds");
                }
                //btnBackUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                // File file = new File(getRealPathFromURI(tempUri));

            } else if (resultCode == RESULT_CANCELED) {
                MyApplication.showToast(branchkycattachedC,"User Canceled");
            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(branchkycattachedC,"Failed");
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title"+ Calendar.getInstance().getTime(), null);
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

    public boolean isFrontUpload=false;
    public boolean isBackUpload=false;
    public boolean isOtherUpload=false;
    public File filesUploadFront() {
        File file = new File(getRealPathFromURI(tempUriFront).toString());
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));     //calculate size of image in KB
        if (file_size <= 100){
            isFrontUpload=true;

            callupload(file,"100041");
        }else {
            MyApplication.showErrorToast(branchkycattachedC,"File size exceeds");
        }
        return file;
    }

    public File filesUploadBack() {
        File file = new File(getRealPathFromURI(tempUriBack).toString());
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));     //calculate size of image in KB
        if (file_size <= 100){
            isBackUpload=true;
            callupload(file,"100042");
        }else {
            MyApplication.showErrorToast(branchkycattachedC,"File size exceeds");
        }
        return file;
    }

    public File filesUploadOther() {
        File file = new File(getRealPathFromURI(tempUriOther).toString());
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));     //calculate size of image in KB
        if (file_size <= 100){
            isOtherUpload=true;
            callupload(file,"100047");
        }else {
            MyApplication.showErrorToast(branchkycattachedC,"File size exceeds");
        }
        return file;
    }

//    private void callApiIdProofType() {
//        try {
//            API.GET_PUBLIC("ewallet/public/idProofType/all",
//                    new Api_Responce_Handler() {
//                        @Override
//                        public void success(JSONObject jsonObject) {
//                            MyApplication.hideLoader();
//
//                            if (jsonObject != null) {
//                                idProofTypeList.clear();
//                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
//                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("idProofTypeList");
//                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
//                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
//                                        idProofTypeModelList.add(new IDProofTypeModel.IDProofType(
//                                                data.optInt("id"),
//                                                data.optString("code"),
//                                                data.optString("type"),
//                                                data.optString("status"),
//                                                data.optString("creationDate")
//
//                                        ));
//
//                                        idProofTypeList.add(data.optString("type").trim());
//
//                                    }
//
//                                    spinnerDialogIdProofType = new SpinnerDialog(branchkycattachedC, idProofTypeList, getString(R.string.val_valid_select_id_proofnew), R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
//                                    spinnerDialogIdProofType.setCancellable(true); // for cancellable
//                                    spinnerDialogIdProofType.setShowKeyboard(false);// for open keyboard by default
//                                    spinnerDialogIdProofType.bindOnSpinerListener(new OnSpinerItemClick() {
//                                        @Override
//                                        public void onClick(String item, int position) {
//                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
//                                            spIdProof.setText(item);
//                                            spIdProof.setTag(position);
//                                        }
//                                    });
//
//                                } else {
//                                    MyApplication.showToast(branchkycattachedC,jsonObject.optString("resultDescription", "N/A"));
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void failure(String aFalse) {
//                            MyApplication.hideLoader();
//
//                        }
//                    });
//
//
//        } catch (Exception e) {
//
//        }
//
//    }

    JSONObject documentUploadJsonObj;
    private void callupload(File file,String idProofTypeCode) {

        MyApplication.showloader(branchkycattachedC, "uploading file...");
        //idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode()
        API.Upload_REQEST_WH_NEW("ewallet/api/v1/fileUpload",file, idProofTypeCode,
                BranchKYC.branchWalletOwnerCode,
                new Api_Responce_Handler() {

                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if (jsonObject != null) {
                            System.out.println("get json"+jsonObject);
                            if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                //MyApplication.showToast(getString(R.string.document_upload_msg));
                                documentUploadJsonObj=jsonObject;
                                MyApplication.showToast(branchkycattachedC,getString(R.string.upload_success_toast));
                                if(check==1){
                                    btnFrontUpload.setVisibility(View.GONE);
                                }if(check==2){
                                    btnBackUpload.setVisibility(View.GONE);
                                }if(check==3){
                                    btnOtherDocUpload.setVisibility(View.GONE);
                                }

                                // callApiUpdateDataApproval();


                            } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                                MyApplication.showToast(branchkycattachedC,getString(R.string.technical_failure));
                            } else {
                                MyApplication.showToast(branchkycattachedC,jsonObject.optString("resultDescription", "N/A"));
                            }
                        }


                    }

                    @Override
                    public void failure(String aFalse) {

                    }
                });
    }

    int doubleBackToExitPressed = 1;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            Intent intent = new Intent(BranchKYCAttached.this, WalletOwnerMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else {
            doubleBackToExitPressed++;
            Toast.makeText(this, R.string.you_will_loose, Toast.LENGTH_SHORT).show();
        }

    }


}