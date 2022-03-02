package com.example.timertest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAINACTIVITY";
    TextView textTimer;
    Button btnStart, btnFinish, btnTimeStart, btnTimeFinish;

    MyService service = new MyService();
    //MyService.backThread backThread;// = new backThread();

    private BroadcastReceiver mReceiver = null;

    private void registerReceiver(){
//        Log.d(TAG, "registerReceiver 시작");
        /** 1. intent filter를 만든다
         *  2. intent filter에 action을 추가한다.
         *  3. BroadCastReceiver를 익명클래스로 구현한다.
         *  4. intent filter와 BroadCastReceiver를 등록한다.
         * */
        if(mReceiver != null) return;
        Log.d(TAG, "registerReceiver 시작");

        final IntentFilter intentFilter  = new IntentFilter();
        intentFilter .addAction("start");
        intentFilter .addAction("stop");

//        LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, intentFilter);

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive 시작");
                for(int i=0; i<intentFilter.countActions(); i++) {
                    Log.d(TAG, intentFilter.getAction(i));
                }
                /*int receviedData = intent.getIntExtra("value",0);
                if(intent.getAction().equals(BROADCAST_MESSAGE)){
                    Toast.makeText(context, "recevied Data : "+receviedData, Toast.LENGTH_SHORT).show();
                }*/
                if (intent.getAction() == "start") {
                    String min = intent.getStringExtra("min");
                    String sec = intent.getStringExtra("sec");

                    String message = min +":"+ sec;
                    textTimer.setText(message);
                    //Toast.makeText(, intent?.getStringExtra("TEST"), Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "time : " + message);
                }
            else if (intent.getAction() == "stop") {
                    String resetMessage = intent.getStringExtra("reset");
                    textTimer.setText(resetMessage);
                }
            }
        };

        LocalBroadcastManager.getInstance(App.ApplicationContext()).registerReceiver(this.mReceiver, intentFilter);
        //registerReceiver(this.mReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        Log.d(TAG, "unregisterReceiver 시작");
        LocalBroadcastManager.getInstance(App.ApplicationContext()).unregisterReceiver(mReceiver);
        //unregisterReceiver(mReceiver);
    }
    boolean isstart = false;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate 시작");

        textTimer = findViewById(R.id.textTimer);
        btnStart= findViewById(R.id.btnStart);
        btnFinish= findViewById(R.id.btnFinish);
        btnTimeStart= findViewById(R.id.btnTimeStart);
        btnTimeFinish= findViewById(R.id.btnTimeFinish);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서비스 시작
                intent = new Intent(App.ApplicationContext(), MyService.class);
                //intent.putExtra("State", "start")
                App.ApplicationContext().startForegroundService(intent);
                Log.d("TAG", "Service Start");
                Toast.makeText(getApplicationContext(), "service start", Toast.LENGTH_SHORT).show();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(App.ApplicationContext(), MyService.class);
                stopService(intent);
                //service.run(false);
                Toast.makeText(getApplicationContext(), "service finish", Toast.LENGTH_SHORT).show();
            }
        });

        btnTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnTimeStart 시작");
                isstart = true;
                //Intent intent = new Intent(getApplicationContext(), MyService.class);
                service.run(true);
            }
        });

        btnTimeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnTimeFinish 시작");
                    isstart = false;
                    //Intent intent = new Intent(getApplicationContext(), MyService.class);
                    service.run(false);

            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume 시작");
        registerReceiver();
    }

    @Override
    protected void onPause(){
        Log.d(TAG, "onPause 시작");
        super.onPause();
        unregisterReceiver();
    }

    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy 시작");
        super.onDestroy();
        //stopService(intent);
    }
}
