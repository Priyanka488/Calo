package com.example.android.vision.userAcoount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vision.MainActivity;
import com.example.android.vision.R;
import com.example.android.vision.StepsCounter.StepDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {
    private TextView textView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private Button SignIn;
    private Button SignUp;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    private String myvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("VerifiedEmails");

        SignIn = (Button) findViewById(R.id.signIn);
        SignUp = (Button) findViewById(R.id.signUp);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                final ProgressDialog progressDialog = new ProgressDialog(UserActivity.this,
                        R.style.Theme_AppCompat_Light_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Successfully signed in with:\n" + user.getEmail());
                progressDialog.show();
               // toastMessage("Successfully signed in with: " + user.getEmail());
            } else {
                // User is signed out
                //Log.d(TAG, "onAuthStateChanged:signed_out");
               /* final ProgressDialog progressDialog = new ProgressDialog(UserActivity.this,
                        R.style.Theme_AppCompat_Light_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Signed Out");
                progressDialog.show(); */
            }
            // ...
        }
    };
}


    @Override
    protected void onStart()
    {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);

        if (auth.getCurrentUser() != null ) {
            checkverification();

        }

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    private void checkverification() {

        String uid = auth.getCurrentUser().getUid();
        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myvalue = dataSnapshot.getValue(String.class);
                Toast.makeText(UserActivity.this, "verification status : "+myvalue, Toast.LENGTH_SHORT).show();
                if(myvalue.equals("Verified"))
                {
                    proceed();
                    finish();
                }
                else
                {
                    Toast.makeText(UserActivity.this, "User not verified", Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void proceed() {
        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
