package com.example.android.vision;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisionActivity extends AppCompatActivity {
    private List<String> movieList;
    private static final int NUM_ITEMS=100;
    private VAdapter mAdapter;
    private RecyclerView mNumberList;
    String message = "";
    String fDescription = "";
    TextView JsonTextView;
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);
        mNumberList=(RecyclerView)findViewById(R.id.RV2);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mNumberList.setLayoutManager(layoutManager);
        movieList = new ArrayList<String>();
        mNumberList.setHasFixedSize(true);

        JsonTextView = (TextView) findViewById(R.id.jsonText);
        txtView=(TextView)findViewById(R.id.textView2);
        mAdapter=new VAdapter(movieList);
        mNumberList.setAdapter(mAdapter);
        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyCQ2y0Oakch3ft5B-T8pN-CLwHIKLcl5gY"));
        final Vision vision = visionBuilder.build();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Convert photo to byte array
                InputStream inputStream =
                        getResources().openRawResource(R.raw.photo2);
                try {
                    byte[] photoData = IOUtils.toByteArray(inputStream);
                    Image inputImage = new Image();
                    inputImage.encodeContent(photoData);
                    Feature desiredFeature = new Feature();
                    desiredFeature.setType("LABEL_DETECTION");
                    AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
                    annotateImageReq.setFeatures(Arrays.asList(desiredFeature));
                    annotateImageReq.setImage(inputImage);
                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(Arrays.asList(annotateImageReq));
                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    String result = response.toString();
                    message = result;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /*  Toast.makeText(getApplicationContext(),
                                    message, Toast.LENGTH_LONG).show();*/
                           // getDataFromJson(message);
                            //JsonTextView.setText(""+message);
                            String parseJson=getDataFromJson(message);
                            display(message,getDataFromJson(message)+"" );
                           // movieList.add(parseJson+"");


                            Toast.makeText(VisionActivity.this,movieList+"",Toast.LENGTH_SHORT).show();



                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //  finally{inputStream.close();}

                // More code here
            }
        });

    }

    void display(String s,String t) {
        JsonTextView.setText("" + s);
        txtView.setText(""+t);
    }

    String getDataFromJson(String MJson) {
        try {
            JSONObject baseJsonObj = new JSONObject(MJson);
            JSONArray responseArray = baseJsonObj.getJSONArray("responses");
           // Toast.makeText(VisionActivity.this,responseArray.length()+"H",Toast.LENGTH_SHORT).show();
            JSONObject label = responseArray.getJSONObject(0);

            JSONArray labelArray = label.getJSONArray("labelAnnotations");
           // Toast.makeText(VisionActivity.this,labelArray.length()+"H",Toast.LENGTH_SHORT).show();
            //for (int i = 0; i < responseArray.length(); ++i) {
                JSONObject iObj = labelArray.getJSONObject(0);
                String description = iObj.getString("description");
               fDescription=description;
               movieList.add(fDescription);
            mAdapter.notifyDataSetChanged();

                //Toast.makeText(VisionActivity.this,description,Toast.LENGTH_SHORT).show();
              //  fDescription = description;

           // }JsonTextView.setText(""+fDescription);
        } catch (Exception e) {
            e.getMessage();
        }
        return fDescription;  }

}
