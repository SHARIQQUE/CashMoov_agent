package com.agent.cashmoovui.transactionhistory_walletscreen;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionListAdapterAgent extends RecyclerView.Adapter<TransactionListAdapterAgent.ViewHolder>{
    private Context context;
    private List<TransactionModelAgent> transactionList = new ArrayList<>();
    private TransactionListLisnersAgent transactionListLisners;

    public TransactionListAdapterAgent(Context context, List<TransactionModelAgent> transactionArrayList) {
        this.context = context;
        this.transactionList = transactionArrayList;
        transactionListLisners = (TransactionListLisnersAgent) context;
    }

    public void onDataChange(ArrayList<TransactionModelAgent> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_transaction_list_new_agent, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TransactionModelAgent transaction = transactionList.get(position);

        try {


            if (position == 3) {
                MyApplication.hideLoader();
            }



            if (transaction.getTransTypeName().equalsIgnoreCase("Remit Receive")) {

                holder.transType.setText(transaction.getTransTypeName());
                holder.transType.setTextColor(Color.parseColor("#008000"));

                holder.transId.setText(transaction.getTransactionId());
                holder.source.setText(transaction.getSrcWalletOwnerName());

                holder.source.setTextColor(Color.parseColor("#004AAD"));

                holder.destination.setText(transaction.getDesWalletOwnerName());
                holder.sourceMSISDN.setText(String.valueOf(transaction.getSrcMobileNumber()));
                holder.destMSISDN.setText(String.valueOf(transaction.getDestMobileNumber()));
                holder.rechargeNumber.setText(transaction.getRechargeNumber());


                holder.amount.setText(transaction.getSrcCurrencySymbol() + " " + (String.valueOf(transaction.getTransactionAmount())));
                holder.amount.setTextColor(Color.parseColor("#008000"));

                holder.fee.setText(MyApplication.addDecimal(String.valueOf((transaction.getFee()))));
                holder.fee.setTextColor(Color.parseColor("#008000"));

                holder.taxType.setText(transaction.getTaxTypeName());
                holder.taxType.setTextColor(Color.parseColor("#008000"));


                holder.tax.setText(MyApplication.addDecimal(String.valueOf(transaction.getTax())));
                holder.tax.setTextColor(Color.parseColor("#008000"));



                holder.transReversed.setText(String.valueOf(transaction.isTransactionReversed()));
                holder.status.setText(transaction.getResultDescription());
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                Date date = null;
                date = inputFormat.parse(transaction.getCreationDate());
                String formattedDate = outputFormat.format(date);

                String[] dateTemp = formattedDate.split("\\ ");

                holder.transDate.setText(dateTemp[0]);

            }

            else

                {
                    holder.transType.setText(transaction.getTransTypeName());
                    holder.transType.setTextColor(Color.parseColor("#FF0000"));



                holder.transId.setText(transaction.getTransactionId());

                holder.source.setText(transaction.getSrcWalletOwnerName());

                holder.source.setTextColor(Color.parseColor("#004AAD"));

                    holder.destination.setText(transaction.getDesWalletOwnerName());
                holder.sourceMSISDN.setText(String.valueOf(transaction.getSrcMobileNumber()));
                holder.destMSISDN.setText(String.valueOf(transaction.getDestMobileNumber()));
                holder.rechargeNumber.setText(transaction.getRechargeNumber());

                holder.amount.setText(transaction.getSrcCurrencySymbol() + " " + (String.valueOf(transaction.getTransactionAmount())));
                    holder.amount.setTextColor(Color.parseColor("#FF0000"));

                    holder.fee.setText(MyApplication.addDecimal(String.valueOf((transaction.getFee()))));
                    holder.fee.setTextColor(Color.parseColor("#FF0000"));

                    holder.taxType.setText(transaction.getTaxTypeName());
                    holder.taxType.setTextColor(Color.parseColor("#FF0000"));

                    holder.tax.setText(MyApplication.addDecimal(String.valueOf(transaction.getTax())));
                    holder.tax.setTextColor(Color.parseColor("#FF0000"));


                    holder.transReversed.setText(String.valueOf(transaction.isTransactionReversed()));
                holder.status.setText(transaction.getResultDescription());
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                Date date = null;
                date = inputFormat.parse(transaction.getCreationDate());
                String formattedDate = outputFormat.format(date);


                    String[] dateTemp = formattedDate.split("\\ ");

                    holder.transDate.setText(dateTemp[0]);



                }


        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        private LinearLayout mainLayout;
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

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}