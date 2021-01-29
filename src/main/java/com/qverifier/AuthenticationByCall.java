package com.qverifier;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.utility.Constant;
import com.utility.PreferancesData;

import pl.droidsonroids.gif.GifImageView;

import static com.qverifier.QVAuthentication.mHandler;
import static com.utility.Constant.ACTION_TYPE;
import static com.utility.Constant.Action_Data;
import static com.utility.Constant.Call_authentication;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.MobileNumber;
import static com.utility.Constant.Success_;
import static com.utility.Constant._transaction;
import static com.utility.JSON_Data.press_back;
import static com.utility.JSON_Data.success_data;
import static com.utility.TimeCoins.Time_COUNTER;
import static com.utility.TimeCoins.Time_INTERVAL;
import static com.utility.TimeCoins.Time_START;

public class AuthenticationByCall extends AppCompatActivity {

    public static TextView text_message,text_other;
    public  int count,start_time,interval_time;
    boolean doubleBackToExitPressedOnce = false;
    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    CountDownTimer count_down=null;
    ImageView thumbs_icon;
    GifImageView callgif;
    TextView counttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_authenticating);
        getSupportActionBar().hide();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName() + "android.net.conn.CONNECTIVITY_CHANGE");
        counttime=findViewById(R.id.counttime);
        thumbs_icon=findViewById(R.id.thumbs_icon);
        text_message=findViewById(R.id.text_message);
        text_other=findViewById(R.id.text_other);
        callgif=findViewById(R.id.callgif);
        callgif.setVisibility(View.VISIBLE);
        thumbs_icon.setVisibility(View.GONE);

if(PreferancesData.getTimeCounter(AuthenticationByCall.this)==null){
    count=8;
    interval_time=Time_INTERVAL;
    start_time=8000;
}
else {
    count= Integer.parseInt(PreferancesData.getTimeCounter(AuthenticationByCall.this));
    interval_time=Time_INTERVAL;
    start_time=Integer.parseInt(PreferancesData.getTimeCounter(AuthenticationByCall.this))*1000;
}
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
                requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
            }
        }
        count_down =  new CountDownTimer(start_time,interval_time) {
            @Override
            public void onTick(long millisUntilFinished) {

                counttime.setText(String.valueOf(count)+":00");
                count--;
                PreferancesData.saveTimeCounter(AuthenticationByCall.this, String.valueOf(count));

                if(Action_Data.equals("Success")){


                    cancel();
                    Call_authentication="Success";
                    PreferancesData.saveMissedCallAuthentication(AuthenticationByCall.this, "Success");
                    Ui_itrupt();
                }
            }
            @Override
            public void onFinish() {
                cancel();
                if(Action_Data.equals("Success")){
                    cancel();
                    Call_authentication="Success";
                    PreferancesData.saveMissedCallAuthentication(AuthenticationByCall.this, "Success");
                    Ui_itrupt();
                }
                else {
                    _transaction=_transaction+1;
                    text_message.setText("Authentication Failed");
                    text_other.setText("Trying another way");
                    counttime.setVisibility(View.GONE);
                    PreferancesData.saveTimeCounter(AuthenticationByCall.this, null);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handler.removeCallbacksAndMessages(null);
                            ACTION_TYPE="SMS";
                            finish();
                            PreferancesData.saveLastAction(AuthenticationByCall.this, null);
                            Intent intent = new Intent(AuthenticationByCall.this, AuthenticationBySms.class);
                            startActivity(intent);
                            handler.removeCallbacksAndMessages(null);
                        }
                    }, 700); } }}.start();

    }
    public void Ui_itrupt(){
        text_message.setText(R.string.call_success);
        text_other.setText(R.string.message_success);
        thumbs_icon.setVisibility(View.VISIBLE);
        callgif.setVisibility(View.GONE);
        counttime.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _transaction=0;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }return; } } }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            count_down.cancel();
            Intent intent =new Intent(AuthenticationByCall.this,QVSession.class);
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
    protected void onResume() {
        super.onResume();
    }
    @Override

    protected void onPause() {
        super.onPause();
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
       count_down.cancel();
    }
}
