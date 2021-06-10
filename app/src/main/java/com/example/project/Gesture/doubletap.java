package com.example.project.Gesture;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.project.R;
import com.example.project.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class doubletap extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    GestureDetector gestureDetector;
    public static final int SYSTEM_ALERT_WINDOW_PERMISSION = 7;
    private Button lock, disable, enable, flock;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    private DatabaseReference mDatabase ;
    SwitchCompat switchBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubletap);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, com.example.project.Gesture.MyAdmin.class);
        gestureDetector = new GestureDetector(doubletap.this, doubletap.this );
        lock = (Button) findViewById(R.id.lock);
        enable = (Button) findViewById(R.id.enableBtn);
        disable = (Button) findViewById(R.id.disableBtn);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        switchBtn = (SwitchCompat) findViewById(R.id.quickLock_switch);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (user != null) {
                        // User is signed in
                        System.out.println("CURRENTTTTT"+user.getEmail());
                        System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                        mDatabase.child("User_Macro").child(user.getUid()).child("Lock").setValue("1");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }

                } else {
                    if (firebaseUser != null) {
                        // User is signed in
                        mDatabase.child("User_Macro").child(user.getUid()).child("Lock").setValue("0");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }
//                    Intent intent1 = new Intent(getApplicationContext(), shakedetection.class);
//                    stopService(intent1);
                }
            }
        });
        for (int i = 0; i < HomeFragment.featureName.size(); i++) {
            if (HomeFragment.featureName.get(i).equals("Lock")) {
                if (HomeFragment.featureValue.get(i).equals("1"))
                    switchBtn.setChecked(true);
                else
                    switchBtn.setChecked(false);
            }
        }

        lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            RuntimePermissionForUser();
        }

        Button flock = (Button) findViewById(R.id.floatlock);
        flock.setOnClickListener(this);
        flock.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (user != null) {
                    // User is signed in
                    System.out.println("CURRENTTTTT"+user.getEmail());
                    System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                    mDatabase.child("User_Macro").child(user.getUid()).child("Lock").setValue("1");
                    mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                } else {
                    System.out.println("INVALID USER....");
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                    startService(new Intent(doubletap.this, FloatingService.class));

                    finish();

                } else if (Settings.canDrawOverlays(doubletap.this)) {

                    startService(new Intent(doubletap.this, FloatingService.class));

                    finish();

                } else {
                    RuntimePermissionForUser();

                    Toast.makeText(doubletap.this, "System Alert Window Permission Is Required For Floating Widget.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    public void RuntimePermissionForUser() {

        Intent PermissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));

        startActivityForResult(PermissionIntent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }
    @Override
    public void onClick(View view) {
        if (view == lock) {
            boolean active = devicePolicyManager.isAdminActive(compName);

            if (active) {
                devicePolicyManager.lockNow();

            } else {
                Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
            }

        } else if (view == enable) {

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
            startActivityForResult(intent, RESULT_ENABLE);

        } else if (view == disable) {
            devicePolicyManager.removeActiveAdmin(compName);
            disable.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RESULT_ENABLE :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(doubletap.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(doubletap.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {

        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent event) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
//        boolean active = devicePolicyManager.isAdminActive(compName);
//
//        if (active) {
//            devicePolicyManager.lockNow();
//        } else {
//            Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
//        }
        Toast.makeText(doubletap.this, "Double Tap on Screen is Working.", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {

        return false;
    }

}