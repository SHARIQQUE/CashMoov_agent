package com.agent.cashmoovui.wallet_owner.add_agent_branch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.R;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberKYC;
import com.agent.cashmoovui.wallet_owner.subscriber.SubscriberKYCAtached;

public class AgentKYCBranch extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,View.OnClickListener {

    TextView tv_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_agent);

        Spinner SpinnertypeAc = findViewById(R.id.SpinnertypeAc);
        SpinnertypeAc.setOnItemSelectedListener(this);

        tv_submit=(TextView) findViewById(R.id.tvNext);
        tv_submit.setOnClickListener(this);

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
        switch (view.getId()) {
            case R.id.tvNext: {
                Intent i = new Intent(AgentKYCBranch.this, AgentKYCAtatched.class);
                startActivity(i);
            }
            break;
        }
    }
}