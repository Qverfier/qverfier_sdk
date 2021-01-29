package com.utility;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static com.utility.Constant.ACTION_TYPE;
import static com.utility.Constant.App_Key;
import static com.utility.Constant.Device_Id;
import static com.utility.Constant.MobileNumber;
import static com.utility.Constant.Secret_Key;


public class JSON_Data {
    static public String press_back="{ \"message\": \"MobileNumber CANCEL\",\"status\": \"error\",\"code\": \"505\",\"phonenumber\": \"\",\"action\": \"\"}";
   static public String success_data="{ \"message\": \"Authentication Success\",\"status\": \"Success\",\"code\": \"200\",\"phonenumber\": \""+ MobileNumber+"\"}";
   static public String progress_sms="{ \"message\": \"Your Request on Progress\",\"status\": \"Progress\",\"code\": \"201\",\"phonenumber\": \""+ MobileNumber+"\",\"action\": \"SMS\"}";
   static public String progress_call="{ \"message\": \"Your Request on Progress\",\"status\": \"Progress\",\"code\": \"201\",\"phonenumber\": \""+ MobileNumber+"\",\"action\": \"MISSED_CALL\"}";
   static public String progress_voice_otp="{ \"message\": \"Your Request on Progress\",\"status\": \"Progress\",\"code\": \"201\",\"phonenumber\": \""+ MobileNumber+"\",\"action\": \"VOICE_OTP\"}";

  /*  static public String success_data="{\n" +
            "    \"phonenumber\": \""+AuthAction.MobileNumber+"\",\n" +
            "    \"status\": \"Success\",\n" +
            "\t\"Action\": \""+AuthAction.ACTION_TYPE+"\"\n" +
            "}";*/
    static public String MobileNumber_error="{ \"message\": \"MOBILE ACCESS NOT AVAILABLE\",\"status\": \"error\",\"code\": \"401\",\"phonenumber\": \"\",\"action\": \"\"}";
    static public String api_error="{ \"message\": \"INTERNAL SERVER ERROR\",\"status\": \"error\",\"code\": \"400\",\"phonenumber\": \"\",\"action\": \"\"}";
    static public String server_reject_error="{ \"message\": \"WAIT TO RETRY\",\"status\": \"error\",\"code\": \"503\",\"phonenumber\": \"\",\"action\": \"\"}";

    static public String internet_error="{ \"message\": \"Couldn't connect to Network. Please check your connection.\",\"status\": \"error\",\"code\": \"504\",\"phonenumber\": \"\",\"action\": \"\"}";
    static public String api_key_error="{ \"message\": \"The App Key entered is invalid. Please try again.\",\"status\": \"error\",\"code\": \"402\",\"phonenumber\": \"\",\"action\": \"\"}";
    static public String server_key_error="{ \"message\": \"The Secret Key entered is invalid. Please try again.\",\"status\": \"error\",\"code\": \"401\",\"phonenumber\": \""+MobileNumber+"\",\"action\": \"\"}";
    static public String invalid_mobile_error="{ \"message\": \"The number you entered is invalid. Please try again.\",\"status\": \"error\",\"code\": \"403\",\"phonenumber\": \""+MobileNumber+"\",\"action\": \"\"}";
    static public String invalid_action_error="{ \"message\": \"INVALID Action\",\"status\": \"error\",\"code\": \"404\",\"phonenumber\": \""+MobileNumber+"\",\"action\": \"\"}";
    static public String invalid_otp_error="{ \"message\": \"INVALID OTP\",\"status\": \"error\",\"code\": \"405\",\"phonenumber\": \""+MobileNumber+"\",\"action\": \"\"}";
    static public String sessio_otp_error="{ \"message\": \"SESSION EXPIRED\",\"status\": \"error\",\"code\": \"406\",\"phonenumber\": \""+MobileNumber+"\",\"action\": \"\"}";
    static public String limit_reached="{ \"message\": \"Daily limit reached\",\"status\": \"error\",\"code\": \"407\",\"phonenumber\": \""+MobileNumber+"\",\"action\": \"\"}";
/*JSONObject getSuccessJson(){
*//*    JSONObject jobj = new JSONObject("");
    return jobj;*//*

}*/
    public static JSONObject request_(){
        JSONObject json = new JSONObject();
        try {
            json.put("appKey",  App_Key);
            json.put("secretKey",Secret_Key);
            json.put("phoneNumber", MobileNumber);
            json.put("actionType", ACTION_TYPE);
            json.put("deviceId", Device_Id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("JSON_Request", String.valueOf(json));
        return json;
    }
}
