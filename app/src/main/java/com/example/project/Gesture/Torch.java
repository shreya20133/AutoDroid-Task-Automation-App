package com.example.project.Gesture;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.project.R;
import com.example.project.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Torch extends AppCompatActivity {
boolean isON=false;
    private DatabaseReference mDatabase ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Button bStart = (Button) findViewById(R.id.torchg);
//        Button bStop = (Button) findViewById(R.id.torchgs);
        SwitchCompat torchSwitch = findViewById(R.id.torch_switch);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        torchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (firebaseUser != null) {
                        // User is signed in
                        System.out.println("CURRENTTTTT" + user.getEmail());
                        System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                        mDatabase.child("User_Macro").child(user.getUid()).child("Torch").setValue("1");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }
                    Intent intent1 = new Intent(getApplicationContext(), shakedetection.class);
                    startService(intent1);
                } else {
                    if (firebaseUser != null) {
                        // User is signed in
                        mDatabase.child("User_Macro").child(user.getUid()).child("Torch").setValue("0");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }
                    Intent intent1 = new Intent(getApplicationContext(), shakedetection.class);
                    stopService(intent1);
                }
            }
        });
        for (int i = 0; i < HomeFragment.featureName.size(); i++) {
            if (HomeFragment.featureName.get(i).equals("Torch")) {
                if (HomeFragment.featureValue.get(i).equals("1"))
                    torchSwitch.setChecked(true);
                else
                    torchSwitch.setChecked(false);
            }
        }
//        bStart.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//                if (user != null) {
//                    // User is signed in
//                    System.out.println("CURRENTTTTT"+user.getEmail());
//                    System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
//                    mDatabase.child("User_Macro").child(user.getUid()).child("Torch").setValue("1");
//                    mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
//                } else {
//                    System.out.println("INVALID USER....");
//                }
//
//
//        Intent intent1 = new Intent(v.getContext(), shakedetection.class);
//        startService(intent1); }
//        });
//        bStop.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//                if (user != null) {
//                    // User is signed in
//                    System.out.println("CURRENTTTTT"+user.getEmail());
//                    System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
//                    mDatabase.child("User_Macro").child(user.getUid()).child("Torch").setValue("0");
//                    mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
//                } else {
//                    System.out.println("INVALID USER....");
//                }
//
//
//                Intent intent1 = new Intent(v.getContext(), shakedetection.class);
//                stopService(intent1); }
//        });

//    }
//    public void ToggleTorch(View v) throws CameraAccessException {
//        Button b1=(Button)v;
//        if(b1.getText().toString().equals("Torch On")){
//            b1.setText("Torch Off");
//            tog("On");
//        }
//        else {
//            b1.setText("Torch On");
//            tog("Off");
//        }
//    }
//
//    private void tog(String ty) throws CameraAccessException {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            CameraManager cameraManager=(CameraManager) getSystemService(Context.CAMERA_SERVICE);
//            String cid=null;
//            if(cameraManager != null){
//                cid=cameraManager.getCameraIdList()[0];
//            }
//            if (cameraManager != null){
//                if(ty.equals("On")){
//                    cameraManager.setTorchMode(cid,true);
//                    isON=true;
//                }
//                else{
//                    cameraManager.setTorchMode(cid,false);
//                    isON=false;
//                }
//
//            }
//        }
    }
}