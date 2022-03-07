package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.listeners.PlanListeners;
import com.agent.cashmoovui.model.ProductModel;
import java.util.ArrayList;
import java.util.List;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder>{
    private Context context;
    private List<ProductModel> productList = new ArrayList<>();
    private PlanListeners planListeners;

    public PlanListAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
        planListeners = (PlanListeners) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_plan_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductModel productModel = productList.get(position);
        holder.tvDescription.setText(productModel.getDescription());
        holder.tvAmount.setText(MyApplication.addDecimal(String.valueOf(productModel.getValue())));

        holder.linPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productModel.getProductMasterCode()!=null)
                 planListeners.onPlanListItemClick(productModel.getCode(),productModel.getProductTypeCode(),productModel.getValue());
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linPlan;
        private TextView tvDescription,tvAmount;
        public ViewHolder(View itemView) {
            super(itemView);
            linPlan = itemView.findViewById(R.id.linPlan);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}