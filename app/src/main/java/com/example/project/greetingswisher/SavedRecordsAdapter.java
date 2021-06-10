package com.example.project.greetingswisher;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SavedRecordsAdapter extends RecyclerView.Adapter<com.example.project.greetingswisher.SavedRecordsAdapter.ViewHolder> {

    Context context;
    DatabaseReference db;
    ArrayList<String> dateList=new ArrayList<>();
    ArrayList<String> contactList=new ArrayList<>();
    ArrayList<String> nameContactList=new ArrayList<>();
    ArrayList<String> messageList=new ArrayList<>();

    public SavedRecordsAdapter(Context context,ArrayList<String> dateList,ArrayList<String> nameContactList,ArrayList<String> contactList,ArrayList<String> messageList) {
        this.context=context;
        this.dateList=dateList;
        this.nameContactList=nameContactList;
        this.contactList=contactList;
        this.messageList=messageList;
    }

    @NonNull
    @Override

    public com.example.project.greetingswisher.SavedRecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewshowsmsrecords,parent,false);
        return new com.example.project.greetingswisher.SavedRecordsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.project.greetingswisher.SavedRecordsAdapter.ViewHolder holder, int position) {
        String date=dateList.get(position);
        String contact=nameContactList.get(position);
        if(contact.equals(""))
        {
            contact=contactList.get(position);
        }
        String message=messageList.get(position);
        holder.tv_date.setText(date);
        holder.tv_contactnum.setText(contact);
        holder.tv_messagesms.setText(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View cardview;
        TextView tv_contactnum;
        TextView tv_date;
        TextView tv_messagesms;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview=itemView;
            tv_contactnum = itemView.findViewById(R.id.contactnum);
            tv_date = itemView.findViewById(R.id.datesms);
            tv_messagesms = itemView.findViewById(R.id.messageSMSwish);
        }
    }
}