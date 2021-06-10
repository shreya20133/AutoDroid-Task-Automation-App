package com.example.project.TimerFunc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DistAppAct extends AppCompatActivity {

    ArrayList<limitApp> ares;
    ArrayList<limitApp> llist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist_app);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ares = new ArrayList<limitApp>();
        llist = new ArrayList<limitApp>();
        if(user!=null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("TimerApp").child(user.getUid()).child("app");
            db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    System.out.println("FIREBASEEEE ..  "+task.getResult().getValue());
                    ares= (ArrayList<limitApp>) task.getResult().getValue();
                    System.out.println("INLOOPPPP "+ares.size());
                    System.out.println(ares);
                }
            });

        }

//        System.out.println("LISSTTTTT...."+llist.size());
//        for(int i=0;i<ares.size();i++){
//            System.out.println("SYS>>>>.."+ares.get(i));
//        }
    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        System.out.println("LISSTTTTT...."+ares.size());
//        for(int i=0;i<ares.size();i++){
//            System.out.println("SYS>>>>.."+ares.get(i));
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("LISSTTTTT...."+ares.size());
        for(int i=0;i<ares.size();i++){
            System.out.println("SYS>>>>.."+ares.get(i));
        }
    }
}