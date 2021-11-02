package com.agent.cashmoovui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.settings.Profile;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class WalletScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    SmoothBottomBar bottomBar;
    ImageView imgQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);

        Spinner Spinnertypedevise = findViewById(R.id.Spinnertypedevise);
        Spinnertypedevise.setOnItemSelectedListener(this);

        bottomBar = findViewById(R.id.bottomBar);
        imgQR = findViewById(R.id.imgQR);
        imgQR.setOnClickListener(this);

        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int bottomId) {
                if (bottomId == 0) {
                    Intent i = new Intent(WalletScreen.this, MainActivity.class);
                    startActivity(i);
                    //  finish();
                }
                if (bottomId == 1) {
//                    Intent i = new Intent(WalletScreen.this, WalletScreen.class);
//                    startActivity(i);
//                    finish();
                }
                if (bottomId == 2) {
                    Intent i = new Intent(WalletScreen.this, Profile.class);
                    startActivity(i);
                    //  finish();
                }
                return true;
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
//        tvClick.setVisibility(View.VISIBLE);
//        tvBalance.setVisibility(View.GONE);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.imgQR:
                intent = new Intent(WalletScreen.this, ShowProfileQr.class);
                startActivity(intent);
                break;
            case R.id.tvClick:
//                tvClick.setVisibility(View.GONE);
//                tvBalance.setVisibility(View.VISIBLE);
        }
    }
}