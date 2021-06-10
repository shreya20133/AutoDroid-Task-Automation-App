package com.example.project.ScheduledReminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.project.R;

import java.util.Locale;

public class TTSReminder extends Service implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    private TextToSpeech mTts;
    private String spokenText;
    //Notification notification;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTts = new TextToSpeech(this, this);
        spokenText=intent.getStringExtra("Reminder2");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
     //  mTts = new TextToSpeech(this, this);
        // This is a good place to set spokenText
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel("channelid1","AUTODROID", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        Notification notification=new NotificationCompat.Builder(this,"channelid1")
                .setContentTitle("Scheduled Reminder!")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("It's time to revisit your tasks for the day!")
                .build();

        if(notification!=null)
            startForeground(3,notification);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.ENGLISH);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TAG","INSIDE INIT");
                mTts.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @Override
    public void onUtteranceCompleted(String uttId) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
            stopForeground(true);
            stopSelf();
            Log.e("TAG","DESTROYED");
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
