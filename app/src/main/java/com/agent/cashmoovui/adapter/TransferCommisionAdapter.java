package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.R;
import com.agent.cashmoovui.model.transaction.CurrencyModel;

import java.util.ArrayList;


public class TransferCommisionAdapter extends RecyclerView.Adapter<TransferCommisionAdapter.ViewHolder> {
    String finalDate;
    private Context context;

    ArrayList<CurrencyModel> arrayList_modalUserData;




    public TransferCommisionAdapter(Context context, ArrayList<CurrencyModel> arrayList_modalUserData) {
        this.context = context;
        this.arrayList_modalUserData = arrayList_modalUserData;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.walletOwnerName_textview.setText(arrayList_modalUserData.get(i).getWalletOwnerName());
        viewHolder.currencyName_textview.setText(arrayList_modalUserData.get(i).getCurrencyName());


        viewHolder.mainWallet_textview.setText(arrayList_modalUserData.get(i).getMainWalletValue());
        viewHolder.acommision_textview.setText(arrayList_modalUserData.get(i).getCommisionWalletValue());

       // viewHolder.mainWallet_textview.setText(value_string);
      //  viewHolder.acommision_textview.setText(value_string);





    }

    @Override
    public int getItemCount() {
        return arrayList_modalUserData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.commision_row_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView walletOwnerName_textview,currencyName_textview,mainWallet_textview,acommision_textview;

        public ViewHolder(View itemView) {
            super(itemView);

            walletOwnerName_textview = (TextView) itemView.findViewById(R.id.walletOwnerName_textview);
            currencyName_textview = (TextView) itemView.findViewById(R.id.currencyName_textview);
            mainWallet_textview = (TextView) itemView.findViewById(R.id.mainWallet_textview);
            acommision_textview = (TextView) itemView.findViewById(R.id.acommision_textview);




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
