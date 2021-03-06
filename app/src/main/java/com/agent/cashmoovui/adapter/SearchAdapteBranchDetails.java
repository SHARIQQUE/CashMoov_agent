package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.R;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.model.UserDetailAgent;
import com.agent.cashmoovui.model.UserDetailBranch;
import com.agent.cashmoovui.transactionhistory_walletscreen.CallBackRecycleViewClick;

import java.util.ArrayList;
import java.util.Locale;


public class SearchAdapteBranchDetails extends RecyclerView.Adapter<SearchAdapteBranchDetails.ViewHolder> {
    String finalDate;
    private Context context;
    CallBackRecycleViewClick callBackRecycleViewClick;


    ArrayList<UserDetailBranch> arrayList_modalUserData;
    private ArrayList<UserDetailBranch> arrayListTemp = null;


    public SearchAdapteBranchDetails(Context context, ArrayList<UserDetailBranch> arrayList_modalUserData, CallBackRecycleViewClick callBackRecycleViewClick) {
        this.context = context;
        this.arrayList_modalUserData = arrayList_modalUserData;

        this.arrayListTemp = new ArrayList<>();

        arrayListTemp.addAll(arrayList_modalUserData);

        this.callBackRecycleViewClick = callBackRecycleViewClick;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.agentName_textview.setText(arrayList_modalUserData.get(i).getOwnerName());
        viewHolder.agent_mobileNumber.setText(arrayList_modalUserData.get(i).getMobileNumber());
        viewHolder.email_textview.setText(arrayList_modalUserData.get(i).getEmail());
        viewHolder.countryName_textview.setText(arrayList_modalUserData.get(i).getEmail());


        viewHolder.linBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackRecycleViewClick.callBackReycleView(arrayList_modalUserData.get(i).getWalletOwnerCode(),
                        arrayList_modalUserData.get(i).getRegisterCountryCode());

            }
        });
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
        View listItem = layoutInflater.inflate(R.layout.search_transaction_branch, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout linBranch;
        public TextView agentName_textview, agent_mobileNumber,email_textview,countryName_textview;

        public ViewHolder(View itemView) {
            super(itemView);

            linBranch = itemView.findViewById(R.id.linBranch);
            agentName_textview = (TextView) itemView.findViewById(R.id.agentName_textview);
            agent_mobileNumber = (TextView) itemView.findViewById(R.id.agent_mobileNumber);
            email_textview = (TextView) itemView.findViewById(R.id.email_textview);
            countryName_textview = (TextView) itemView.findViewById(R.id.countryName_textview);


        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayList_modalUserData.clear();
        if (charText.length() == 0) {
            arrayList_modalUserData.addAll(arrayListTemp);
        } else {
            for (UserDetailBranch wp : arrayListTemp) {
                if (wp.getOwnerName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList_modalUserData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
