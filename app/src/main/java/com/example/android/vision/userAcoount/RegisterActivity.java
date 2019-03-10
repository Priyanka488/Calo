package com.example.android.vision.userAcoount;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vision.KalcMainActivity;
import com.example.android.vision.MainActivity;
import com.example.android.vision.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText pwd;
    private Button verify;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    private TextView nameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        nameView=(TextView)findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        verify = (Button) findViewById(R.id.verifyButton);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("VerifiedEmails");




    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

    }
    public void toLogin(View view){
        Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
    }


    public void signInwithemail(View view)
    {   if(nameView.getText().toString()==null){Toast.makeText(RegisterActivity.this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();}
        final String myEmail = email.getText().toString();
        final String myPassword = pwd.getText().toString();
        mAuth.createUserWithEmailAndPassword(myEmail, myPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {



                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            myRef.child(id).setValue("Not Verified");
                            verifyEmail();

                        } else {
                            //Toast.makeText(RegisterActivity.this, "email is "+myEmail+"my password is "+myPassword, Toast.LENGTH_LONG).show();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
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
                            Toast.makeText(RegisterActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            verify.setVisibility(View.VISIBLE);


                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                });



    }

    private void proceed() {
        Intent intent = new Intent(RegisterActivity.this, KalcMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume()
    {
        //Toast.makeText(RegisterActivity.this, "On resume", Toast.LENGTH_SHORT).show();
        super.onResume();

    }


    public void verifyEmail(View view) {
        Task usertask = mAuth.getCurrentUser().reload();
        usertask.addOnSuccessListener(new OnSuccessListener() {


            @Override
            public void onSuccess(Object o) {
                FirebaseUser user = mAuth.getCurrentUser();
                boolean useremailveri = user.isEmailVerified();
                Toast.makeText(RegisterActivity.this, "On resume email verified = "+useremailveri, Toast.LENGTH_SHORT).show();
                if(useremailveri==true){

                    String id = user.getUid();
                    myRef.child(id).setValue("Verified");
                    proceed();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Check the link sent on your email and come back again !", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
