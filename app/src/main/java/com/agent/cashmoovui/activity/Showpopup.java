package com.agent.cashmoovui.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.agent.cashmoovui.LogoutAppCompactActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.agent.cashmoovui.R;

public class Showpopup extends LogoutAppCompactActivity {

    Dialog myDialog1;
    Dialog myDialog2;
    Dialog myDialog3;
    Dialog myDialog4;
    Dialog myDialog5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpopup);

        myDialog1 = new Dialog(this);
        myDialog2 = new Dialog(this);
        myDialog3 = new Dialog(this);

        myDialog4 = new Dialog(this);
        myDialog5 = new Dialog(this);

    }

    public void ShowPopup1(View v) {

        myDialog1.setContentView(R.layout.custompopup);

        Button buttonanule;

        buttonanule =(Button) myDialog1.findViewById(R.id.buttonanule);

        buttonanule.setText("ANNULER");

        buttonanule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog1.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog1.show();
    }

    public void ShowPopup2(View v) {


        myDialog2.setContentView(R.layout.wallet_recu_pop);

        Button buttonanule;

        buttonanule =(Button) myDialog2.findViewById(R.id.buttonanule);

        buttonanule.setText("ANNULER");

        buttonanule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }

    public void ShowPopup3(View v) {


        myDialog3.setContentView(R.layout.popup_pin_solde);

        Button buttonanule;

        buttonanule =(Button) myDialog3.findViewById(R.id.buttonanule);

        buttonanule.setText("ANNULER");

        buttonanule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog3.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog3.show();
    }


    public void ShowPopup4(View v) {


        myDialog4.setContentView(R.layout.fraisdeserviceretrait);

        Button buttonanule;

        buttonanule =(Button) myDialog4.findViewById(R.id.buttonanule);

        buttonanule.setText("ANNULER");

        buttonanule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog4.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog4.show();
    }

    public void ShowPopup5(View v) {


        myDialog5.setContentView(R.layout.detailfraisdeservice);

        Button buttonanule;

        buttonanule =(Button) myDialog5.findViewById(R.id.buttonanule);

        buttonanule.setText("ANNULER");

        buttonanule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog5.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog5.show();
    }



}