package com.example.project.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("autodroid","broadcast receiver");
        //if("com.example.loginpage.START_ALARM".equals(intent.getAction())){
            try {
                Log.d("autodroid","broadcast receiver class entered");
                Bundle bundle = intent.getExtras();
                SmsManager smsManager = SmsManager.getDefault();
                String msg = bundle.getString("sms");
                String mobileNo = bundle.getString("receiver_number");
                Log.d("autodroid","sending sms");
                Log.d("autodroid",msg);
                Log.d("autodroid",mobileNo);
                smsManager.sendTextMessage(mobileNo, null, msg, null, null);
                Toast.makeText(context, "message sent successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Log.d("autodroid",e.getMessage());
            }
        //}

    }
}
