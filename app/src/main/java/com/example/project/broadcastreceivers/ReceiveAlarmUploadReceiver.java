package com.example.project.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project.driveUpload.photoUploader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveAlarmUploadReceiver extends BroadcastReceiver {
    private String TAG = "autodroid";
    //private Drive drive;
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("autodroid","broadcast receiver");

        try {
            Log.d("autodroid","broadcast receiver class entered");
            Bundle bundle = intent.getExtras();
           // drive = (Drive) bundle.getSerializable("Drive");
            ArrayList<String> imagePathList = getImageList(context);

            if(imagePathList.size()>0) {
                Toast.makeText(context, "Started Uploading in drive", Toast.LENGTH_SHORT).show();
                uploadFile(imagePathList, context);
            }
            else{
                Toast.makeText(context, "No photos for uploading from previous day", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "ERROR while uploading", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.d("autodroid",e.getMessage());
        }


    }

    public ArrayList<String> getImageList(Context context) throws IOException {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] col = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        Cursor cursor = context.getContentResolver().query(uri, col, null,
                null, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        String imagePath = null;
        ArrayList<String> imagePathList = new ArrayList<String>();
        int index= cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String dateFormatted = simpleDateFormat.format(date);
        Pattern pattern = Pattern.compile(dateFormatted);
        boolean matchValue;
        while (cursor.moveToNext()) {
            imagePath = cursor.getString(index);
            Matcher matcher = pattern.matcher(imagePath);
            matchValue = matcher.find();
            if(matchValue) {
                Log.d(TAG, String.valueOf(imagePath));
                Log.d(TAG, imagePath);
                imagePathList.add(imagePath);
            }
        }
        return imagePathList;
    }



    public void createFile(String path, String folderId)throws Exception{
        String imageName = path.substring(path.lastIndexOf('/') + 1);
        File driveMetaFile = new File();
        driveMetaFile.setName(imageName);
        driveMetaFile.setParents(Collections.singletonList(folderId));
        //driveMetaFile.setMimeType("application/vnd.google-apps.file");
        driveMetaFile.setMimeType("image/jpg");
        Log.d("autodroid", String.valueOf(driveMetaFile));

        java.io.File filePath = new java.io.File(path);
        FileContent mediaContent = new FileContent("image/jpg", filePath);
        Log.d("autodroid", String.valueOf(mediaContent));
        File driveFile = null;
        try {
            driveFile = photoUploader.drive.files().create(driveMetaFile, mediaContent).setFields("id , parents").execute();

        }catch(Exception e){
            e.printStackTrace();
            Log.d("autodroid", "file error" + e.getMessage() );
        }
        if (driveFile == null) {
            throw new IOException("file is null!!");

        }

    }

    public Task<String> createAndUploadFiles(ArrayList<String> pathList) throws IOException {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
        Log.d("autodroid "," entered the  createfile ");
        Tasks.call(threadPoolExecutor, ()-> {
            Log.d("autodroid "," entered the  call function ");
            File  fileMetadata = new File();
            fileMetadata.setName("My_Photos_Folder");
            fileMetadata.setMimeType("application/vnd.google-apps.folder");

            Drive.Files.List request = photoUploader.drive.files().list().setQ(
                    "mimeType='application/vnd.google-apps.folder' and trashed=false");
            FileList f = request.execute();
            int flag = 0;
            String folderId  = "";
            for (File f1 : f.getFiles()) {
                if(f1.getName().equals("My_Photos_Folder")){
                    flag = 1;
                    folderId = f1.getId();
                    break;
                }
            }
            File file = null;
            if(flag == 0){
                try {
                    file = photoUploader.drive.files().create(fileMetadata).execute();
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.d("autodroid", e.getMessage());
                }
                Log.d("autodroid "," new folder created");
                Log.d("autodroid "," folder id "+ file.getId());
                folderId = file.getId();
            }else{
                Log.d("autodroid "," folder already exists");
                Log.d("autodroid "," folder id "+ folderId);

            }
            if( pathList.size() > 0){
                for (int i = 0; i<pathList.size(); ++i){
                    createFile(pathList.get(i), folderId);
                }
            }

            //Log.d("autodroid","File ID: " + driveFile.getId());
            taskCompletionSource.setResult(folderId);
            return folderId;

        });
        return taskCompletionSource.getTask();

    }

    public void uploadFile(ArrayList<String> imagePathList, Context context) throws IOException {

        Log.d("autodroid", String.valueOf(photoUploader.drive));
        createAndUploadFiles(imagePathList).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //progressDialog.dismiss();
                Log.d("autodroid", "file creation error: " + e.getMessage());
                Toast.makeText(context, "Check your google drive", Toast.LENGTH_LONG).show();
            }
        });

    }
}
