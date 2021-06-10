package com.example.project.Gesture;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.example.project.R;
import com.example.project.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.project.R.*;

public class silent extends AppCompatActivity {
    private DatabaseReference mDatabase ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_silent);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Button bStart=(Button) findViewById(id.son);
//        Button bStop=(Button) findViewById(id.soff);
        SwitchCompat btnOnOff = (SwitchCompat)findViewById(R.id.silentonShake_switch);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btnOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (firebaseUser != null) {
                        // User is signed in
                        System.out.println("CURRENTTTTT" + user.getEmail());
                        System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                        mDatabase.child("User_Macro").child(user.getUid()).child("Silent").setValue("1");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }
                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            && !notificationManager.isNotificationPolicyAccessGranted()) {

                        Intent intent1 = new Intent(
                                android.provider.Settings
                                        .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                        startActivity(intent1);
                    }
                    Intent intent1 = new Intent(getApplicationContext(), com.example.project.Gesture.sService.class);
                    startService(intent1);
                } else {
                    if (firebaseUser != null) {
                        // User is signed in
                        mDatabase.child("User_Macro").child(user.getUid()).child("Silent").setValue("0");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }
                    Intent intent1 = new Intent(getApplicationContext(), com.example.project.Gesture.sService.class);
                    stopService(intent1);
                }
            }
        });
        for (int i = 0; i < HomeFragment.featureName.size(); i++) {
            if (HomeFragment.featureName.get(i).equals("Silent")) {
                if (HomeFragment.featureValue.get(i).equals("1"))
                    btnOnOff.setChecked(true);
                else
                    btnOnOff.setChecked(false);
            }
        }
//        bStart.setOnClickListener(new View.OnClickListener(){
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//
//
//                if (user != null) {
//                    // User is signed in
//                    System.out.println("CURRENTTTTT"+user.getEmail());
//                    System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
//                    mDatabase.child("User_Macro").child(user.getUid()).child("Silent").setValue("1");
//                    mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
//                } else {
//                    System.out.println("INVALID USER....");
//                }
//
//
//
//                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                        && !notificationManager.isNotificationPolicyAccessGranted()) {
//
//                    Intent intent1 = new Intent(
//                            android.provider.Settings
//                                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//
//                    startActivity(intent1);
//                }
//                Intent intent1 = new Intent(v.getContext(), com.example.project.Gesture.sService.class);
//                startService(intent1);
//
//            }
//        });
//        bStop.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//
//                if (user != null) {
//                    // User is signed in
//                    System.out.println("CURRENTTTTT"+user.getEmail());
//                    System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
//                    mDatabase.child("User_Macro").child(user.getUid()).child("Silent").setValue("0");
//                    mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
//                } else {
//                    System.out.println("INVALID USER....");
//                }
//
//
//                Intent intent1 = new Intent(v.getContext(), com.example.project.Gesture.sService.class);
//                stopService(intent1); }
//        });

    }
}