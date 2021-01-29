package com.utility;

import static com.utility.Constant.Failed_Response;
import static com.utility.JSON_Data.api_key_error;
import static com.utility.JSON_Data.server_key_error;

public class Validation {
    static String result="";
public static String secretKey_(String key){

    if(key==null){
        Failed_Response=server_key_error;
result="false";
    }
    else if(key.equals("")){
        Failed_Response=server_key_error;
        result="false";
    }
    else {
        Constant.Secret_Key = key;

        result="true";
    }
return result;
}
    public static String appKey_(String key){

        if(key==null){
            Failed_Response=api_key_error;
            result="false";
        }
        else if(key.equals("")){
            Failed_Response=api_key_error;
            result="false";
        }
        else {
            Constant.App_Key = key;
            result="true";
        }
        return result;
    }
}
