package com.example.project.Gesture;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.project.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class lock extends AppCompatActivity {
    String s=" ";
    private DevicePolicyManager devicePolicyManager;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s="onCreate";
        setContentView(R.layout.activity_lock);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName compName = new ComponentName(this, com.example.project.Gesture.MyAdmin.class);
        boolean active = devicePolicyManager.isAdminActive(compName);
       if (active) {
            devicePolicyManager.lockNow();
       } else {
           Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
       }
       //onDestroy();

    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Intent intent1 = new Intent(this, lockSer.class);
//        startService(intent1);
//    }
@Override
public void  onStart() {
    super.onStart();
    if(s.equals("onRestart")){
       // android.os.Process.killProcess(android.os.Process.myPid());
 finish();
    }
    s="onStart";
}

    @Override
    protected void onStop() {
        super.onStop();
        s="onStop";
    }

    @Override
    protected void onPause() {
        super.onPause();
        s="onPause";
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        s="onRestart";
    }
}