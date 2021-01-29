package com.call_auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.utility.Constant;

import java.lang.reflect.Method;
import static com.utility.Constant.Response_Message;

public class IncomingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ITelephony telephonyService;
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Method m = tm.getClass().getDeclaredMethod("getITelephony");

                    m.setAccessible(true);
                    telephonyService = (ITelephony) m.invoke(tm);

                    if ((number != null)) {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (number == null) {

                } else if (number.equals("null")) {

                } else if (number.equals("")) {

                } else {
                    if (Response_Message.equals(number)) {
                        Constant.Action_Data = "Success";

                    }


                }
            }
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            }
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
