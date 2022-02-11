package com.agent.cashmoovui.apiCalls;

import org.json.JSONObject;

/**
 * Created by Abhay Singh on 22,September,2020
 * Estel Technology,
 */
public interface Api_Responce_Handler {

     void success(JSONObject jsonObject);
     void failure(String aFalse);
}
