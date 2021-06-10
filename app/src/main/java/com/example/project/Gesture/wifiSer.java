package com.example.project.Gesture;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.wifi.WifiManager;
import android.os.Build;

public class wifiSer {
    Context context;
    public wifiSer(Context context){
        this.context=context;
    }
    public  boolean isON=false;
    //private WifiManager wifiManager;
    public boolean togg(String ty)  {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
           // CameraManager cameraManager=(CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
           // wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
            else{
                mBluetoothAdapter.enable();
            }
        }
        return isON;
    }

}
