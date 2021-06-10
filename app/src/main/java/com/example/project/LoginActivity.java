package com.example.project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

//import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =1 ;
    public static FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText editTextUserName,editTextPwd;
    //LottieAnimationView loadingAnimation;
    AlertDialog.Builder builder;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primarydark));

        //get FirebaseAuthentication instance
        mAuth = FirebaseAuth.getInstance();

        //find all required views by Id
        editTextUserName=findViewById(R.id.editTextUsername);
        editTextPwd=findViewById(R.id.editTextPassword);
        //loadingAnimation=findViewById(R.id.loadinggooglepopupAnimation);
        TextView tv_ForgotPassword = findViewById(R.id.tv_ForgotPassword);
        Button loginBtn = findViewById(R.id.loginBtn);
        CardView googleSignInView = findViewById(R.id.googleSignIn);
        TextView signUpTv=findViewById(R.id.SignUpTv);

        //Initialise Alert Dialog builder object
        builder = new AlertDialog.Builder(this);

        loginBtn.setOnClickListener(v -> loginUser());
        googleLogin();
        googleSignInView.setOnClickListener(v -> {
           // loadingAnimation.setVisibility(View.VISIBLE);
            signIn();
        });
        tv_ForgotPassword.setOnClickListener(v -> actionForForgotPassword());
        signUpTv.setOnClickListener(v -> SignUpNewUser());
    }

    private void loginUser() {
        String currEmail=editTextUserName.getText().toString();
        String currPassword=editTextPwd.getText().toString();
        boolean statusEmailCorrect=validateEmail(currEmail);
        if(statusEmailCorrect && !currEmail.isEmpty() && !currPassword.isEmpty())
        {
            mAuth.signInWithEmailAndPassword(currEmail,currPassword).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    User user=new User(currEmail,currPassword);
                    FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                            .setValue(user).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"You are logged in successfully.Redirecting to Home page...",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
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
        else if(!statusEmailCorrect)
            Toast.makeText(getApplicationContext(),"Please enter valid username",Toast.LENGTH_SHORT).show();
        else if(currEmail.isEmpty())
            Toast.makeText(getApplicationContext(),"Please enter Username",Toast.LENGTH_SHORT).show();
        else if(currPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please enter Password",Toast.LENGTH_SHORT).show();
        }
    }

    private void SignUpNewUser() {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    private void actionForForgotPassword() {
        String currEmail=editTextUserName.getText().toString();
        boolean statusEmailCorrect=validateEmail(currEmail);
        if(!currEmail.isEmpty())
        {
            if(statusEmailCorrect) {
                builder.setTitle("Reset Password");
                builder.setMessage("A password reset will be sent to " + currEmail + ". Please click on the link mentioned in mail to reset your password")
                        .setCancelable(true)
                        .setPositiveButton("OK", (dialog, which) -> mAuth.sendPasswordResetEmail(currEmail)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Reset email sent!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }))
                        .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.setTitle("Forgot Password?");
                alert.show();
            }
            else
                Toast.makeText(getApplicationContext(),"Please enter valid Username",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Username cannot be empty!",Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean validateEmail(String currEmail) {
        return !TextUtils.isEmpty(currEmail) && Patterns.EMAIL_ADDRESS.matcher(currEmail).matches();
    }

    private void googleLogin() {

        //show the popup containing email accounts by requesting google
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    private void signIn() {
       // googleLogin();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mGoogleSignInClient.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful())
            {
                Toast.makeText(getApplicationContext(),"You are logged in Successfully.Redirecting to Home page...",Toast.LENGTH_LONG).show();
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if(account!=null)
                    {
                        firebaseAuthWithGoogle(account.getIdToken());
                    }
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.e("TAG", "Google sign in failed", e);
                    Toast.makeText(getApplicationContext(),"Error encountered: "+e.getStatusCode(),Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                   // loadingAnimation.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user!=null)
                        {
                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(),"Authentication failed!Please try again!",Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        }
    }
}