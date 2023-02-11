package com.agent.cashmoovui.apiCalls;



import android.content.Context;
import android.util.Log;

import static com.agent.cashmoovui.MyApplication.okClient;

import com.agent.cashmoovui.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.agent.cashmoovui.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


public class API {


    private static final String TAG = "API CALLS";

    //################ IP DETAILS  #######################################

    //public static String BASEURrL="http://202.131.144.130:8081/";       // ##### QA ######
    //public static String BASEURL="http://202.131.144.129:8081/";    // ##### UAT ######
    //  public static String BASEURL="http://180.179.201.110:8081/";  //Production
  //  public static String BASEURL="https://cashmoovmm.com:8081/";
    //public static String BASEURL="http://180.179.201.109:8081/";


    //  mujhe remiitance local /international  benficairy detail  email id remove karni h jab prod pe build dena hoga to
    public static String BASEURL="http://180.179.201.109:8081/"; //pre prod

    //###############################################################
    public static String BASEURL_AMOUNT="http://192.168.1.170:8081/";
    //public static String BASEURL="http://202.140.50.116:8081/"; //UAT




    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();


    public static OkHttpClient clientBASIC = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
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
                            .header("source","AGENT")
                            .header("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                            .build();
                }
            })
            .build();

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
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
                            .build();
                }
            })
            .build();

    public static OkHttpClient okHttpClient1 = new OkHttpClient.Builder()
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
                            .header("author","Cashmoov1")
                            .build();
                }
            })
            .build();




    public static void POST_REQEST_LOGIN_TOKEN(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())){
            AndroidNetworking.post(BASEURL+URL)
                    .addBodyParameter("username",jsonObject.optString("username")) // posting json
                    .addBodyParameter("password",jsonObject.optString("password"))
                    .addBodyParameter("fcmToken",jsonObject.optString("fcmToken"))
                    .addBodyParameter("grant_type","password")
                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                    .addHeaders("channel","APP")
                    .addHeaders("source","AGENT")

                    //.addHeaders("Accept-Language",MyApplication.getLang())

                    .addHeaders("mac",MyApplication.getUniqueId())
                    .addHeaders("deviceId",MyApplication.getUniqueId())
                    .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))
                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //  MyApplication.hideLoader();

                            responce_handler.success(response);


                            // Log.e("=============", " Token Request ==============" +BASEURL+URL);
                            // Log.e("=============", " Token Response =============="+response.toString()+"=============");


                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            try {

                                if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                    MyApplication.getInstance().callLogin();
                                    if( MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                        MyApplication.showAPIToast("Connecté sur le WEB...");
                                    }else{
                                        MyApplication.showAPIToast("Logged in on WEB...");
                                    }
                                }

                                responce_handler.failure(error.toString());

                                JSONObject error1=new JSONObject(error.getErrorBody());

                                if(error1.optString("error").equalsIgnoreCase("1251")){

                                    JSONObject errorJ=new JSONObject(error.getErrorBody());

                                    responce_handler.success(errorJ);

                                    //  responce_handler.failure(errorJ.toString());
                                }

                                else{
                                    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    // responce_handler.failure(errorJ.optString("error_message"));
                                    responce_handler.success(errorJ);
                                }

                            }catch (Exception e)
                            {
                                System.out.println(e);

                            }

                            if (error.getErrorCode() != 0) {
                                if(error.getErrorCode()==401){
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                responce_handler.failure(error.getErrorDetail());
                           /* if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }*/
                            }
                        }
                    });



        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }


    public static void POST_GET_OTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("channel", "APP")
                    //.addHeaders("Authorization","Bearer b1b80862-17b3-48f0-83a3-b4d27ddd09e2")

                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())

                    .addJSONObjectBody(jsonObject)
                    .setOkHttpClient(okHttpClient)

                    .setPriority(Priority.MEDIUM)
                    .setTag("LOGINOTP")
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            responce_handler.failure(error.getErrorDetail());
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {


                                    //MyApplication.showToast("Unauthorized Request......");
                                    //   MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }


    public static void POST_REQUEST_VERIFY_OTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.put(BASEURL + URL)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("channel", "APP")
                    //.addHeaders("Authorization","Bearer b1b80862-17b3-48f0-83a3-b4d27ddd09e2")

                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addJSONObjectBody(jsonObject)
                    .setOkHttpClient(okHttpClient)

                    .setPriority(Priority.MEDIUM)
                    .setTag("LOGINMPINSUBSCRIBER")
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            responce_handler.failure(error.getErrorDetail());
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_REQEST_GENERATEOTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)
                    .addBodyParameter("username", jsonObject.optString("username")) // posting json
                    .addBodyParameter("password", jsonObject.optString("password"))
                    .addBodyParameter("fcmToken",jsonObject.optString("fcmToken"))
                    .addBodyParameter("grant_type", "password")
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("type", "GENERATEOTP")
                    .addHeaders("source", "AGENT")
                    // .addHeaders("Accept-Language",MyApplication.getLang())
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            MyApplication.hideLoader();
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {

                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_REQEST_RESETPIN(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)
                    .addBodyParameter("username", jsonObject.optString("username")) // posting json
                    .addBodyParameter("password", jsonObject.optString("password"))
                    .addBodyParameter("fcmToken",jsonObject.optString("fcmToken"))
                    .addBodyParameter("grant_type", "password")
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("type", "RESETPIN")
                    .addHeaders("source", "AGENT")
                    // .addHeaders("Accept-Language",MyApplication.getLang())
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {

                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        } else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }
    public static void POST_REQEST_LoginOTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)
                    .addBodyParameter("username", jsonObject.optString("username")) // posting json
                    .addBodyParameter("password", jsonObject.optString("password"))
                    .addBodyParameter("fcmToken",jsonObject.optString("fcmToken"))
                    .addBodyParameter("grant_type", "password")
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("type", "LOGINOTP")
                    .addHeaders("source", "AGENT")
                    // .addHeaders("Accept-Language",MyApplication.getLang())
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {

                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }


    public static void POST_REQUEST_OTP_GENERATE(String URL, JSONObject jsonObject,String ACCESS_TOKEN, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("email",jsonObject.optString("email")) // posting json
                .addBodyParameter("notificationTypeCode",jsonObject.optString("notificationTypeCode"))
                .addBodyParameter("transTypeCode",jsonObject.optString("transTypeCode"))
                .addBodyParameter("status",jsonObject.optString("status"))
                .addBodyParameter("walletOwnerUserCode",jsonObject.optString("walletOwnerUserCode"))
                .addHeaders("channel","APP")
                .addHeaders("source","AGENT")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization","Bearer "+ACCESS_TOKEN)
                // .addHeaders("Authorization","bearer "+ MyApplication.getSaveString("ACCESS_TOKEN",MyApplication.getInstance()))
                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //  MyApplication.hideLoader();

                        Log.e("=============", " OTP Generate Request ==============" +BASEURL+URL);
                        Log.e("=============", " OTP Generate Request ======="+response+"=========");


                        responce_handler.success(response);


                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }
                        try {

                            responce_handler.failure(error.toString());

                            JSONObject error1=new JSONObject(error.getErrorBody());

                            if(error1.optString("error").equalsIgnoreCase("1251")){

                                JSONObject errorJ=new JSONObject(error.getErrorBody());

                                responce_handler.success(errorJ);

                                //  responce_handler.failure(errorJ.toString());
                            }

                            else{
                                JSONObject errorJ=new JSONObject(error.getErrorBody());
                                // responce_handler.failure(errorJ.optString("error_message"));
                                responce_handler.success(errorJ);
                            }

                        }catch (Exception e)
                        {
                            System.out.println(e);

                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }


                        }
                    }
                });

    }

    public static void POST_REQUEST_OTP_VERYFY(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type","password")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","AGENT")
                //.addHeaders("Accept-Language",MyApplication.getLang())
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))
                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //  MyApplication.hideLoader();

                        responce_handler.success(response);


                        Log.e("=============", " Token Request ==============" +BASEURL+URL);
                        Log.e("=============", " Token Response =============="+response.toString()+"=============");


                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }
                        try {

                            responce_handler.failure(error.toString());

                            JSONObject error1=new JSONObject(error.getErrorBody());

                            if(error1.optString("error").equalsIgnoreCase("1251")){

                                JSONObject errorJ=new JSONObject(error.getErrorBody());

                                responce_handler.success(errorJ);

                                //  responce_handler.failure(errorJ.toString());
                            }

                            else{
                                JSONObject errorJ=new JSONObject(error.getErrorBody());
                                // responce_handler.failure(errorJ.optString("error_message"));
                                responce_handler.success(errorJ);
                            }

                        }catch (Exception e)
                        {
                            System.out.println(e);

                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }

    public static void POST_REQEST_CHECK(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)
                    .setOkHttpClient(okHttpClient)
                    .addJSONObjectBody(jsonObject) // posting json
                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))
                    //   .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();
                            if (response.optString("resultCode").equalsIgnoreCase("1059")) {
                                responce_handler.failure(response.optString("resultDescription"));
                            } else {
                                responce_handler.success(response);
                            }
                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }


    }

    public static void POST_REQEST_REGISTER(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setOkHttpClient(okClient)
                    .setTag("test")
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();
                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            responce_handler.failure(error.getErrorDetail());
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }


    // String idProofTypeCode, String customerCode,
    public static void Upload_REQUEST_WH(String URL, File file,String idProofTypeCode,String customerCode, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {


            AndroidNetworking.upload(BASEURL + URL)
                    .addHeaders("channel", "APP")
                    .addMultipartFile("file", file)
                    .addMultipartParameter("idProofTypeCode", idProofTypeCode)
                    .addMultipartParameter("customerCode", customerCode)
                    .setTag("uploadTest")
                    .setPriority(Priority.HIGH)
                    .setOkHttpClient(okClientfileUpload)
                    .setContentType("multipart/form-data")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .build()
                    // setting an executor to get response or completion on that executor thread
                    /* .setUploadProgressListener(new UploadProgressListener() {
                         @Override
                         public void onProgress(long bytesUploaded, long totalBytes) {
                             // do anything with progress
                         }
                     })*/
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responce_handler.success(response);
                            // below code will be executed in the executor provided
                            // do anything with response
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }

    public static void Upload_REQEST(String URL, File file,String docTypeCode, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {


            AndroidNetworking.upload(BASEURL + URL)
                    .addHeaders("channel", "APP")
                    .addMultipartFile("file", file)

                    .addMultipartParameter("docTypeCode", docTypeCode)
                    .addMultipartParameter("walletOwnerCode", MyApplication.getSaveString("walletOwnerCode", MyApplication.getInstance()))
                    .setTag("uploadTest")
                    .setPriority(Priority.HIGH)
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setOkHttpClient(okClientfileUpload)
                    .setContentType("multipart/form-data")
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .build()
                    // setting an executor to get response or completion on that executor thread
                    /* .setUploadProgressListener(new UploadProgressListener() {
                         @Override
                         public void onProgress(long bytesUploaded, long totalBytes) {
                             // do anything with progress
                         }
                     })*/
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responce_handler.success(response);
                            // below code will be executed in the executor provided
                            // do anything with response
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }


    public static void Upload_REQESTID(String URL, String idProofTypeCode,String idProofNumber,File Frontfile,File BackFile,
                                       final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.upload(BASEURL + URL)
                    .addHeaders("channel", "APP")
                    .addMultipartFile("filefront", Frontfile)
                    .addMultipartFile("fileback", BackFile)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addMultipartParameter("docTypeCodeFront", "100012")
                    .addMultipartParameter("docTypeCodeback", "100013")
                    .addMultipartParameter("idProofTypeCode", idProofTypeCode)
                    .addMultipartParameter("idProofNumber", idProofNumber)
                    .addMultipartParameter("walletOwnerCode", MyApplication.getSaveString("walletOwnerCode", MyApplication.getInstance()))
                    .setTag("uploadTest")
                    .setPriority(Priority.HIGH)
                    .setOkHttpClient(clientBASIC)
                    .setContentType("multipart/form-data")
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")

                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .build()
                    // setting an executor to get response or completion on that executor thread
                    /* .setUploadProgressListener(new UploadProgressListener() {
                         @Override
                         public void onProgress(long bytesUploaded, long totalBytes) {
                             // do anything with progress
                         }
                     })*/
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            responce_handler.success(response);
                            // below code will be executed in the executor provided
                            // do anything with response
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();
            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }



    public static void Upload_REQEST_WH_NEW(String URL, File file,String docTypeCode,String walletOwnerCode, final Api_Responce_Handler responce_handler)
    {


        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.upload(BASEURL + URL)
                    .addHeaders("channel", "APP")
                    .addMultipartFile("file", file)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addMultipartParameter("docTypeCode", docTypeCode)
                    .addMultipartParameter("walletOwnerCode", walletOwnerCode)
                    .setTag("uploadTest")
                    .setPriority(Priority.HIGH)
                    .setOkHttpClient(okClientfileUpload)
                    .setContentType("multipart/form-data")
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .build()
                    // setting an executor to get response or completion on that executor thread
                    /* .setUploadProgressListener(new UploadProgressListener() {
                         @Override
                         public void onProgress(long bytesUploaded, long totalBytes) {
                             // do anything with progress
                         }
                     })*/
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responce_handler.success(response);
                            // below code will be executed in the executor provided
                            // do anything with response
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_REQEST_WH_NEW(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();
                            responce_handler.success(response);
                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_REQEST_SETPIN(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.put(BASEURL + URL)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .setTag("test")
                    .addHeaders("channel", "APP")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();
                            responce_handler.success(response);
                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                if (errorJ.has("resultDescription")) {
                                    responce_handler.failure(errorJ.optString("resultDescription"));
                                } else {
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_REQEST_CHANGEPIN(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.put(BASEURL + URL)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .setTag("test")
                    .addHeaders("channel", "APP")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();
                            responce_handler.success(response);
                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                if (errorJ.has("resultDescription")) {
                                    responce_handler.failure(errorJ.optString("resultDescription"));
                                } else {
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }


    }


    public static void PUT_Forgot_Pass(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.put(BASEURL+URL)
                .addJSONObjectBody(jsonObject) // posting json
                .setOkHttpClient(okClient)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyApplication.hideLoader();
                        responce_handler.success(response);
                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }


    public static void GET_CASHIN_DETAILS(String URL,String languageToUse, final Api_Responce_Handler responce_handler)
    {
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })


                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("=============", " GET_CASHIN_MOBILENO Request ==============" + BASEURL + URL);
                            Log.e("=============", " GET_CASHIN_MOBILENO Response ==============" + response + "=============");

                            responce_handler.success(response);

                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                responce_handler.failure(error.toString());
                            } catch (Exception e) {
                                responce_handler.failure(error.toString());
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }




    public static void GET_TRANSFER_DETAILS(String URL,String languageToUse, final Api_Responce_Handler responce_handler)
    {
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })


                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("=============", " REQUEST ==============" + BASEURL + URL);
                            Log.e("=============", " RESPONSE ==============" + response + "=============");

                            responce_handler.success(response);

                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                responce_handler.failure(error.toString());
                            } catch (Exception e) {
                                responce_handler.failure(error.toString());
                            }

                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }
    public static void GET_REMMITANCE_DETAILS(String URL,String languageToUse, final Api_Responce_Handler responce_handler)
    {
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })


                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("=============", " GET_CASHIN_MOBILENO Request ==============" + BASEURL + URL);
                            Log.e("=============", " GET_CASHIN_MOBILENO Response ==============" + response + "=============");

                            responce_handler.success(response);

                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                responce_handler.failure(error.toString());
                            } catch (Exception e) {
                                responce_handler.failure(error.toString());
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }

    public static void GET_CASHOUT_DETAILS(String URL,String languageToUse, final Api_Responce_Handler responce_handler)
    {
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })


                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("=============", " GET_CASHOUT_DETAILS Request ==============" + BASEURL + URL);
                            Log.e("=============", " GET_CASHOUT_DETAILS Response ==============" + response + "=============");

                            responce_handler.success(response);

                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                responce_handler.failure(error.toString());
                            } catch (Exception e) {
                                responce_handler.failure(error.toString());
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }


    public static void GET_CASHOUT_CONFCODE_DETAILS(String URL,String languageToUse, final Api_Responce_Handler responce_handler)
    {
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })


                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("=============", " GET_CASHOUT_DETAILS Request ==============" + BASEURL + URL);
                            Log.e("=============", " GET_CASHOUT_DETAILS Response ==============" + response + "=============");

                            responce_handler.success(response);

                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                responce_handler.failure(error.toString());
                            } catch (Exception e) {
                                responce_handler.failure(error.toString());
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //   MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }


    public static void GET_MSDIN(String URL,String languageToUse, final Api_Responce_Handler responce_handler)
    {
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("channel", "APP")
                    .addHeaders("User", "Agent")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })


                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("=============", " mssidn Request ==============" + BASEURL + URL);
                            Log.e("=============", " mssidn Response ==============" + response + "=============");

                            responce_handler.success(response);

                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                responce_handler.failure(error.toString());
                            } catch (Exception e) {
                                responce_handler.failure(error.toString());
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }

    public static void GET_PUBLICN(String URL, final Api_Responce_Handler responce_handler)
    {
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(URL)
                    .setOkHttpClient(okClient)


                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })


                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("=============", " mssidn Request ==============" + BASEURL + URL);
                            Log.e("=============", " mssidn Response ==============" + response + "=============");

                            responce_handler.success(response);

                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                responce_handler.failure(error.toString());
                            } catch (Exception e) {
                                responce_handler.failure(error.toString());
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }



    public static void POST_CASHIN_MPIN(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)

                    .addJSONObjectBody(jsonObject)

                    // .addBodyParameter(jsonObject)

                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //   MyApplication.getInstance().callLogin();

                                }


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    // MyApplication.showToast("Unauthorized Request......");
                                    //MyApplication.getInstance().callLogin();

                                }

                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_TRANSFER(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)

                    .addJSONObjectBody(jsonObject)

                    // .addBodyParameter(jsonObject)

                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }

                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }


    public static void POST_TRANSFERDETAILS(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)

                    .addJSONObjectBody(jsonObject)

                    // .addBodyParameter(jsonObject)

                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }

                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_REMMIT_CASHTOWALLET(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)

                    .addJSONObjectBody(jsonObject)

                    // .addBodyParameter(jsonObject)

                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }

                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_REMIT_SENDER_RECEIVER(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)

                    .addJSONObjectBody(jsonObject)

                    // .addBodyParameter(jsonObject)

                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("resultDescription"));
                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }

                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }



    public static void POST_REMMIT_LOCAL(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)

                    .addJSONObjectBody(jsonObject)

                    // .addBodyParameter(jsonObject)

                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //   MyApplication.getInstance().callLogin();

                                }


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }

                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_CASHOUT_MPIN(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)

                    .addJSONObjectBody(jsonObject)

                    // .addBodyParameter(jsonObject)

                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }

                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void POST_CASHOUT_CONFCODE_MPIN(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL + URL)

                    .addJSONObjectBody(jsonObject)

                    // .addBodyParameter(jsonObject)

                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .setOkHttpClient(okHttpClient)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();


                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                            try {
                                JSONObject error1 = new JSONObject(error.getErrorBody());
                                if (error1.optString("error").equalsIgnoreCase("1251")) {
                                    //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                    responce_handler.failure("1251");
                                } else {
                                    JSONObject errorJ = new JSONObject(error.getErrorBody());
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //   MyApplication.getInstance().callLogin();

                                }


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }

                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }


    public static void GET_ALL(String URL,String languageToUse, final Api_Responce_Handler responce_handler)
    {
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", languageToUse)
                    .addHeaders("channel", "APP")
                    .addHeaders("User", "Agent")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })


                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("=============", " ALL Request ==============" + BASEURL + URL);
                            Log.e("=============", " ALL Response ==============" + response + "=============");

                            responce_handler.success(response);

                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                responce_handler.failure(error.toString());
                            } catch (Exception e) {
                                responce_handler.failure(error.toString());
                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }
    }


    public static void GET(String URL, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void GET_PUBLIC(String URL, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "AGENT")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    //   .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))

                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                                MyApplication.getInstance().callLogin();
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }

                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }

    public static void PUT_REQUEST_NEW(String URL, final Api_Responce_Handler responce_handler){

        AndroidNetworking.put(BASEURL+URL)
                .setOkHttpClient(okClient)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","AGENT")
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }


    public static void PUT_REQEST_WH(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.put(BASEURL+URL)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("Authorization","Bearer "+ MyApplication.getSaveString("token",MyApplication.getInstance()))
                .setTag("test")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())

                .addHeaders("channel","APP")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyApplication.hideLoader();
                        if(response.optBoolean("success")) {
                            responce_handler.success(response);
                        }else{
                            responce_handler.failure(response.optString("errorMessage"));
                        }
                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }
                        try {

                            JSONObject errorJ=new JSONObject(error.getErrorBody());
                            responce_handler.failure(errorJ.optString("error_message"));
                        }catch (Exception e)
                        {

                        }
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }


    public static void POST_LOGIN_OTP(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("source","AGENT")
                .addHeaders("channel","APP")
                .addHeaders("type","LOGINOTP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)

                .setPriority(Priority.MEDIUM)
                .setTag("LOGINOTP")
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }
                        responce_handler.failure(error.getErrorDetail());
                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }

    public static void POST_REQEST_Login(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type","password")
                .addBodyParameter("scope","read write")
                .addHeaders("Accept-Language",languageToUse)
                .addHeaders("channel","APP")
                //.addHeaders("channel","APP")
                // .addHeaders("type","LOGINOTP")
                .addHeaders("source","AGENT")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))
                .setOkHttpClient(client)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());

                        Log.e("=============", " Token Request ==============" +BASEURL+URL);
                        Log.e("=============", " Token Response =============="+response+"=============");

                    }

                    @Override
                    public void onError(ANError error) {

                        try {
                            responce_handler.failure(error.toString());
                        }
                        catch (Exception e)
                        {
                            responce_handler.failure(error.toString());
                        }
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();
                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }

    public static void POST_REQEST_OTP_VERIFY(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username"))
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type","password")
                .addHeaders("Accept-Language",languageToUse)
                .addHeaders("channel","APP")
                .addHeaders("type","LOGINOTP")
                .addHeaders("source","AGENT")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyApplication.hideLoader();


                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }
                        try {
                            JSONObject error1=new JSONObject(error.getErrorBody());
                            if(error1.optString("error").equalsIgnoreCase("1251")){
                                //    JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure("1251");
                            }else{
                                JSONObject errorJ=new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            }

                        }catch (Exception e)
                        {

                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }


    public static void POST_TOKEN_OTP(String URL, JSONObject jsonObject,String languageToUse, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type","password")
                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("source","AGENT")
                // .addHeaders("Accept-Language",MyApplication.getLang())
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                .addHeaders("Authorization", Credentials.basic("cashmoov", "123456"))

                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());

                        Log.e("=============", " Token Request ==============" +BASEURL+URL);
                        Log.e("=============", " Token Response =============="+response+"=============");

                    }

                    @Override
                    public void onError(ANError error) {
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")){
                            MyApplication.getInstance().callLogin();
                            MyApplication.showAPIToast("Logged in on WEB...");
                        }
                        try {
                            responce_handler.failure(error.toString());
                        }
                        catch (Exception e)
                        {
                            responce_handler.failure(error.toString());
                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();
                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }

    public static void POST_REQEST_GenerateLogin(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        AndroidNetworking.post(BASEURL+URL)
                .addBodyParameter("username",jsonObject.optString("username")) // posting json
                .addBodyParameter("password",jsonObject.optString("password"))
                .addBodyParameter("grant_type",jsonObject.optString("grant_type"))
                .addBodyParameter("scope",jsonObject.optString("scope"))

                .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                .addHeaders("channel","APP")
                .addHeaders("mac",MyApplication.getUniqueId())
                .addHeaders("deviceId",MyApplication.getUniqueId())
                // .addHeaders("Authorization", "Basic Y2FzaG1vb3Y6MTIzNDU2")
                .addHeaders("source","AGENT")
                .addHeaders("type", "LOGINOTP")
                .setOkHttpClient(okHttpClient)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyApplication.hideLoader();

                        responce_handler.success(response);

                        Log.d(TAG, "onResponse object : " + response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        MyApplication.hideLoader();
                        if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                            MyApplication.getInstance().callLogin();
                            if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                MyApplication.showAPIToast("Connecté sur le WEB...");
                            } else {
                                MyApplication.showAPIToast("Logged in on WEB...");
                            }
                        }
                        try {
                            JSONObject jsonObject1=new JSONObject(error.getErrorBody());
                            responce_handler.failure(jsonObject1.optString("error_message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (error.getErrorCode() != 0) {
                            if(error.getErrorCode()==401){
                                MyApplication.showAPIToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            if(error.getErrorDetail().equalsIgnoreCase("connectionError")){
                                //MyApplication.showToast("Unauthorized Request......");
                                MyApplication.getInstance().callLogin();

                            }
                        }
                    }
                });

    }

    public static void PUT_SET_PASS(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.put(BASEURL + URL)
                    .addJSONObjectBody(jsonObject)
                    .setOkHttpClient(okHttpClient)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("channel", "APP")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("type", "LOGINOTP")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();

                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }
                            try {
                                JSONObject jsonObject1 = new JSONObject(error.getErrorBody());
                                responce_handler.failure(jsonObject1.optString("error_message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    //  MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }




    public static void PUT(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.put(BASEURL + URL)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setOkHttpClient(okClient)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("source", "AGENT")
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))
                    .setTag("test")
                    .addHeaders("channel", "APP")
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            MyApplication.hideLoader();
                            responce_handler.success(response);
                            Log.d(TAG, "onResponse sonu object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            if(error.toString().equalsIgnoreCase("com.androidnetworking.error.ANError: com.androidnetworking.error.ANError: java.net.ProtocolException: Too many follow-up requests: 21")) {
                                MyApplication.getInstance().callLogin();
                                if (MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")) {

                                    MyApplication.showAPIToast("Connecté sur le WEB...");
                                } else {
                                    MyApplication.showAPIToast("Logged in on WEB...");
                                }
                            }

                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                if (errorJ.has("resultDescription")) {
                                    responce_handler.failure(errorJ.optString("resultDescription"));
                                } else {
                                    responce_handler.failure(errorJ.optString("error_message"));
                                }

                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                if (error.getErrorDetail().equalsIgnoreCase("connectionError")) {
                                    //MyApplication.showToast("Unauthorized Request......");
                                    // MyApplication.getInstance().callLogin();

                                }
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();

            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }

    }
    public static void POST_REQEST_TransferAMount(String URL, JSONObject jsonObject, final Api_Responce_Handler responce_handler){
        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.post(BASEURL_AMOUNT + URL)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setTag("test")
                    .setOkHttpClient(client)
                    .addHeaders("Accept-Language",MyApplication.getSaveString("Locale",MyApplication.getInstance()))
                    .addHeaders("channel", "APP")
                    .addHeaders("source", "SUBSCRIBER")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            responce_handler.success(response);

                            Log.d(TAG, "onResponse sonu object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            MyApplication.hideLoader();
                            responce_handler.failure(error.getErrorDetail());
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();
            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }


    }
    public static void GETPreProd(String URL, final Api_Responce_Handler responce_handler){

        if(MyApplication.isConnectingToInternet(MyApplication.getInstance().getApplicationContext())) {

            AndroidNetworking.get(BASEURL + URL)
                    .setOkHttpClient(client)
                    .addHeaders("Accept-Language", MyApplication.getSaveString("Locale", MyApplication.getInstance()))
                    .addHeaders("channel", "WEB")
                    .addHeaders("source", "AGENT")
                    .addHeaders("mac", MyApplication.getUniqueId())
                    .addHeaders("deviceId", MyApplication.getUniqueId())
                    .addHeaders("Authorization", "Bearer " + MyApplication.getSaveString("token", MyApplication.getInstance()))

                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responce_handler.success(response);

                            Log.d(TAG, "onResponse object : " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {

                            try {

                                JSONObject errorJ = new JSONObject(error.getErrorBody());
                                responce_handler.failure(errorJ.optString("error_message"));
                            } catch (Exception e) {

                            }
                            if (error.getErrorCode() != 0) {
                                if (error.getErrorCode() == 401) {
                                    MyApplication.showAPIToast("Unauthorized Request......");
                                    MyApplication.getInstance().callLogin();

                                }
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());


                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            }
                        }
                    });
        }
        else{
            MyApplication.hideLoader();
            MyApplication.showToastNew(MyApplication.getInstance().getApplicationContext(),MyApplication.getInstance().getApplicationContext().getString(R.string.please_check_internet));
        }


    }



}
