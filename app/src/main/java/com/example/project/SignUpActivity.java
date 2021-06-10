package com.example.project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText editTextName,editTextEmail,editTextPassword;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primarydark));

        //get FirebaseAuthentication instance
        mAuth = FirebaseAuth.getInstance();

        //find all required views by Id
        editTextName=findViewById(R.id.et_SignUp_name);
        editTextEmail=findViewById(R.id.et_SignUp_email);
        editTextPassword=findViewById(R.id.et_SignUp_password);

        Button SignUpBtn = findViewById(R.id.SignUpBtn);
        TextView LogInTv=findViewById(R.id.LoginTv);
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpNewUser();
            }
        });

        LogInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void SignUpNewUser() {

        String currName=editTextName.getText().toString();
        String currEmail=editTextEmail.getText().toString();
        String currPassword=editTextPassword.getText().toString();
        boolean statusEmailCorrect=validateEmail(currEmail);
        if(statusEmailCorrect && !currName.isEmpty() && !currEmail.isEmpty() && !currPassword.isEmpty())
        {
            mAuth.createUserWithEmailAndPassword(currEmail,currPassword).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    User user=new User(currEmail,currPassword);
                    FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                            .setValue(user).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Your account is created Successfully",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),"Please enter valid credentials",Toast.LENGTH_SHORT).show();
    }

    private static boolean validateEmail(String currEmail) {
        return !TextUtils.isEmpty(currEmail) && Patterns.EMAIL_ADDRESS.matcher(currEmail).matches();
    }
}