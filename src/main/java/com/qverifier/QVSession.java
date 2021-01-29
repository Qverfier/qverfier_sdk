package com.qverifier;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.Interface.VolleyResponseListener;
import com.networking.PostServer;
import com.squareup.picasso.Picasso;
import com.utility.Constant;
import com.utility.PreferancesData;
import com.utility.QVAction;

import org.json.JSONException;
import org.json.JSONObject;
import static com.qverifier.QVAuthentication.mHandler;
import static com.utility.Constant.ACTION_TYPE;
import static com.utility.Constant.Action_Data;
import static com.utility.Constant.Call_authentication;
import static com.utility.Constant.Response_Message;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.MobileNumber;
import static com.utility.Constant.call;
import static com.utility.Constant.sms;
import static com.utility.Constant.validation_digit;
import static com.utility.Constant.validation_mobile_number;
import static com.utility.Constant.voice_otp;
import static com.utility.JSON_Data.api_error;
import static com.utility.JSON_Data.api_key_error;
import static com.utility.JSON_Data.internet_error;
import static com.utility.JSON_Data.limit_reached;
import static com.utility.JSON_Data.request_;
import static com.utility.JSON_Data.server_key_error;
import static com.utility.JSON_Data.server_reject_error;
import static com.utility.Constant.url;
import static com.utility.TimeCoins.Time_BackButton;
import static com.utility.TimeCoins.Time_COUNTER;

public class QVSession extends AppCompatActivity  {
    AlertDialog alertDialog ;    AlertDialog.Builder builder;
    EditText mobile_number;TextView text_error; ImageView logo;Button submit_data;
    boolean  doubleBackToExitPressedOnce = false;
    TextView loder_text;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qv_session);
        getSupportActionBar().hide();
        builder = new AlertDialog.Builder(QVSession.this);
        submit_data= findViewById(R.id.submit_data);
        ProgressBar= findViewById(R.id.ProgressBar);
        loder_text= findViewById(R.id.loder_text);
        mobile_number=findViewById(R.id.mobile_number);
        text_error=findViewById(R.id.text_error);
        logo=findViewById(R.id.logo);
        loder_text.setVisibility(View.GONE);
        ProgressBar.setVisibility(View.GONE);
        text_error.setVisibility(View.GONE);
        if (Constant.logo==null){
            logo.setVisibility(View.GONE);
        }
        else if(Constant.logo.equals("")){
            logo.setVisibility(View.GONE);
        }
        else {
            logo.setVisibility(View.VISIBLE);
            Picasso.with(QVSession.this).load(Constant.logo).into(logo);
        }

        mobile_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() >= validation_mobile_number) {
                    String limitReached = "Invalid";
                    mobile_number.setError(limitReached);
                } else {
                    mobile_number.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        submit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobile_number.getText().toString().equals("")){
                    text_error.setVisibility(View.VISIBLE);
                }
                else if(mobile_number.getText().toString().length()==validation_digit){
                    MobileNumber=mobile_number.getText().toString();
                    PreferancesData.saveMobileNumber(QVSession.this, MobileNumber);
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (activeNetwork != null) {
                        confirm_mobileNumber(MobileNumber);
                    } else {
                        Toast.makeText(QVSession.this, "You seem to be offline. Please check your connection.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    text_error.setVisibility(View.VISIBLE);
                    text_error.setText("The number you entered is invalid. Please try again.");
                }
            }
        });
    }
    public void  callApi(){
        Action_Data="";
        try {
            JSONObject json = request_();
            new PostServer(QVSession.this).commonServiceForPost(url, json,new  VolleyResponseListener(){
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
                            PreferancesData.saveLastAction(QVSession.this, Constant.ACTION_TYPE);
                            success_(response1.getString("message"));
                        }
                        else {
                            if(response1.getString("message").equals(R.string.invalid_key))
                                Failed_Response=api_key_error;
                            else if(response1.getString("message").equals(R.string.invalid_secret))
                                Failed_Response=server_key_error;
                            else if(response1.getString("message").contains("limit"))
                                Failed_Response=limit_reached;
                            else
                                Failed_Response=server_reject_error;

                         failed();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {

        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
      //  Toast.makeText(this, R.string.back_press, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, Time_BackButton);
    }
    public  void failed(){
        Message msg = mHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("Status", "Failed");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        finish();
    }
    public  void success_( String message){
        Response_Message="+91"+message;
        Intent intent = new Intent(QVSession.this, AuthenticationByCall.class);
        startActivity(intent);
        finish();
    }
    public void confirm_mobileNumber(final String number){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.confirm_number, viewGroup, false);
        final TextView text_num=dialogView.findViewById(R.id.phone_number);
        final LinearLayout layout2=dialogView.findViewById(R.id.layout2);
        final LinearLayout layout1=dialogView.findViewById(R.id.layout1);
        layout2.setVisibility(View.GONE);
        text_num.setText("+91 "+number);
        Button ok=dialogView.findViewById(R.id.ok);
        Button cancel=dialogView.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                submit_data.setVisibility(View.GONE);
                loading();
             if(PreferancesData.getLastAction(QVSession.this)==null ||PreferancesData.getTimeCounter(QVSession.this)==null){
                 ACTION_TYPE="MISSED_CALL";

           callApi();
               }
             else {

                 if(PreferancesData.getLastAction(QVSession.this).equals(call)){
                     ACTION_TYPE="MISSED_CALL";
                     callApi();
                 }
               else   if(PreferancesData.getLastAction(QVSession.this).equals(sms)){
                     ACTION_TYPE="SMS";
                     finish();
                     Intent intent = new Intent(QVSession.this, AuthenticationBySms.class);
                     startActivity(intent);
                 }
              else    if(PreferancesData.getLastAction(QVSession.this).equals(voice_otp)){
                     ACTION_TYPE="VOICE_OTP";
                     finish();
                     Intent intent = new Intent(QVSession.this, AuthenticationByVoiceOtp.class);
                     startActivity(intent);
                 }
              else {
                     ACTION_TYPE="MISSED_CALL";
                     callApi(); }

             } }});
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });
        builder.setView(dialogView);
        alertDialog= builder.create();
        alertDialog.show();
    }
    public void loading(){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.loading_popup, viewGroup, false);

        builder.setView(dialogView);
        alertDialog= builder.create();
        alertDialog.show();
    }
}
