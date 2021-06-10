package com.example.project.StepCounterFit;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;

public class MainPagefit extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor stepCounterSensor;
    Sensor stepDetectorSensor;
    int notification_id = 1711101;
    int currentStepsDetected;

    int stepCounter;
    int newStepCounter;

//    boolean serviceStopped;

//    Intent intent;

//    private static final String TAG = "StepService";
//    public static final String BROADCAST_ACTION = ".StepCountingService";

    // Create a handler - that will be used to broadcast our data, after a specified amount of time.
//    private final Handler handler = new Handler();
    // counter number of times the service carried out updates.
    int counter = 0;
    TextView t1,t2;
    SwitchCompat startStop;

    // Service is being created

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pagefit);

        createNotificationChannel();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        }
        startStop = (SwitchCompat)findViewById(R.id.startStep);
        t1 =findViewById(R.id.stcnt);
        t2 = findViewById(R.id.stdist);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        startStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    registerListener();
                    currentStepsDetected = 0;

                    stepCounter = 0;
                    newStepCounter = 0;
                }else{
                    deregister();
                    currentStepsDetected = 0;

                    stepCounter = 0;
                    newStepCounter = 0;

                }
            }
        });

        System.out.println(stepCounterSensor);
        System.out.println(stepDetectorSensor);
        //currentStepCount = 0;

        System.out.println("Starting steps counter");

//        serviceStopped = false;

        // Existing callbacks to the handler are removed
//        handler.removeCallbacks(updateBroadcastData);
        // handler call
//        handler.post(updateBroadcastData);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "AutoDroid",
                    "AutoDroid",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    void registerListener(){

        sensorManager.registerListener(this, stepCounterSensor, 5);
        sensorManager.registerListener(this, stepDetectorSensor, 5);
    }



    void deregister(){
        sensorManager.unregisterListener(this, stepCounterSensor);
        sensorManager.unregisterListener(this, stepDetectorSensor);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        //Step count sensor
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int) event.values[0];

            // Initially stepCounter will be zero
            if (stepCounter == 0) {
                stepCounter = (int) event.values[0];
            }
            newStepCounter = countSteps - stepCounter;
            t1.setText(newStepCounter+"");
        }

        // Step detector sensor
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            int detectSteps = (int) event.values[0];
            currentStepsDetected += detectSteps;
            t1.setText(currentStepsDetected+"");
        }

//        Counting day
        float dist = getDistanceRun(currentStepsDetected);
        t2.setText("Distance: "+dist+"km");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this, stepCounterSensor);
//        sensorManager.unregisterListener(this, stepDetectorSensor);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        sensorManager.unregisterListener(this, stepCounterSensor);
//        sensorManager.unregisterListener(this, stepDetectorSensor);
//    }

    public float getDistanceRun(long steps){
        float distance = (float)(steps*78)/(float)100000;
        return distance;
    }
}