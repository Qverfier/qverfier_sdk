package com.qverifier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.listner.ResponseListener;
import com.listner.ResponseListnerOnBoard;
import com.utility.Constant;
import com.utility.PreferancesData;
import com.utility.QVAction;
import com.utility.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qverifier.OnBoardAuthentication.startSession_;
import static com.utility.Constant.ACTION_TYPE;
import static com.utility.Constant.Device_Id;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.MobileNumber;
import static com.utility.Constant.Progress_;
import static com.utility.Constant.Success_;
import static com.utility.OnboardRequest.check_otp;

public class QVAuthentication {
    static String data;
    public static ResponseListener listener;
    public static Handler mHandler;
    public static Handler OnboardHandler;

    public QVAuthentication(ResponseListener listener) {
        this.listener = listener;
    }
    public static void authenticate(final Context context, String app_Key, String secret_Key, String logo, final ResponseListener listener) {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String data = bundle.getString("Status");
                if (data.equals("Success")) {
                    try {
                        JSONObject response = new JSONObject(Success_);
                        PreferancesData.saveLastAction(context, null);
                        PreferancesData.saveTimeCounter(context, null);
                        listener.onSuccess(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else    if (data.equals("Failed")) {
                    try {
                        JSONObject response = new JSONObject(Failed_Response);
                        listener.onError(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        };
        Device_Id= QVAction.getDeviceId(context);
        if(Validation.appKey_(app_Key).equals("false")){
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("Status", "Failed");
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
        else if(Validation.secretKey_(secret_Key).equals("false")){
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("Status", "Failed");
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
        else {
            Constant.logo = logo;
            Intent intent = new Intent(context, QVSession.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }



    }


    public static void authenticationOnboard(final Context context, String app_Key, String secret_Key, String mobile_number, int action, final ResponseListnerOnBoard listner_) {
        Constant.Secret_Key = secret_Key;
        Constant.App_Key = app_Key;

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
}
}
