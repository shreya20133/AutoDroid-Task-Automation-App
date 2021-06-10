package com.example.project.TimerFunc;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.LoginActivity;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{

    Context context1;
    List<String> stringList;
    private DatabaseReference mDatabase ;
    public HashMap<String,limitApp> dapps = new HashMap<>();
    public FirebaseUser user;
//    List<limitApp> allset;


    public AppsAdapter(Context context, List<String> list){

        context1 = context;
//        dapps = new HashMap<>();
//        allset = new ArrayList<>();
        stringList = list;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(LoginActivity.mAuth!=null)
            user = LoginActivity.mAuth.getCurrentUser();
        if(user!=null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("TimerApp").child(user.getUid()).child("app");
            db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    //System.out.println("FIREBASEEEE ..  " + task.getResult().getValue());
                    if(task.isSuccessful())
                        dapps = (HashMap<String, limitApp>) task.getResult().getValue();//
                }
//            }
                                           });
    }
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public ImageView imageView;
        public TextView textView_App_Name;
        public TextView textView_App_Package_Name;
        public TextView textView_App_Time;
        public Button limitButton;

        public ViewHolder (View view){

            super(view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            textView_App_Name = (TextView) view.findViewById(R.id.Apk_Name);
            textView_App_Time =(TextView) view.findViewById(R.id.Apk_Time);
//            textView_App_Package_Name = (TextView) view.findViewById(R.id.Apk_Package_Name);
            limitButton = (Button) view.findViewById(R.id.limit);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view2 = LayoutInflater.from(context1).inflate(R.layout.cardview_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(view2);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){

        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(context1);

        final String ApplicationPackageName = (String) stringList.get(position);
        String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);
        // System.out.println(ApplicationLabelName);
        String ti=apkInfoExtractor.GetAppTime(ApplicationPackageName);
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);

        viewHolder.textView_App_Name.setText(ApplicationLabelName);

//        viewHolder.textView_App_Package_Name.setText(ApplicationPackageName);

        viewHolder.imageView.setImageDrawable(drawable);
        viewHolder.textView_App_Time.setText(ti);
        viewHolder.limitButton.setOnClickListener(new View.OnClickListener() {
            int t1hour,t1min;
            long timili;
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(context1, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        t1hour = hourOfDay;
                        t1min = minute;
//                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        Intent intent = new Intent("apps details");
                        intent.putExtra("appname",viewHolder.textView_App_Name.getText().toString());
                        intent.putExtra("packagename",ApplicationPackageName);
                        timili = t1hour*60*60*1000+t1min*60*1000;
                        System.out.println("APPADAPTERRRRR:  "+timili);
                        intent.putExtra("limit",String.valueOf(timili));
                        LocalBroadcastManager.getInstance(context1).sendBroadcast(intent);
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            System.out.println("CURRENTTTTT"+user.getEmail() + ApplicationPackageName);
//                            mDatabase.child("TimerApp").child(user.getUid());
                            dapps.put(viewHolder.textView_App_Name.getText().toString(),new limitApp(viewHolder.textView_App_Name.getText().toString(),ApplicationPackageName,timili+""));
                            //Check kro already hai ya nhi (query)
//                            allset.add(new limitApp(viewHolder.textView_App_Name.getText().toString(),ApplicationPackageName,timili+""));
                            System.out.println(mDatabase.child("TimerApp").child(user.getUid()).get());
                            mDatabase.child("TimerApp").child(user.getUid()).child("app").setValue(dapps);
                            mDatabase.child("TimerApp").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            // No user is signed in
                            System.out.println("INVALID USER....");
                        }
                    }
                },24,0,true);
                tpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tpd.updateTime(t1hour,t1min);
                tpd.show();
                //ApksListToLimit.add(new limitApp(viewHolder.textView_App_Name.getText().toString(),viewHolder.textView_App_Package_Name.getText().toString(),"5"));
                //System.out.println(ApksListToLimit.get(0).ApkName +ApksListToLimit.get(0).PackageName +ApksListToLimit.get(0).limit );
            }
        });

        //Adding click listener on CardView to open clicked application directly from here .
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = context1.getPackageManager().getLaunchIntentForPackage(ApplicationPackageName);
//                if(intent != null){
//
//                    context1.startActivity(intent);
//
//                }
//                else {
//
//                    Toast.makeText(context1,ApplicationPackageName + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

    @Override
    public int getItemCount(){

        return stringList.size();
    }
}
