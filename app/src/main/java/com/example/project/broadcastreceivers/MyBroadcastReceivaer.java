package com.example.project.broadcastreceivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.project.Services.NotifyService;
import com.example.project.TimerFunc.ApkInfoExtractor;
import com.example.project.TimerFunc.limitApp;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MyBroadcastReceivaer extends BroadcastReceiver implements Serializable {
    List<limitApp> ApksListToLimit = new ArrayList<limitApp>();
    //ApkInfoExtractor ob =new ApkInfoExtractor(MainActivity);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //MediaPlayer mp = MediaPlayer.create(context, notification);
        //mp.start();
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 234324243, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        System.out.println("inside service");
        ApkInfoExtractor ob =new ApkInfoExtractor(context);
        //ApksListToLimit = (List<MainActivity.limitApp>) intent.getSerializableExtra("LIST");
        // System.out.println(intent.hasExtra("ex"));
        int size = intent.getIntExtra("size",0);
        System.out.println(size);
        int i=intent.getIntExtra("j",0);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss", Locale.CHINA);
        TimeZone inTimeZone = TimeZone.getTimeZone("IST");
        for (;i<size;i++) {
            System.out.println(intent.getStringExtra("name"+i));
            String name = intent.getStringExtra("name"+i);
            System.out.println(intent.getStringExtra("pkname"+i));
            String simit = intent.getStringExtra("limit"+i);
            try {
                dateFormat.setTimeZone(inTimeZone);
                Date d = dateFormat.parse(ob.GetAppTime(intent.getStringExtra("pkname"+i)));
                long t= d.getTime();
                long limit = Long.parseLong(simit);
                System.out.println(t+" :t______l: "+limit);
                NotificationManager mNotificationManager;
                if(t>limit) {
                    System.out.println("EXCEEEEEEEEEEEEEEDDDDDDDDD");
//                    Toast.makeText(context,"App Exceed",Toast.LENGTH_SHORT).show();
//                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//                        NotificationChannel notificationChannel=new NotificationChannel("Channelid1","Demo Notification",
//                                NotificationManager.IMPORTANCE_DEFAULT);
//                        NotificationManager manager=context.getSystemService(NotificationManager.class);
//                        manager.createNotificationChannel(notificationChannel);
//
//                    }
////                    Intent inten=new Intent(this,MainActivity.class);
////                    Log.d("text",text);
//                    PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
//                    Notification notification=new NotificationCompat.Builder(context,"Channelid1").setContentTitle("Song ForegroundService")
//                            .setContentText("Agyiiii").setSmallIcon(R.drawable.ic_launcher_background).setContentIntent(pendingIntent).build();

//                    context.startForeground(1,notification);
                    Intent i1=new Intent(context, NotifyService.class);
                    i1.putExtra("extrastring","Func1");
                    i1.putExtra("appname",name);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        context.startForegroundService(i1);
//                    }else {
//                        context.startService(i1);
//                    }
                    ContextCompat.startForegroundService(context,i1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if(limit.equals(ob.GetAppTime(intent.getStringExtra("pkname"+i))))
            System.out.println("usage time : "+ob.GetAppTime(intent.getStringExtra("pkname"+i)));
        }

//        if (intent.hasExtra("LIST")) {
//           // System.out.println("true");
//        //    Bundle bundle = intent.getExtras();
//        //    if(bundle!=null)
//         //       ApksListToLimit = (ArrayList<limitApp>) bundle.getSerializable("data");
//        }
        // Bundle bundle = intent.getExtras();
        // ApksListToLimit = (ArrayList<limitApp>) intent.getSerializableExtra("LIST");
        // System.out.println(ApksListToLimit.get(0));
//        for(int i=0; i<ApksListToLimit.size();i++) {
//            System.out.println(ApksListToLimit.get(i).ApkName + ApksListToLimit.get(i).PackageName + ApksListToLimit.get(i).limit);
//           System.out.println("limit time :"+ ApksListToLimit.get(i).limit + ApksListToLimit.get(i).ApkName);
//           System.out.println( "usage time"+ob.GetAppTime(ApksListToLimit.get(i).PackageName));
//        }
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
    }



}