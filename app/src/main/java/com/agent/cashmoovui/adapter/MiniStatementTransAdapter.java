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
import com.agent.cashmoovui.transactionhistory_walletscreen.TransactionHistoryMainPage;
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

     MiniStatementTrans miniStatementTrans;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
        miniStatementTrans = miniStatementTransList.get(position);

        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("100000")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashin);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("100001")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashout);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("101441")){
            holder.imgLogo.setImageResource(R.drawable.ic_moneytransfert);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("106449")){
            holder.imgLogo.setImageResource(R.drawable.ic_moneytransfert);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("104591")){
            holder.imgLogo.setImageResource(R.drawable.ic_lewallet);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("101777")){
            holder.imgLogo.setImageResource(R.drawable.ic_airtime);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("100005")){
            holder.imgLogo.setImageResource(R.drawable.ic_baseline_payment_24);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("106443")){
            holder.imgLogo.setImageResource(R.drawable.ic_cashpickup24);
        }
        if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("105068")){
            holder.imgLogo.setImageResource(R.drawable.icon_merchant);
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
//                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForInstitute())+" "+miniStatementTrans.getFromCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
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
//                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForAgent())+" "+miniStatementTrans.getFromCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
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
//                    if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
//                        holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
//                        holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForBranch())+" "+miniStatementTrans.getFromCurrencySymbol());
//                        holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
//                        return;
//                    }
//                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
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

        if(walletTypeCode.equalsIgnoreCase("100009")){
            if(miniStatementTrans.getFromWalletTypeCode().equalsIgnoreCase("100009")){
                holder.tvAmount.setTextColor(Color.parseColor("#D32F2F"));

                holder.tvAmount.setText(MyApplication.addDecimal(miniStatementTrans.getFromAmount()+"")+" "+ MyApplication.currencySymbol);
                MyApplication.Amount=	MyApplication.addDecimal(""+miniStatementTrans.getFromAmount());
                //  holder.tvAmount.setText(df.format(miniStatementTrans.getFromAmount())+" "+miniStatementTrans.getFromCurrencySymbol());
                holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
            }else{
                holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
                if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("106445")) {
                    holder.tvAmount.setText(	MyApplication.addDecimal(""+miniStatementTrans.getFromAmount())+" "+MyApplication.currencySymbol);
                    MyApplication.Amount=	MyApplication.addDecimal(""+miniStatementTrans.getFromAmount());
                   // holder.tvAmount.setText(df.format(miniStatementTrans.getFromAmount())+" "+miniStatementTrans.getFromCurrencySymbol());
                }else {
                    if(MyApplication.AgentPage){
                        MyApplication.Amount=	MyApplication.addDecimal(""+miniStatementTrans.getCommissionAmountForAgent());
                        holder.tvAmount.setText(	MyApplication.addDecimal(""+miniStatementTrans.getCommissionAmountForAgent()) + " " + MyApplication.currencySymbol);
                    }
                    if(MyApplication.InstPage){
                        MyApplication.Amount=	MyApplication.addDecimal(""+miniStatementTrans.getCommissionAmountForInstitute());
                        holder.tvAmount.setText(	MyApplication.addDecimal(""+miniStatementTrans.getCommissionAmountForInstitute()) + " " + MyApplication.currencySymbol);
                    }
                    if(MyApplication.BranchPage){
                        MyApplication.Amount=	MyApplication.addDecimal(""+miniStatementTrans.getCommissionAmountForBranch());
                        holder.tvAmount.setText(	MyApplication.addDecimal(""+miniStatementTrans.getCommissionAmountForBranch()) + " " + MyApplication.currencySymbol);
                    }

                    //holder.tvAmount.setText(df.format(miniStatementTrans.getCommissionAmountForInstitute()) + " " + miniStatementTrans.getToCurrencySymbol());
                }
                holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());

            }
        }

        if(walletTypeCode.equalsIgnoreCase("100008")){
            if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
                holder.tvAmount.setTextColor(Color.parseColor("#D32F2F"));
                //holder.tvAmount.setText(df.format(miniStatementTrans.getFromAmount())+" "+miniStatementTrans.getFromCurrencySymbol());
                holder.tvAmount.setText(	MyApplication.addDecimal(""+miniStatementTrans.getFromAmount())+" "+MyApplication.currencySymbol);
                holder.tvMsisdn.setText(miniStatementTrans.getToWalletOwnerMsisdn());
                MyApplication.Amount=	MyApplication.addDecimal(""+miniStatementTrans.getFromAmount());
            }
            if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
                holder.tvAmount.setTextColor(Color.parseColor("#388E3C"));
                if(miniStatementTrans.getTransactionTypeCode().equalsIgnoreCase("106445")) {
                    holder.tvAmount.setText(	MyApplication.addDecimal(""+miniStatementTrans.getFromAmount())+" "+MyApplication.currencySymbol);
                    MyApplication.Amount=	MyApplication.addDecimal(""+miniStatementTrans.getFromAmount());
                    // holder.tvAmount.setText(df.format(miniStatementTrans.getFromAmount())+" "+miniStatementTrans.getFromCurrencySymbol());
                }else {
                    holder.tvAmount.setText(	MyApplication.addDecimal(""+miniStatementTrans.getToAmount()) + " " + MyApplication.currencySymbol);
                    MyApplication.Amount=	MyApplication.addDecimal(""+miniStatementTrans.getToAmount());
                    // holder.tvAmount.setText(df.format(miniStatementTrans.getToAmount()) + " " + miniStatementTrans.getToCurrencySymbol());
                }
                holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerMsisdn());

            }



        }
        if(holder.tvMsisdn.getText().toString().isEmpty()){
            holder.tvMsisdn.setText(miniStatementTrans.getFromWalletOwnerName());

        }

        //}



        holder.linItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // MyApplication.showToast(MyApplication.getInstance(),holder.getAbsoluteAdapterPosition()+"");
                System.out.println("pososososo+  "+holder.getAbsoluteAdapterPosition());



                int pos=holder.getAbsoluteAdapterPosition();

                if(miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("105068")) {
                    call(miniStatementTrans.getFromAmount(),pos);
                }
                if(walletTypeCode.equalsIgnoreCase("100009")){
                    if(miniStatementTransList.get(pos).getFromWalletTypeCode().equalsIgnoreCase("100009")){
                        call(miniStatementTransList.get(pos).getFromAmount(),pos);
                    }else{

                        if(miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("106445")) {
                            call(miniStatementTrans.getFromAmount(),pos);
                        }else {
                            if(MyApplication.AgentPage){
                                call(miniStatementTransList.get(pos).getCommissionAmountForAgent(),pos);
                            }
                            if(MyApplication.InstPage){
                                call(miniStatementTransList.get(pos).getCommissionAmountForInstitute(),pos);
                            }
                            if(MyApplication.BranchPage){
                                call(miniStatementTransList.get(pos).getCommissionAmountForBranch(),pos);
                                }
                        }

                    }
                }

                if(walletTypeCode.equalsIgnoreCase("100008")){
                    if(miniStatementTransList.get(pos).getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
                        call(miniStatementTransList.get(pos).getFromAmount(),pos);
                    }
                    if(miniStatementTransList.get(pos).getToWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){

                        if(miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("106445")) {
                            call(miniStatementTransList.get(pos).getFromAmount(),pos);

                        }else {
                            call(miniStatementTransList.get(pos).getToAmount(),pos);
                        }


                    }



                }


                   /* if(miniStatementTrans.getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
                        miniStatemetListners.onMiniStatementListItemClick(miniStatementTrans.getTransactionTypeName(),
                                miniStatementTrans.getFromWalletOwnerName(),miniStatementTrans.getToWalletOwnerName(),
                                miniStatementTrans.getFromWalletOwnerMsisdn(),
                                miniStatementTrans.getFromCurrencySymbol(),
                                miniStatementTrans.getFromAmount(),miniStatementTrans.getTransactionId(),
                                miniStatementTrans.getCreationDate(), miniStatementTrans.getStatus(),0.0,
                                miniStatementTrans.getToWalletOwnerMsisdn(),miniStatementTrans.getTransactionAmount());
                        return;
                    }
                    if(miniStatementTrans.getToWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)){
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

*/
            }
        });

    }


    public void call(double Amount,int pos){
        MyApplication.currencySymbol=miniStatementTransList.get(pos).getFromCurrencySymbol();
        if(miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("100002")
        ||miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("100001")) {
            miniStatemetListners.onMiniStatementListItemClick(miniStatementTransList.get(pos).getTransactionTypeName(),
                    miniStatementTransList.get(pos).getFromWalletOwnerName(), miniStatementTransList.get(pos).getToWalletOwnerName(),
                    miniStatementTransList.get(pos).getFromWalletOwnerMsisdn(),
                    miniStatementTransList.get(pos).getFromCurrencySymbol(),
                    Amount, miniStatementTransList.get(pos).getTransactionId(),
                    miniStatementTransList.get(pos).getCreationDate(), miniStatementTransList.get(pos).getStatus(), 0.0,
                    miniStatementTransList.get(pos).getToWalletOwnerMsisdn(), miniStatementTransList.get(pos).getTransactionAmount(),
                    miniStatementTransList.get(pos).getFee(), miniStatementTransList.get(pos).
                            getTaxAsJson(), miniStatementTransList.get(pos).getDestPostBalance());
        }else  if(miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("101611")
        ||miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("101612")) {




            if(miniStatementTransList.get(pos).getFromWalletOwnerCode().equalsIgnoreCase(MyApplication.userCodeTransaction)) {
                miniStatemetListners.onMiniStatementListItemClick(miniStatementTransList.get(pos).getTransactionTypeName(),
                        miniStatementTransList.get(pos).getFromWalletOwnerName(), miniStatementTransList.get(pos).getToWalletOwnerName(),
                        miniStatementTransList.get(pos).getFromWalletOwnerMsisdn(),
                        miniStatementTransList.get(pos).getFromCurrencySymbol(),
                        Amount, miniStatementTransList.get(pos).getTransactionId(),
                        miniStatementTransList.get(pos).getCreationDate(), miniStatementTransList.get(pos).getStatus(), 0.0,
                        miniStatementTransList.get(pos).getToWalletOwnerMsisdn(), miniStatementTransList.get(pos).getTransactionAmount(),
                        miniStatementTransList.get(pos).getFee(), miniStatementTransList.get(pos).
                                getTaxAsJson(), miniStatementTransList.get(pos).getSrcPostBalance());


            }else{
                miniStatemetListners.onMiniStatementListItemClick(miniStatementTransList.get(pos).getTransactionTypeName(),
                        miniStatementTransList.get(pos).getFromWalletOwnerName(), miniStatementTransList.get(pos).getToWalletOwnerName(),
                        miniStatementTransList.get(pos).getFromWalletOwnerMsisdn(),
                        miniStatementTransList.get(pos).getFromCurrencySymbol(),
                        Amount, miniStatementTransList.get(pos).getTransactionId(),
                        miniStatementTransList.get(pos).getCreationDate(), miniStatementTransList.get(pos).getStatus(), 0.0,
                        miniStatementTransList.get(pos).getToWalletOwnerMsisdn(), miniStatementTransList.get(pos).getTransactionAmount(),
                        miniStatementTransList.get(pos).getFee(), miniStatementTransList.get(pos).
                                getTaxAsJson(), miniStatementTransList.get(pos).getDestPostBalance());
            }


        }else if(miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("113093")) {

            miniStatemetListners.onMiniStatementListItemClick(miniStatementTransList.get(pos).getTransactionTypeName(),
                    context.getString(R.string.commision_wallet), context.getString(R.string.main_Wallet_singlen),
                    "",
                    miniStatementTransList.get(pos).getFromCurrencySymbol(),
                    Amount, miniStatementTransList.get(pos).getTransactionId(),
                    miniStatementTransList.get(pos).getCreationDate(), miniStatementTransList.get(pos).getStatus(), 0.0,
                   "", miniStatementTransList.get(pos).getTransactionAmount(),
                    miniStatementTransList.get(pos).getFee(), miniStatementTransList.get(pos).
                            getTaxAsJson(), miniStatementTransList.get(pos).getDestPostBalance());

        }else if(miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("101441")){
            miniStatemetListners.onMiniStatementListItemClick(miniStatementTransList.get(pos).getTransactionTypeName(),
                    miniStatementTransList.get(pos).getFromWalletOwnerName(), miniStatementTransList.get(pos).getToWalletOwnerName(),
                    miniStatementTransList.get(pos).getFromWalletOwnerMsisdn(),
                    miniStatementTransList.get(pos).getFromCurrencySymbol(),
                    Amount, miniStatementTransList.get(pos).getTransactionId(),
                    miniStatementTransList.get(pos).getCreationDate(), miniStatementTransList.get(pos).getStatus(), 0.0,
                    miniStatementTransList.get(pos).getToWalletOwnerMsisdn(), miniStatementTransList.get(pos).getPrincipalAmount(),
                    miniStatementTransList.get(pos).getFee(), miniStatementTransList.get(pos).
                            getTaxAsJson(), miniStatementTransList.get(pos).getSrcPostBalance());

        }else if(miniStatementTransList.get(pos).getTransactionTypeCode().equalsIgnoreCase("101442")){
            //MyApplication.currencySymbol=miniStatementTransList.get(pos).getToCurrencySymbol();
            String taxJSON="";
            Double fee=0.00;
            if (miniStatementTransList.get(pos).isBearerSender()){
                taxJSON=miniStatementTransList.get(pos).getTaxAsJson();
                fee=miniStatementTransList.get(pos).getFee();
            }else{
                fee=0.00;
                taxJSON="";
            }
            miniStatemetListners.onMiniStatementListItemClick(miniStatementTransList.get(pos).getTransactionTypeName(),
                    miniStatementTransList.get(pos).getFromWalletOwnerName(), miniStatementTransList.get(pos).getToWalletOwnerName(),
                    miniStatementTransList.get(pos).getFromWalletOwnerMsisdn(),
                    miniStatementTransList.get(pos).getToCurrencySymbol(),
                    Amount, miniStatementTransList.get(pos).getTransactionId(),
                    miniStatementTransList.get(pos).getCreationDate(), miniStatementTransList.get(pos).getStatus(), 0.0,
                    miniStatementTransList.get(pos).getToWalletOwnerMsisdn(), miniStatementTransList.get(pos).getPrincipalAmount(),
                   fee, taxJSON
                           , miniStatementTransList.get(pos).getDestPostBalance());

        }else{
            miniStatemetListners.onMiniStatementListItemClick(miniStatementTransList.get(pos).getTransactionTypeName(),
                    miniStatementTransList.get(pos).getFromWalletOwnerName(), miniStatementTransList.get(pos).getToWalletOwnerName(),
                    miniStatementTransList.get(pos).getFromWalletOwnerMsisdn(),
                    miniStatementTransList.get(pos).getFromCurrencySymbol(),
                    Amount, miniStatementTransList.get(pos).getTransactionId(),
                    miniStatementTransList.get(pos).getCreationDate(), miniStatementTransList.get(pos).getStatus(), 0.0,
                    miniStatementTransList.get(pos).getToWalletOwnerMsisdn(), miniStatementTransList.get(pos).getTransactionAmount(),
                    miniStatementTransList.get(pos).getFee(), miniStatementTransList.get(pos).
                            getTaxAsJson(), miniStatementTransList.get(pos).getSrcPostBalance());
        }
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