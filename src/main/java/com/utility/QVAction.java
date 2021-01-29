package com.utility;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class QVAction {
    public static int CALL=1600;
    public static int SMS=1601;
    public static int VOICE_OTP=1602;

    public static String getDeviceId(Context context) {

       String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }
}
