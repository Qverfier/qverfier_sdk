package com.qverifier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.control.AuthTranData;
import com.listner.ResponseListner;
import com.listner.ResponseListnerOnBoard;
import com.utility.AuthAction;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qverifier.OnBoardAuthentication.startSession_;
import static com.utility.AuthAction.Failed_Response;
import static com.utility.AuthAction.Progress_;
import static com.utility.AuthAction.Success_;
import static com.utility.OnboardRequest.check_otp;

public class QverifierAuthentication {
    static String data;
    public static ResponseListner listner;
    public static Handler mHandler;
    public static Handler OnboardHandler;

    public QverifierAuthentication(ResponseListner listner) {
        this.listner = listner;
    }
// change name ---> authenticate_....chnage listner speling
    public static void authenticate(final Context context, String app_Key, String secret_Key, String logo, final ResponseListner listner) {
        //  listner.onSuccess(data);
        // null data handel
        AuthTranData.Secret_Key = secret_Key;
        AuthTranData.App_Key = app_Key;
        AuthAction.logo = logo;
        Intent intent = new Intent(context, SessionStart.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                // change name
                String string = bundle.getString("Status");
                if (string.equals("Success")) {
                    try {
                        JSONObject response = new JSONObject(Success_);
                        listner.onSuccess(response);
                    } catch (JSONException e) {
                        //failed
                        e.printStackTrace();
                    }

                }
             else    if (string.equals("Failed")) {
                    try {
                        JSONObject response = new JSONObject(Failed_Response);
                        listner.onError(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        };
    }


    public static void sendRequestOnboard(final Context context, String app_Key, String secret_Key, String mobile_number, int action, final ResponseListnerOnBoard listner_) {
        AuthTranData.Secret_Key = secret_Key;
        AuthTranData.App_Key = app_Key;

        OnboardHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String string = bundle.getString("Status");
                if (string.equals("Success")) {
                    try {
                        JSONObject response = new JSONObject(Success_);
                        listner_.onSuccess(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (string.equals("Failed")) {
                    try {
                        JSONObject response = new JSONObject(Failed_Response);
                        listner_.onError(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                if (string.equals("Progress")) {
                    try {
                        JSONObject response = new JSONObject(Progress_);
                        listner_.onProgress(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        };
        startSession_(action,mobile_number,context);
    }




    public static void validate_otp(String otp){
        String status=check_otp(otp);
    Log.e("StatusCode",status+"OTP-"+otp);
}
}
