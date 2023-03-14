package com.agent.cashmoovui.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.LogoutAppCompactActivity;
import android.os.Bundle;

import com.agent.cashmoovui.R;

public class ListUsers extends LogoutAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
    }
}