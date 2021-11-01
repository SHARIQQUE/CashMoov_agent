package com.agent.cashmoovui.model;

public class RecentReceiptsModel {

    int img_sendmoney;
    String  tv_sendmoney;

    public RecentReceiptsModel(int img_sendmoney, String tv_sendmoney) {
        this.img_sendmoney = img_sendmoney;
        this.tv_sendmoney = tv_sendmoney;
    }

    public int getImg_sendmoney() {
        return img_sendmoney;
    }

    public void setImg_sendmoney(int img_sendmoney) {
        this.img_sendmoney = img_sendmoney;
    }

    public String getTv_sendmoney() {
        return tv_sendmoney;
    }

    public void setTv_sendmoney(String tv_sendmoney) {
        this.tv_sendmoney = tv_sendmoney;
    }
}
