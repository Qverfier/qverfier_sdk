package com.qverifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Interface.VolleyResponseListener;
import com.networking.PostServer;
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

import static com.utility.JSON_Data.MobileNumber_error;
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

public class AuthenticationByVoiceOtp extends AppCompatActivity implements TextWatcher {
    EditText editText_one,editText_two,editText_three,editText_four;
    static String otp,phone;
    public  int count,start_time,interval_time;
    Button submit_otp;
    public static TextView text_message,text_other;
    boolean doubleBackToExitPressedOnce = false;
    public static GifImageView success,calling_gif;
    public Handler handler;
    LinearLayout linear_two,linear_one,layout_otp;
    CountDownTimer count_down=null;
    LinearLayout timer_text;
    ImageView thumbs_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voice__otp);
        linear_two=findViewById(R.id.linear_two);
        thumbs_icon=findViewById(R.id.thumbs_icon);
        linear_one=findViewById(R.id.linear_one);
        editText_one=findViewById(R.id.editTextone);
        editText_two=findViewById(R.id.editTexttwo);
        editText_three=findViewById(R.id.editTextthree);
        editText_four=findViewById(R.id.editTextfour);
        submit_otp=findViewById(R.id.submit_otp);
        timer_text=findViewById(R.id.timer_text);
        layout_otp=findViewById(R.id.layout_otp);
        getSupportActionBar().hide();
        final TextView count_time=findViewById(R.id.counttime);
        text_message=findViewById(R.id.text_message);
        text_other=findViewById(R.id.text_other);
        success=findViewById(R.id.success);
        thumbs_icon.setVisibility(View.GONE);
        ACTION_TYPE="VOICE_OTP";
        PreferancesData.saveLastAction(AuthenticationByVoiceOtp.this, "VOICE_OTP");
        success.setVisibility(View.GONE);
        editText_one.addTextChangedListener(this);
        editText_two.addTextChangedListener(this);
        editText_three.addTextChangedListener(this);
        editText_four.addTextChangedListener(this);
        linear_two.setVisibility(View.GONE);
        count=0;
        if(PreferancesData.getTimeCounter(AuthenticationByVoiceOtp.this)==null){
            count=Time_COUNTER*2;
            interval_time=Time_INTERVAL;
            start_time=Time_START*2;
            validating();
        }
        else {
            count= Integer.parseInt(PreferancesData.getTimeCounter(AuthenticationByVoiceOtp.this));
            interval_time=Time_INTERVAL;
            start_time=Integer.parseInt(PreferancesData.getTimeCounter(AuthenticationByVoiceOtp.this))*1000;
        }
         count_down =    new CountDownTimer(start_time,interval_time) {
            @Override
            public void onTick(long millisUntilFinished) {

                count_time.setText(String.valueOf(count)+":00");
                count--;
                PreferancesData.saveTimeCounter(AuthenticationByVoiceOtp.this, String.valueOf(count));

            }
            @Override
            public void onFinish() {
                cancel();
                PreferancesData.saveVOICE_OTP_Response(AuthenticationByVoiceOtp.this, null);
                text_message.setText("Authentication Failed");
                text_other.setText("Try to another way");
                count_time.setVisibility(View.GONE);
                linear_two.setVisibility(View.VISIBLE);
                linear_one.setVisibility(View.GONE);
                PreferancesData.saveLastAction(AuthenticationByVoiceOtp.this, null);
                PreferancesData.saveTimeCounter(AuthenticationByVoiceOtp.this, null);
                 handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        finish();
                        Failed_Response=MobileNumber_error;
                        Message msg = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("Status", "Failed");
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                }, 2200);
            }
        }.start();
        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp=editText_one.getText().toString()+editText_two.getText().toString()+editText_three.getText().toString()+editText_four.getText().toString();
                int otp_length=otp.length();
                Log.e("VoiceOtpResponse",Response_Message);
                if(PreferancesData.getVOICE_OTP_Response(AuthenticationByVoiceOtp.this)==null){
                    Toast.makeText(AuthenticationByVoiceOtp.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (PreferancesData.getVOICE_OTP_Response(AuthenticationByVoiceOtp.this).equals(otp)) {
                        count_down.cancel();
                        PreferancesData.saveVOICE_OTP_Response(AuthenticationByVoiceOtp.this, null);

                        Ui_itrupt();
                    } else {
                        Toast.makeText(AuthenticationByVoiceOtp.this, "The OTP entered is invalid. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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

    public void Ui_itrupt(){
        //Reset Action and time counter-----------
        PreferancesData.saveLastAction(AuthenticationByVoiceOtp.this, null);
        PreferancesData.saveTimeCounter(AuthenticationByVoiceOtp.this, null);
        text_message.setText(R.string.call_success);
        text_other.setText(R.string.message_success);
        text_other.setVisibility(View.VISIBLE);
        success.setVisibility(View.GONE);
        timer_text.setVisibility(View.GONE);
        submit_otp.setVisibility(View.GONE);
        layout_otp.setVisibility(View.GONE);
        thumbs_icon.setVisibility(View.VISIBLE);

//        calling_gif.setVisibility(View.GONE);
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
    }
    public void  validating(){
        try {

            JSONObject json = request_();
            new PostServer(AuthenticationByVoiceOtp.this).commonServiceForPost(url, json,new  VolleyResponseListener(){
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
                            Response_Message=response1.getString("message");
                            PreferancesData.saveVOICE_OTP_Response(AuthenticationByVoiceOtp.this, response1.getString("message"));
                        }
                        else {
                            if(response1.getString("message").equals(R.string.invalid_key))
                                Failed_Response=api_key_error;
                            else if(response1.getString("message").equals(R.string.invalid_secret))
                                Failed_Response=server_key_error;
                            else if(response1.getString("message").contains("limit")) {
                                PreferancesData.saveLastAction(AuthenticationByVoiceOtp.this, null);
                                PreferancesData.saveTimeCounter(AuthenticationByVoiceOtp.this, null);
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


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            count_down.cancel();
            Intent intent =new Intent(AuthenticationByVoiceOtp.this,QVSession.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click \"Back\" to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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
