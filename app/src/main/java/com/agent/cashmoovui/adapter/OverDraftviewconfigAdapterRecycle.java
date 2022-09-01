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
import com.agent.cashmoovui.model.OverdraftViewconfigModel;
import com.agent.cashmoovui.overdraft.OverDraftModal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OverDraftviewconfigAdapterRecycle extends RecyclerView.Adapter<OverDraftviewconfigAdapterRecycle.ViewHolder> {
    String finalDate;
    private Context context;

    ArrayList<OverdraftViewconfigModel> arrayList_overdraftviewconfiglist;

    public OverDraftviewconfigAdapterRecycle(Context context, ArrayList<OverdraftViewconfigModel> arrayList_overdraftviewconfig) {
        this.context = context;
        this.arrayList_overdraftviewconfiglist = arrayList_overdraftviewconfig;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.channeltypeText.setText(arrayList_overdraftviewconfiglist.get(i).getChannelTypeName());
        viewHolder.servicenameText.setText(arrayList_overdraftviewconfiglist.get(i).getEwalletServicesName());
        viewHolder.servicecatnameText.setText(arrayList_overdraftviewconfiglist.get(i).getServicesCategoryName());
        viewHolder.serviceprovidernameText.setText(arrayList_overdraftviewconfiglist.get(i).getServicesProviderName());
        viewHolder.status.setText(arrayList_overdraftviewconfiglist.get(i).getStatus());


    }

    @Override
    public int getItemCount() {
        return arrayList_overdraftviewconfiglist.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.overdraft_viewconfig, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView channeltypeText, servicenameText,servicecatnameText,serviceprovidernameText,status;
        Button action_click;
        public LinearLayout rowLinear;

        public ViewHolder(View itemView) {
            super(itemView);

            channeltypeText = (TextView) itemView.findViewById(R.id.channeltypeText);
            servicenameText = (TextView) itemView.findViewById(R.id.servicenameText);
            servicecatnameText=(TextView)itemView.findViewById(R.id.servicecatnameText);
            serviceprovidernameText = (TextView) itemView.findViewById(R.id.serviceprovidernameText);
            status = (TextView) itemView.findViewById(R.id.statusText);


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
