package com.example.project.ScheduledReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ReminderActivity extends AppCompatActivity {

    TimePicker timePicker;
    EditText editText;
    Button savebtn;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    HashMap<String,ReminderDB> reminderDBHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduledreminder);
        reminderDBHashMap = new HashMap<>();
        editText = findViewById(R.id.editText_reminder);
        savebtn = findViewById(R.id.Reminder_SaveButton);
        timePicker = findViewById(R.id.timePickerReminder);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        savebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                if (Build.VERSION.SDK_INT >= 23) {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                } else {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                }

                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Log.e("TAG", "inside if reminder db");
                    reminderDBHashMap.put(user.getUid(), new ReminderDB(editText.getText().toString(), calendar.getTimeInMillis()));
                    mDatabase.child("ScheduledReminder").child(user.getUid()).child("reminder").setValue(reminderDBHashMap);
                    mDatabase.child("ScheduledReminder").child(user.getUid()).child("email").setValue(user.getEmail());
                } else {
                    System.out.println("INVALID USER....");
                }
                setAlarm(calendar.getTimeInMillis());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarm(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, DailyAlarmReminder.class);

        if(user!=null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("ScheduledReminder").child(user.getUid()).child("reminder");
            db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                        reminderDBHashMap = (HashMap<String, ReminderDB>) task.getResult().getValue();
                }
            });
        }

        i.putExtra("Reminder","Hi there!. Here are your Reminders from AutoDroid."+ Objects.requireNonNull(reminderDBHashMap.get(user.getUid())).reminder + " Hope you complete all your tasks!");

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //setting the repeating alarm that will be fired every day
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pi);
//        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Relax! We'll remind you all your tasks", Toast.LENGTH_SHORT).show();
    }
}
