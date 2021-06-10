package com.example.project.Gesture;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.NoCopySpan;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;
import com.example.project.Gesture.Utility;

@RequiresApi(api = VERSION_CODES.LOLLIPOP)
public class shakedetection extends Service implements SensorEventListener {



    public final int min_time=1000;
    SensorManager sensorManager=null;
    Vibrator vibrator=null;
    private long lasttime=0;
    private boolean isflash=false;
    private float shakethreshold=  40.0f;
    Utility utility;

    @Override
    public int  onStartCommand(Intent intent, int flags, int startId){

       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           NotificationChannel notificationChannel=new NotificationChannel("Channelid1","Autodroid App", NotificationManager.IMPORTANCE_DEFAULT);
           NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
        Intent inten=new Intent(this, com.example.project.Gesture.Torch.class);
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
                float x = event.values[2];
              //  float y= event.values[1];
               // float z= event.values[2];
              //  double acc =Math.sqrt(Math.pow(x,2) + Math.pow(y,2)+ Math.pow(z,2)- SensorManager.GRAVITY_EARTH);
                double acc =Math.sqrt(Math.pow(x,2) - SensorManager.GRAVITY_EARTH);
                if(acc > shakethreshold){
                    lasttime = currTime;
                    if(!isflash){
                        try {
                            isflash = utility.togg("On");
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            isflash = utility.togg("Off");
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
