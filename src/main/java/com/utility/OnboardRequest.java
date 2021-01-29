package com.utility;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import static com.qverifier.QVAuthentication.OnboardHandler;
import static com.utility.Constant.Progress_;
import static com.utility.Constant.Response_Message;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.Response_Message;
import static com.utility.Constant.Success_;
import static com.utility.Constant._otp;
import static com.utility.Constant.otp;
import static com.utility.Constant.otp_;
import static com.utility.JSON_Data.invalid_otp_error;
import static com.utility.JSON_Data.progress_call;
import static com.utility.JSON_Data.progress_sms;
import static com.utility.JSON_Data.progress_voice_otp;
import static com.utility.JSON_Data.sessio_otp_error;
import static com.utility.JSON_Data.success_data;

public class OnboardRequest {
public static String getauth_(int i){
    String type_="";
    if(i==1600) {
        Progress_=progress_call;
        type_ = "MISSED_CALL";
    }
    else if(i==1601) {
        Progress_=progress_sms;
        type_ = "SMS";
    }
    else if(i==1602) {
        Progress_=progress_voice_otp;
        type_ = "VOICE_OTP";
    }



    return type_;
}
public static String check_otp(String data){
    String code="";
    Log.e("ActionData",Response_Message);
    if(Response_Message.equals("")){
        code=_otp;
        Failed_Response=sessio_otp_error;
        Message msg = OnboardHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("Status", "Failed");
        msg.setData(bundle);
        OnboardHandler.sendMessage(msg);
    }
    else if(Response_Message.equals(data)){
        code=otp;
        Success_=success_data;
        Message msg = OnboardHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("Status", "Success");
        msg.setData(bundle);
        OnboardHandler.sendMessage(msg);
        Response_Message="Validate";

    }
    else {
        code=otp_;
        Failed_Response=invalid_otp_error;
        Message msg = OnboardHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("Status", "Failed");
        msg.setData(bundle);
        OnboardHandler.sendMessage(msg);

    }

    return code;
}

}
