package com.example.project.Gesture;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.project.R;

public class pService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    Boolean isOn = true;
    MediaPlayer player;
    private AudioManager mAudioManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel("Channelid1","Autodroid App", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
        Intent inten=new Intent(this,com.example.project.Gesture.playpause.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,inten,0);
        Notification notification=new NotificationCompat.Builder(this,"Channelid1")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("AUTODROID")
                .setContentText("Enabled Gesture mode from AutoDroid!")
                .build();
        startForeground(1,notification);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        return START_NOT_STICKY;
    }

//    protected void onPause() {
//        super.onPause();
//        mAudioManager.setMode(AudioManager.MODE_NORMAL);
//        try {
//            // unregisterReceiver(mReciever);
//            mSensorManager.unregisterListener(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (player != null) {
//            player.stop();
//            player.release();
//            player = null;
//        }
//    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        Log.e("distance", String.valueOf(event.values[0]));
        Log.e("MaximumRange", String.valueOf(mSensor.getMaximumRange()));

        if (event.values[0] <= mSensor.getMaximumRange()) {
            if (mAudioManager.isMusicActive() && isOn == true) {
                isOn = false;
                System.out.println("offfffff");
                System.out.println(mAudioManager.isMusicActive());
//                mAudioManager.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) this);
                //mAudioManager.requestAudioFocus(null,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            } else if (isOn == false) {
                isOn = true;
                System.out.println("onnnnnnn");
                System.out.println(mAudioManager.isMusicActive());

                mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            }
        } else {
            mAudioManager.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) this);
        }

    }
}