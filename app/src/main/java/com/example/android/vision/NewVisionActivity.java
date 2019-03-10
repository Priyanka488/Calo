package com.example.android.vision;

import android.graphics.Bitmap;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewVisionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private VisionAdapter mAdapter;
    String message="";
    String fDescription;
    private RecyclerView mList;TextView T;
    private static final int LOADER = 22;
    private ProgressBar mLoadingIndicator;

    private TextView mSearchResultsTextView,mUrlDisplayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vision);
        T=(TextView)findViewById(R.id.jT);
        mLoadingIndicator=(ProgressBar)findViewById(R.id.pb_loading_indicator);
        getSupportLoaderManager().initLoader(LOADER, null, this);
        Bundle queryBundle = new Bundle();
        // COMPLETED (20) Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        //  queryBundle.putString(SEARCH_QUERY_URL_EXTRA, githubSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        // COMPLETED (22) Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> githubSearchLoader = loaderManager.getLoader(LOADER);
        // COMPLETED (23) If the Loader was null, initialize it. Else, restart it.
        if (githubSearchLoader == null) {
            loaderManager.initLoader(LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER, queryBundle, this);
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
// COMPLETED (4) Return a new AsyncTaskLoader<String> as an anonymous inner class with this as the constructor's parameter
        return new AsyncTaskLoader<String>(this) {

            // COMPLETED (5) Override onStartLoading
            @Override
            protected void onStartLoading() {

                if (args == null) {

                    return;
                }

                // COMPLETED (7) Show the loading indicator
                /*
                 * When we initially begin loading in the background, we want to display the
                 * loading indicator to the user
                 */
                mLoadingIndicator.setVisibility(View.VISIBLE);
                // Toast.makeText(TopicActivity.this,"Hello",Toast.LENGTH_SHORT).show();
                forceLoad();

            }

            // COMPLETED (9) Override loadInBackground
            @Override
            public String loadInBackground() { String a="";
                Vision.Builder visionBuilder = new Vision.Builder(
                        new NetHttpTransport(),
                        new AndroidJsonFactory(),
                        null);

                visionBuilder.setVisionRequestInitializer(
                        new VisionRequestInitializer("AIzaSyBhS0fMuTYDXyetCGzvSdcyAl-Co5WwHtE"));
                Vision vision = visionBuilder.build();
                // InputStream inputStream =
                //       getResources().openRawResource(R.raw.photo2);


                try {
                    // byte[] photoData = IOUtils.toByteArray(inputStream);
                    byte[] photoData = getIntent().getByteArrayExtra("BITMAP_IMG");
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

                    a=result;
                    // message = result;



                } catch (Exception e) {
                    e.printStackTrace();
                }
                // COMPLETED (10) Get the String for our URL from the bundle passed to onCreateLoader
                /* Extract the search query from the args using our constant */


                // COMPLETED (11) If the URL is null or empty, return null
                /* If the user didn't enter anything, there's nothing to search for */

                return a;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        //    Toast.makeText(NewVisionActivity.this,"HELLO"+data,Toast.LENGTH_SHORT).show();
        // COMPLETED (14) Hide the loading indicator
        /* When we finish loading, we want to hide the loading indicator from the user. */
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        List<Collection> Listdata=new ArrayList<>();
        // COMPLETED (15) Use the same logic used in onPostExecute to show the data or the error message
        /*
         * If the results are null, we assume an error has occurred. There are much more robust
         * methods for checking errors, but we wanted to keep this particular example simple.
         */
        if (null == data) {//Toast.makeText(this,"Hello Data Empty",Toast.LENGTH_SHORT).show();

        } else {
            // mSearchResultsTextView.setText(data);
            try {
                // Toast.makeText(this,""+data,Toast.LENGTH_LONG).show();
                T.setText(""+data);


                JSONObject baseJsonObj = new JSONObject(data);
                JSONArray responseArray = baseJsonObj.getJSONArray("responses");
                // Toast.makeText(VisionActivity.this,responseArray.length()+"H",Toast.LENGTH_SHORT).show();
                JSONObject label = responseArray.getJSONObject(0);
                JSONArray labelArray = label.getJSONArray("labelAnnotations");
                // Toast.makeText(VisionActivity.this,labelArray.length()+"H",Toast.LENGTH_SHORT).show();
                for (int i=0;i<labelArray.length();i++) {
                    // Toast.makeText(NewVisionActivity.this,"h"+labelArray.length(),Toast.LENGTH_SHORT).show();
                    JSONObject iObj = labelArray.getJSONObject(i);
                    Collection NewsData = new Collection();
                    String description = iObj.getString("description");
                    NewsData.probability=iObj.getString("score");
                    //   Toast.makeText(NewVisionActivity.this,"h"+description,Toast.LENGTH_SHORT).show();
                    NewsData.itemName=description;

                    Listdata.add(NewsData);}


                // Setup and Handover data to recyclerview
                //  mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new VisionAdapter(NewVisionActivity.this, Listdata);
                mList=(RecyclerView)findViewById(R.id.Recycler2);
                mList.setAdapter(mAdapter);
                // Toast.makeText(this,""+Listdata.get(4),Toast.LENGTH_SHORT);
                // Toast.makeText(this, Listdata.size()+"", Toast.LENGTH_SHORT).show();
                mList.setLayoutManager(new LinearLayoutManager(NewVisionActivity.this));


            } catch (Exception e) {
                Log.v("","HHHHH");

                Toast.makeText(NewVisionActivity.this, "h"+e.toString(), Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    // COMPLETED (13) Override onLoadFinished



    // COMPLETED (16) Override onLoaderReset as it is part of the interface we implement, but don't do anything in this method




}
