package com.example.android.vision.nutrition;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vision.R;
import com.example.android.vision.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NutritionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private TextView useless;
    private TextView useless2;
    URL finalUrl;
    RecyclerView mNumbersList;
    NutriAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private final static int NUTRITIONS_SEARCH_LOADER = 22;
    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        String foodName = getIntent().getStringExtra("Foodname");
        useless = (TextView) findViewById(R.id.useless);
        useless2 = (TextView) findViewById(R.id.useless2);


        String url = NetworkUtils.buildUrl(foodName);
        try {
            finalUrl = new URL(url);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        makeNuritionSeacrhQuery();
        mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);

        getSupportLoaderManager().initLoader(NUTRITIONS_SEARCH_LOADER, null, this);

    }


    @Override
    public Loader<String> onCreateLoader(int i, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }

                //mLoadingIndicator.setVisibility(View.VISIBLE);
                // COMPLETED (8) Force a load
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);

                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }


                try {
                    URL githubUrl = new URL(searchQueryUrlString);
                    String githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubUrl);
                    return githubSearchResults;
                } catch (IOException e) {
                    Toast.makeText(NutritionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return null;
                }


            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String results) {
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (null == results) {
            Toast.makeText(NutritionActivity.this, "No results", Toast.LENGTH_LONG).show();
        } else {


            List<DataNutrition> data = new ArrayList<>();
            //useless2.setText(results);

            try {

                JSONObject root = new JSONObject(results);
                JSONArray jArray = root.getJSONArray("hits");

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject json_data = jArray.getJSONObject(i).getJSONObject("fields");

                    DataNutrition nutriData = new DataNutrition();

                    nutriData.itemName = json_data.getString("item_name");
                    nutriData.calories = json_data.getDouble("nf_calories");
                    nutriData.fat = json_data.getDouble("nf_total_fat");

                    data.add(nutriData);
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(NutritionActivity.this);
                mNumbersList.setLayoutManager(layoutManager);
                mNumbersList.setHasFixedSize(true);
                mAdapter = new NutriAdapter(this, data);
                mNumbersList.setAdapter(mAdapter);



                //mResultsTextView.setText(s);
                //showJsonDataView();
            } catch (JSONException e) {
                Toast.makeText(NutritionActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onLoaderReset (Loader< String > loader) {

    }





    public void makeNuritionSeacrhQuery()
    {
        Bundle moviesBundle = new Bundle();
        moviesBundle.putString(SEARCH_QUERY_URL_EXTRA, finalUrl.toString());

        /*
         * Now that we've created our bundle that we will pass to our Loader, we need to decide
         * if we should restart the loader (if the loader already existed) or if we need to
         * initialize the loader (if the loader did NOT already exist).
         *
         * We do this by first store the support loader manager in the variable loaderManager.
         * All things related to the Loader go through through the LoaderManager. Once we have a
         * hold on the support loader manager, (loaderManager) we can attempt to access our
         * githubSearchLoader. To do this, we use LoaderManager's method, "getLoader", and pass in
         * the ID we assigned in its creation. You can think of this process similar to finding a
         * View by ID. We give the LoaderManager an ID and it returns a loader (if one exists). If
         * one doesn't exist, we tell the LoaderManager to create one. If one does exist, we tell
         * the LoaderManager to restart it.
         */
        // COMPLETED (21) Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getSupportLoaderManager();
        // COMPLETED (22) Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> githubSearchLoader = loaderManager.getLoader(NUTRITIONS_SEARCH_LOADER);
        // COMPLETED (23) If the Loader was null, initialize it. Else, restart it.
        if (githubSearchLoader == null) {
            loaderManager.initLoader(NUTRITIONS_SEARCH_LOADER, moviesBundle, this);
        } else {
            loaderManager.restartLoader(NUTRITIONS_SEARCH_LOADER, moviesBundle, this);
        }
    }
}