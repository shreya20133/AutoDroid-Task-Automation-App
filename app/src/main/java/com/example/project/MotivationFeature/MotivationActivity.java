package com.example.project.MotivationFeature;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MotivationActivity extends AppCompatActivity {

    TimePicker timePicker;
    DatabaseReference myRef;
    DatabaseReference motivationDBReference;
    ArrayList<String> listQuotes=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        motivationDBReference=myRef.child("motivation");
        motivationDBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    listQuotes.add(ds.child("text").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(),"Error in reading from Database!",Toast.LENGTH_SHORT).show();
            }
        });
        timePicker = findViewById(R.id.timePicker);

        //attaching clicklistener on button
        findViewById(R.id.buttonAlarm).setOnClickListener(view -> {
            //We need a calendar object to get the specified time in millis
            //as the alarm manager method takes time in millis to setup the alarm
            Calendar calendar = Calendar.getInstance();
            if (Build.VERSION.SDK_INT >= 23) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        timePicker.getHour(), timePicker.getMinute(), 0);
            } else {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
            }


            setAlarm(calendar.getTimeInMillis());
        });
    }
    private void setAlarm(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, DailyAlarm.class);

        if(listQuotes!=null && !listQuotes.isEmpty())
        {
            Random random_method = new Random();
            int index = random_method.nextInt(listQuotes.size());
            i.putExtra("QuoteofDay","Hi there!. Here is the Quote of the Day from AutoDroid."+listQuotes.get(index)+". Hope you have an amazing day!");
        }

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }
}
