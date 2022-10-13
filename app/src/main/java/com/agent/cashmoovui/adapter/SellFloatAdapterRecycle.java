package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.model.UserDetail;
import com.agent.cashmoovui.transfer_float.CallBackSellFloatRecycleViewClick;
import com.agent.cashmoovui.transfer_float.SellFloat;
import com.agent.cashmoovui.transfer_float.SellFloatModal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class SellFloatAdapterRecycle extends RecyclerView.Adapter<SellFloatAdapterRecycle.ViewHolder> {
    String finalDate;
    private Context context;

    ArrayList<SellFloatModal> arrayList_modalUserData;

    CallBackSellFloatRecycleViewClick callBackSellFloatRecycleViewClick;



    public SellFloatAdapterRecycle(Context context, ArrayList<SellFloatModal> arrayList_modalUserData,CallBackSellFloatRecycleViewClick callBackSellFloatRecycleViewClick) {
        this.context = context;
        this.arrayList_modalUserData = arrayList_modalUserData;
       this.callBackSellFloatRecycleViewClick = callBackSellFloatRecycleViewClick;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,  int i) {

        viewHolder.receiverName_textview.setText(arrayList_modalUserData.get(i).getSellFloat_desWalletOwnerName());
        viewHolder.receiver_msisdn_textview.setText(arrayList_modalUserData.get(i).getSellFloat_desWalletOwnerNumber());
        viewHolder.currency_Name_textview.setText(arrayList_modalUserData.get(i).getSellFloat_desCurrencyName());


        String amount_str=Double.toString(arrayList_modalUserData.get(i).getSellFloat_amount());
        viewHolder.amount_textview.setText(MyApplication.addDecimal(amount_str));

        String fee_str=Double.toString(arrayList_modalUserData.get(i).getSellFloat_fee());
        viewHolder.fee_textview.setText(MyApplication.addDecimal(fee_str));

        String tax_str=Double.toString(arrayList_modalUserData.get(i).getSellFloat_tax());
        viewHolder.tax_textview.setText(MyApplication.addDecimal(tax_str));

        String tfinalAmount_str=Double.toString(arrayList_modalUserData.get(i).getSellFloat_finalAmount());
        viewHolder.finalAmount_textview.setText(MyApplication.addDecimalthree(tfinalAmount_str));

        viewHolder.status_textview.setText(arrayList_modalUserData.get(i).getSellFloat_status());

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date = null;
            date = inputFormat.parse(arrayList_modalUserData.get(i).getSellFloat_creationDate());
            String formattedDate = outputFormat.format(date);
            viewHolder.datetime_textview.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        viewHolder.action_click.setId(i);

        viewHolder.action_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  transationDetails="";

               String srcWalletOwnerCode =arrayList_modalUserData.get(i).getSellFloat_srcWalletOwnerCode();
               String desWalletOwnerCode =arrayList_modalUserData.get(i).getSellFloat_desWalletOwnerCode();
               String srcWalletOwnerName =arrayList_modalUserData.get(i).getSellFloat_srcWalletOwnerName();
               String desWalletOwnerName =arrayList_modalUserData.get(i).getSellFloat_desWalletOwnerName();
               String currenyName =arrayList_modalUserData.get(i).getSellFloat_desCurrencyName();
               String amount_str=Double.toString(arrayList_modalUserData.get(i).getSellFloat_amount());
               String fee_str=Double.toString(arrayList_modalUserData.get(i).getSellFloat_fee());
               String tax_tax=Double.toString(arrayList_modalUserData.get(i).getSellFloat_tax());
               String texchangeRate=Double.toString(arrayList_modalUserData.get(i).getSellFloat_exchangeRate());
               String finnalAmount=Double.toString(arrayList_modalUserData.get(i).getSellFloat_finalAmount());
               String date=arrayList_modalUserData.get(i).getSellFloat_creationDate();
               String status=arrayList_modalUserData.get(i).getSellFloat_status();
                String MSSDN=arrayList_modalUserData.get(i).getSellFloat_desWalletOwnerNumber();

                System.out.println("get fee"+arrayList_modalUserData);

                transationDetails ="|"+srcWalletOwnerCode+"|"+desWalletOwnerCode
                        +"|"+srcWalletOwnerName+"|"+desWalletOwnerName+"|"+currenyName
                        +"|"+amount_str+"|"+fee_str+"|"+tax_tax
                        +"|"+texchangeRate+"|"+finnalAmount+"|"+date
                        +"|"+status+"|"+MSSDN+"|"+tax_tax+"|";

                   callBackSellFloatRecycleViewClick.callBackSellFloat(transationDetails);
            }
        });



//        if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//            viewHolder.receiver_msisdn_textview.setTextColor(Color.parseColor("#D32F2F"));
//        }
//        if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//            viewHolder.receiver_msisdn_textview.setTextColor(Color.parseColor("#388E3C"));
//        }


//        viewHolder.viewAgent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//              //  viewagent_request(searchViewModels.get(finalI));
//
//                callBackViewUpdateInterface.agentSearchViewClick(arrayList.get(i));
//
//
//            }
//        });

//        viewHolder.modify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // modify_request(searchViewModels.get(finalI));
//
//
//                callBackViewUpdateInterface.agentSearchUpdateClick(arrayList.get(i));
//
//
//            }
//        });




    }

    @Override
    public int getItemCount() {
        return arrayList_modalUserData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.sellfloat_row_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView receiverName_textview, receiver_msisdn_textview,currency_Name_textview,amount_textview,fee_textview,tax_textview,finalAmount_textview,status_textview,datetime_textview;
        Button action_click;

        public ViewHolder(View itemView) {
            super(itemView);

            receiverName_textview = (TextView) itemView.findViewById(R.id.receiverName_textview);
            receiver_msisdn_textview = (TextView) itemView.findViewById(R.id.receiver_msisdn_textview);
            currency_Name_textview = (TextView) itemView.findViewById(R.id.currency_Name_textview);
            amount_textview = (TextView) itemView.findViewById(R.id.amount_textview);
            fee_textview = (TextView) itemView.findViewById(R.id.fee_textview);
            tax_textview = (TextView) itemView.findViewById(R.id.tax_textview);
            finalAmount_textview = (TextView) itemView.findViewById(R.id.finalAmount_textview);
            status_textview = (TextView) itemView.findViewById(R.id.status_textview);
            datetime_textview = (TextView) itemView.findViewById(R.id.datetime_textview);

            action_click = (Button) itemView.findViewById(R.id.action_click);



        }
    }

//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        arrayList_modalUserData.clear();
//        if (charText.length() == 0) {
//            arrayList_modalUserData.addAll(arrayListTemp);
//        } else {
//            for (UserDetail wp : arrayListTemp) {
//                if (wp.getTransTypeName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    arrayList_modalUserData.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
}
