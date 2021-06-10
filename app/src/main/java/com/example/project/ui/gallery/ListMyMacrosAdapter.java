package com.example.project.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Gesture.Torch;
import com.example.project.Gesture.cam;
import com.example.project.Gesture.doubletap;
import com.example.project.Gesture.playpause;
import com.example.project.Gesture.silent;
import com.example.project.R;
import com.example.project.driveUpload.photoUploader;
import com.example.project.greetingswisher.SmsWisher;
import com.example.project.ui.home.MyMacrosAdapter;

import java.util.List;

public class ListMyMacrosAdapter extends RecyclerView.Adapter<com.example.project.ui.gallery.ListMyMacrosAdapter.ViewHolder> {

    List<String> fName= MyMacrosAdapter.fName;
    List<String> fValue= MyMacrosAdapter.fValue;

    Context context;

    public ListMyMacrosAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override

    public com.example.project.ui.gallery.ListMyMacrosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewlistmymacros,parent,false);
        return new com.example.project.ui.gallery.ListMyMacrosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (fName != null) {
        holder.tv_title.setText(fName.get(position));
        if (fValue.get(position).equals("1")) {
            holder.tv_description.setText("Status: Enabled");
        }
        else{
            holder.tv_description.setText("Status: Disabled");
        }
            switch (fName.get(position)) {
                case "Torch":
                    holder.tv_icon.setImageResource(R.drawable.torchicn);
                    holder.tv_title.setText("Auto Flashlight");
                    holder.tv_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, Torch.class);
                            v.getContext().startActivity(i);
                        }
                    });

                    break;
                case "Bluetooth":

                    holder.tv_icon.setImageResource(R.drawable.bluetoothicn);
                    holder.tv_title.setText("Auto Bluetooth");
                    holder.tv_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, cam.class);
                            v.getContext().startActivity(i);
                        }
                    });

                    break;
                case "Lock":

                    holder.tv_icon.setImageResource(R.drawable.lockicn);
                    holder.tv_title.setText("Auto phone lock");
                    holder.tv_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, doubletap.class);
                            v.getContext().startActivity(i);
                        }
                    });

                    break;
                case "Playpause":

                    holder.tv_icon.setImageResource(R.drawable.pauseicn);
                    holder.tv_title.setText("Auto pause playback");
                    holder.tv_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, playpause.class);
                            v.getContext().startActivity(i);
                        }
                    });

                    break;
                case "Silent":

                    holder.tv_icon.setImageResource(R.drawable.silenceicn);
                    holder.tv_title.setText("Auto scheduled silent");
                    holder.tv_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, silent.class);
                            v.getContext().startActivity(i);
                        }
                    });
                    break;
                case "SMSWisher":
                    holder.tv_icon.setImageResource(R.drawable.sms_icn);
                    holder.tv_title.setText("Auto SMS Wisher");
                    holder.tv_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, SmsWisher.class);
                            v.getContext().startActivity(i);
                        }
                    });
                    break;
                case "Drive":
                    holder.tv_icon.setImageResource(R.drawable.drive_icn);
                    holder.tv_title.setText("Auto Drive Upload");
                    holder.tv_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, photoUploader.class);
                            v.getContext().startActivity(i);
                        }
                    });
                    break;
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public int getItemCount() {
        if(fName!=null)
            return fName.size();
        else
            return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View cardview;
        TextView tv_title;
        TextView tv_description;
        ImageView tv_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview=itemView;
            tv_title = itemView.findViewById(R.id.titleMyMacro);
            tv_description = itemView.findViewById(R.id.description);
            tv_icon = itemView.findViewById(R.id.cardlistmymacros_icon);

        }
    }
}
