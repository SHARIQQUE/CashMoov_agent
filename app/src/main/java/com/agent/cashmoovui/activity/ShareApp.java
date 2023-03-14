package com.agent.cashmoovui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.LogoutAppCompactActivity;

import com.agent.cashmoovui.R;

public class ShareApp extends LogoutAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);
    }
}