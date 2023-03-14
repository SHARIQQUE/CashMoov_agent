package com.agent.cashmoovui.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.LogoutAppCompactActivity;
import android.os.Bundle;

import com.agent.cashmoovui.R;

public class SettingCurrency extends LogoutAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_currency);
    }
}