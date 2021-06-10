package com.example.project.greetingswisher;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;

public class ShowSMSWisherRecords extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100 ;
    RecyclerView recyclerViewSMSWisherRecords;
    SavedRecordsAdapter savedRecordsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smswisherrecords);

        Intent intent=getIntent();
        ArrayList<String> datelist=intent.getStringArrayListExtra("datelist");
        ArrayList<String> contactList=intent.getStringArrayListExtra("contactlist");
        ArrayList<String> messageList=intent.getStringArrayListExtra("messagelist");
        ArrayList<String> nameContactList=new ArrayList<>();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//        }
        for(String contact: contactList)
        {
            nameContactList.add(getContactName(contact,getApplicationContext()));
        }
        recyclerViewSMSWisherRecords=findViewById(R.id.SavedRecordsListRV);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewSMSWisherRecords.setLayoutManager(linearLayoutManager);
        savedRecordsAdapter= new SavedRecordsAdapter(getApplicationContext(),datelist,nameContactList,contactList,messageList);
        recyclerViewSMSWisherRecords.setAdapter(savedRecordsAdapter);
        savedRecordsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewSMSWisherRecords.setAdapter(savedRecordsAdapter);
        savedRecordsAdapter.notifyDataSetChanged();
    }
    public String getContactName(final String phoneNumber, Context context)
    {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
            String contactName = "";
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(0);
                }
                cursor.close();
            }
            return contactName;
    }
}
