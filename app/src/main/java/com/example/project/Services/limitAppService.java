package com.example.project.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.project.TimerFunc.limitApp;

import java.util.ArrayList;
import java.util.List;

public class limitAppService extends Service {
    List<limitApp> list = new ArrayList<limitApp>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("inside service");
        list = (List<limitApp>) intent.getSerializableExtra("LIST");
        System.out.println("from service "+list.get(0).getApkName() +list.get(0).getPackageName() +list.get(0).getLimit() );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
