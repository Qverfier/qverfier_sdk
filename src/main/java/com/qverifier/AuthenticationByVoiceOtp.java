package com.qverifier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Interface.VolleyResponseListener;
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
import static com.utility.JSON_Data.user_failed;
import static com.utility.Constant.url;
import static com.utility.TimeCoins.Time_COUNTER;
import static com.utility.TimeCoins.Time_INTERVAL;
import static com.utility.TimeCoins.Time_START;

public class AuthVOICE extends AppCompatActivity implements TextWatcher {
    EditText editText_one,editText_two,editText_three,editText_four;
    static String otp,phone;
    public  int count=Time_COUNTER;
    Button submit_otp;
    public static TextView text_message,text_other;
    boolean doubleBackToExitPressedOnce = false;
    public static GifImageView success,calling_gif;
    public Handler handler;
    LinearLayout linear_two,linear_one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice__otp);
        linear_two=findViewById(R.id.linear_two);
        linear_one=findViewById(R.id.linear_one);
        editText_one=findViewById(R.id.editTextone);
        editText_two=findViewById(R.id.editTexttwo);
        editText_three=findViewById(R.id.editTextthree);
        editText_four=findViewById(R.id.editTextfour);
        submit_otp=findViewById(R.id.submit_otp);
        getSupportActionBar().hide();
        final TextView counttime=findViewById(R.id.counttime);
        text_message=findViewById(R.id.text_message);
        text_other=findViewById(R.id.text_other);
        success=findViewById(R.id.success);
        ACTION_TYPE="VoiceOTP";
        success.setVisibility(View.GONE);
        editText_one.addTextChangedListener(this);
        editText_two.addTextChangedListener(this);
        editText_three.addTextChangedListener(this);
        editText_four.addTextChangedListener(this);
        linear_two.setVisibility(View.GONE);
        validating();


        final CountDownTimer count_down =    new CountDownTimer(Time_START,Time_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

                counttime.setText(String.valueOf(count)+":00");
                count--;

            }
            @Override
            public void onFinish() {
                cancel();
                text_message.setText("Authentication Failed");
                text_other.setText("Try to another way");
                counttime.setVisibility(View.GONE);
                linear_two.setVisibility(View.VISIBLE);
                linear_one.setVisibility(View.GONE);
//                calling_gif.setVisibility(View.GONE);
                 handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        finish();
                        Failed_Response=user_failed;
                        Message msg = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("Status", "Failed");
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                     /*   Intent intent = new Intent(AuthVOICE.this, AuthSMS.class);
                        startActivity(intent);
                        finish();*/

                    }
                }, 2200);
            }
        }.start();
        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp=editText_one.getText().toString()+editText_two.getText().toString()+editText_three.getText().toString()+editText_four.getText().toString();
                int otp_length=otp.length();
                if(ACTION_NUM.equals(otp)){
               //     handler.removeCallbacksAndMessages(null);
                    count_down.cancel();
                    Ui_itrupt();
                 //   formdata_request();
                }
                else {
                    Toast.makeText(AuthVOICE.this, "OTP invalid", Toast.LENGTH_LONG).show();
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
        text_message.setText("AuthCALL \n Success");
        text_other.setVisibility(View.GONE);
        success.setVisibility(View.VISIBLE);
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
        }, 700);
    }
    public void  validating(){
        try {

            JSONObject json = request_();
            System.out.print("RequestVoiceOtp----"+String.valueOf(json));
            new PostServer(AuthVOICE.this).commonServiceForPost(url, json,new  VolleyResponseListener(){
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
            Failed_Response=press_back;
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("Status", "Failed");
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            finish();
            super.onBackPressed();
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
