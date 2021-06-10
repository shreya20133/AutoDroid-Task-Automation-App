package com.example.project.driveUpload;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.project.R;
import com.example.project.broadcastreceivers.ReceiveAlarmUploadReceiver;
import com.example.project.ui.home.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class photoUploader extends AppCompatActivity {
    private DatabaseReference mDatabase ;
    private SwitchCompat imgUploader ;
    private int SIGN_REQUEST_CODE = 200, eventId = 0;
    public ImageView helpImageView;
    public MaterialCardView featureInfoDriveCardView;
    private String TAG = "autodroid";
    public static Drive drive;
    private ArrayList<String> imageArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo_uploader);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        imgUploader = findViewById(R.id.Driveupload_switch);
        helpImageView=findViewById(R.id.helpDrive);
        featureInfoDriveCardView=findViewById(R.id.featureinfo_Drive);
        helpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                featureInfoDriveCardView.setVisibility(View.VISIBLE);
            }
        });
        imgUploader.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if (firebaseUser != null) {
                        // User is signed in
                        System.out.println("CURRENTTTTT" + user.getEmail());
                        System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                        mDatabase.child("User_Macro").child(user.getUid()).child("Drive").setValue("1");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }
                    boolean internetStatus = checkInternetConnection();
                    Log.d(TAG, "internet status: " + internetStatus);
                    if (internetStatus) {
                        Log.d(TAG, "entered inside the if cond of INTStatus");
                        RequestForGoogleAccountSignUp();
                        startAlarm();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Internet connection is not available. Cannot Enable Feature.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if (firebaseUser != null) {
                        // User is signed in
                        mDatabase.child("User_Macro").child(user.getUid()).child("Drive").setValue("0");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }
                }
            }
        });
        for (int i = 0; i < HomeFragment.featureName.size(); i++) {
            if (HomeFragment.featureName.get(i).equals("Drive")) {
                if (HomeFragment.featureValue.get(i).equals("1"))
                    imgUploader.setChecked(true);
                else
                    imgUploader.setChecked(false);
            }
        }
    }

    public boolean checkInternetConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ReceiveAlarmUploadReceiver.class);
        //intent.putExtra("Drive ", (Serializable) drive);
        //intent.putExtra("frag Context", getActivity());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),eventId,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        eventId +=1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.MINUTE, 00);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


    public void RequestForGoogleAccountSignUp(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope("https://www.googleapis.com/auth/drive"),new Scope("https://www.googleapis.com/auth/drive.appdata"),new Scope("https://www.googleapis.com/auth/drive.file"), new Scope("https://www.googleapis.com/auth/drive.metadata"), new Scope("https://www.googleapis.com/auth/drive.scripts")).build();
        //requestScopes(new Scope(DriveScopes.DRIVE_FILE))  requestScopes(new Scope("https://www.googleapis.com/auth/drive"))
        Log.d("autodroid", "signUpoptions" +String.valueOf(googleSignInOptions));
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);
        Log.d("autodroid", "signin client" +String.valueOf(googleSignInClient));
        Log.d("autodroid", "signin client intent" +String.valueOf(googleSignInClient.getSignInIntent()));
        startActivityForResult(googleSignInClient.getSignInIntent(), SIGN_REQUEST_CODE);
        Log.d("autodroid","called startActivityfor result");
        googleSignInClient.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("autodroid", String.valueOf(requestCode)+ " "+ String.valueOf(resultCode));
        Log.d("autodroid", String.valueOf(data));
        if(requestCode == SIGN_REQUEST_CODE && resultCode == RESULT_OK){
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                @Override
                public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                    GoogleAccountCredential googleAccountCredential =  GoogleAccountCredential.usingOAuth2(getApplicationContext(),Collections.singleton(DriveScopes.DRIVE_FILE));
                    Log.d("autodroid","credential "+ String.valueOf(googleAccountCredential));
                    googleAccountCredential.setSelectedAccount(googleSignInAccount.getAccount());
                    Log.d("autodroid","account "+ String.valueOf(googleSignInAccount.getAccount()));
                    drive = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), googleAccountCredential).setApplicationName("PhotoUploder").build();
                    Log.d("autodroid","dive "+ String.valueOf(drive));
                    Toast.makeText(getApplicationContext()," Successfull Sign Up ",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("autodroid","error" + e.getMessage());
                }
            });
        }else{
            Log.d("autodroid","enter in else part of activity result");
        }
    }



}