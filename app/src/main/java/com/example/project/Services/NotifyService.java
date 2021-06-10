package com.example.project.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.project.R;

import java.util.HashSet;
import java.util.Random;

public class NotifyService extends Service {
    Random rnd;
    String all_name="";
    HashSet<String> uniqueapp;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rnd= new Random();
        uniqueapp=new HashSet<>();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String text = intent.getStringExtra("extrastring");
        String name = intent.getStringExtra("appname");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Channelid1", "LIMIT APP USAGE TIME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
//        Intent inten = new Intent(this, MainActivity.class);
        uniqueapp.add(name);
        for(String app:uniqueapp){
            all_name=all_name+"\n"+app;
        }
        Log.d("text", text);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, "Channelid1").setContentTitle("STOP!! Using these app ")
                .setContentText("OVERUSED APP").setSmallIcon(R.mipmap.ic_launcher).setStyle(new NotificationCompat.BigTextStyle().bigText(all_name)).setContentIntent(pendingIntent).build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        startForeground(rnd.nextInt(1000), notification);
        all_name="";
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

}