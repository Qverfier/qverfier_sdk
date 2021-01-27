package com.utility;

import android.os.CountDownTimer;

public class Constant {
    public static String url="http://112.196.109.70:8171/rv/api/ringverifier/process";
    public static int ACTION_CALL=0;
    public static int ACTION_SMS=0;
    public static int ACTION_VOICE_SMS=0;
    public static String ACTION_TYPE="MISSED_CALL",Response_Message="",Action_Data="",MobileNumber="",Failed_Response="",Success_="",Progress_="",logo="",Device_Id="",Call_authentication="null",call="MISSED_CALL",sms="SMS",voice_otp="VOICE_OTP";
    //session expaired
    public static String otp="AS";
    public static String otp_="FS";
    public static String _otp="IS";
    public static int _transaction=0;
    public static int CALL=1600,SMS=1601,VOICE_OTP=1602;
    public static int validation_mobile_number=11,validation_digit=10;
   public static CountDownTimer count_down;
    public static String Secret_Key="secretkey";
    public static String App_Key="appkey";
}
