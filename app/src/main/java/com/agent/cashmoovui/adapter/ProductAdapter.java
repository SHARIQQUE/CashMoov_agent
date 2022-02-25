package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.listeners.ProductListeners;
import com.agent.cashmoovui.model.ProductModel;
import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context context;
    private List<ProductModel> productList = new ArrayList<>();
    private ProductListeners productListeners;

    public ProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
        productListeners = (ProductListeners) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductModel productModel = productList.get(position);
        holder.tvProductName.setText(productModel.getName());
        if(productModel.getOperatorCode().equalsIgnoreCase("100055")){
            holder.ivProductLogo.setImageResource(R.drawable.canalplus);
        }
        if(productModel.getOperatorCode().equalsIgnoreCase("100046")){
            holder.ivProductLogo.setImageResource(R.drawable.startimeslogo);
        }
        holder.linProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productModel.getCode()!=null)
                 productListeners.onProductListItemClick(productModel.getCode(),productModel.getName());
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linProduct;
        private ImageView ivProductLogo;
        private TextView tvProductName;
        public ViewHolder(View itemView) {
            super(itemView);
            linProduct = itemView.findViewById(R.id.linProduct);
            ivProductLogo = itemView.findViewById(R.id.ivProductLogo);
            tvProductName = itemView.findViewById(R.id.tvProductName);
        }
    }
}