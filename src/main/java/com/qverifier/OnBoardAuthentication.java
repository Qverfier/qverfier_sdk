package com.qverifier;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;

import com.Interface.VolleyResponseListener;
import com.networking.PostServer;
import com.utility.Constant;
import com.utility.QVAction;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qverifier.QVAuthentication.OnboardHandler;
import static com.utility.Constant.Device_Id;
import static com.utility.Constant.Response_Message;
import static com.utility.Constant.ACTION_TYPE;
import static com.utility.Constant.Action_Data;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.Progress_;
import static com.utility.Constant.Success_;
import static com.utility.Constant.MobileNumber;
import static com.utility.JSON_Data.MobileNumber_error;
import static com.utility.JSON_Data.api_error;
import static com.utility.JSON_Data.api_key_error;
import static com.utility.JSON_Data.invalid_action_error;
import static com.utility.JSON_Data.invalid_mobile_error;
import static com.utility.JSON_Data.limit_reached;
import static com.utility.JSON_Data.request_;
import static com.utility.JSON_Data.server_key_error;
import static com.utility.JSON_Data.server_reject_error;
import static com.utility.JSON_Data.success_data;
import static com.utility.OnboardRequest.getauth_;
import static com.utility.Constant.CALL;
import static com.utility.Constant.SMS;
import static com.utility.Constant.VOICE_OTP;
import static com.utility.Constant.url;
import static com.utility.TimeCoins.Time_COUNTER;
import static com.utility.TimeCoins.Time_COUNTER_OnBoard;
import static com.utility.TimeCoins.Time_INTERVAL;
import static com.utility.TimeCoins.Time_INTERVAL_OnBoard;
import static com.utility.TimeCoins.Time_START;
import static com.utility.TimeCoins.Time_START_OnBoard;

public class OnBoardAuthentication {
    public static int count=Time_COUNTER;
    public static int count_OnBoard=Time_COUNTER_OnBoard;
    public static int count_OnBoardVO=Time_COUNTER_OnBoard*2;
    public static void startSession_(int action , String mobilenumber, Context context){
        Device_Id= QVAction.getDeviceId(context);
        if(action==CALL ||action==SMS ||action==VOICE_OTP){
            Action_Data="";
            ACTION_TYPE=getauth_(action);
            if (mobilenumber.length()==10){
                MobileNumber= String.valueOf(mobilenumber);
                Message msg = OnboardHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("Status", "Progress");
                msg.setData(bundle);
                OnboardHandler.sendMessage(msg);
                validating(context);

            }
            else {
                Failed_Response=invalid_mobile_error;
                failed();
            }

        }
        else {
            //wrong action
            Failed_Response=invalid_action_error;
            failed();

        }



    }
    public static void  validating(Context cntx){
        try {
            JSONObject json = request_();
            new PostServer(cntx).commonServiceForPost(url, json,new  VolleyResponseListener(){
                @Override
                public void onError(String message) {
                    Failed_Response=api_error;
                    failed();
                }

                @Override
                public void onResponse(Object response) {
                    try {

                        JSONObject response1 = new JSONObject(String.valueOf(response));
                        if (response1.getString("status").equals("Success")) {
                            success_(response1.getString("message"));

                        }
                        else if(response1.getString("status").equals("Failure")){

                            if(response1.getString("message").equals(R.string.invalid_key)){
                                Failed_Response=api_key_error;
                            }
                            else if(response1.getString("message").equals(R.string.invalid_secret)){
                                Failed_Response=server_key_error;
                            }
                            else if(response1.getString("message").contains("limit")) {
                                Failed_Response = limit_reached;
                            }
                            else {
                                Failed_Response=server_reject_error;
                            }

                            failed();
                        }
                        else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (Exception ex) {

        }
    }
    public static void failed(){

        Message msg = OnboardHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("Status", "Failed");
        msg.setData(bundle);
        OnboardHandler.sendMessage(msg);

    }
    public static void success_( String message){

        if (ACTION_TYPE.equals("MISSED_CALL")){

            Response_Message="+91"+message;
            onBoardcall_(message);

        }
        else if(ACTION_TYPE.equals("SMS")){
            Response_Message=message;
            onBoardTime_(message);

        }
        else if(ACTION_TYPE.equals("VOICE_OTP")){
            Response_Message=message;
            onBoardTime_VO(message);
        }


    }
    public static void onBoardcall_(String message){
        count=Time_COUNTER;
        new CountDownTimer(Time_START,Time_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                count--;
                if(Action_Data.equals("Success")){
                    cancel();
                    Success_=success_data;
                    Message msg = OnboardHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("Status", "Success");
                    msg.setData(bundle);
                    OnboardHandler.sendMessage(msg);
                }
            }
            @Override
            public void onFinish() {
                cancel();
                Failed_Response=MobileNumber_error;
                Message msg = OnboardHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("Status", "Failed");
                msg.setData(bundle);
                OnboardHandler.sendMessage(msg);
            }
        }.start();

    }
    public static void onBoardTime_(String message){

        count_OnBoard=Time_COUNTER_OnBoard;
        new CountDownTimer(Time_START_OnBoard,Time_INTERVAL_OnBoard) {
            @Override
            public void onTick(long millisUntilFinished) {
                count_OnBoard--;

            }
            @Override
            public void onFinish() {

                cancel();
                if(Response_Message.equals("Validate")){
                    Response_Message="";
                }
                else {
                    Response_Message="";
                    Failed_Response=MobileNumber_error;
                    failed();

                }

            }
        }.start();

    }

    public static void onBoardTime_VO(String message){

        count_OnBoardVO=Time_COUNTER_OnBoard*2;
        new CountDownTimer(Time_START_OnBoard*2,Time_INTERVAL_OnBoard) {
            @Override
            public void onTick(long millisUntilFinished) {
                count_OnBoardVO--;

            }
            @Override
            public void onFinish() {

                cancel();
                if(Response_Message.equals("Validate")){
                    Response_Message="";
                }
                else {
                    Response_Message="";
                    Failed_Response=MobileNumber_error;
                    failed();

                }

            }
        }.start();

    }
}
