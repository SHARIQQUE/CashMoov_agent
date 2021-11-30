package com.agent.cashmoovui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agent.cashmoovui.MainActivity;
import com.agent.cashmoovui.MyApplication;
import com.agent.cashmoovui.R;
import com.agent.cashmoovui.adapter.NotificationListAdapter;
import com.agent.cashmoovui.apiCalls.API;
import com.agent.cashmoovui.apiCalls.Api_Responce_Handler;
import com.agent.cashmoovui.model.NotificationModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class NotificationList extends AppCompatActivity {
    public static NotificationList notificationlistC;
    ImageView imgBack,imgHome;
    RecyclerView rvNotification;
    private List<NotificationModel> notificationModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        notificationlistC=this;
        setBackMenu();
        getIds();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        rvNotification = findViewById(R.id.rvNotification);

        callApiNotificationList();

    }

    private void callApiNotificationList() {
        try {
            API.GET_PUBLIC("ewallet/api/v1/inappholding/"+ MyApplication.getSaveString("walletOwnerCode",notificationlistC),
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            //   MyApplication.hideLoader();

                            if (jsonObject != null) {
                                notificationModelArrayList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("appHoldinglist");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        notificationModelArrayList.add(new NotificationModel(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("notificationCode"),
                                                data.optString("message"),
                                                data.optString("fcmToken"),
                                                data.optString("resultCode"),
                                                data.optString("resultDescription"),
                                                data.optString("status"),
                                                data.optString("creationDate"),
                                                data.optString("modificationDate"),
                                                data.optString("subject"),
                                                data.optString("wltOwnerCatCode")

                                        ));
                                    }

                                   setData(notificationModelArrayList);

                                } else {
                                    MyApplication.showToast(notificationlistC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });

        } catch (Exception e) {

        }

    }


    private void setData(List<NotificationModel> notificationModelArrayList){
        NotificationListAdapter notificationListAdapter = new NotificationListAdapter(notificationlistC,notificationModelArrayList);
        rvNotification.setHasFixedSize(true);
        rvNotification.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvNotification.setAdapter(notificationListAdapter);
    }

}