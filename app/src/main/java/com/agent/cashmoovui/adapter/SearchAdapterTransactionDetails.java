package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.model.MiniStatementTrans;
import com.agent.cashmoovui.model.UserDetail;
import com.agent.cashmoovui.model.UserDetailAgent;

import java.util.ArrayList;
import java.util.Locale;


public class SearchAdapterTransactionDetails extends RecyclerView.Adapter<SearchAdapterTransactionDetails.ViewHolder> {
    String finalDate;
    private Context context;



    ArrayList<MiniStatementTrans> arrayList_modalUserData;
    private ArrayList<MiniStatementTrans> arrayListTemp = null;



    public SearchAdapterTransactionDetails(Context context, ArrayList<MiniStatementTrans> arrayList_modalUserData) {
        this.context = context;
        this.arrayList_modalUserData = arrayList_modalUserData;

        this.arrayListTemp = new ArrayList<>();

        arrayListTemp.addAll(arrayList_modalUserData);
        System.out.println();

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.transactionType.setText(arrayList_modalUserData.get(i).getTransactionTypeName());
        viewHolder.amount.setText(String .valueOf(arrayList_modalUserData.get(i).getTransactionAmount()));
        viewHolder.mobileNumber.setText(arrayList_modalUserData.get(i).getFromWalletOwnerMsisdn());
        viewHolder.transactionDate.setText(arrayList_modalUserData.get(i).getCreationDate());


//        if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//            viewHolder.amount.setTextColor(Color.parseColor("#D32F2F"));
//        }
//        if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//            viewHolder.amount.setTextColor(Color.parseColor("#388E3C"));
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
        View listItem = layoutInflater.inflate(R.layout.search_transaction_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView transactionType, amount,mobileNumber,transactionDate;

        public ViewHolder(View itemView) {
            super(itemView);

            transactionType = (TextView) itemView.findViewById(R.id.transactionType);
            amount = (TextView) itemView.findViewById(R.id.amount);
            mobileNumber = (TextView) itemView.findViewById(R.id.mobileNumber);
            transactionDate = (TextView) itemView.findViewById(R.id.transactionDate);


        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayList_modalUserData.clear();
        if (charText.length() == 0) {
            arrayList_modalUserData.addAll(arrayListTemp);
        } else {
            for (MiniStatementTrans wp : arrayListTemp) {
                if (wp.getTransactionTypeName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList_modalUserData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
