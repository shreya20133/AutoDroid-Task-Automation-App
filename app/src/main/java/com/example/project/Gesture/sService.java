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
import android.hardware.camera2.CameraAccessException;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.project.R;
import  com.example.project.Gesture.Utility;

public class sService extends Service implements SensorEventListener {

    public final int min_time=1000;
    SensorManager sensorManager=null;
    Vibrator vibrator=null;
    private long lasttime=0;
    private boolean isflash=false;
    private float shakethreshold=  40.0f;
    private AudioManager myAudioManager;
    Utility utility;

    @Override
    public int  onStartCommand(Intent intent, int flags, int startId){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel("Channelid1","Autodroid App", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
        Intent inten=new Intent(this,silent.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,inten,0);
        Notification notification=new NotificationCompat.Builder(this,"Channelid1")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setContentTitle("AUTODROID")
                .setContentText("Enabled Gesture mode from AutoDroid!")
                .build();
        startForeground(1,notification);

        utility = new Utility(this);
        vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);

        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        if(sensorManager != null){
            Sensor accelerometer= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            long currTime=System.currentTimeMillis();
            if((currTime - lasttime) > min_time){
                float x = event.values[0];

                double acc =Math.sqrt(Math.pow(x,2) - SensorManager.GRAVITY_EARTH);
                if(acc > shakethreshold){
                    lasttime = currTime;
                    myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    if (!isflash){
                            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    isflash =  true;
                    }
                    else{
                                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        isflash =  false;
                    }

                }
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}