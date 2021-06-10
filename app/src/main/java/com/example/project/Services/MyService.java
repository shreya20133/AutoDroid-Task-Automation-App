package com.example.project.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.project.R;

public class MyService extends Service {
    private String App_name;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int  onStartCommand(Intent intent, int flags, int startId){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel("Channelid2","Action on Jack", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
//        Intent inten=new Intent(this, JackActivity.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,inten,0);
//        Notification notification=new NotificationCompat.Builder(this,"Channelid2")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentIntent(pendingIntent)
//                .build();
//        startForeground(2,notification);
//        String presence = intent.getStringExtra("presence");
//        if(presence.equals("True")){
        App_name = intent.getStringExtra("App");
        System.out.println("APPPPPPPPPPPPPPPPPPPP"+App_name);
//        }
        System.out.println("APPPPPPPPPPPPPPPPPPPP"+App_name);
        registerReceiver(br, filter);
        return START_NOT_STICKY;

    }
    boolean Microphone_Plugged_in = false;
    IntentFilter filter = new IntentFilter("android.intent.action.HEADSET_PLUG");
    BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context,"Headphone connect",Toast.LENGTH_LONG).show();
            final String action = intent.getAction();
            int iii=2;
            if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                iii=intent.getIntExtra("state", -1);
                if(Integer.valueOf(iii)==0){
                    Microphone_Plugged_in = false;
                    Toast.makeText(context,"microphone not plugged in..",Toast.LENGTH_LONG).show();
                }if(Integer.valueOf(iii)==1){
                    Microphone_Plugged_in = true;
                    Toast.makeText(context,"microphone plugged in..",Toast.LENGTH_LONG).show();
                    Intent i1 = context.getPackageManager().getLaunchIntentForPackage(App_name);
                    context.startActivity(i1);
                    if(i1!= null){
//                        context.startActivity(i1);
                    }
                    else {
                        System.out.println("In elseeeeeeeeeeeeeeeeeeeee");
                        Toast.makeText(context,App_name + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
                    }
//                    intent.putExtra("App",App_name);
//                    String mimeType = "audio";
//                    ShareCompat.IntentBuilder
//                            .from(context.this)
//                            .setType(mimeType)
//                            .setChooserTitle("Open with: ")
//                            .startChooser();
                }
            }
        }
    };
}

