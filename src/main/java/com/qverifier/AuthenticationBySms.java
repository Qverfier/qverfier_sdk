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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

import static com.qverifier.QVAuthentication.mHandler;
import static com.utility.Constant.ACTION_NUM;
import static com.utility.Constant.ACTION_TYPE;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.Success_;
import static com.utility.JSON_Data.api_failed;
import static com.utility.JSON_Data.press_back;
import static com.utility.JSON_Data.request_;
import static com.utility.JSON_Data.success_data;
import static com.utility.Constant.url;
import static com.utility.TimeCoins.Time_COUNTER;
import static com.utility.TimeCoins.Time_INTERVAL;
import static com.utility.TimeCoins.Time_START;

public class AuthSMS extends AppCompatActivity  {
  public static   EditText editText_one,editText_two,editText_three,editText_four;
    static String otp="No",phone;
    Button submit_otp;
    public  int count=Time_COUNTER;
    public static TextView text_message,text_other;
    LinearLayout layout_otp;
    boolean doubleBackToExitPressedOnce = false;
    public static GifImageView calling_gif;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__authentication);
        editText_one=findViewById(R.id.editTextone);
        editText_two=findViewById(R.id.editTexttwo);
        editText_three=findViewById(R.id.editTextthree);
        editText_four=findViewById(R.id.editTextfour);
        layout_otp=findViewById(R.id.layout_otp);
        getSupportActionBar().hide();
        final TextView counttime=findViewById(R.id.counttime);
        text_message=findViewById(R.id.text_message);
        text_other=findViewById(R.id.text_other);
        ACTION_TYPE="SMS";
     /*   submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        validating();

        new CountDownTimer(Time_START,Time_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
              //  otp=editText_one.getText().toString();
if(ACTION_NUM.contains(otp)){
    text_message.setText(R.string.call_success);
    text_other.setText(R.string.message_success);
    layout_otp.setVisibility(View.GONE);
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
    }, 900);
    cancel();

}
                counttime.setText(String.valueOf(count)+":00");
                count--;

            }
            @Override
            public void onFinish() {
                cancel();
                text_message.setText("Authentication Failed");
                text_other.setVisibility(View.GONE);
                counttime.setVisibility(View.GONE);
                editText_one.setVisibility(View.GONE);
                Ui_itrupt();

            }
        }.start();
    }

    public void Ui_itrupt(){
        text_message.setText("Failed");
        text_other.setVisibility(View.GONE);
     //   calling_gif.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AuthSMS.this, AuthVOICE.class);
                startActivity(intent);
                finish();


            }
        }, 700);
    }



    public void  validating(){
        try {


            JSONObject json = request_();
            System.out.print("RequestSMS----"+String.valueOf(json));
            new PostServer(AuthSMS.this).commonServiceForPost(url, json,new  VolleyResponseListener(){
                @Override
                public void onError(String message) {
                    Failed_Response=api_failed;
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
                        System.out.print("Responseserver----"+String.valueOf(response));
                        if (response1.getString("status").equals("Success")) {

                            ACTION_NUM=response1.getString("message");
                            startSmsUserConsent();
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



    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               // Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
               // String sender = data.getStringExtra(SmsRetriever.);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
otp=message;
        char c=otp.charAt(0);//returns h
       String otp_1= String.valueOf(c);
        editText_one.setText(otp_1);
        char c1=otp.charAt(1);//returns h
        editText_two.setText(String.valueOf(c1));
        char c2=otp.charAt(2);//returns h
        editText_three.setText(String.valueOf(c2));
        char c3=otp.charAt(3);//returns h
        editText_four.setText(String.valueOf(c3));
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
            Failed_Response=press_back;
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("Status", "Failed");
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
