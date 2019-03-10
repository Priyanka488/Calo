package com.example.android.vision.nutrition;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.vision.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NutritionHistory extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String uid;
    String itemName;
    double calories;
    private ListView mListView;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_history);

        mListView = (ListView) findViewById(R.id.listview);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        uid = user.getUid();
        Date c = Calendar.getInstance().getTime();
        DateFormat d = new SimpleDateFormat("dd-MM-yyyy");
        date = d.format(c);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserData");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot==null)
                {
                    Toast.makeText(NutritionHistory.this, "data snapshot is null", Toast.LENGTH_SHORT).show();
                }

                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

               // error.setText("The read failed: " + databaseError.getCode());

            }
        });



    }

    private void showData(DataSnapshot ds) {


            DataHist obj = new DataHist();
            obj.setCalories(ds.child(uid).getValue(DataHist.class).getCalories());
            obj.setItemName(ds.child(uid).getValue(DataHist.class).getItemName());


            ArrayList<String> array  = new ArrayList<>();
            itemName = obj.getItemName();
            calories=obj.getCalories();
            array.add(obj.getItemName());
            String cal = Double.toString(obj.getCalories());
            array.add(cal);

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mListView.setAdapter(adapter);




    }
}
