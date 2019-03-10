package com.example.android.vision.userPreferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.vision.R;

public class UserPref extends AppCompatActivity {

    private EditText age;
    private EditText weight;
    private EditText heightinches;
    private EditText heighFt;

    Button submit;


    RadioGroup activity;
    RadioButton activityChosen;

    RadioGroup gender;
    RadioButton genderChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pref);
        weight = (EditText) findViewById(R.id.weight);
        heightinches = (EditText) findViewById(R.id.heightInches);
        heighFt = (EditText) findViewById(R.id.heightFt);
        age = (EditText) findViewById(R.id.age);


        submit = (Button) findViewById(R.id.GoButton);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mm = "kuch bhi";
                int wt = Integer.parseInt(weight.getText().toString());
                int hF = Integer.parseInt(heighFt.getText().toString());
                int hI = Integer.parseInt(heightinches.getText().toString());
                int age1 = Integer.parseInt(age.getText().toString());

                int activityselected = activity.getCheckedRadioButtonId();
                activityChosen = (RadioButton) findViewById(activityselected);
                int genders = gender.getCheckedRadioButtonId();
                if (genders == R.id.male) ;
                if (activityselected == R.id.LightActivity) mm = "LightActivity";

                Toast.makeText(UserPref.this, "weight is " + wt + " height ft " + hF + " height Inches " +
                        +hI + " age is " + age1 + " gender is " + genders + "radio button chosen is " + mm, Toast.LENGTH_LONG).show();
            }
        });
    }
}
