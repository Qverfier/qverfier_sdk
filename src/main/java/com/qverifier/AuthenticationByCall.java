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
import android.widget.TextView;
import android.widget.Toast;

import static com.qverifier.QVAuthentication.mHandler;
import static com.utility.Constant.ACTION_TYPE;
import static com.utility.Constant.Action_Data;
import static com.utility.Constant.Failed_Response;
import static com.utility.Constant.Success_;
import static com.utility.Constant._transaction;
import static com.utility.JSON_Data.press_back;
import static com.utility.JSON_Data.success_data;
import static com.utility.TimeCoins.Time_COUNTER;
import static com.utility.TimeCoins.Time_INTERVAL;
import static com.utility.TimeCoins.Time_START;

public class AuthCALL extends AppCompatActivity {

    public static TextView text_message,text_other;
    public  int count=Time_COUNTER;
    private static final int REQ_USER_CONSENT = 200;
    boolean doubleBackToExitPressedOnce = false;
    CallStateListener callBroadcastReceiver;
    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticating);
        getSupportActionBar().hide();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName() + "android.net.conn.CONNECTIVITY_CHANGE");
        final TextView counttime=findViewById(R.id.counttime);
        text_message=findViewById(R.id.text_message);
        text_other=findViewById(R.id.text_other);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
                requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
            }
        }

        new CountDownTimer(Time_START,Time_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

                counttime.setText(String.valueOf(count)+":00");
                count--;
                if(Action_Data.equals("Success")){


                    cancel();
                    Ui_itrupt();
                }
            }
            @Override
            public void onFinish() {
                cancel();
                _transaction=_transaction+1;
                text_message.setText("Authentication Failed");
                text_other.setText("Try to another way");
                counttime.setVisibility(View.GONE);
              //  calling_gif.setVisibility(View.GONE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        ACTION_TYPE="SMS";
                        finish();
                        Intent intent = new Intent(AuthCALL.this, AuthSMS.class);
                        startActivity(intent);
                        handler.removeCallbacksAndMessages(null);
                    }
                }, 700);
            }
        }.start();
    }
    public void Ui_itrupt(){
        text_message.setText(R.string.call_success);
        text_other.setText(R.string.message_success);
      //  calling_gif.setVisibility(View.GONE);
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
        }, 900);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted: " , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission NOT granted: " + PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
                }

                return;
            }
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
        Toast.makeText(this, R.string.back_press, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
