package com.example.android.vision.nutrition;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vision.MainActivity;
import com.example.android.vision.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NutritionAlert extends AppCompatActivity {
    Dialog myDialog;
    private FirebaseAuth mAuth;
    Button yes,no; private TextView txtclose;
    SharedPreferences preferences;
    FirebaseDatabase database ;
    DatabaseReference myRef ; TextView NetCal,CalBurnt;
    private TextView itemName , itemcal,TotalCal,UserCal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_alert);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("UserData");

        myDialog = new Dialog(this);

        TotalCal= (TextView) findViewById(R.id.TAC);
        UserCal= (TextView) findViewById(R.id.CI);
        NetCal= (TextView) findViewById(R.id.NC);
        CalBurnt= (TextView) findViewById(R.id.CB);


        final String itemname = getIntent().getStringExtra("ProductSelected");
        final double selectedCalorie = getIntent().getDoubleExtra("CurrentCalorie", 0);
        preferences = getSharedPreferences(getString(R.string.profile_fileName), Context.MODE_PRIVATE);
        final int totalCalories = preferences.getInt("Totalcalories",0 );
        final int userCalories = preferences.getInt("UserCalories", 0);
        int steps=preferences.getInt("CaloriesBurnt",0);
        final int caloriesBurnt=steps/20;
        final int netCalories=totalCalories+caloriesBurnt-userCalories;
        TotalCal.setText(""+totalCalories);
        UserCal.setText(""+userCalories);
        NetCal.setText(""+netCalories);
        CalBurnt.setText(""+caloriesBurnt);
 if (userCalories>netCalories){ myDialog.setContentView(R.layout.yesfood);
     View inflatedView = getLayoutInflater().inflate(R.layout.yesfood, null);
     TextView TT1=(TextView)inflatedView.findViewById(R.id.t11);
     txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
     txtclose.setText("M");
     //btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
     txtclose.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             myDialog.dismiss();
         }
     });
     myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
     myDialog.show();
     TT1.setText("Mind It, You exceeded the calorie intake limit");
     TT1.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
             Intent i=new Intent(NutritionAlert.this,MainActivity.class);
             startActivity(i);
             return false;
         }
     });}
        yes = (Button) findViewById(R.id.yesButton);
        no = (Button) findViewById(R.id.noButton);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userCalory=userCalories+(int)selectedCalorie;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("UserCalories", userCalory);
                editor.commit();

                int mm = preferences.getInt("UserCalories",0 );
                Toast.makeText(NutritionAlert.this, "Your calorie count is "+mm, Toast.LENGTH_LONG).show();

                myDialog.setContentView(R.layout.yesfood);
                View inflatedView = getLayoutInflater().inflate(R.layout.yesfood, null);
                TextView TT1=(TextView)myDialog.findViewById(R.id.t11);
                Toast.makeText(NutritionAlert.this,"Hurrah! You ate"+itemname+"\n Hope it was Yummy",Toast.LENGTH_SHORT).show();
                TT1.setText("Hurrah! You ate"+itemname+"\n Hope it was Yummy");
                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                txtclose.setText("M");
                //btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                TT1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Intent i=new Intent(NutritionAlert.this,MainActivity.class);
                        startActivity(i);
                        return false;
                    }
                });
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                Date c = Calendar.getInstance().getTime();
                DateFormat d = new SimpleDateFormat("dd-MM-yyyy");
                String date = d.format(c);


                DataHist obj = new DataHist();
                obj.itemName = itemname;
                obj.calories=selectedCalorie;


                myRef.child(uid).setValue(obj);



            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialog.setContentView(R.layout.nofood);

                int mm = preferences.getInt("UserCalories",0 );
                Toast.makeText(NutritionAlert.this, "Your calorie count is "+mm, Toast.LENGTH_LONG).show();
                View inflatedView = getLayoutInflater().inflate(R.layout.yesfood, null);
                TextView TT1=myDialog.findViewById(R.id.t11);
                TT1.setText("Oppppss! Have it next time for sure!");
                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                txtclose.setText("M");
                //btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                TT1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                      Intent i=new Intent(NutritionAlert.this,MainActivity.class);
                      startActivity(i);
                        return false;
                    }
                });
            }
        });

    }

    public void proceedHist(View view) {
        Intent intent = new Intent(NutritionAlert.this, NutritionHistory.class);
        startActivity(intent);
    }

    public void proceedMain(View view) {

        Intent intent = new Intent(NutritionAlert.this, MainActivity.class);
        startActivity(intent);
    }
}
