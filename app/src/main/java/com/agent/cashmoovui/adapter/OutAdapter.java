package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.agent.cashmoovui.R;
import com.agent.cashmoovui.listeners.OperatorListenersnew;
import com.agent.cashmoovui.model.OutTransferModel;

import java.util.ArrayList;
import java.util.List;


public class OutAdapter extends RecyclerView.Adapter<OutAdapter.ViewHolder>{
    private Context context;
    private List<OutTransferModel.OutModel> operatorList = new ArrayList<>();
    private OperatorListenersnew operatorListners;

    public OutAdapter(Context context, List<OutTransferModel.OutModel> operatorList) {
        this.context = context;
        this.operatorList = operatorList;
        operatorListners = (OperatorListenersnew) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_bill_pay_operator, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OutTransferModel.OutModel opearatorModel = operatorList.get(position);
        holder.tvOperatorName.setText(opearatorModel.getName());
        if(opearatorModel.getServiceItemId().equalsIgnoreCase("65")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_five);
        }
        if(opearatorModel.getServiceItemId().equalsIgnoreCase("14")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_fourteen);
        }
        if(opearatorModel.getServiceItemId().equalsIgnoreCase("17")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_seventeen);
        }
        if(opearatorModel.getServiceItemId().equalsIgnoreCase("5")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_five);
        }

        //IN

        if(opearatorModel.getServiceItemId().equalsIgnoreCase("43")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_fourtythree);
        }

        if(opearatorModel.getServiceItemId().equalsIgnoreCase("69")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_68);
        }

        if(opearatorModel.getServiceItemId().equalsIgnoreCase("19")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_nineteen);
        }

        if(opearatorModel.getServiceItemId().equalsIgnoreCase("18")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_eighteen);
        }

        if(opearatorModel.getServiceItemId().equalsIgnoreCase("15")){
            holder.ivOperatorLogo.setImageResource(R.drawable.item_fifteen);
        }
        holder.linOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(opearatorModel.getCode()!=null)
                 operatorListners.onOperatorListItemClick(opearatorModel.getCode(),opearatorModel.getName(),
                         opearatorModel.getServiceItemId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return operatorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linOperator;
        private ImageView ivOperatorLogo;
        private TextView tvOperatorName;
        public ViewHolder(View itemView) {
            super(itemView);
            linOperator = itemView.findViewById(R.id.linOperator);
            ivOperatorLogo = itemView.findViewById(R.id.ivOperatorLogo);
            tvOperatorName = itemView.findViewById(R.id.tvOperatorName);
        }
    }
}