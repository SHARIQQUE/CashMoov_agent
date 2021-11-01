package com.agent.cashmoovui.apiCalls;

import org.json.JSONObject;

/**
 * Created by Rahul Singh on 22,September,2020
 * Aditya Infotech Pvt Ltd,
 */
public interface Api_Responce_Handler {

     void success(JSONObject jsonObject);
     void failure(String aFalse);
}
