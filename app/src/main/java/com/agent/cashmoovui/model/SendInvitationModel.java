package com.agent.cashmoovui.model;

public class SendInvitationModel {

    int img_sendinvitation;
    String  tv_sendinvitation;

    public SendInvitationModel(int img_sendinvitation, String tv_sendinvitation) {
        this.img_sendinvitation = img_sendinvitation;
        this.tv_sendinvitation = tv_sendinvitation;
    }

    public int getImg_sendinvitation() {
        return img_sendinvitation;
    }

    public void setImg_sendinvitation(int img_sendinvitation) {
        this.img_sendinvitation = img_sendinvitation;
    }

    public String getTv_sendinvitation() {
        return tv_sendinvitation;
    }

    public void setTv_sendinvitation(String tv_sendinvitation) {
        this.tv_sendinvitation = tv_sendinvitation;
    }
}
