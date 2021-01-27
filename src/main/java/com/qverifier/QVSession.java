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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Interface.VolleyResponseListener;
import com.listner.ResponseListener;
import com.networking.PostServer;
import com.squareup.picasso.Picasso;
import com.utility.AuthenticationData;
import com.utility.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qverifier.QVAuthentication.mHandler;
import static com.utility.Constant.ACTION_NUM;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.User;
import static com.utility.JSON_Data.api_failed;
import static com.utility.JSON_Data.api_key_failed;
import static com.utility.JSON_Data.internet_failed;
import static com.utility.JSON_Data.request_;
import static com.utility.JSON_Data.server_key_failed;
import static com.utility.JSON_Data.server_reject_failed;
import static com.utility.Constant.url;

public class SessionStart extends AppCompatActivity  {

static Object data;

  LinearLayout startung_ui,calling_ui,sms_ui;
  Button submit_data;
    boolean doubleBackToExitPressedOnce = false;
  EditText mobile_number;
    private AuthenticationData util;
  TextView text_error;
  ImageView logo;
    AlertDialog.Builder builder;
    AlertDialog alertDialog ;
 private ResponseListener listner_data= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.new_act);
        getSupportActionBar().hide();
        builder = new AlertDialog.Builder(SessionStart.this);
        sms_ui=(LinearLayout) findViewById(R.id.sms_ui);
        calling_ui=(LinearLayout) findViewById(R.id.calling_ui);
        startung_ui=(LinearLayout) findViewById(R.id.startung_ui);
        submit_data=(Button) findViewById(R.id.submit_data);
        mobile_number=(EditText) findViewById(R.id.mobile_number);
        text_error=(TextView) findViewById(R.id.text_error);
        logo=(ImageView) findViewById(R.id.logo);
        text_error.setVisibility(View.GONE);
        Picasso.with(SessionStart.this).load(Constant.logo).into(logo);
        mobile_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //get the value of number validation another variabel
                if (charSequence.toString().length() >= 11) {
                    String limitReached = "Invalid mobile number!";
                    mobile_number.setError(limitReached);
                } else {
                    // clear error
                    mobile_number.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        submit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobile_number.getText().toString().equals("")){
                    text_error.setVisibility(View.VISIBLE);
                }
                else if(mobile_number.getText().toString().length()==10){
                    User=mobile_number.getText().toString();
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (activeNetwork != null) {
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            cofirmation(User);

                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                            cofirmation(User);


                        }
                    } else {
                        Message msg = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        Failed_Response=internet_failed;
                        bundle.putString("Status", "Failed");
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                        finish();
                    }


                  //  validating();
                }
                else {
                    text_error.setVisibility(View.VISIBLE);
                    text_error.setText("Invalid Mobile number.Please enter valid number");
                }


            }
        });
    }




    public void  validating(){
        try {
            JSONObject json = request_();
            new PostServer(SessionStart.this).commonServiceForPost(url, json,new  VolleyResponseListener(){
                @Override
                public void onError(String message) {
                    Failed_Response=api_failed;
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
                                Failed_Response=api_key_failed;
                            }
                            else if(response1.getString("message").equals(R.string.invalid_secret)){
                                Failed_Response=server_key_failed;
                            }
                            else {
                                Failed_Response=server_reject_failed;
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
           failed();
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.back_press, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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
        ACTION_NUM="+91"+message;

        Intent intent = new Intent(SessionStart.this, AuthCALL.class);
        startActivity(intent);
        finish();
    }
    public void cofirmation(String number){
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
                validating();
              /*  if ( _transaction==2){
                    ACTION_TYPE="SMS";
                    finish();
                    Intent intent = new Intent(SessionStart.this, AuthSMS.class);
                    startActivity(intent);
                }

                else {
                    validating();
                }*/

            }
        });
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
}
