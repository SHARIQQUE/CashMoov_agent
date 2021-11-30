package com.agent.cashmoovui.model;

public class NotificationModel {
    public int id;
    public String code;
    public String notificationCode;
    public String message;
    public String fcmToken;
    public String resultCode;
    public String resultDescription;
    public String status;
    public String creationDate;
    public String modificationDate;
    public String subject;
    public String wltOwnerCatCode;

    public NotificationModel(int id, String code, String notificationCode, String message, String fcmToken, String resultCode, String resultDescription, String status, String creationDate, String modificationDate, String subject, String wltOwnerCatCode) {
        this.id = id;
        this.code = code;
        this.notificationCode = notificationCode;
        this.message = message;
        this.fcmToken = fcmToken;
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
        this.status = status;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.subject = subject;
        this.wltOwnerCatCode = wltOwnerCatCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotificationCode() {
        return notificationCode;
    }

    public void setNotificationCode(String notificationCode) {
        this.notificationCode = notificationCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWltOwnerCatCode() {
        return wltOwnerCatCode;
    }

    public void setWltOwnerCatCode(String wltOwnerCatCode) {
        this.wltOwnerCatCode = wltOwnerCatCode;
    }
}
