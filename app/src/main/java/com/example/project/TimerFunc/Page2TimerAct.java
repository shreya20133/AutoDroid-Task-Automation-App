package com.example.project.TimerFunc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.Services.NotifyService;
import com.example.project.broadcastreceivers.MyBroadcastReceivaer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page2TimerAct extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    Button startService;
    Button stopSer;
    AlarmManager alarmManager;
    List<limitApp> ApksListToLimit = new ArrayList<limitApp>();
    HashMap<String,HashMap<String,String>>ares = new HashMap<>();
    int j=0;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String appname = intent.getStringExtra("appname");
            String packagename = intent.getStringExtra("packagename");
            String limit = intent.getStringExtra("limit");
            System.out.println("MAINACTIVITYYYYYY:  "+limit);
            ApksListToLimit.add(new limitApp(appname,packagename,limit));
            for(int i=0; i<ApksListToLimit.size();i++) {
                System.out.println(ApksListToLimit.get(i).getApkName() + ApksListToLimit.get(i).getPackageName() + ApksListToLimit.get(i).getLimit());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2_timer);
//
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        ares = new HashMap<>();
        if(user!=null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("TimerApp").child(user.getUid()).child("app");
            db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    System.out.println("FIREBASEEEE ..  "+task.getResult().getValue());
                    ares= (HashMap<String,HashMap<String, String>>) task.getResult().getValue();
//                    System.out.println("INLOOPPPP "+ares.size());
//                    System.out.println(ares);
                    String apkn="",limitime="",pckg="";
                   if(ares!=null){
                       for(Map.Entry<String,HashMap<String,String>> bet:ares.entrySet()){
                           for (Map.Entry<String,String> entry : bet.getValue().entrySet()) {
                               System.out.println("Key = " + entry.getKey() +", Value = " + entry.getValue());
                               if (entry.getKey().equals("apkName")){
                                   apkn = entry.getValue();
                               }else if (entry.getKey().equals("limit")){
                                   limitime = entry.getValue();
                               }else if(entry.getKey().equals("packageName")){
                                   pckg = entry.getValue();
                               }
                           }
                           System.out.println(apkn+"..."+limitime+"..."+pckg);
                           ApksListToLimit.add(new limitApp(apkn,pckg,limitime));
//                        System.out.println("Object11"+ares.get(i).getClass());
                       }
                   }
                }
            });
        }
//        if(ares!=null){
//
//            for (Map.Entry<String,String> entry : ares.entrySet()) {
//                System.out.println("Key = " + entry.getKey() +
//                        ", Value = " + entry.getValue());
//                System.out.println(entry.getKey());
////                ApksListToLimit.add(new limitApp(entry.getKey(),entry.getValue()));
//            }
//        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        startService = (Button) findViewById(R.id.startService);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlert(savedInstanceState);

            }
        });
        stopSer = (Button) findViewById(R.id.stopService);
        stopSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopAlert(savedInstanceState);
//                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1253, intent, 0);

                Intent intent1 = new Intent(getApplicationContext(), NotifyService.class);
                stopService(intent1);

                Intent intent = new Intent(getApplicationContext(), MyBroadcastReceivaer.class);
                AlarmManager alarmMger = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE);
                alarmMger.cancel(pendingIntent);
            }
        });
        recyclerViewLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new AppsAdapter(this, new ApkInfoExtractor(this).GetAllInstalledApkInfo());
        recyclerView.setAdapter(adapter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("apps details"));
    }


    public void startAlert(Bundle savedInstanceState){

        int i=120;


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////        ares = new HashMap<>();
//        if(user!=null) {
//            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("TimerApp").child(user.getUid()).child("app");
//            db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
////                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    System.out.println("FIREBASEEEE ..  " + task.getResult().getValue());
//                    ares = (HashMap<String, HashMap<String, String>>) task.getResult().getValue();
////                    System.out.println("INLOOPPPP "+ares.size());
////                    System.out.println(ares);
//                    String apkn = "", limitime = "", pckg = "";
//                    if (ares != null) {
//                        for (Map.Entry<String, HashMap<String, String>> bet : ares.entrySet()) {
//                            for (Map.Entry<String, String> entry : bet.getValue().entrySet()) {
//                                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//                                if (entry.getKey().equals("apkName")) {
//                                    apkn = entry.getValue();
//                                } else if (entry.getKey().equals("limit")) {
//                                    limitime = entry.getValue();
//                                } else if (entry.getKey().equals("packageName")) {
//                                    pckg = entry.getValue();
//                                }
//                            }
//                            System.out.println(apkn + "..." + limitime + "..." + pckg);
//                            ApksListToLimit.add(new limitApp(apkn, pckg, limitime));
////                        System.out.println("Object11"+ares.get(i).getClass());
//                        }
//                    }
//                }
//            });
//
//        }

        Intent intent = new Intent(getApplicationContext(), MyBroadcastReceivaer.class);

        intent.putExtra("size",ApksListToLimit.size());
        intent.putExtra("j",j);
        for (; j <ApksListToLimit.size();j++){
            System.out.println(ApksListToLimit.get(j).getApkName());
            intent.putExtra("name"+j,ApksListToLimit.get(j).getApkName());
            intent.putExtra("pkname"+j,ApksListToLimit.get(j).getPackageName());
            intent.putExtra("limit"+j,ApksListToLimit.get(j).getLimit());
        }
//        234324243
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ (i), pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+10,
                100,pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
    }

}