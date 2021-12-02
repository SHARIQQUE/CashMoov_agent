package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.listeners.MiniStatemetListners;
import com.agent.cashmoovui.model.MiniStatementTrans;
import com.agent.cashmoovui.wallet_owner.WalletOwnerMenu;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MiniStatementTransAdapter extends RecyclerView.Adapter<MiniStatementTransAdapter.ViewHolder>{
    private Context context;
    private List<MiniStatementTrans> miniStatementTransList = new ArrayList<>();
    private MiniStatemetListners miniStatemetListners;
    private String walletTypeCode;

    public MiniStatementTransAdapter(Context context, List<MiniStatementTrans> miniStatementTransList, String walletTypeCode) {
        this.context = context;
        this.miniStatementTransList = miniStatementTransList;
        this.walletTypeCode = walletTypeCode;
        miniStatemetListners = (MiniStatemetListners) context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_wallet, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
        final MiniStatementTrans miniStatementTrans = miniStatementTransList.get(position);

        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Cash-in")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashin);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Cash-out")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashout);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Remit Send")){
            holder.imgLogo.setImageResource(R.drawable.ic_moneytransfert);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Wallet Transfer")){
            holder.imgLogo.setImageResource(R.drawable.ic_lewallet);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Airtime Purchase")){
            holder.imgLogo.setImageResource(R.drawable.ic_rechargement);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Remit Send Reversal")){
            holder.imgLogo.setImageResource(R.drawable.ic_moneytransfert);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Recharge & Payment")){
            holder.imgLogo.setImageResource(R.drawable.ic_baseline_payment_24);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Cash PickUp")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashpickup24);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Pay")){
            holder.imgLogo.setImageResource(R.drawable.ic_paimentfacture);
        }
        if(miniStatementTrans.getTransactionTypeName().equalsIgnoreCase("Withdrawal")){
            holder.imgLogo.setImageResource(R.drawable.ic_paymane24);
        }

        holder.tvTransType.setText(miniStatementTrans.getTransactionTypeName());
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            date = inputFormat.parse(miniStatementTrans.getCreationDate());
            String formattedDate = outputFormat.format(date);
            holder.tvCreationDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        if(walletTypeCode.equalsIgnoreCase("100009")){
//            if(miniStatementTrans.isReverse()){
//            }else {
//                if(MyApplication.getSaveString("walletOwnerCategoryCode",context).equalsIgnoreCase(MyApplication.InstituteCode)){
//                    if(miniStatementTrans.getFromWalletTypeCode().equalsIgnoreCase("100009")){
//                        holder.tvAmount.setTextColor(Color.parseColor("#D32F2F"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getToAmount())+" "+miniStatementTrans.getToCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForInstitute())+" "+miniStatementTrans.getFromCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForInstitute())+" "+miniStatementTrans.getToCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());
//                        return;
//                    }
//
//                    if(holder.tvMsisdn.getText().toString().isEmpty()){
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerName());
//                        return;
//                    }
//                }
//                if(MyApplication.getSaveString("walletOwnerCategoryCode",context).equalsIgnoreCase(MyApplication.AgentCode)){
//                    if(miniStatementTrans.getFromWalletTypeCode().equalsIgnoreCase("100009")){
//                        holder.tvAmount.setTextColor(Color.parseColor("#D32F2F"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getToAmount())+" "+miniStatementTrans.getToCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForAgent())+" "+miniStatementTrans.getFromCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForAgent())+" "+miniStatementTrans.getToCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());
//                        return;
//                    }
//
//                    if(holder.tvMsisdn.getText().toString().isEmpty()){
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerName());
//                        return;
//                    }
//                }
//                if(MyApplication.getSaveString("walletOwnerCategoryCode",context).equalsIgnoreCase(MyApplication.BranchCode)){
//                    if(miniStatementTrans.getFromWalletTypeCode().equalsIgnoreCase("100009")){
//                        holder.tvAmount.setTextColor(Color.parseColor("#D32F2F"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getToAmount())+" "+miniStatementTrans.getToCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForBranch())+" "+miniStatementTrans.getFromCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForBranch())+" "+miniStatementTrans.getToCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());
//                        return;
//                    }
//
//                    if(holder.tvMsisdn.getText().toString().isEmpty()){
//                        holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerName());
//                        return;
//                    }
//
//                }
//            }
//        }else{
            if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                holder.tvAmount.setTextColor(Color.parseColor("#D32F2F"));
                holder.tvAmount.setText(df.format(miniStatementTrans.getFromAmount())+" "+miniStatementTrans.getFromCurrencySymbol());
                holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());

            }
            if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
                holder.tvAmount.setText(df.format(miniStatementTrans.getToAmount())+" "+miniStatementTrans.getToCurrencySymbol());
                holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());

            }

            if(holder.tvMsisdn.getText().toString().isEmpty()){
                holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerName());

            }

        //}



        holder.linItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  if(miniStatementTrans.getCode()!=null)
//                if(walletTypeCode.equalsIgnoreCase("100009")){
//                    if(miniStatementTrans.isReverse()){
//
//                    }else{
//                        if(MyApplication.getSaveString("walletOwnerCategoryCode", context).equalsIgnoreCase(MyApplication.InstituteCode)){
//                            if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                                miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                        miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getFromCurrencySymbol(),
//                                        miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),
//                                        miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForInstitute());
//                                return;
//                            }
//                            if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                                miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                        miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
//                                        miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
//                                        miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForInstitute());
//                                return;
//                            }
//                            miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                    miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
//                                    miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
//                                    miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForInstitute());
//                                return;
//                        }
//                        if(MyApplication.getSaveString("walletOwnerCategoryCode",context).equalsIgnoreCase(MyApplication.AgentCode)){
//                            if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                                miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                        miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getFromCurrencySymbol(),
//                                        miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),
//                                        miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForAgent());
//                                return;
//                            }
//                            if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                                miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                        miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
//                                        miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
//                                        miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForAgent());
//                                return;
//                            }
//                            miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                    miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
//                                    miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
//                                    miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForAgent());
//                                return;
//                        }
//                        if(MyApplication.getSaveString("walletOwnerCategoryCode",context).equalsIgnoreCase(MyApplication.BranchCode)){
//                            if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                                miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                        miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getFromCurrencySymbol(),
//                                        miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),
//                                        miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForBranch());
//                                return;
//                            }
//                            if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
//                                miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                        miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
//                                        miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
//                                        miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForBranch());
//                                return;
//                            }
//                            miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
//                                    miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),holder.tvMsisdn.getText().toString().trim(),miniStatementTrans.getToCurrencySymbol(),
//                                    miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
//                                    miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),miniStatementTrans.getCommissionAmountForBranch());
//
//                                return;
//                        }
//
//                    }
//                    return;
//                }

                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                        miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                                miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),
                                miniStatementTrans.getFromWalletOwnerMsisdn(),
                                miniStatementTrans.getFromCurrencySymbol(),
                                miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),
                                miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),0.0,
                                miniStatementTrans.getToWalletOwnerMsisdn(),miniStatementTrans.getTransactionAmount());
                        return;
                    }
                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.getSaveString("walletOwnerCode",context))){
                        miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                                miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),
                                miniStatementTrans.getFromWalletOwnerMsisdn(),miniStatementTrans.getToCurrencySymbol(),
                                miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
                                miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),0.0,
                                miniStatementTrans.getToWalletOwnerMsisdn(),miniStatementTrans.getTransactionAmount());
                        return;
                    }
                miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                        miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),
                        miniStatementTrans.getFromWalletOwnerMsisdn(),miniStatementTrans.getToCurrencySymbol(),
                        miniStatementTrans.getToAmount(),miniStatementTrans.getTransactionId(),
                        miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),0.0,
                        miniStatementTrans.getToWalletOwnerMsisdn(),miniStatementTrans.getTransactionAmount());


            }
        });

    }


    @Override
    public int getItemCount() {
        return miniStatementTransList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linItem;
        private ImageView imgLogo;
        private TextView tvTransType,tvMsisdn,tvAmount,tvCreationDate;
        public ViewHolder(View itemView) {
            super(itemView);
            linItem = itemView.findViewById(R.id.linItem);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            tvTransType = itemView.findViewById(R.id.tvTransType);
            tvMsisdn = itemView.findViewById(R.id.tvMsisdn);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCreationDate = itemView.findViewById(R.id.tvCreationDate);
        }
    }
}