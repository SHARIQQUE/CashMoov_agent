package com.agent.cashmoovui;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.apiCalls.API;

public class LogoutAppCompactActivity extends AppCompatActivity implements LogOutTimerUtil.LogOutListener {
    @Override
    public void doLogout() {
        runOnUiThread(new Runnable() {
            public void run() {

                if(API.BASEURL.equalsIgnoreCase("https://cashmoovmm.com:8081/")){

                }else{
                    MyApplication.getInstance().callLogout();
                }
               //
            }
        });
    }
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TAG", "User interacting with screen");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }
}
