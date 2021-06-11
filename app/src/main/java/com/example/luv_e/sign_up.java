package com.example.luv_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class sign_up extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email,pass;
    private ProgressBar ProBar;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        ProBar = findViewById(R.id.progressBar);
        Button signUp = findViewById(R.id.SignUp);
        Button Dsin = findViewById(R.id.Dsin);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.GsIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GSignIn();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Femail = email.getText().toString();
                String Fpass = pass.getText().toString();
                ProBar.setVisibility(View.VISIBLE);
                SignUn(Femail,Fpass);
            }
        });


        Dsin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up.this,sign_in.class);
                startActivity(intent);
                finish();
            }
        });

        Window window = sign_up.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(sign_up.this,R.color.fws));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void GSignIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(sign_up.this,"Login Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END onactivityresult]

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]
        ProBar.setVisibility(View.VISIBLE);
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ProBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Intent home  = new Intent(sign_up.this,MainActivity.class);
                            startActivity(home);
                            finish();
                            Toast.makeText(sign_up.this, "SignIn Success", Toast.LENGTH_SHORT).show();
                        } else {


                            Toast.makeText(sign_up.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END auth_with_google]

    private void SignUn(String femail, String fpass) {
        mAuth.createUserWithEmailAndPassword(femail,fpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ProBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent home  = new Intent(sign_up.this,MainActivity.class);
                    startActivity(home);
                    finish();
                    Toast.makeText(sign_up.this, "SignIn Success", Toast.LENGTH_SHORT).show();
                } else {


                    Toast.makeText(sign_up.this,"Login Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}