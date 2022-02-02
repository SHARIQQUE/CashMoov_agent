package com.agent.cashmoovui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.model.transaction.CurrencyModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.ConnectionQualityChangeListener;
import com.agent.cashmoovui.login.PhoneNumberRegistrationScreen;
import com.balsikandar.crashreporter.CrashReporter;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


public class MyApplication extends Application {

    public static String currencySymbol;
    public static boolean AgentPage=false;
    public static boolean InstPage=false;
    public static boolean BranchPage=false;
    public static String Amount="0.00";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private static final String TAG = MyApplication.class.getSimpleName();
    public static String UserMobile;
    private static KProgressHUD hud;
    public static MyApplication appInstance;
    public static String lang;

    public static String setProtection;



    private SharedPreferences mSharedPreferences;
    private String PREF_NAME = "cashmoove_sh";

    public static String ImageURL;
    public static String AgentCode = "100002";
    public static String BranchCode = "100001";
    public static String InstituteCode = "100000";
    public static String MerchatCode = "100011";
    public static String OutletCode = "100012";
    public static String SubscriberCode = "100010";
    public static String channelTypeCode = "100000";

    public static ArrayList<CurrencyModel> currencyModelArrayList=new ArrayList<>();
    public static ArrayList<CurrencyModel> currencyModelArrayList_temp=new ArrayList<>();


    public SharedPreferences getmSharedPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        }
        return mSharedPreferences;
    }


    public static MyApplication getInstance() {
        return appInstance;
    }

    public  void callLogin() {
        saveBool("isLogin",false,getInstance());
        MyApplication.saveBool("FirstLogin",false,getInstance());
        // MyApplication.saveString("ImageName", "1", LogoutC);



        getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();


        Intent intent = new Intent(getInstance(), LoginMsis.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;


        ImageURL= API.BASEURL+"ewallet/api/v1/fileUpload/download/" +
                getSaveString("walletOwnerCode",appInstance)+"/";

        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", appInstance);
        AndroidNetworking.initialize(getApplicationContext());

       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        AndroidNetworking.setBitmapDecodeOptions(options);*/
        AndroidNetworking.enableLogging();
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BASIC);

        CrashReporter.initialize(this);

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        AndroidNetworking.setConnectionQualityChangeListener(new ConnectionQualityChangeListener() {
            @Override
            public void onChange(ConnectionQuality currentConnectionQuality, int currentBandwidth) {
                Log.d(TAG, "onChange: currentConnectionQuality : " + currentConnectionQuality + " currentBandwidth : " + currentBandwidth);
            }
        });
    }

    public static OkHttpClient okClient = new OkHttpClient.Builder()
            .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY))
            .authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    return response.request().newBuilder()
                            .header("source","AGENT")
                            .header("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                            .build();
                }
            })
            .build();

    public static OkHttpClient okClientfileUpload = new OkHttpClient.Builder()
            .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BASIC))
            .authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    return response.request().newBuilder()
                            .header("source","SUBSCRIBER")
                            .header("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                            .build();
                }
            })
            .build();

    public static String doubleRound(double value){
        return String.format("%.2f", value);
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void showTipError(Activity activity,String msg,View v){
        ViewTooltip
                .on(activity, v)
                .autoHide(true, 2000)
                .clickToHide(true)
                .corner(30)
                .color(Color.RED)
                .position(ViewTooltip.Position.TOP)
                .text(msg)
                .show();
    }

    public static void showAPIToast(String message){
        Toast toast= Toast.makeText(getInstance(),
                "   "+message+"  ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(Activity activity, String message){
        Toast toast= Toast.makeText(activity,
                "   "+message+"  ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
//        View view = toast.getView();
//        // view.getBackground().setColorFilter(appInstance.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//        view.setBackgroundResource(R.drawable.success_toast);
//        TextView text = view.findViewById(android.R.id.message);
//        text.setPadding(20,10,20,10);
//        text.setTextSize(13);
//        text.setTextColor(ContextCompat.getColor(activity, R.color.white));
        toast.show();
    }



    public static void showErrorToast(Activity activity, String message){

        Toast toast= Toast.makeText(activity, message, Toast.LENGTH_SHORT);

      /*  toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL, 0, 0);
        View view = toast.getView();
        // view.getBackground().setColorFilter(appInstance.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        view.setBackgroundResource(R.drawable.error_toast);
        TextView text = view.findViewById(android.R.id.message);
        text.setPadding(20,10,20,10);
        text.setTextSize(13);
        text.setTextColor(ContextCompat.getColor(activity, R.color.white));*/
        toast.show();
    }


    public static void showloader(Activity activity, String message){
        hud = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                // .setDetailsLabel("Downloading data")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void hideLoader(){
        if(hud!=null){
            hud.dismiss();
        }
    }

    public static boolean isEmail(String email) {
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(email).matches();
    }

    public static boolean email_validation(String email) {
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(email).matches();
    }

    public boolean isPassword(String pass) {
        return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{6,}$").matcher(pass).matches();
    }

    public static void saveString(String key, String value, Context activity) {
        SharedPreferences preferences =activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSaveString(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        String json = preferences.getString(key,"");
        return json;
    }


    public static void saveInt(String key, int value, Context activity) {
        SharedPreferences preferences =activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static Integer getSaveInt(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        int json = preferences.getInt(key,0);
        return json;
    }
    public static void saveBool(String key, Boolean value, Context activity) {
        SharedPreferences preferences =activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getSaveBool(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        Boolean json = preferences.getBoolean(key,false);
        return json;
    }



    public  static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                        /*if(getSpeed()){
                            return true;
                        }else{
                            return false;
                        }*/

                    }
                }
            }
        }
        return false;
    }

    public static String addDecimal(String number){

        if(number.contains(".00")){
            return number;
        }
        if(number.contains(".0")){
            return number+"0";
        }
        if(!number.contains(".")){
            return number+".00";
        }


        return number;
    }

    public  static void setrequired(TextView textView,String str){
        TextView fname_label=textView;
        String t=str+appInstance.getString(R.string.required_asterisk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fname_label.setText( Html.fromHtml(t,Html.FROM_HTML_MODE_LEGACY));
        } else {
            fname_label.setText(Html.fromHtml(t), TextView.BufferType.SPANNABLE);
        }

    }

    public static String getUniqueId(){
        String android_id = Settings.Secure.getString(MyApplication.appInstance.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("Android==","Android ID : "+android_id);

        // return  "SA2132425277828";
        return  android_id;

    }

    public static void setLang(Context context){
        lang =  getSaveString("Locale", context);
        if(lang!=null && !MyApplication.lang.isEmpty()){
            if(lang.equalsIgnoreCase("en")){
                changeLocale(context,lang);
                MyApplication.saveString("Locale", lang, context);
            }else{
                changeLocale(context,lang);
                MyApplication.saveString("Locale", lang, context);
            }

            //change to fr
        }else{
            changeLocale(context,"en");
            MyApplication.saveString("Locale", "en", context);
        }
    }

    public static void changeLocale(Context context, String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);//Set Selected Locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());//Update the config
    }

    public static JSONObject ServiceConfiguration;
    public static void setService(JSONArray serviceListArray){

        ServiceConfiguration=new JSONObject();
        for(int i=0;i<serviceListArray.length();i++){
            JSONObject object=serviceListArray.optJSONObject(i);
            JSONArray serviceCategoryListArray=object.optJSONArray("serviceCategoryList");
            for(int j=0;j<serviceCategoryListArray.length();j++){
                try {
                    JSONObject data=serviceCategoryListArray.optJSONObject(j);
                    //Sell Float
                    if(data.optString("code").equalsIgnoreCase("100016")){//Sell Float
                        ServiceConfiguration.put("isSellFloat",true);
                    }
                    //Float Transfer
                    if(data.optString("code").equalsIgnoreCase("100017")){
                        ServiceConfiguration.put("isFloatTransfer",true);
                    }
                    //Recharge & Payment
                    if(data.optString("code").equalsIgnoreCase("100028")){
                        ServiceConfiguration.put("isRechargePayment",true);
                    }
                    //Receive Remittance
                    if(data.optString("code").equalsIgnoreCase("100018")){
                        ServiceConfiguration.put("isReceiveRemittance",true);
                    }
                    //Cash to Wallet
                    if(data.optString("code").equalsIgnoreCase("100061")){
                        ServiceConfiguration.put("isCashtoWallet",true);
                    }
                    //Send Remittance
                    if(data.optString("code").equalsIgnoreCase("100001")){
                        ServiceConfiguration.put("isSendRemittance",true);
                    }
                    //Mobile Prepaid
                    if(data.optString("code").equalsIgnoreCase("100021")){
                        ServiceConfiguration.put("isMobilePrepaid",true);
                    }
                    //Transaction Details
                    if(data.optString("code").equalsIgnoreCase("TRNSDT")){
                        ServiceConfiguration.put("isTransactionDetails",true);
                    }
                    //Overdraft Transfer
                    if(data.optString("code").equalsIgnoreCase("ODTRF")){
                        ServiceConfiguration.put("isOverdraftTransfer",true);
                    }
                    //Commission Transfer
                    if(data.optString("code").equalsIgnoreCase("COMTRF")){
                        ServiceConfiguration.put("isCommissionTransfer",true);
                    }

                }catch (Exception e){

                }



            }

        }

        System.out.println("ServiceConfiguration   "+ServiceConfiguration.toString());

    }



    public static void biometricAuth(Activity activity, BioMetric_Responce_Handler bioMetric_responce_handler){

        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(activity);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:

                // msgText.setText("You can use the fingerprint sensor to login");
                // msgText.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                bioMetric_responce_handler.failure(activity.getString(R.string.no_fingerprint_senser));
                //msgText.setText(getString(R.string.no_fingerprint_senser));
                //tvFinger.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                bioMetric_responce_handler.failure(activity.getString(R.string.no_biometric_senser));
              /*  msgText.setText(getString(R.string.no_biometric_senser));
                tvFinger.setVisibility(View.GONE);*/
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                bioMetric_responce_handler.failure(activity.getString(R.string.device_not_contain_fingerprint));

                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(activity);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                // tvFinger.setText("Login Successful");

                System.out.println("Biomatric   =>"+result.toString());
                bioMetric_responce_handler.success("Call API");

               /* Intent intent = new Intent(loginpinC, MainActivity.class);
                startActivity(intent);*/
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CASHMOOV")
                .setDescription(activity.getString(R.string.use_finger_to_transaction)).setNegativeButtonText(activity.getString(R.string.cancel)).build();

        biometricPrompt.authenticate(promptInfo);


    }
}
