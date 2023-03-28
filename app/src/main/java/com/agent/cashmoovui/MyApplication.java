package com.agent.cashmoovui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.media.MediaMetadataEditor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.BioMetric_Responce_Handler;
import com.agent.cashmoovui.apiCalls.CommonData;
import com.agent.cashmoovui.cash_in.CashIn;
import com.agent.cashmoovui.login.LoginMsis;
import com.agent.cashmoovui.login.LoginPin;
import com.agent.cashmoovui.model.transaction.CurrencyModel;

import com.agent.cashmoovui.set_pin.AESEncryption;
import com.aldoapps.autoformatedittext.AutoFormatUtil;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


public class MyApplication extends Application {
    public static boolean ipEnable = false;
    public static String currencySymbol;
    public static boolean PasswordEncription = true;

    public static boolean AgentPage = false;
    public static boolean InstPage = false;
    public static boolean BranchPage = false;
    public static boolean MerchantPage = false;
    public static boolean OutletPage = false;

    public static String Amount = "0.00";
    public static boolean isNotification = false;
    public static TinyDB tinyDB;
    public static boolean showCashIn = false;
    public static boolean showCashOut = false;
    public static boolean showPayment = false;
    public static boolean showRemittance = false;
    public static boolean showToSubscriber=false;
    public static boolean showToNonSubscriber=false;
    public static boolean showInternationalRemit=false;

    public static boolean showSendRemit = false;
    public static boolean showReceiveRemit = false;
    public static boolean showCashtoWallet = false;
    public static boolean showCreditPurchase = false;
    public static boolean showTransfer = false;
    public static boolean showSellFloat = false;
    public static boolean showTransferFloat = false;
    public static boolean showTransferCommission = false;
    public static int ToCashInMinAmount;
    public static int ToCashInMaxAmount;
    public static int ToCashOutMinAmount;
    public static int ToCashOutMaxAmount;
    public static int ToCashWalletMinAmount;
    public static int ToCashWalletMaxAmount;


    public static int ToCreditPurchaseMinAmount;
    public static int ToCreditPurchaseMaxAmount;
    public static int ToSelfFloatMinAmount;
    public static int ToSelfFloatMaxAmount;
    public static int ToTransferFloatMinAmount;
    public static int ToTransferFloatMaxAmount;
    public static int ToCommisionTransferMinAmount;
    public static int ToCommisionTransferMaxAmount;
    public static int RemittanceMinValue;
    public static int RemittanceMaxValue;
    public static int RecRemittanceMinValue;
    public static int RecRemittanceMaxValue;
    public static int CashToWalletMinValue;
    public static int CashToWalletMaxValue;
    public static String userCodeTransaction;

    public static int mobileLengthnormal=10;

    public static  String checkWalletTypeCode="100008";
    public static long mLastClickTime=0;
    public static String parentTransID="";

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
    public static String    BranchCode = "100001";
    public static String InstituteCode = "100000";
        public static String MerchatCode = "100011";
    public static String OutletCode = "100012";
    public static String SubscriberCode = "100010";
    public static String channelTypeCode = "100000";
    public static boolean isContact=false;
    public static boolean isFirstTime=false;
    public static int amountLength=13;
    public static int mobileLengthinternational=14;

    public static int ToReceiveMoneyMinAmount;
    public static int ToReceiveMoneyMaxAmount;
    public static int amountLengthpartners=10;

    public static ArrayList<CurrencyModel> currencyModelArrayList = new ArrayList<>();
    public static ArrayList<CurrencyModel> currencyModelArrayList_temp = new ArrayList<>();

    String languageToUse = "";




    public SharedPreferences getmSharedPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        }
        return mSharedPreferences;
    }


    public static MyApplication getInstance() {
        return appInstance;
    }

    public void callLogin() {
        saveBool("isLogin", false, getInstance());
        MyApplication.saveBool("FirstLogin", false, getInstance());
        // MyApplication.saveString("ImageName", "1", LogoutC);


        getmSharedPreferences().edit().putString("isFirstRun", "YES").commit();


        Intent intent = new Intent(getInstance(), LoginMsis.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public   void callLogout() {
        //saveBool("isLogin",false,getInstance());
        Toast.makeText(getApplicationContext(), R.string.time_out_timer,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getInstance(), LoginPin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public static void setLocale( String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = appInstance.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        tinyDB = new TinyDB(appInstance);
        setLocale("fr");

        System.out.println("tetstshjlashhsa"+android.os.Build.VERSION.SDK_INT);
        languageToUse = appInstance.getmSharedPreferences().getString("languageToUse", "");

        if (languageToUse.trim().length() == 0) {
            MyApplication.changeLocale(appInstance, "fr");
            MyApplication.saveString("Locale", "fr", appInstance);
            appInstance.getmSharedPreferences().edit().putString("languageToUse", "fr").commit();


        }else {
            MyApplication.changeLocale(appInstance, languageToUse);
            MyApplication.saveString("Locale", languageToUse, appInstance);


        }

        ImageURL = API.BASEURL + "ewallet/api/v1/fileUpload/download/" +
                getSaveString("walletOwnerCode", appInstance) + "/";

        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", appInstance);
        System.out.println("get value"+MyApplication.setProtection);
        if (MyApplication.setProtection.equalsIgnoreCase("Activate")|| MyApplication.setProtection.equalsIgnoreCase("Deactivate")) {

        } else {
            MyApplication.saveString("ACTIVATEPROTECTION", "Activate", appInstance);
            MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", appInstance);

        }
        //if( MyApplication.setProtection==)

        AndroidNetworking.initialize(getApplicationContext());

       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        AndroidNetworking.setBitmapDecodeOptions(options);*/
        AndroidNetworking.enableLogging();
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BASIC);

        CrashReporter.initialize(this);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
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
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY))
            .authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    return response.request().newBuilder()
                            .header("source", "AGENT")
                            .header("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                            .build();
                }
            })
            .build();

    public static OkHttpClient okClientfileUpload = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new okhttp3.logging.HttpLoggingInterceptor().setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BASIC))
            .authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    return response.request().newBuilder()
                            .header("source", "SUBSCRIBER")
                            .header("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                            .build();
                }
            })
            .build();

    public static String doubleRound(double value) {
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

    public static void showTipError(Activity activity, String msg, View v) {
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



    public static void showAPIToast(String message) {
        Toast toast = Toast.makeText(getInstance(),
                "   " + message + "  ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void showToastNew(Context activity, String message) {
        Toast toast = Toast.makeText(activity,
                "   " + message + "  ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
//        View view = toast.getView();
//        // view.getBackground().setColorFilter(appInstance.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//        view.setBackgroundResource(R.drawable.success_toast);
//        TextView text = view.findViewById(android.R.id.message);
//        text.setPadding(20,10,20,10);
//        text.setTextSize(13);
//        text.setTextColor(ContextCompat.getColor(activity, R.color.white));
        toast.show();
    }



    public static void showToast(Activity activity, String message) {
        Toast toast = Toast.makeText(activity,
                "   " + message + "  ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
//        View view = toast.getView();
//        // view.getBackground().setColorFilter(appInstance.getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//        view.setBackgroundResource(R.drawable.success_toast);
//        TextView text = view.findViewById(android.R.id.message);
//        text.setPadding(20,10,20,10);
//        text.setTextSize(13);
//        text.setTextColor(ContextCompat.getColor(activity, R.color.white));
        toast.show();
    }


    public static void showErrorToast(Activity activity, String message) {

        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);

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


    public static void showloader(Activity activity, String message) {
        //        ImageView imageView = new ImageView(activity);
//        imageView.setBackgroundResource(R.drawable.spin_animation);
//        AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground();
//        drawable.start();
        hud = KProgressHUD.create(activity)
                //.setCustomView(imageView)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setMaxProgress(100)
                // .setDetailsLabel("Downloading data")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        hud.setProgress(90);
    }

    public static void hideLoader() {
        if (hud != null) {
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
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSaveString(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        String json = preferences.getString(key, "");
        return json;
    }


    public static void saveInt(String key, int value, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static Integer getSaveInt(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        int json = preferences.getInt(key, 0);
        return json;
    }

    public static void saveBool(String key, Boolean value, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getSaveBool(String key, Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE);
        Boolean json = preferences.getBoolean(key, false);
        return json;
    }


    public static boolean isConnectingToInternet(Context context) {
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

    public static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
public static String addDecimal(String number) {
        String data="0.00";

       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")||
                MyApplication.getSaveString("Locale", MyApplication.getInstance()).isEmpty()) {
            DecimalFormat df = new DecimalFormat("0.00", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
             data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }else{
            if(number.equalsIgnoreCase("0")){
                number="0.00";
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(',');
                symbols.setGroupingSeparator('.');
                NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.00#", symbols);
                data = goodNumberFormat1.format(Double.parseDouble(number));

            }else{
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(',');
                symbols.setGroupingSeparator('.');
                NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.00#", symbols);
                data = goodNumberFormat1.format(Double.parseDouble(number));
            }

        }
        return data;
    }






    public static int prevCommaAmount;
    public static String formatInput(CharSequence s, int start, int count) {


        StringBuilder sbResult = new StringBuilder();
        String result;
        int newStart = start;

        try {
            // Extract value without its comma
            String digitAndDotText = s.toString().replace(",", "");
            int commaAmount = 0;




            if (digitAndDotText.contains(".")) {
                // escape sequence for .
                String[] wholeText = digitAndDotText.split("\\.");



                // in 150,000.45 non decimal is 150,000 and decimal is 45
                String nonDecimal = wholeText[0];


                // only format the non-decimal value
                result = AutoFormatUtil.formatToStringWithoutDecimal(nonDecimal);

                sbResult
                        .append(result)
                        .append(".");

                if (wholeText.length > 1) {
                    sbResult.append(wholeText[1]);
                }

            } else {
                result = AutoFormatUtil.formatWithDecimal(digitAndDotText);
                sbResult.append(result);
            }

            // count == 0 indicates users is deleting a text
            // count == 1 indicates users is entering a text
            newStart += ((count == 0) ? 0 : 1);

            // calculate comma amount in edit text
            commaAmount += AutoFormatUtil.getCharOccurance(result, ',');

            // flag to mark whether new comma is added / removed
            if (commaAmount >= 1 && prevCommaAmount != commaAmount) {
                newStart += ((count == 0) ? -1 : 1);
                prevCommaAmount = commaAmount;
            }

            // case when deleting without comma
            if (commaAmount == 0 && count == 0 && prevCommaAmount != commaAmount) {
                newStart -= 1;
                prevCommaAmount = commaAmount;
            }

            // case when deleting without dots
            if (count == 0 && !sbResult.toString()
                    .contains(".") && prevCommaAmount != commaAmount) {
                newStart = start;
                prevCommaAmount = commaAmount;
            }

            //editText.setText(sbResult.toString());

            // ensure newStart is within result length
            if (newStart > sbResult.toString().length()) {
                newStart = sbResult.toString().length();
            } else if (newStart < 0) {
                newStart = 0;
            }

           // editText.setSelection(newStart);
            return sbResult.toString();

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return sbResult.toString();
    }


    public static String addDecimalfiveinternatonal(String number) {
        String data="00.000";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.00000#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));

        }else{
            DecimalFormat df = new DecimalFormat("00.00000", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }
        return data;


       /* DecimalFormat df = new DecimalFormat("0.000",symbols);
        return df.format(Double.parseDouble(number));*/
    }

    public static String addDecimalfive(String number) {
        String data="00.000";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.00000#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));

        }else{
            DecimalFormat df = new DecimalFormat("00.00000", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }
        return data;


       /* DecimalFormat df = new DecimalFormat("0.000",symbols);
        return df.format(Double.parseDouble(number));*/
    }


    public static String addDecimalthreenew(String number) {
        String data="00.000";
       /* DecimalFormat df = new DecimalFormat("0.00", symbols);
        System.out.println(("get datatype" + (Object) number).getClass().getName());
        data = formatInput(df.format(Double.parseDouble(number)), 0, 0);*/
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            NumberFormat goodNumberFormat1 = new DecimalFormat("#,##0.000#", symbols);
            data = goodNumberFormat1.format(Double.parseDouble(number));

        }else{
            DecimalFormat df = new DecimalFormat("00.000", symbols);
            System.out.println(("get datatype" + (Object) number).getClass().getName());
            data = formatInput(df.format(Double.parseDouble(number)), 0, 0);
        }
        return data;


       /* DecimalFormat df = new DecimalFormat("0.000",symbols);
        return df.format(Double.parseDouble(number));*/
    }

    public static String addDecimalthree(String number) {

        DecimalFormat df = new DecimalFormat("0.000",symbols);


        return df.format(Double.parseDouble(number));
    }



    public static void setrequired(TextView textView, String str) {
        TextView fname_label = textView;
        String t = str + appInstance.getString(R.string.required_asterisk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fname_label.setText(Html.fromHtml(t, Html.FROM_HTML_MODE_LEGACY));
        } else {
            fname_label.setText(Html.fromHtml(t), TextView.BufferType.SPANNABLE);
        }

    }

    public static String getUniqueId() {
        String android_id = Settings.Secure.getString(MyApplication.appInstance.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("Android==", "Android ID : " + android_id);

        // return  "SA2132425277828";
        return android_id;

    }

    public static void setLang(Context context) {
        lang = getSaveString("Locale", context);
        if (lang != null && !MyApplication.lang.isEmpty()) {
            if (lang.equalsIgnoreCase("en")) {
                changeLocale(context, lang);
                MyApplication.saveString("Locale", lang, context);
            } else {
                changeLocale(context, lang);
                MyApplication.saveString("Locale", lang, context);
            }

            //change to fr
        } else {
            changeLocale(context, "en");
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


    String dateformat = "Thu Aug 04 09:38:31 UTC 2022";


    public static String convertUTCToLocaldate(String Date) {


      /*  java.util.Date date = new Date(Date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String format = formatter.format(date);



        System.out.println("get date" + format);
            return  format;*/

        return Date;
        }




    public static String convertUTCToLocalTime(String Date){
        String dateStr = Date;
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        //
        //df.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");

        java.util.Date date1 = null;
        try {
            date1 = df1.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        android.text.format.DateFormat df2 = new android.text.format.DateFormat();



        String df_medium_us_str= (String) df2.format("dd-MMM-yyyy", date1);
       /* if(isToday(date1)){
            String df_medium_us_str= (String) df2.format("hh:mm:ss a", date1);
            return "Today, "+df_medium_us_str;
        }

        if(isYesterday(date1)){
            String df_medium_us_str= (String) df2.format("hh:mm:ss a", date1);
            return "Yesterday, "+df_medium_us_str;
        }
        String df_medium_us_str= (String) df2.format("dd-MMM-yyyy hh:mm:ss a", date1);*/
        return  df_medium_us_str;
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

    public static int bioMetricCounter=0;
    public static BiometricPrompt biometricPrompt=null;
    public static Activity activityNew;
    public static boolean isCancelCalled=false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void biometricAuth(Activity activity, BioMetric_Responce_Handler bioMetric_responce_handler){
        isCancelCalled=false;
        if (activity.getSystemService(Context.FINGERPRINT_SERVICE) == null) {
            bioMetric_responce_handler.failure("");
            return;
        }
        MyApplication.setProtection = MyApplication.getSaveString("ACTIVATEPROTECTION", activity);
        if(MyApplication.setProtection!=null && !MyApplication.setProtection.isEmpty()) {
            if (MyApplication.setProtection.equalsIgnoreCase("Activate")) {

            }else {
                bioMetric_responce_handler.failure("");
               return;
            }
        }else{

        }

        activityNew=activity;
        FingerprintManager fingerprintManager = (FingerprintManager) activity.getSystemService(Context.FINGERPRINT_SERVICE);

        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(activity);
        switch (biometricManager.canAuthenticate()) {


            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:


                //showToast(activity,"hardwatre unvaliba 00");

                // msgText.setText("You can use the fingerprint sensor to login");
                // msgText.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
               //showToast(activity,"hardwatre unvaliba 11");
                bioMetric_responce_handler.failure("");

              //  bioMetric_responce_handler.failure(activity.getString(R.string.no_fingerprint_senser));
                //msgText.setText(getString(R.string.no_fingerprint_senser));
                //tvFinger.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                bioMetric_responce_handler.failure("");
               // bioMetric_responce_handler.failure(message);
               // showToast(activity,"hardwatre 22");

               //  bioMetric_responce_handler.failure(activity.getString(R.string.no_biometric_senser));
              /*  msgText.setText(getString(R.string.no_biometric_senser));
                tvFinger.setVisibility(View.GONE);*/
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                bioMetric_responce_handler.failure("");
             //   bioMetric_responce_handler.failure(activity.getString(R.string.device_not_contain_fingerprint));
              //  showToast(activity,"hardwatre 33");

                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(activity);
        // this will give us result of AUTHENTICATION
          biometricPrompt = new BiometricPrompt((FragmentActivity) activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
              //  showToast(activity,"hardwatre "+errorCode+"  "+errString);
                   // bioMetric_responce_handler.failure(activity.getString(R.string.no_biometric_senser));


                if (activity.getString(R.string.cancel).equalsIgnoreCase(errString.toString()) ||
                        errString.toString().equalsIgnoreCase("Cancel")) {
                    // onAuthenticationFailed();
                    bioMetric_responce_handler.failure("");
                    isCancelCalled=true;
                }


               if(!fingerprintManager.hasEnrolledFingerprints()) {

                   // bioMetric_responce_handler.failure(activity.getResources().getString(R.string.no_fingerprint_senser));

                    // User hasn't enrolled any fingerprints to authenticate with
                } else {
                    // Everything is ready for fingerprint authentication
                   if (activity.getString(R.string.cancel).equalsIgnoreCase(errString.toString()) ||
                           errString.toString().equalsIgnoreCase("Cancel")) {
                      // onAuthenticationFailed();
                       bioMetric_responce_handler.failure("");
                       isCancelCalled=true;
                   }
                }

                if (!isCancelCalled) {
                    onAuthenticationFailed();
                    isCancelCalled=false;
                }

             //   checkCounter(bioMetric_responce_handler,errString+"");

            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                // tvFinger.setText("Login Successful");

                System.out.println("Biomatric   =>"+result.toString());
                bioMetric_responce_handler.success("Call API");
               // showToast(activity,"hardwatre 55");

               /* Intent intent = new Intent(loginpinC, MainActivity.class);
                startActivity(intent);*/
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();


                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 2000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                checkCounter(bioMetric_responce_handler,activity.getResources().getString(R.string.please_enter_pin_bio));
                //showToast(activity,"hardwatre 66");

                biometricPrompt.cancelAuthentication();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
         BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CASHMOOV")
                .setDescription(activity.getString(R.string.use_finger_to_transaction)).setNegativeButtonText(activity.getString(R.string.cancel)).
                         setConfirmationRequired(false).build();

        biometricPrompt.authenticate(promptInfo);


    }

    public static void checkCounter(BioMetric_Responce_Handler bioMetric_responce_handler,String message){
        if(bioMetricCounter==1){
            bioMetricCounter=0;
            bioMetric_responce_handler.failure(message);
        }else{
            bioMetricCounter=bioMetricCounter+1;
           // showToast(activityNew,activityNew.getResources().getString(R.string.tryagain));
        }
        
    }

    public static void biometricAuthNew(Activity activity, LinearLayout layout, BioMetric_Responce_Handler bioMetric_responce_handler){

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
        biometricPrompt = new BiometricPrompt((FragmentActivity) activity, executor, new BiometricPrompt.AuthenticationCallback() {
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

                bioMetric_responce_handler.failure("VISIBLE");
                biometricPrompt.cancelAuthentication();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CASHMOOV")
                .setDescription(activity.getString(R.string.use_finger_to_transaction)).setNegativeButtonText(activity.getString(R.string.cancel)).setConfirmationRequired(false).build();

        biometricPrompt.authenticate(promptInfo);


    }
    public static  void contactValidation(String Phoneno, EditText editText){
        if(Phoneno.length()>11)
        {
            if(Phoneno.contains("+91") || Phoneno.length()==11){
                int startidx=Phoneno.length()-10;
                String getnumber=Phoneno.substring(startidx,Phoneno.length());
                editText.setText(getnumber);
            }else{
                int startidx=Phoneno.length()-9;
                String getnumber=Phoneno.substring(startidx,Phoneno.length());
                editText.setText(getnumber);

            }


        }
        else
        {
            editText.setText(Phoneno);
        }
    }


    public static boolean checkMinMax(Activity activity,CharSequence s,EditText editText,int minAmount,int maxAmount){
       /* if(s.length()==1 && s.toString().equalsIgnoreCase(".")){
            return true;
        }
        if (Double.parseDouble(s.toString().trim().replace(",","")) < minAmount) {
            MyApplication.showTipError(activity, activity.getString(R.string.min_amount) + minAmount, editText);
            return true;
        }

        if (Double.parseDouble(s.toString().trim().replace(",","")) > maxAmount) {
            MyApplication.showTipError(activity, activity.getString(R.string.max_amount) + maxAmount, editText);
            return true;
        }*/
        return false;
    }

    public static void saveOrUpdateValueInSharedPreferences (Context context, String key, String
            value){
        SharedPreferences sp = context.getSharedPreferences(CommonData.SHARED_PREF_NAME, MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getValueFromSharedPreferences (Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(CommonData.SHARED_PREF_NAME, MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static String getTaxString(String test){
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")
        ||MyApplication.getSaveString("Locale", MyApplication.getInstance()).isEmpty()||
                MyApplication.getSaveString("Locale", MyApplication.getInstance())==null){

            return test +" :";
        }else {
            if (test.equalsIgnoreCase("VAT")) {
                return "T.V.A :";
            }
            if (test.equalsIgnoreCase("Financial Tax")) {
                return "Taxe financière :";
            }
        }

        return test+" :";
    }

    public static String getTaxStringnew(String test){
        if(MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("en")
                ||MyApplication.getSaveString("Locale", MyApplication.getInstance()).isEmpty()||
                MyApplication.getSaveString("Locale", MyApplication.getInstance())==null){

            return test +" ";
        }else {
            if (test.equalsIgnoreCase("VAT")) {
                return "T.V.A :";
            }
            if (test.equalsIgnoreCase("Financial Tax")) {
                return "Taxe financière :";
            }
        }

        return test+" ";
    }

   public static String getEncript(String dataencript) {
       if (PasswordEncription) {
           String encryptionDatanew = AESEncryption.getAESEncryption(dataencript);
           return encryptionDatanew;

       }
       return dataencript;
   }

   public static void preventDoubleClick(long mLastClickTime){

       if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
           return;
       }
       mLastClickTime = SystemClock.elapsedRealtime();
   }


}
