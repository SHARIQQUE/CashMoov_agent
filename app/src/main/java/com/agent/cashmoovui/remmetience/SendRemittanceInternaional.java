package com.agent.cashmoovui.remmetience;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.R;

public class SendRemittanceInternaional extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remittance_international);

        Spinner Spinnertypedevise = findViewById(R.id.Spinnertypedevise);
        Spinnertypedevise.setOnItemSelectedListener(this);
        Spinner Spinnerprovider = findViewById(R.id.Spinnerprovider);
        Spinnerprovider.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}