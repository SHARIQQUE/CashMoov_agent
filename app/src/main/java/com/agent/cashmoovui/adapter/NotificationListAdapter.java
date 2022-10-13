package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.model.NotificationModel;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder>{
    private Context context;
    private List<NotificationModel> notificationArrayList = new ArrayList<>();

    public NotificationListAdapter(Context context, List<NotificationModel> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_notification, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
        final NotificationModel notificationModel = notificationArrayList.get(position);

        holder.tvSubject.setText(notificationModel.getSubject());

        holder.tvMessage.setText(Html.fromHtml(notificationModel.getMessage()));
        //holder.tvCreationDate.setText(notificationModel.getCreationDate());

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            Date date = null;
            date = inputFormat.parse(notificationModel.getCreationDate());
            String formattedDate = outputFormat.format(date);
            holder.tvCreationDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSubject,tvMessage,tvCreationDate;
        public ViewHolder(View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvCreationDate = itemView.findViewById(R.id.tvCreationDate);
        }
    }
}