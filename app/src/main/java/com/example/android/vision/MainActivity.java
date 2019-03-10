package com.example.android.vision;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vision.Barcode.BarcodeMainActivity;
import com.example.android.vision.userAcoount.LoginActivity;
import com.example.android.vision.userAcoount.UserActivity;
import com.example.android.vision.userAcoount.UserActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    String message="";   private Button btnCapture;
    Dialog myDialog;Button gallaryButton,proButton;
    private final int RC_PHOTO_PICKER=2;Button toBarCodeBtn;
    private ImageView imgCapture; Bitmap bp;TextView stepsCounterT;
    private static final int Image_Capture_Code = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.aboutApp,
                R.string.aboutApp);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        View inflatedView = getLayoutInflater().inflate(R.layout.nav_header_main, null);
        stepsCounterT = (TextView) inflatedView.findViewById(R.id.stepsText);
        toBarCodeBtn = (Button) findViewById(R.id.toBarBtn);
    //   int stepsC=Integer.parseInt(getIntent().getStringExtra("StepCount"));
       int stepsCC=getIntent().getIntExtra("StepCount",1);
      // stepsCounterT.setText("Hey "+stepsC);
        stepsCounterT.setText("\n Hey "+stepsCC);
        Toast.makeText(MainActivity.this,"Hey",Toast.LENGTH_SHORT).show();
        myDialog = new Dialog(this);
        btnCapture =(Button)findViewById(R.id.btnTakePicture);
        imgCapture = (ImageView) findViewById(R.id.capturedImage);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, Image_Capture_Code);
                Log.e("Hy", "First Step");    }
        });
        gallaryButton=(Button)findViewById(R.id.gallaryBtn);
        gallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });

       /* Button Sub = (Button) findViewById(R.id.SubmitBtn);
        Sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toVisionActivity = new Intent(MainActivity.this, NewVisionActivity.class);
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] byteArray = bStream.toByteArray();
                toVisionActivity.putExtra("BITMAP_IMG", byteArray);

                startActivity(toVisionActivity);
            }
        }); */

        toBarCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toVisionActivity = new Intent(MainActivity.this, BarcodeMainActivity.class);
                startActivity(toVisionActivity);
            }
        });
    }

    public void ShowPopup() {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.aboutus);
        View inflatedView = getLayoutInflater().inflate(R.layout.aboutus, null);
        TextView TT1=(TextView)myDialog.findViewById(R.id.t11);
        TextView TT2=(TextView)myDialog.findViewById(R.id.t22);
        TextView TT3=(TextView)myDialog.findViewById(R.id.t33);
        TT1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(MainActivity.this,"Recognize the Food You Eat!",Toast.LENGTH_SHORT).show();
                Intent iii=new Intent(MainActivity.this,MainActivity.class);
                startActivity(iii);
                return false;
            }
        });
        TT2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(MainActivity.this,"Know the calories in food!",Toast.LENGTH_SHORT).show();
                Intent iii=new Intent(MainActivity.this,MainActivity.class);
                startActivity(iii);
                return false;
            }
        });
        TT3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(MainActivity.this,"Track Your Health Status!",Toast.LENGTH_SHORT).show();
                Intent iii=new Intent(MainActivity.this,KalcMainActivity.class);
                startActivity(iii);
                return false;
            }
        });
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("M");
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            try{
               // BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(selectedImageUri, false);
               // Bitmap region = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Intent toVisionActivity = new Intent(MainActivity.this, NewVisionActivity.class);
                ByteArrayOutputStream bbStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bbStream);
                byte[] byteArray = bbStream.toByteArray();
                toVisionActivity.putExtra("BITMAP_IMG", byteArray);

                startActivity(toVisionActivity);}
            catch(Exception e){}

        }

        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                //images has been obtained as bitmap
                Log.e("Hy", "Works fine till here");
                bp = (Bitmap) data.getExtras().get("data");
                Intent toVisionActivity = new Intent(MainActivity.this, NewVisionActivity.class);
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] byteArray = bStream.toByteArray();
                toVisionActivity.putExtra("BITMAP_IMG", byteArray);

                startActivity(toVisionActivity);
                //imgCapture.setImageBitmap(bp);


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.sign_out: FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, UserActivity.class));


        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch(item.getItemId())
        {
            case R.id.about:
                drawerLayout.closeDrawer(GravityCompat.START);
               ShowPopup(); break;
            case R.id.nav_profile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent iii=new Intent(MainActivity.this,KalcMainActivity.class);
                startActivity(iii);break;
            case R.id.nav_stepcounter:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent ii=new Intent(MainActivity.this,WalkActivity2.class);
                startActivity(ii);break;
        }
        return true;
    }
}
