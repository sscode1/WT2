package com.chetan.wt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginTutor extends AppCompatActivity {

    int flag = 0;
    private String id;
    private static int RC_SIGN_IN = 100;

    private String Password;
    private String Mail;
    private Matcher matcher;
    private Button bu;
    private ProgressDialog pb;

    private FirebaseAuth mAuth;
    private DatabaseReference dataBase;
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tutor);

        Intent intent = getIntent();

        final TextView mail = findViewById(R.id.email);
        final TextView pass = findViewById(R.id.pass);
        final Button signup = findViewById(R.id.signIn);

        SignInButton googleSignIn = findViewById(R.id.google_signin_tutor);
        googleSignIn.setSize(SignInButton.SIZE_STANDARD);

        pb=new ProgressDialog(this);
        pb.setMessage("Logging In...");

        mAuth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance().getReference().child("Tutor");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(loginTutor.this, "Login unsuccessfull", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        googleSignIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.google_signin_tutor:
                        signIn();
                        break;
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;

                Mail = mail.getText().toString();
                Password = pass.getText().toString();

                if(TextUtils.isEmpty(Mail)){
                    mail.setError("Please Enter Email ID");
                    Toast.makeText(loginTutor.this, "Please Fill all the details!!", Toast.LENGTH_LONG).show();
                    flag = 1;
                }

                if(TextUtils.isEmpty(Password)){
                    pass.setError("Please Enter Password");
                    Toast.makeText(loginTutor.this, "Please Fill all the details!!", Toast.LENGTH_LONG).show();
                    flag = 1;
                }

                String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(Mail);

                if (flag == 0) {

                    pb.show();
                    mAuth.signInWithEmailAndPassword(Mail, Password).addOnCompleteListener(loginTutor.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                pb.dismiss();
                                Intent intent = new Intent(loginTutor.this, ListOfCourseTutor.class);

                                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                            else
                            {
                                pb.dismiss();

                                if (!matcher.matches()) {
                                    mail.setError("invalid e-mail");
                                    Toast.makeText(loginTutor.this, "Invalid E-Mail ID!!", Toast.LENGTH_SHORT).show();
                                }
                                else if (Password.length() <= 8) {
                                    pass.setError("password not long enough");
                                    Toast.makeText(loginTutor.this, "Invalid Password!!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(loginTutor.this, "Invalid Email/Password!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });

                }

            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(loginTutor.this, "Error in Authentication", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Signed In with Google Account!!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(loginTutor.this, ListOfCourseTutor.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Signin Failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


    public void forgot_pass(View view){
        Intent i = new Intent(loginTutor.this, forgotPassword.class);
        startActivity(i);
    }

    public void register_student(View view) {
        Intent intent = new Intent(loginTutor.this, TutorRegistration.class);
        startActivity(intent);
    }
}
