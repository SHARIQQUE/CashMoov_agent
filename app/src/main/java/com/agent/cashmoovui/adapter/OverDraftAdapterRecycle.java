package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.overdraft.OverDraftModal;
import com.agent.cashmoovui.transfer_float.CallBackSellFloatRecycleViewClick;
import com.agent.cashmoovui.transfer_float.SellFloatModal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OverDraftAdapterRecycle extends RecyclerView.Adapter<OverDraftAdapterRecycle.ViewHolder> {
    String finalDate;
    private Context context;

    ArrayList<OverDraftModal> arrayList_modalUserData;

    public OverDraftAdapterRecycle(Context context, ArrayList<OverDraftModal> arrayList_modalUserData) {
        this.context = context;
        this.arrayList_modalUserData = arrayList_modalUserData;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.currencyName.setText(arrayList_modalUserData.get(i).getCurrencyName());
        viewHolder.amount.setText(arrayList_modalUserData.get(i).getCurrencySymbol()+" "+ MyApplication.addDecimal(arrayList_modalUserData.get(i).getAmount()));
        viewHolder.status_textview.setText(arrayList_modalUserData.get(i).getStatus());

        if(arrayList_modalUserData.get(i).getStatus().equalsIgnoreCase("Closed")){
            viewHolder.rowLinear.setVisibility(View.GONE);
        }else {
            viewHolder.rowLinear.setVisibility(View.VISIBLE);

        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            Date date = null;
            date = inputFormat.parse(arrayList_modalUserData.get(i).getCreationDate());
            String formattedDate = outputFormat.format(date);
            viewHolder.datetime_textview.setText(formattedDate);
            Date date1 = inputFormat.parse(arrayList_modalUserData.get(i).getDuaDate());
            String dueDate = outputFormat.format(date1);
            viewHolder.dueDate_textview.setText(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList_modalUserData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.overdraft_row_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView currencyName, amount,status_textview,datetime_textview,dueDate_textview;
        Button action_click;
        public LinearLayout rowLinear;

        public ViewHolder(View itemView) {
            super(itemView);

            currencyName = (TextView) itemView.findViewById(R.id.currencyName);
            amount = (TextView) itemView.findViewById(R.id.amount);
            rowLinear=(LinearLayout)itemView.findViewById(R.id.rowLinear);
            status_textview = (TextView) itemView.findViewById(R.id.status_textview);
            datetime_textview = (TextView) itemView.findViewById(R.id.datetime_textview);
            dueDate_textview= (TextView) itemView.findViewById(R.id.dueDate_textview);


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
