package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.model.FeeDetailModel;
import com.google.zxing.common.StringUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FeeDetailsAdapter extends RecyclerView.Adapter<FeeDetailsAdapter.ViewHolder>{
    private Context context;
    private List<FeeDetailModel> feeDetailsList = new ArrayList<>();

    public FeeDetailsAdapter(Context context, List<FeeDetailModel> feeDetailsList) {
        this.context = context;
        this.feeDetailsList = feeDetailsList;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_fee_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
        FeeDetailModel feeDetailModel = feeDetailsList.get(position);


        holder.tvRange.setText(feeDetailModel.getRange());
        if(feeDetailModel.getValue().contains("%")){
            String string = feeDetailModel.getValue().substring(0, (feeDetailModel.getValue().length() - 1));

            holder.tvValue.setText(((MyApplication.addDecimal(string)+"%")));

        }else{
            if(feeDetailModel.getValue().length()==0){
                //holder.tvValue.setText((feeDetailModel.getValue()));

            }else{
                holder.tvValue.setText(MyApplication.addDecimal(feeDetailModel.getValue()));

            }

        }

    }


    @Override
    public int getItemCount() {
        return feeDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRange,tvValue;
        public ViewHolder(View itemView) {
            super(itemView);
            tvRange = itemView.findViewById(R.id.tvRange);
            tvValue = itemView.findViewById(R.id.tvValue);
        }
    }
}