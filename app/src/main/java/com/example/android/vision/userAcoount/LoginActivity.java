package com.example.android.vision.userAcoount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.vision.MainActivity;
import com.example.android.vision.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText email;
    private EditText pwd;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    private String myvalue="";
    private Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        verify = (Button) findViewById(R.id.verify);

        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("VerifiedEmails");

    }
    public void toSignup(View view){
        Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);
    }
    public void SignIn(View view)
    {final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
            R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        final String myEmail = email.getText().toString();
        final String myPassword = pwd.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.signInWithEmailAndPassword(myEmail, myPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login activity", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            checkverification();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login activity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void checkverification() {

        String uid = mAuth.getCurrentUser().getUid();
        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myvalue = dataSnapshot.getValue(String.class);
                Toast.makeText(LoginActivity.this, "verification status : "+myvalue, Toast.LENGTH_SHORT).show();
                if(myvalue.equals("Verified"))
                {
                    proceed();
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "User not verified", Toast.LENGTH_SHORT).show();
                    verifyEmail();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onResume()
    {
       // Toast.makeText(LoginActivity.this, "On resume", Toast.LENGTH_SHORT).show();
        super.onResume();

    }

    private void proceed() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void verifyEmail()
    {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                        //findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            verify.setVisibility(View.VISIBLE);


                        } else {
                            Log.v("login problem", "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email."+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                });



    }

    public void verify(View view) {

        Task usertask = mAuth.getCurrentUser().reload();
        usertask.addOnSuccessListener(new OnSuccessListener() {


            @Override
            public void onSuccess(Object o) {
                FirebaseUser user = mAuth.getCurrentUser();
                boolean useremailveri = user.isEmailVerified();
                Toast.makeText(LoginActivity.this, "On resume email verified = "+useremailveri, Toast.LENGTH_SHORT).show();
                if(useremailveri==true){

                    String id = user.getUid();
                    myRef.child(id).setValue("Verified");
                    proceed();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Check the link sent on your email and come back again !", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}


