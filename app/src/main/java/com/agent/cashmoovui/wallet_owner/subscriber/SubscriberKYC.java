package com.agent.cashmoovui.wallet_owner.subscriber;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agent.cashmoovui.R;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;
import com.agent.cashmoovui.wallet_owner.wallet_owner.WalletOwner;

import java.util.Calendar;

public class SubscriberKYC extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    DatePickerDialog picker;
    EditText eText;
    TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_abonne_one);


        eText=(EditText) findViewById(R.id.datenaiss);
        tv_submit=(TextView) findViewById(R.id.tvNext);
        tv_submit.setOnClickListener(this);

        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SubscriberKYC.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Spinner Spinnergenre = findViewById(R.id.Spinnergenre);
        Spinnergenre.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNext: {

                Intent i = new Intent(SubscriberKYC.this, SubscriberKYCAtached.class);
                startActivity(i);
            }
            break;
        }
    }
}