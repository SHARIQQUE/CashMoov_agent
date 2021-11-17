package com.agent.cashmoovui.transactionhistory_walletscreen;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.activity.ShowProfileQr;
import com.agent.cashmoovui.internet.InternetCheck;
import com.agent.cashmoovui.remmetience.RemittanceReceive;
import com.agent.cashmoovui.settings.Profile;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class WalletScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    SmoothBottomBar bottomBar;
    ImageView imgQR;

    String searchStr="";
    EditText edittext_search;
    ImageView search_imageView;

    TextView insitute_textview,insitute_branch,agent_textview,mainwallet_textview,commision_wallet_textview,overdraft_wallet_textview;

    Spinner spinner_currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);

        spinner_currency = (Spinner) findViewById(R.id.spinner_currency);
        spinner_currency.setOnItemSelectedListener(this);

        bottomBar = findViewById(R.id.bottomBar);
        imgQR = findViewById(R.id.imgQR);
        imgQR.setOnClickListener(this);

        bottomBar.setItemActiveIndex(1);
        bottomBar.setBarIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));

        insitute_textview =(TextView)findViewById(R.id.insitute_textview);
        insitute_branch =(TextView)findViewById(R.id.insitute_branch);
        agent_textview =(TextView)findViewById(R.id.agent_textview);

        mainwallet_textview =(TextView)findViewById(R.id.mainwallet_textview);
        commision_wallet_textview =(TextView)findViewById(R.id.commision_wallet_textview);
        search_imageView =(ImageView) findViewById(R.id.search_imageView);
        search_imageView.setOnClickListener(this);
        overdraft_wallet_textview =(TextView)findViewById(R.id.overdraft_wallet_textview);

        if(MyApplication.getSaveString("walletOwnerCategoryCode", WalletScreen.this).equalsIgnoreCase(MyApplication.InstituteCode)){
            insitute_textview.setClickable(false);
        }
        if(MyApplication.getSaveString("walletOwnerCategoryCode",WalletScreen.this).equalsIgnoreCase(MyApplication.AgentCode)){
            agent_textview.setClickable(false);
            insitute_textview.setVisibility(View.GONE);

        }
        if(MyApplication.getSaveString("walletOwnerCategoryCode",WalletScreen.this).equalsIgnoreCase(MyApplication.BranchCode)){
            insitute_branch.setClickable(false);
            insitute_textview.setVisibility(View.GONE);
            agent_textview.setVisibility(View.GONE);
        }

        edittext_search =(EditText)findViewById(R.id.edittext_search);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (new InternetCheck().isConnected(WalletScreen.this)) {

                    searchStr = edittext_search.getText().toString().trim();

                } else {
                    Toast.makeText(WalletScreen.this, getString(R.string.please_check_internet), Toast.LENGTH_LONG).show();
                }
            }
        });


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

            case R.id.search_imageView:

                Toast.makeText(WalletScreen.this, "---------search_imageView-------", Toast.LENGTH_LONG).show();

                break;

        }
    }
}