package com.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by coderzlab on 16/7/15.
 */
public class PreferancesData {


    public static boolean saveLastAction(Context context, String LastAction){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("LastAction",LastAction).commit();
    }
    public static String getLastAction(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("LastAction", null);
    }
    public static boolean saveMissedCallAuthentication(Context context, String MissedCallAuthentication){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("MissedCallAuthentication",MissedCallAuthentication).commit();
    }
    public static String getMissedCallAuthentication(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("MissedCallAuthentication", null);
    }

    public static boolean saveMobileNumber(Context context, String MobileNumber){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("MobileNumber",MobileNumber).commit();
    }
    public static String getMobileNumber(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("MobileNumber", null);
    }
    public static boolean saveTimeCounter(Context context, String TimeCounter){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("TimeCounter",TimeCounter).commit();
    }
    public static String getTimeCounter(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("TimeCounter", null);
    }


    public static boolean saveVOICE_OTP_Response(Context context, String TimeCounter){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("VOICE_OTP_Response",TimeCounter).commit();
    }
    public static String getVOICE_OTP_Response(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("VOICE_OTP_Response", null);
    }
/*
    public static boolean saveAPIResponse(Context context, String APIResponse){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("APIResponse",APIResponse).commit();
    }
    public static String getAPIResponse(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("APIResponse", null);
    }*/
// HIERARCHY_SALES_USERNAME

    public  static SharedPreferences getPreferances(Context context){
       return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }
}
