package com.example.project.Gesture;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.project.HomeActivity;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FloatingService extends Service {

    private long lastTouchTime = 0;
    private long currentTouchTime = 0;
    private int clks =0;
    WindowManager windowManager;
    View floatingView, collapsedView, expandedView;
    WindowManager.LayoutParams params ;
    private DatabaseReference mDatabase ;
    public FloatingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layoutl, null);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);
        collapsedView = floatingView.findViewById(R.id.Layout_Collapsed);
        expandedView = floatingView.findViewById(R.id.Layout_Expended);
        ImageButton exlock = (ImageButton) floatingView.findViewById(R.id.expanded_menu_lock);
        exlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("lock btn clicked");
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                Intent intent = new Intent(FloatingService.this, com.example.project.Gesture.lock.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        ImageButton exhome = (ImageButton) floatingView.findViewById(R.id.expanded_menu_home);
        exhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                Intent intent = new Intent(FloatingService.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        expandedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);

//                if (currentTouchTime - lastTouchTime < 2500) {
//                    lastTouchTime = 0;
//                    currentTouchTime = 0;
//                    collapsedView.setVisibility(View.VISIBLE);
//                    expandedView.setVisibility(View.GONE);
//                    Intent intent = new Intent(FloatingService.this, com.example.project.Gesture.lock.class);
//               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//               startActivity(intent);
//                }
//                else{
//                collapsedView.setVisibility(View.VISIBLE);
//                expandedView.setVisibility(View.GONE);
//                 }

            }
        });
        floatingView.findViewById(R.id.Widget_Close_Icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    // User is signed in
                    System.out.println("CURRENTTTTT"+user.getEmail());
                    System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                    mDatabase.child("User_Macro").child(user.getUid()).child("Lock").setValue("0");
                    mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                } else {
                    System.out.println("INVALID USER....");
                }
                stopSelf();
            }
        });
        floatingView.findViewById(R.id.MainParentRelativeLayout).setOnTouchListener(new View.OnTouchListener() {
            int X_Axis, Y_Axis;
            float TouchX, TouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        X_Axis = params.x;
                        Y_Axis = params.y;
                        TouchX = event.getRawX();
                        TouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = X_Axis + (int) (event.getRawX() - TouchX);
                        params.y = Y_Axis + (int) (event.getRawY() - TouchY);
                        windowManager.updateViewLayout(floatingView, params);
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }

}