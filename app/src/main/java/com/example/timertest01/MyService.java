package com.example.timertest01;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyService extends Service {
    String CHANNEL_ID = "ForegroundChannel";
    Thread myThread;
    Thread myThread2;
//    Context mContext;
    //Handler handler;

    private String TAG = "SERVICE";

    public MyService() {

    }

    @Override
    public void onCreate() {
//        mContext = this;

        super.onCreate();
        Log.d(TAG, "onCreate 시작");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(
                    NotificationManager.class
            );
            manager.createNotificationChannel(serviceChannel);
        }
    }

    int total = 0;
    public static boolean started = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand 시작");
//        Context mContext = getApplicationContext();
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();

        startForeground(1, notification);

        //started = message;

//        if (started == true) {
//            handler = new Handler(Looper.getMainLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    String minute = String.format("%02d", total / 60);
//                    String second = String.format("%02d", total % 60);
//                    //val intent = Intent()
//                    Intent intent = new Intent("start");
//                    intent.putExtra("min", minute);
//                    intent.putExtra("sec", second);
//                    Log.d(TAG, "MIN : ${minute.toString()}");
//                    Log.d(TAG, "SEC : ${second.toString()}");
//                    //sendBroadcast(intent)
//                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//                }
//            };

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (started == true) {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (started == true) {
//                            total = total + 1;
//                            try {
//                                Thread.sleep(1000);
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            handler.sendEmptyMessage(0);
//                            if (Thread.interrupted()) {
//                                break;
//                            }
//                        } else if (started == false) {
//                            break;
//                        }
//                    }
//                }
//            }).start();
//        }else if (started == false) {
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (started == false) {
//                        total = 0;
//                        Intent intent = new Intent("stop");
//                        intent.putExtra("reset", "00:00");
//                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (Thread.interrupted()) {
//                            break;
//                        }
//                    }
//                }
//            }).start();
//        }

        return super.onStartCommand(intent, flags, startId);
        //START_NOT_STICKY
    }

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage 시작");
            String minute = String.format("%02d", total / 60);
            String second = String.format("%02d", total % 60);
            //val intent = Intent()
            Intent intent = new Intent("start");
            intent.putExtra("min", minute);
            intent.putExtra("sec", second);
            Log.d(TAG, "MIN : "+minute);
            Log.d(TAG, "SEC : "+second);
            //sendBroadcast(intent);
            LocalBroadcastManager.getInstance(App.ApplicationContext()).sendBroadcast(intent);
        }
    };
    Thread busyLoop;
    Thread busyLoop2;

    public void connectStart() {
        Log.d(TAG, "connectStart 시작");
        busyLoop = new Thread() {
            public void run() {
                Log.d(TAG, "connectStart run 시작");
                //busyLoop2.interrupt();
                while (started == true && !busyLoop.interrupted()) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (started == true) {
                        total = total + 1;
                        handler.sendEmptyMessage(0);
                        if (Thread.interrupted()) {
                            break;
                        }
                    } else if (started == false) {
                        break;
                    }
                }
            }
        };
    }

    private void connectStop() {
        Log.d(TAG, "connectStop 시작");
        total = 0;
        Intent intent = new Intent("stop");
        intent.putExtra("reset", "00:00");
        LocalBroadcastManager.getInstance(App.ApplicationContext()).sendBroadcast(intent);
    }


    public void run(Boolean message) {
        Log.d(TAG, "run 시작");
        started = message;
        if (started == true) {
            Log.d(TAG, started + "started 값");
            connectStart();
            busyLoop.start();
            Log.d(TAG, busyLoop.getState() + "");
            /*if (busyLoop2 != null && busyLoop2.isAlive()) {

                busyLoop2.interrupt();

            }*/
            //busyLoop2.interrupt();
        } else {
//            if(busyLoop.getState() == Thread.State.NEW) {
            connectStop();
//            busyLoop2.start();
//            Log.d(TAG, busyLoop.getState() + "");
//            if (busyLoop != null && busyLoop.isAlive()) {
//
//                busyLoop.interrupt();
//
//            }
            // busyLoop.interrupt();
        }
        /*if (started == true) {
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    String minute = String.format("%02d", total / 60);
                    String second = String.format("%02d", total % 60);
                    //val intent = Intent()
                    Intent intent = new Intent("start");
                    intent.putExtra("min", minute);
                    intent.putExtra("sec", second);
                    Log.d(TAG, "MIN : ${minute.toString()}");
                    Log.d(TAG, "SEC : ${second.toString()}");
                    //sendBroadcast(intent)
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
            };

            myThread.start();

        } else if (started == false) {
            myThread2.start();
            *//*thread(start = true) {
           while (started == false)
           {
               total =0;
               Intent intent = new Intent("stop");
               intent.putExtra("reset", "00:00");
               LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
               Thread.sleep(1000);
               if (Thread.interrupted()){
                   break;
               }
           }*//*
        }*/
        //total = 0
//           val intent = Intent("stop")
//           intent.putExtra("reset", "00:00")
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy 시작");

    }
}
