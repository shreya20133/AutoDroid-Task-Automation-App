package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.Gesture.*;

public class GestureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        Button bneto=(Button) findViewById(R.id.torchon);
        bneto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GestureActivity.this, Torch.class);
                startActivity(intent1);
            }
        });
        Button bneto1=(Button) findViewById(R.id.lockon);
        bneto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GestureActivity.this,doubletap.class);
                startActivity(intent1);
            }
        });
        Button bneto2=(Button) findViewById(R.id.cam);
        bneto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GestureActivity.this,cam.class);
                startActivity(intent1);
            }
        });
        Button ss22=(Button) findViewById(R.id.s1_btn);
        ss22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GestureActivity.this,"clicked",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(GestureActivity.this, silent.class);
                startActivity(intent1);
            }
        });
        Button pp22=(Button) findViewById(R.id.p1_btn);
        pp22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GestureActivity.this, playpause.class);
                startActivity(intent1);
            }
        });
    }
}