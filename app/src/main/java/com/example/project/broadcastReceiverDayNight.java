package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.widget.Toast;

public class broadcastReceiverDayNight extends BroadcastReceiver {
    private AudioManager myAudioManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        myAudioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
        //you can check the log that it is fired
        //Here we are actually not doing anything
        //but you can do any task here that you want to be done at a specific time everyday
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        Toast.makeText(context, "alarm for silent", Toast.LENGTH_SHORT).show();
        System.out.println("alarm for silent");
    }
}
