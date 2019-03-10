package com.example.android.vision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class BarCodeActivity extends AppCompatActivity {
TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        txtView=(TextView)findViewById(R.id.textView);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ImageView myImageView = (ImageView) findViewById(R.id.imgview);
        Bitmap myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.barcode);
        myImageView.setImageBitmap(myBitmap);
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();
        if(!detector.isOperational()){
            txtView.setText("Could not set up the detector!");
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        Barcode thisCode = barcodes.valueAt(0);
        TextView txtView = (TextView) findViewById(R.id.txtContent);
        txtView.setText(thisCode.rawValue);
    }
}
