package com.example.project.MotivationFeature;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class DailyAlarm extends BroadcastReceiver {

    //the method will be fired when the alarm is triggerred
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //you can check the log that it is fired
        //Here we are actually not doing anything
        //but you can do any task here that you want to be done at a specific time everyday
        String quoteOfDay=intent.getStringExtra("QuoteofDay");
        Intent intent1=new Intent(context,TTS.class);
        intent1.putExtra("Quote",quoteOfDay);
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
//        {
//            NotificationChannel notificationChannel=new NotificationChannel("channelid","AUTODROID", NotificationManager.IMPORTANCE_LOW);
//            NotificationManager manager=context.getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(notificationChannel);
//        }
//
//        Notification notification=new NotificationCompat.Builder(context,"channelid")
//                .setContentTitle("AUTODROID")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentText("Its time for Quote of the day!")
//                .build();

//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // notificationID allows you to update the notification later on.
//        mNotificationManager.notify(1, notification);
        context.startForegroundService(intent1);
        Log.d("MyAlarmBelal", "Alarm just fired");
    }

}