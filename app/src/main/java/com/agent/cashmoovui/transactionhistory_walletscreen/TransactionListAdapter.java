package com.agent.cashmoovui.transactionhistory_walletscreen;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder>{
    private Context context;
    private List<TransactionModel> transactionList = new ArrayList<>();
    private TransactionListLisners transactionListLisners;

    public TransactionListAdapter(Context context, List<TransactionModel> transactionArrayList) {
        this.context = context;
        this.transactionList = transactionArrayList;
        transactionListLisners = (TransactionListLisners) context;
    }

    public void onDataChange(ArrayList<TransactionModel> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_transaction_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position==3){
            MyApplication.hideLoader();
        }
        final TransactionModel transaction = transactionList.get(position);
        holder.transId.setText(transaction.getTransactionId());
        holder.source.setText(transaction.getSrcWalletOwnerName());
        holder.destination.setText(transaction.getDesWalletOwnerName());
        holder.sourceMSISDN.setText(String.valueOf(transaction.getSrcMobileNumber()));
        holder.destMSISDN.setText(String.valueOf(transaction.getDestMobileNumber()));
        holder.rechargeNumber.setText(transaction.getRechargeNumber());
        holder.transType.setText(transaction.getTransTypeName());
        holder.amount.setText(transaction.getSrcCurrencySymbol()+" "+(String.valueOf(transaction.getTransactionAmount())));
        holder.fee.setText(MyApplication.addDecimal(String.valueOf((transaction.getFee()))));
        holder.taxType.setText(transaction.getTaxTypeName());
        holder.tax.setText(MyApplication.addDecimal(String.valueOf(transaction.getTax())));
        holder.transReversed.setText(String.valueOf(transaction.isTransactionReversed()));
        holder.status.setText(transaction.getResultDescription());
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date = null;
            date = inputFormat.parse(transaction.getCreationDate());
            String formattedDate = outputFormat.format(date);
            holder.transDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.fab_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if(transaction.getCode()!=null)
                    transactionListLisners.onTransactionViewItemClick(transaction.getTransactionId(),transaction.getTransTypeName(),
                            transaction.getCreationDate(),transaction.getSrcWalletOwnerName(),transaction.getDesWalletOwnerName(),
                            transaction.getSrcMobileNumber(),transaction.getDestMobileNumber(),transaction.getSrcCurrencySymbol(),
                            transaction.getTransactionAmount(),transaction.getFee(),transaction.getTaxTypeName(),transaction.getTax(),
                            transaction.getDestPostBalance(),transaction.getResultDescription());
            }
        });
    }


    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private FloatingActionButton fab_view;
        private TextView transId,source,destination,sourceMSISDN,destMSISDN,rechargeNumber,transType,amount,fee,
                taxType,tax,transReversed,transDate,status;
        public ViewHolder(View itemView) {
            super(itemView);
            transId = itemView.findViewById(R.id.transId);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            sourceMSISDN = itemView.findViewById(R.id.sourceMSISDN);
            destMSISDN = itemView.findViewById(R.id.destMSISDN);
            rechargeNumber = itemView.findViewById(R.id.rechargeNumber);
            transType = itemView.findViewById(R.id.transType);
            amount = itemView.findViewById(R.id.amount);
            fee = itemView.findViewById(R.id.fee);
            taxType = itemView.findViewById(R.id.taxType);
            tax = itemView.findViewById(R.id.tax);
            transReversed = itemView.findViewById(R.id.transReversed);
            transDate = itemView.findViewById(R.id.transDate);
            status = itemView.findViewById(R.id.status);

            fab_view = itemView.findViewById(R.id.fab_view);
        }
    }
}