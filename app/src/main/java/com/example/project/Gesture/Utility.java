package com.example.project.Gesture;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

public class Utility {
    Context context;
    public Utility(Context context){
    this.context=context;
    }
    public  boolean isON=false;
    public boolean togg(String ty) throws CameraAccessException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            CameraManager cameraManager=(CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
            String cid=null;
            if(cameraManager != null){
                cid=cameraManager.getCameraIdList()[0];
            }
            if (cameraManager != null){
                if(ty.equals("On")){
                    cameraManager.setTorchMode(cid,true);
                    isON=true;
                }
                else{
                    cameraManager.setTorchMode(cid,false);
                    isON=false;
                }

            }
        }
        return isON;
    }
}
