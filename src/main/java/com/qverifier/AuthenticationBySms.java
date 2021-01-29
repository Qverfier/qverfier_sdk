package com.qverifier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Interface.VolleyResponseListener;
import com.control.SmsBroadcastReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.networking.PostServer;
import com.utility.Constant;
import com.utility.PreferancesData;
import com.utility.QVAction;

import org.json.JSONException;
import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

import static com.qverifier.QVAuthentication.mHandler;
import static com.utility.Constant.Response_Message;
import static com.utility.Constant.ACTION_TYPE;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.Success_;
import static com.utility.JSON_Data.api_error;
import static com.utility.JSON_Data.api_key_error;
import static com.utility.JSON_Data.limit_reached;
import static com.utility.JSON_Data.press_back;
import static com.utility.JSON_Data.request_;
import static com.utility.JSON_Data.server_key_error;
import static com.utility.JSON_Data.server_reject_error;
import static com.utility.JSON_Data.success_data;
import static com.utility.Constant.url;
import static com.utility.TimeCoins.Time_COUNTER;
import static com.utility.TimeCoins.Time_INTERVAL;
import static com.utility.TimeCoins.Time_START;

public class AuthenticationBySms extends AppCompatActivity implements TextWatcher {
  public static   EditText editText_one,editText_two,editText_three,editText_four;
    static String otp="No",phone;
    Button submit_otp;
    public  int count,start_time,interval_time;
    public static TextView text_message,text_other,text_expire;
    LinearLayout layout_otp;
    boolean doubleBackToExitPressedOnce = false;
    public static GifImageView calling_gif;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    CountDownTimer count_down=null;
    ImageView thumbs_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sms__authentication);
        thumbs_icon=findViewById(R.id.thumbs_icon);
        editText_one=findViewById(R.id.editTextone);
        editText_two=findViewById(R.id.editTexttwo);
        editText_three=findViewById(R.id.editTextthree);
        editText_four=findViewById(R.id.editTextfour);
        layout_otp=findViewById(R.id.layout_otp);
        text_expire=findViewById(R.id.text_expire);
        thumbs_icon.setVisibility(View.GONE);
        getSupportActionBar().hide();
        final TextView counttime=findViewById(R.id.counttime);
        text_message=findViewById(R.id.text_message);
        text_other=findViewById(R.id.text_other);
        ACTION_TYPE="SMS";
        editText_one.addTextChangedListener(this);
        editText_two.addTextChangedListener(this);
        editText_three.addTextChangedListener(this);
        editText_four.addTextChangedListener(this);
        validating();
        if(PreferancesData.getTimeCounter(AuthenticationBySms.this)==null){

            count=Time_COUNTER;
            interval_time=Time_INTERVAL;
            start_time=Time_START;
        }
        else {
            count= Integer.parseInt(PreferancesData.getTimeCounter(AuthenticationBySms.this));
            interval_time=Time_INTERVAL;
            start_time=Integer.parseInt(PreferancesData.getTimeCounter(AuthenticationBySms.this))*1000;
        }
     count_down=   new CountDownTimer(start_time,interval_time) {
            @Override
            public void onTick(long millisUntilFinished) {
                counttime.setText(String.valueOf(count)+":00");
                count--;
                PreferancesData.saveTimeCounter(AuthenticationBySms.this, String.valueOf(count));
       if(Response_Message.contains(otp)){
    text_message.setText(R.string.call_success);
    text_other.setText(R.string.message_success);
    layout_otp.setVisibility(View.GONE);
           thumbs_icon.setVisibility(View.VISIBLE);
           text_expire.setVisibility(View.GONE);
    final Handler handler = new Handler();

    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            Success_=success_data;
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("Status", "Success");
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            finish();

        }
    }, 7000);
    count_down.cancel();

} }
            @Override
            public void onFinish() {
                count_down.cancel();
                PreferancesData.saveTimeCounter(AuthenticationBySms.this, null);
                text_message.setText("Authentication Failed");
                text_other.setText("Try to another way");
                counttime.setVisibility(View.GONE);
                editText_one.setVisibility(View.GONE);
                editText_four.setVisibility(View.GONE);
                editText_three.setVisibility(View.GONE);
                editText_two.setVisibility(View.GONE);
                authentication_failed();
            }
        }.start();
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 1) {
            if (editText_one.length() == 1) {
                editText_two.requestFocus();
            }

            if (editText_two.length() == 1) {
                editText_three.requestFocus();
            }
            if (editText_three.length() == 1) {
                editText_four.requestFocus();
            }
        } else if (editable.length() == 0) {
            if (editText_four.length() == 0) {
                editText_three.requestFocus();
            }
            if (editText_three.length() == 0) {
                editText_two.requestFocus();
            }
            if (editText_two.length() == 0) {
                editText_one.requestFocus();
            }
        }
    }
    public void authentication_failed(){
        text_message.setText("Authentication Failed");
        text_other.setVisibility(View.VISIBLE);
        text_expire.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PreferancesData.saveLastAction(AuthenticationBySms.this, null);
                Intent intent = new Intent(AuthenticationBySms.this, AuthenticationByVoiceOtp.class);
                startActivity(intent);
                finish();


            }
        }, 700);
    }



    public void  validating(){
        try {
            JSONObject json = request_();
            new PostServer(AuthenticationBySms.this).commonServiceForPost(url, json,new  VolleyResponseListener(){
                @Override
                public void onError(String message) {
                    Failed_Response=api_error;
                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("Status", "Failed");
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    finish();
                }

                @Override
                public void onResponse(Object response) {
                    try {
                        JSONObject response1 = new JSONObject(String.valueOf(response));
                        if (response1.getString("status").equals("Success")) {
                            PreferancesData.saveLastAction(AuthenticationBySms.this, Constant.ACTION_TYPE);
                            Response_Message=response1.getString("message");
                            startSmsUserConsent();
                        }
                        else {
                            if(response1.getString("message").equals(R.string.invalid_key))
                                Failed_Response=api_key_error;
                            else if(response1.getString("message").equals(R.string.invalid_secret))
                                Failed_Response=server_key_error;
                            else if(response1.getString("message").contains("limit")) {
                                PreferancesData.saveLastAction(AuthenticationBySms.this, null);
                                PreferancesData.saveTimeCounter(AuthenticationBySms.this, null);
                                Failed_Response = limit_reached;
                            }
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



    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        if(message.contains("Qverifier")){
            String[] parsing = message.split("code is");
            otp=parsing[1].replace(" ","");
            char c=otp.charAt(0);
            String otp_1= String.valueOf(c);
            editText_one.setText(otp_1);
            char c1=otp.charAt(1);
            editText_two.setText(String.valueOf(c1));
            char c2=otp.charAt(2);
            editText_three.setText(String.valueOf(c2));
            char c3=otp.charAt(3);
            editText_four.setText(String.valueOf(c3));
        }

    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }

                    @Override
                    public void onFailure() {

                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            count_down.cancel();
            Intent intent =new Intent(AuthenticationBySms.this,QVSession.class);
            startActivity(intent);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click \"Back\" to exitt", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    protected void onResume() {
        super.onResume();
    }
    @Override

    protected void onPause() {
        super.onPause();//invisible

    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
        count_down.cancel();
    }
    public  void failed(){

        Message msg = mHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("Status", "Failed");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        finish();
    }
}
