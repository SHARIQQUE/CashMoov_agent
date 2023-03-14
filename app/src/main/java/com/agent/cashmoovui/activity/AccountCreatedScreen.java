package com.agent.cashmoovui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.LogoutAppCompactActivity;

import com.agent.cashmoovui.R;
import com.agent.cashmoovui.login.PhoneNumberRegistrationScreen;

public class AccountCreatedScreen extends AppCompatActivity implements View.OnClickListener {
    public static AccountCreatedScreen accountcreatedscreenC;
    TextView tvContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created_screen);
        accountcreatedscreenC = this;
        getIds();
    }

    private void getIds() {
        tvContinue = findViewById(R.id.tvContinue);

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        tvContinue.setOnClickListener(accountcreatedscreenC);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(accountcreatedscreenC, PhoneNumberRegistrationScreen.class);
        startActivity(i);
        finish();
    }
}
