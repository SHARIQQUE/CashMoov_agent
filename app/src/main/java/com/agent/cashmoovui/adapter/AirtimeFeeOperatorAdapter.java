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
import com.agent.cashmoovui.listeners.OperatorAirtimeFeeListeners;
import com.agent.cashmoovui.model.OperatorModel;
import java.util.ArrayList;
import java.util.List;

public class AirtimeFeeOperatorAdapter extends RecyclerView.Adapter<AirtimeFeeOperatorAdapter.ViewHolder>{
    private Context context;
    private List<OperatorModel> operatorList = new ArrayList<>();
    private OperatorAirtimeFeeListeners operatorAirtimeFeeListeners;

    public AirtimeFeeOperatorAdapter(Context context, List<OperatorModel> operatorList) {
        this.context = context;
        this.operatorList = operatorList;
        operatorAirtimeFeeListeners = (OperatorAirtimeFeeListeners) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_airtime_fee, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OperatorModel opearatorModel = operatorList.get(position);
        holder.txt1.setText(opearatorModel.getName());
        if(opearatorModel.getCode().equalsIgnoreCase("100051")){
            holder.imgLogo.setImageResource(R.drawable.mtn);
        } if(opearatorModel.getCode().equalsIgnoreCase("100050")){
            holder.imgLogo.setImageResource(R.drawable.cellcom);
        }
        if(opearatorModel.getCode().equalsIgnoreCase("100049")){
            holder.imgLogo.setImageResource(R.drawable.orange);
        }

        holder.linOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(opearatorModel.getCode()!=null)
                 operatorAirtimeFeeListeners.onOperatorAirtimeFeeListItemClick(opearatorModel.getCode(),opearatorModel.getName());
            }
        });
        holder.txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(opearatorModel.getCode()!=null)
                    operatorAirtimeFeeListeners.onOperatorAirtimeFeeListItemClick(opearatorModel.getCode(),opearatorModel.getName());
            }
        });
    }


    @Override
    public int getItemCount() {
        return operatorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linOperator;
        private ImageView imgLogo;
        private TextView txt1;
        public ViewHolder(View itemView) {
            super(itemView);
            linOperator = itemView.findViewById(R.id.linOperator);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            txt1 = itemView.findViewById(R.id.txt1);
        }
    }
}