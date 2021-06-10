package com.example.project.TimerFunc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApkInfoExtractor {

    Context context1;
    private UsageStatsManager mUsageStatsManager;
    public ApkInfoExtractor(Context context2){

        context1 = context2;
    }

    public List<String> GetAllInstalledApkInfo(){

        List<String> ApkPackageName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN,null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );

        List<ResolveInfo> resolveInfoList = context1.getPackageManager().queryIntentActivities(intent,0);

        for(ResolveInfo resolveInfo : resolveInfoList){

            ActivityInfo activityInfo = resolveInfo.activityInfo;

            if(!isSystemPackage(resolveInfo)){

                ApkPackageName.add(activityInfo.applicationInfo.packageName);
            }
        }
//System.out.println("list "+ApkPackageName);
        return ApkPackageName;

    }

    public boolean isSystemPackage(ResolveInfo resolveInfo){

        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public Drawable getAppIconByPackageName(String ApkTempPackageName){

        Drawable drawable;

        try{
            drawable = context1.getPackageManager().getApplicationIcon(ApkTempPackageName);

        }
        catch (PackageManager.NameNotFoundException e){

            e.printStackTrace();

            drawable = ContextCompat.getDrawable(context1, R.mipmap.ic_launcher);
        }
        return drawable;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public String GetAppTime(String ApkPackageName){
        StringBuilder t = new StringBuilder("");
        //DateUtils t=new DateUtils();
        @SuppressLint("WrongConstant") UsageStatsManager mUsageStatsManager = (UsageStatsManager) context1.getSystemService("usagestats");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long Time = System.currentTimeMillis();
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context1.getPackageManager();
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                cal.getTimeInMillis(), System.currentTimeMillis());
        boolean isEmpty = stats.isEmpty();

        if (!isAccessGranted()) {
            Intent i1 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context1.startActivity(i1);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            context1.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//            Log.e("TAG","usage permission");
//        }

//        if (isEmpty) {
//            context1.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//            Log.e("TAG","usage permission");
//        }
        try {
            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);
            if(applicationInfo!=null){
                if(stats != null)
                {
                    final int statCount = stats.size();
                    for (int i = 0; i < statCount; i++) {

                        if (stats.get(i).getPackageName().equals(ApkPackageName)) {
                            long TimeInforground = 500;

                            int minutes = 500, seconds = 500, hours = 500;

                            TimeInforground = stats.get(i).getTotalTimeInForeground();

                            minutes = (int) ((TimeInforground / (1000 * 60)) % 60);

                            seconds = (int) (TimeInforground / 1000) % 60;

                            hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);

                            Log.i("BAC", "PackageName is" + stats.get(i).getPackageName() + "Time is: " + hours + "h" + ":" + minutes + "m" + seconds + "s");
                            t.append(hours);
                            t.append(":");
                            t.append(minutes);
                            t.append(":");
                            t.append(seconds);
                            System.out.println(t);
                            break;
                        }

                    }

                }}

        }catch (PackageManager.NameNotFoundException e1) {
            // Log.i("alerr","i m here 5");
            e1.printStackTrace();
        }
        return String.valueOf(t);
    }

    public String GetAppName(String ApkPackageName){

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context1.getPackageManager();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if(applicationInfo!=null){

                Name = (String)packageManager.getApplicationLabel(applicationInfo);

                //Toast.makeText(context1,"i used "+ us.getTotalTimeInForeground(),Toast.LENGTH_SHORT).show();

            }

        }catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = context1.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context1.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context1.getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}