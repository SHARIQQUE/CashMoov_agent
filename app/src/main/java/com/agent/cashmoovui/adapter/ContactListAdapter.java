package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.listeners.ContactListLisners;
import com.agent.cashmoovui.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{
    private Context context;
    private List<Contact> contactArrayList = new ArrayList<>();
    private ContactListLisners contactListLisners;

    public ContactListAdapter(Context context, List<Contact> contactArrayList) {
        this.context = context;
        this.contactArrayList = contactArrayList;
        contactListLisners = (ContactListLisners) context;

    }

    public void onDataChange(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Contact contactModel = contactArrayList.get(position);

        holder.tvName.setText(contactModel.getName());
        holder.tvMobile.setText(contactModel.getPhoneNumber());

        holder.linItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - MyApplication.mLastClickTime < 1000) { // 1000 = 1second
                    return;
                }
                MyApplication.mLastClickTime = SystemClock.elapsedRealtime();
                    contactListLisners.onContactViewItemClick(contactModel.getName(),
                            contactModel.getPhoneNumber());
            }
        });
    }


    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linItem;
        private CircleImageView imgContact;
        private TextView tvName,tvMobile;
        public ViewHolder(View itemView) {
            super(itemView);
            linItem = itemView.findViewById(R.id.linItem);
            imgContact = itemView.findViewById(R.id.imgContact);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
        }
    }
}