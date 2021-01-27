package com.utility;

import org.json.JSONException;
import org.json.JSONObject;

import static com.control.AuthTranData.App_Key;
import static com.control.AuthTranData.Secret_Key;
import static com.utility.AuthAction.User;

public class JSON_Data {
    static public String press_back="{ \"message\": \"USER CANCEL\",\"status\": \"Failed\",\"code\": \"505\",\"phonenumber\": \"\",\"action\": \"\"}";
   static public String success_data="{ \"message\": \"Authentication Success\",\"status\": \"Success\",\"code\": \"200\",\"phonenumber\": \""+AuthAction.User+"\",\"action\": \""+AuthAction.ACTION_TYPE+"\"}";

  /*  static public String success_data="{\n" +
            "    \"phonenumber\": \""+AuthAction.User+"\",\n" +
            "    \"status\": \"Success\",\n" +
            "\t\"Action\": \""+AuthAction.ACTION_TYPE+"\"\n" +
            "}";*/
    static public String user_failed="{ \"message\": \"MOBILE ACCESS NOT AVAILABLE\",\"status\": \"Failed\",\"code\": \"401\",\"phonenumber\": \"\",\"action\": \"\"}";
    static public String api_failed="{ \"message\": \"INTERNAL SERVER ERROR\",\"status\": \"Failed\",\"code\": \"400\",\"phonenumber\": \"\",\"action\": \"\"}";
    static public String server_reject_failed="{ \"message\": \"WAIT TO RETRY\",\"status\": \"Failed\",\"code\": \"503\",\"phonenumber\": \"\",\"action\": \"\"}";

    static public String internet_failed="{ \"message\": \"INTERNET NOT ACCESS\",\"status\": \"Failed\",\"code\": \"504\",\"phonenumber\": \"\",\"action\": \"\"}";
    static public String api_key_failed="{ \"message\": \"INVALID APP KEY\",\"status\": \"Failed\",\"code\": \"402\",\"phonenumber\": \"\",\"action\": \"\"}";
    static public String server_key_failed="{ \"message\": \"INVALID SECRET KEY\",\"status\": \"Failed\",\"code\": \"401\",\"phonenumber\": \"\",\"action\": \"\"}";
/*JSONObject getSuccessJson(){
*//*    JSONObject jobj = new JSONObject("");
    return jobj;*//*

}*/
    public static JSONObject request_(){
        JSONObject json = new JSONObject();
        try {
            json.put("appKey",  App_Key);
            json.put("secretKey",Secret_Key);
            json.put("phoneNumber", User);
            json.put("actionType", AuthAction.ACTION_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
