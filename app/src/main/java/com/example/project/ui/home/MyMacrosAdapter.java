package com.example.project.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Gesture.*;
import com.example.project.R;
import com.example.project.driveUpload.photoUploader;
import com.example.project.greetingswisher.SmsWisher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
public class MyMacrosAdapter extends RecyclerView.Adapter<MyMacrosAdapter.ViewHolder> {

    public static List<String> fName;
    public static List<String> fValue;
    Context context;

    public  MyMacrosAdapter(Context context, List<String> fValue, List<String> fName) {
        this.context = context;
        this.fValue = fValue;
        this.fName = fName;
    }
    @NonNull
    @Override

    public MyMacrosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewmymacros,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        Button bStart = (Button) findViewById(R.id.torchg);
//        Button bStop = (Button) findViewById(R.id.torchgs);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.tv_Mymacro.setText(fName.get(position));
        if(fValue.get(position).equals("1")){
            holder.switch_mymacro.setChecked(true);
        }else {
            holder.switch_mymacro.setChecked(false);
        }
        if(fName.get(position).equals("Torch")){
            holder.tv_Mymacro_img.setImageResource(R.drawable.torchicn);
            holder.tv_Mymacro_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), Torch.class);
                    v.getContext().startActivity(i);
                }
            });
            holder.switch_mymacro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (firebaseUser != null) {
                            // User is signed in
                            System.out.println("CURRENTTTTT" + user.getEmail());
                            System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                            mDatabase.child("User_Macro").child(user.getUid()).child("Torch").setValue("1");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                        Intent intent1 = new Intent(context, shakedetection.class);
                        context.startService(intent1);
                    } else {
                        if (firebaseUser != null) {
                            // User is signed in
                            mDatabase.child("User_Macro").child(user.getUid()).child("Torch").setValue("0");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                        Intent intent1 = new Intent(context, shakedetection.class);
                        context.stopService(intent1);
                    }
                }
            });
        }
        else if(fName.get(position).equals("Bluetooth")){
            holder.tv_Mymacro_img.setImageResource(R.drawable.bluetoothicn);
            holder.tv_Mymacro_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), cam.class);
                    v.getContext().startActivity(i);
                }
            });
            holder.switch_mymacro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (firebaseUser != null) {
                            // User is signed in
                            System.out.println("CURRENTTTTT" + user.getEmail());
                            System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                            mDatabase.child("User_Macro").child(user.getUid()).child("Bluetooth").setValue("1");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                        Intent intent1 = new Intent(context, camService.class);
                        context.startService(intent1);
                    } else {
                        if (firebaseUser != null) {
                            // User is signed in
                            mDatabase.child("User_Macro").child(user.getUid()).child("Bluetooth").setValue("0");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                        Intent intent1 = new Intent(context, camService.class);
                        context.stopService(intent1);
                    }
                }
            });
        }
        else if(fName.get(position).equals("Lock")){
            holder.tv_Mymacro_img.setImageResource(R.drawable.lockicn);
            holder.tv_Mymacro_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), doubletap.class);
                    v.getContext().startActivity(i);
                }
            });
            holder.switch_mymacro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (firebaseUser != null) {
                            // User is signed in
                            System.out.println("CURRENTTTTT" + user.getEmail());
                            System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                            mDatabase.child("User_Macro").child(user.getUid()).child("Lock").setValue("1");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                    } else {
                        if (firebaseUser != null) {
                            // User is signed in
                            mDatabase.child("User_Macro").child(user.getUid()).child("Lock").setValue("0");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                    }
                }
            });
        }
        else if(fName.get(position).equals("Playpause")){
            holder.tv_Mymacro_img.setImageResource(R.drawable.pauseicn);
            holder.tv_Mymacro_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), playpause.class);
                    v.getContext().startActivity(i);
                }
            });
            holder.switch_mymacro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (firebaseUser != null) {
                            // User is signed in
                            System.out.println("CURRENTTTTT" + user.getEmail());
                            System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                            mDatabase.child("User_Macro").child(user.getUid()).child("Playpause").setValue("1");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                        Intent intent1 = new Intent(context, pService.class);
                        context.startService(intent1);
                    } else {
                        if (firebaseUser != null) {
                            // User is signed in
                            mDatabase.child("User_Macro").child(user.getUid()).child("Playpause").setValue("0");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                        Intent intent1 = new Intent(context, pService.class);
                        context.stopService(intent1);
                    }
                }
            });
        }
        else if(fName.get(position).equals("Silent")){
            holder.tv_Mymacro_img.setImageResource(R.drawable.silenceicn);
            holder.tv_Mymacro_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), silent.class);
                   v.getContext().startActivity(i);
                }
            });
            holder.switch_mymacro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (firebaseUser != null) {
                            // User is signed in
                            System.out.println("CURRENTTTTT" + user.getEmail());
                            System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                            mDatabase.child("User_Macro").child(user.getUid()).child("Silent").setValue("1");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                        Intent intent1 = new Intent(context, sService.class);
                        context.startService(intent1);
                    } else {
                        if (firebaseUser != null) {
                            // User is signed in
                            mDatabase.child("User_Macro").child(user.getUid()).child("Silent").setValue("0");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                        Intent intent1 = new Intent(context, sService.class);
                        context.stopService(intent1);
                    }
                }
            });
        }
        else if(fName.get(position).equals("SMSWisher")){
            holder.tv_Mymacro_img.setImageResource(R.drawable.sms_icn);
            holder.tv_Mymacro_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, SmsWisher.class);
                    context.startActivity(i);
                }
            });
            holder.switch_mymacro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (firebaseUser != null) {
                            // User is signed in
                            System.out.println("CURRENTTTTT" + user.getEmail());
                            System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                            mDatabase.child("User_Macro").child(user.getUid()).child("SMSWisher").setValue("1");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                    } else {
                        if (firebaseUser != null) {
                            // User is signed in
                            mDatabase.child("User_Macro").child(user.getUid()).child("SMSWisher").setValue("0");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                    }
                }
            });
        }
        else if(fName.get(position).equals("Drive")){
            holder.tv_Mymacro_img.setImageResource(R.drawable.drive_icn);
            holder.tv_Mymacro_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, photoUploader.class);
                    context.startActivity(i);
                }
            });
            holder.switch_mymacro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (firebaseUser != null) {
                            // User is signed in
                            System.out.println("CURRENTTTTT" + user.getEmail());
                            System.out.println(mDatabase.child("User_Macro").child(user.getUid()).get());
                            mDatabase.child("User_Macro").child(user.getUid()).child("Drive").setValue("1");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                    } else {
                        if (firebaseUser != null) {
                            // User is signed in
                            mDatabase.child("User_Macro").child(user.getUid()).child("Drive").setValue("0");
                            mDatabase.child("User_Macro").child(user.getUid()).child("email").setValue(user.getEmail());
                        } else {
                            System.out.println("INVALID USER....");
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return fName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View cardview;
        TextView tv_Mymacro;
        SwitchCompat switch_mymacro;
        ImageButton tv_Mymacro_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview=itemView;
            tv_Mymacro = itemView.findViewById(R.id.tv_CardMyMacros);
            switch_mymacro = itemView.findViewById(R.id.switch_card_MyMacros);
            tv_Mymacro_img = itemView.findViewById(R.id.tv_CardMyMacros_img);

        }
    }
}
