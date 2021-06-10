package com.example.project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.TimerFunc.ApkInfoExtractor;
import com.example.project.TimerFunc.AppsAdapter;
import com.example.project.TimerFunc.DistAppAct;
import com.example.project.TimerFunc.Page2TimerAct;
import com.example.project.TimerFunc.limitApp;
import com.example.project.broadcastreceivers.MyBroadcastReceivaer;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class TimerActivity extends AppCompatActivity {

    ArrayList<PieEntry> pe = new ArrayList<>();
    ApkInfoExtractor a1;
    PieChart pc;
//    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent
//            String appname = intent.getStringExtra("appname");
//            String packagename = intent.getStringExtra("packagename");
//            String limit = intent.getStringExtra("limit");
//            System.out.println("MAINACTIVITYYYYYY:  "+limit);
//            ApksListToLimit.add(new limitApp(appname,packagename,limit));
//            for(int i=0; i<ApksListToLimit.size();i++) {
//                System.out.println(ApksListToLimit.get(i).getApkName() + ApksListToLimit.get(i).getPackageName() + ApksListToLimit.get(i).getLimit());
//            }
//        }
//    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer2);

//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        startService = (Button) findViewById(R.id.startService);
//        startService.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startAlert(savedInstanceState);
//
//            }
//        });
//        stopService = (Button) findViewById(R.id.stopService);
//        stopService.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                stopAlert(savedInstanceState);
//                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1253, intent, 0);
//                alarmManager.cancel(pendingIntent);
//            }
//        });
//        recyclerViewLayoutManager = new GridLayoutManager(TimerActivity.this, 1);
//        recyclerView.setLayoutManager(recyclerViewLayoutManager);
//        adapter = new AppsAdapter(TimerActivity.this, new ApkInfoExtractor(TimerActivity.this).GetAllInstalledApkInfo());
//        recyclerView.setAdapter(adapter);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("apps details"));
        a1 = new ApkInfoExtractor(TimerActivity.this);
        List<String> apps = a1.GetAllInstalledApkInfo();
        pc =findViewById(R.id.pappusg);
        int others=0;
        for(int i=0;i<apps.size();i++){
            System.out.println("in "+a1.GetAppTime(apps.get(i)));
            String res = a1.GetAppTime(apps.get(i));
            String[] a = res.split(":");
            System.out.println("time res"+a.length);
            int aux=0;
            if(!a[0].equals("") && !a[1].equals(""))
                aux = Integer.parseInt(a[0])*60+Integer.parseInt(a[1]);
            String name = a1.GetAppName(apps.get(i));
            System.out.println(name+" namess minutessss");
            if(aux>0) {
                if(aux<=15) {
                    others += aux;
                }
                else
                    pe.add(new PieEntry(aux, name+"  ("+aux/60+"hr "+aux%60+"min) "));
            }
        }
        pe.add(new PieEntry(others,"Others"+"  ("+others/60+"hr "+others%60+"min) "));
        PieDataSet pds = new PieDataSet(pe,"App Usage");
        pds.setColors(ColorTemplate.COLORFUL_COLORS);
        pds.setValueTextColor(Color.BLACK);
        pds.setValueTextSize(16f);
        pds.setDrawValues(false);
        PieData pd =new PieData(pds);
//        pds.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pc.setData(pd);
        pc.setHoleRadius(20f);
        pc.setTransparentCircleRadius(30f);

        pc.setDrawCenterText(true);

        pc.setRotationAngle(0);
        // enable rotation of the chart by touch
        pc.setRotationEnabled(true);
        pc.setHighlightPerTapEnabled(true);
        pc.getDescription().setText("");
        pc.getLegend().setEnabled(false);
        pc.animateY(2400, Easing.EaseInOutQuad);
//        pc.animate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        a1 = new ApkInfoExtractor(TimerActivity.this);
    }

    public void ShowAll(View view){
        Intent i1 =new Intent(this, Page2TimerAct.class);
        startActivity(i1);
    }

    public void dapp(View view){
        Intent i1 =new Intent(this, DistAppAct.class);
        startActivity(i1);
    }
//
//    public void startAlert(Bundle savedInstanceState){
//
//        int i=120;
//        Intent intent = new Intent(getApplicationContext(), MyBroadcastReceivaer.class);
//
//        intent.putExtra("size",ApksListToLimit.size());
//        intent.putExtra("j",j);
//        for (; j <ApksListToLimit.size();j++){
//            System.out.println(ApksListToLimit.get(j).getApkName());
//            intent.putExtra("name"+j,ApksListToLimit.get(j).getApkName());
//            intent.putExtra("pkname"+j,ApksListToLimit.get(j).getPackageName());
//            intent.putExtra("limit"+j,ApksListToLimit.get(j).getLimit());
//        }
////        234324243
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                getApplicationContext(), 0, intent, 0);
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ (i), pendingIntent);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+10,
//                100,pendingIntent);
//        Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
//    }
}