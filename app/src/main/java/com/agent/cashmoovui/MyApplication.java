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
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.ConnectionQualityChangeListener;
import com.agent.cashmoovui.login.PhoneNumberRegistrationScreen;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    private static KProgressHUD hud;
    public static MyApplication appInstance;
    public static String lang;

    private SharedPreferences mSharedPreferences;
    private String PREF_NAME = "cashmoove_sh";

    public static String ImageURL;


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
        Intent intent = new Intent(getInstance(), PhoneNumberRegistrationScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;

        ImageURL="http://202.131.144.129:8081/ewallet/api/v1/fileUpload/download/" +
                getSaveString("walletOwnerCode",appInstance)+"/";

        AndroidNetworking.initialize(getApplicationContext());
       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        AndroidNetworking.setBitmapDecodeOptions(options);*/
        AndroidNetworking.enableLogging();
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BASIC);
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
                            .header("source","SUBSCRIBER")
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
        toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL, 0, 0);
        View view = toast.getView();
        // view.getBackground().setColorFilter(appInstance.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        view.setBackgroundResource(R.drawable.error_toast);
        TextView text = view.findViewById(android.R.id.message);
        text.setPadding(20,10,20,10);
        text.setTextSize(13);
        text.setTextColor(ContextCompat.getColor(activity, R.color.white));
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
}
