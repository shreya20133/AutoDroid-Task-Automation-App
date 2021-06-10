package com.example.project.ScheduledReminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class DailyAlarmReminder extends BroadcastReceiver {

    //the method will be fired when the alarm is triggerred
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        String ReminderOfDay=intent.getStringExtra("Reminder");
        Intent intent1=new Intent(context, TTSReminder.class);
        intent1.putExtra("Reminder2",ReminderOfDay);
        context.startForegroundService(intent1);
        Log.d("MyAlarmReminder", "Alarm just fired");
    }

}