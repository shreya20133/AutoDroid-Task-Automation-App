package com.example.project.greetingswisher;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.Gesture.pService;
import com.example.project.R;
import com.example.project.broadcastreceivers.AlarmReceiver;
import com.example.project.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SmsWisher extends AppCompatActivity {

    FirebaseUser user;
    HashMap<String,SMSDB> smsdbHashMap;
    private DatabaseReference mDatabase;
    SwitchCompat switchCompatSMS;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100 ;
//
//    ArrayList<String> dateList=new ArrayList<>();
//    ArrayList<String> messageList=new ArrayList<>();
//    ArrayList<String> contactList=new ArrayList<>();


    private EditText TextEditor_mobileNo, TextEditor_date, TextEditor_message;
    private String mobileNo ,occasionType,msg;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar = Calendar.getInstance();
    int PERMISSION_REQUEST_SEND_SMS = 1;

    public static final String[] monthlist = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String[] occasionList = {"Birthday", "Anniversary", "Other"};
    public static final String[] wishList = {"Happy birthday!! I hope your day is filled with lots of love and laughter! May all of your birthday wishes come true.", "Congratulations on another year of falling deeper in love with each other. Happy anniversary!!"};
    static final int REQUEST_SELECT_CONTACT = 200;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sms_wisher);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        switchCompatSMS=findViewById(R.id.SMSWisher_switch);
        switchCompatSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (user != null) {
                        // User is signed in
                        System.out.println("CURRENTTTTT" + user.getEmail());
                        System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                        mDatabase.child("User_Macro").child(user.getUid()).child("SMSWisher").setValue("1");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }
                } else {
                    if (user != null) {
                        // User is signed in
                        mDatabase.child("User_Macro").child(user.getUid()).child("SMSWisher").setValue("0");
                        mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                    } else {
                        System.out.println("INVALID USER....");
                    }

                }
            }
        });

        smsdbHashMap = new HashMap<>();
//        dateList=new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ImageView helpImageView=findViewById(R.id.helpSMS);
        MaterialCardView featureInfoDriveCardView=findViewById(R.id.featureinfo_SMS);
        helpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                featureInfoDriveCardView.setVisibility(View.VISIBLE);
            }
        });
        ImageButton b =  findViewById(R.id.open_phoneBook);
        TextEditor_mobileNo = findViewById(R.id.editText_mobileNo);
        TextEditor_date = findViewById(R.id.editText_date);
        TextEditor_date.setOnClickListener(v -> {
            showCalendar(calendar, v);
            dateSetListener = (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
                TextEditor_date.setText(dayOfMonth + " " + monthlist[month] );
            };
        });
        b.setOnClickListener(this::openPhoneBook);
        TextEditor_mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mobileNo = TextEditor_mobileNo.getText().toString().trim();
                if(validateMobileNo())
                    setMobileNo();
                else
                    TextEditor_mobileNo.setError("Please enter  valid 10 digit mobile number");

            }


        });

        TextEditor_message = (EditText) findViewById(R.id.editText_message);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_occasion);

        List<String> occasions = new ArrayList<String>();
        occasions.add(0,"Select Occasion");
        for (int i = 0;i< occasionList.length; ++i)
            occasions.add(occasionList[i]);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, occasions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select Occasion");
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occasionType = (String) parent.getItemAtPosition(position);
                if(occasionType.equals("Select Occasion")){
                    TextEditor_message.setVisibility(View.INVISIBLE);
                    // TextEditor_message.setError("Please Select an occasion");
                }else{
                    TextEditor_message.setVisibility(View.VISIBLE);
                    switch (occasionType) {
                        case "Birthday":
                            TextEditor_message.setText(wishList[0]);
                            break;
                        case "Anniversary":
                            TextEditor_message.setText(wishList[1]);
                            break;
                        case "Other":
                            TextEditor_message.setText("Your message here..");
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextEditor_message.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                msg = TextEditor_message.getText().toString();
                if(msg.length()== 0)
                    TextEditor_message.setError("Please enter a message");
            }
        });
        Button saveButton = findViewById(R.id.smsWisher_SaveButton);
        Button showSavedButton =findViewById(R.id.smsWisher_ShowSavedButton);

        if(user!=null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("SMSWisher").child(user.getUid()).child("mobile");
            db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                        smsdbHashMap = (HashMap<String, SMSDB>) task.getResult().getValue();
                }
            });
        }
        if( ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SmsWisher.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        saveButton.setOnClickListener(v -> {
            if(TextEditor_date.getText().toString().isEmpty() || TextEditor_mobileNo.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please fill required data",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                //int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Log.e("TAG", "inside if db");
                    smsdbHashMap.put(TextEditor_mobileNo.getText().toString() + TextEditor_date.getText().toString(), new SMSDB(TextEditor_mobileNo.getText().toString(), TextEditor_message.getText().toString(), TextEditor_date.getText().toString()));
                    mDatabase.child("SMSWisher").child(user.getUid()).child("mobile").setValue(smsdbHashMap);
                    mDatabase.child("SMSWisher").child(user.getUid()).child("email").setValue(user.getEmail());
                } else {
                    System.out.println("INVALID USER....");
                }
                sendSMS();
//                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        sendSMS();
//                    }
//                } else {
//                    Log.e("TAG", "inside else db");
//                    ActivityCompat.requestPermissions(SmsWisher.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
//                }
            }
        });
        ArrayList<String> dateList=new ArrayList<>();
        ArrayList<String> messageList=new ArrayList<>();
        ArrayList<String> contactList=new ArrayList<>();
        if(user!=null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("SMSWisher").child(user.getUid()).child("mobile");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int counter=0;
                    dateList.clear();
                    contactList.clear();
                    messageList.clear();
                    for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()){
//                        Log.e("TAG",Objects.requireNonNull(uniqueKeySnapshot.getKey()));
                        for(DataSnapshot dataSnapshot:uniqueKeySnapshot.getChildren())
                        {
                            Log.e("TAG",Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                            if(counter%3==0) {
                                dateList.add(Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                            }
                            else if(counter%3==1)
                            {
                                messageList.add(Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                            }
                            else
                            {
                                contactList.add(Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                            }
                            counter++;
                            // Log.e("date in for loop",dataSnapshot.child("date").getValue().toString());
                            //dateList.add(dataSnapshot.child("date").getValue().toString());
                        }
//                                dateList.add(Objects.requireNonNull(uniqueKeySnapshot.child("date").getValue()).toString());
//                        messageList.add(Objects.requireNonNull(uniqueKeySnapshot.child("message").getValue()).toString());
//                        contactList.add(Objects.requireNonNull(uniqueKeySnapshot.child("mobileNo").getValue()).toString());
//                                Log.e("TAG",Objects.requireNonNull(uniqueKeySnapshot.child("date").getValue()).toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        showSavedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG","user is "+user);
                Log.e("size",dateList.size()+"");
                Intent intent=new Intent(getApplicationContext(),ShowSMSWisherRecords.class);
                intent.putStringArrayListExtra("datelist",dateList);
                intent.putStringArrayListExtra("contactlist",contactList);
                intent.putStringArrayListExtra("messagelist",messageList);
                startActivity(intent);
            }
        });
        for (int i = 0; i < HomeFragment.featureName.size(); i++) {
            if (HomeFragment.featureName.get(i).equals("SMSWisher")) {
                if (HomeFragment.featureValue.get(i).equals("1"))
                    switchCompatSMS.setChecked(true);
                else
                    switchCompatSMS.setChecked(false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_SEND_SMS){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sendSMS();
            }
        }
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(getApplicationContext(), AlarmReceiver.class);
        String msg = TextEditor_message.getText().toString();
        String no = TextEditor_mobileNo.getText().toString();
        no = no.replaceAll("\\+91", "");
        intent.putExtra("sms", msg );
        intent.putExtra("receiver_number", no);
        Log.d("autodroid", no);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),12345,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendSMS(){
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,2);
        calendar.set(Calendar.SECOND,0);
        startAlarm(calendar);
    }

    //validate mobileNo
    public boolean validateMobileNo(){
        String number = TextEditor_mobileNo.getText().toString();
        if(number.isEmpty() || number.length()<14){
            return false;
        }
        return true;
    }

    // display popup calendar
    public void showCalendar(Calendar calendar, View view){
        DatePickerDialog datePickerDialog= new DatePickerDialog(SmsWisher.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);
        datePickerDialog.show();
    }

    // creating implicit intent to open contact list.
    public void openPhoneBook(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //getting data from intent.
                Uri uri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    mobileNo = cursor.getString(numberIndex);
                    mobileNo = PhoneNumberUtils.formatNumber(mobileNo);
                    mobileNo = mobileNo.replaceAll("\\s", "");
                    setMobileNo();
                }
            }

        }
    }
    //setting mobile no in edittext
    public void setMobileNo(){
        if(mobileNo.startsWith("+91") && mobileNo.length() == 13) {
            mobileNo = mobileNo.replaceAll("\\+91", "");
            TextEditor_mobileNo.setText(mobileNo);
            Selection.setSelection(TextEditor_mobileNo.getText(), TextEditor_mobileNo.getText().length());
        }else if(mobileNo.startsWith("0") && mobileNo.length() == 11) {
            mobileNo = mobileNo.replaceAll("0", "");
            TextEditor_mobileNo.setText(mobileNo);
            Selection.setSelection(TextEditor_mobileNo.getText(), TextEditor_mobileNo.getText().length());
        }else if(!mobileNo.startsWith("+91") && mobileNo.length() == 10){
            TextEditor_mobileNo.setText(mobileNo);
            Selection.setSelection(TextEditor_mobileNo.getText(), TextEditor_mobileNo.getText().length());
        }
    }
}
