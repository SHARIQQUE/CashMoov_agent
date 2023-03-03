package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.listeners.OperatorListeners;
import com.agent.cashmoovui.model.OperatorModel;
import java.util.ArrayList;
import java.util.List;


public class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.ViewHolder>{
    private Context context;
    private List<OperatorModel> operatorList = new ArrayList<>();
    private OperatorListeners operatorListners;

    public OperatorAdapter(Context context, List<OperatorModel> operatorList) {
        this.context = context;
        this.operatorList = operatorList;
        operatorListners = (OperatorListeners) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_payments_operator, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OperatorModel opearatorModel = operatorList.get(position);
        holder.tvOperatorName.setText(opearatorModel.getName());
        if(opearatorModel.getCode().equalsIgnoreCase("100055")){
            holder.ivOperatorLogo.setImageResource(R.drawable.canalplus);
        }
        if(opearatorModel.getCode().equalsIgnoreCase("100046")){
            holder.ivOperatorLogo.setImageResource(R.drawable.startimeslogo);
        }
        holder.linOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                if(opearatorModel.getCode()!=null)
                 operatorListners.onOperatorListItemClick(opearatorModel.getCode(),opearatorModel.getName());
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