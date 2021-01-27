package com.vedanta_ias_academy.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by coderzlab on 16/7/15.
 */
public class PreferancesUserData {


    public static boolean saveUserID(Context context, String UserID){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("UserID",UserID).commit();
    }
    public static String getUserID(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("UserID", null);
    }


    public static boolean savephonenumber(Context context, String phonenumber){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("phonenumber",phonenumber).commit();
    }
    public static String getphonenumber(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("phonenumber", null);
    }


    public static boolean saveemail(Context context, String email){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("email",email).commit();
    }
    public static String getemail(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("email", null);
    }

    public static boolean saveUserName(Context context, String UserName){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("UserName",UserName).commit();
    }
    public static String getUserName(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("UserName", null);
    }

    public static boolean saveFatherName(Context context, String FatherName){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("FatherName",FatherName).commit();
    }
    public static String getFatherName(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("FatherName", null);
    }

    public static boolean saveUserGender(Context context, String UserGender){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("UserGender",UserGender).commit();
    }
    public static String getUserGender(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("UserGender", null);
    }

    public static boolean savePayment(Context context, String Payment){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("Payment",Payment).commit();
    }
    public static String getPayment(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("Payment", null);
    }

    public static boolean savePlanExpaire(Context context, String PlanExpaire){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.edit().putString("PlanExpaire",PlanExpaire).commit();
    }
    public static String getPlanExpaire(Context context){
        SharedPreferences sharedPreferences=getPreferances(context);
        return sharedPreferences.getString("PlanExpaire", null);
    }
// HIERARCHY_SALES_USERNAME

    public  static SharedPreferences getPreferances(Context context){
       return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }
}
