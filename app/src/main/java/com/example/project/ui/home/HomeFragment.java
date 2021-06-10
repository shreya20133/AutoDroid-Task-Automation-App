package com.example.project.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.AqiMonitor;
import com.example.project.Gesture.Torch;
import com.example.project.Gesture.cam;
import com.example.project.Gesture.doubletap;
import com.example.project.Gesture.playpause;
import com.example.project.Gesture.silent;
import com.example.project.MotivationFeature.MotivationActivity;
import com.example.project.R;
import com.example.project.ScheduledReminder.ReminderActivity;
import com.example.project.StepCounterFit.MainPagefit;
import com.example.project.TimerActivity;
import com.example.project.daynightfeature;
import com.example.project.driveUpload.photoUploader;
import com.example.project.greetingswisher.SmsWisher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private RecyclerView rvMyMacros;
    int url1=R.drawable.poster1;
    int url2=R.drawable.p3;
    int url3=R.drawable.p4;
    int url4=R.drawable.p5;
    int url5=R.drawable.p6;
    HashMap<String,String> ares;
    public static List<String> featureName ;
    public static List<String> featureValue;
    MyMacrosAdapter a;
    FirebaseUser user;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.primarydark));
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rvMyMacros=root.findViewById(R.id.rvMyMacros);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        rvMyMacros.setLayoutManager(linearLayoutManager);
        user = FirebaseAuth.getInstance().getCurrentUser();

        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        SliderView sliderView = root.findViewById(R.id.slider);
        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        sliderDataArrayList.add(new SliderData(url4));
        sliderDataArrayList.add(new SliderData(url5));
        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(getActivity(), sliderDataArrayList);
        //below method is used to set auto cycle direction in left to right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        //below method is used to setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);
        //below method is use to set scroll time in seconds.
        sliderView.setScrollTimeInSec(3);
        //to set it scrollable automatically we use below method.
        sliderView.setAutoCycle(true);
        //to start autocycle below method is used.
        sliderView.startAutoCycle();
        //On click listener for Greeting wisher functionality
        CardView btnGreetingWisher = root.findViewById(R.id.Feature_smswisher);
        btnGreetingWisher.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),SmsWisher.class);
            startActivity(intent);
        });
        CardView btnMotivation = root.findViewById(R.id.Feature_motivation);
        btnMotivation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MotivationActivity.class);
            startActivity(intent);
        });
        CardView btnDayNight = root.findViewById(R.id.Feature_daynight);
        btnDayNight.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), daynightfeature.class);
            startActivity(intent);
        });
        CardView btnAqiMonitor = root.findViewById(R.id.Feature_aqi);
        btnAqiMonitor.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AqiMonitor.class);
            startActivity(i);
        });


        CardView btnquicklock = root.findViewById(R.id.Feature_lockscreen);
        btnquicklock.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), doubletap.class);
            startActivity(i);
        });


        CardView btnvideopause = root.findViewById(R.id.Feature_video_gesture);
        btnvideopause.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), playpause.class);
            startActivity(i);
        });


        CardView btnsilent = root.findViewById(R.id.Feature_silent_ring);
        btnsilent.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), silent.class);
            startActivity(i);
        });

        CardView btnbluetoothon = root.findViewById(R.id.Feature_bluetoothon);
        btnbluetoothon.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), cam.class);
            startActivity(i);
        });



        //On click listener for photo upload to GDrive functionality
        CardView btnphotoUploader = root.findViewById(R.id.Feature_uploadondrive);
        btnphotoUploader.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), photoUploader.class);
            startActivity(i);
        });

        CardView btnTimer = root.findViewById(R.id.Feature_screentime);
        btnTimer.setOnClickListener(v -> {
            Intent i1 = new Intent(getActivity(), TimerActivity.class);
            startActivity(i1);
        });

        CardView btnstepcnt = root.findViewById(R.id.Feature_stepcounter);
        btnstepcnt.setOnClickListener(v -> {
            Intent i1 = new Intent(getActivity(), MainPagefit.class);
            startActivity(i1);
        });

        CardView btnges = root.findViewById(R.id.Feature_torchgesture);
        btnges.setOnClickListener(v -> {
            Intent i1 = new Intent(getActivity(), Torch.class);
            startActivity(i1);
        });

        CardView btnjck = root.findViewById(R.id.Feature_playontrigger);
        btnjck.setOnClickListener(v -> {
            Intent i1 = new Intent(getActivity(), ReminderActivity.class);
            startActivity(i1);
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {
//                textView.setText(s);
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("inside resume........................................................................");
        ares = new HashMap<>();
        featureName = new ArrayList<>() ;
        featureValue = new ArrayList<>() ;
        if(user!=null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("User_Macro").child(user.getUid());
            db.get().addOnCompleteListener(task -> {
                //  System.out.println("FIREBASEEEE ..  "+ Objects.requireNonNull(task.getResult()).getValue());
                if(task.isSuccessful())
                {
                    ares= (HashMap<String, String>) Objects.requireNonNull(task.getResult()).getValue();
                }
                if(ares != null){
                    for (Map.Entry<String,String> entry : ares.entrySet()) {
                        if(!(entry.getKey().equals("email"))){
                            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                            System.out.println(entry.getKey());
                            featureName.add(entry.getKey());
                            featureValue.add(entry.getValue());
                        }
                    }}
                a = new MyMacrosAdapter(getActivity(),featureValue,featureName);
                rvMyMacros.setAdapter(a);

            });
        }
    }

}