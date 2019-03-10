package com.example.android.vision;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.vision.logger.Log;
import com.example.android.vision.logger.LogView;
import com.example.android.vision.logger.LogWrapper;
import com.example.android.vision.logger.MessageOnlyLogFilter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on Dec/8/2016.
 * <p/>
 * <p/>
 * http://stackoverflow.com/questions/28476809/step-counter-google-fit-api?rq=1
 */
public class WalkActivity2 extends AppCompatActivity {
        int totalnew=0;SharedPreferences preferences;
        public static final String TAG = "StepCounter";
        TextView titleText;
        private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;  Timestamp timestamp;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_walk);
            preferences = getSharedPreferences(getString(R.string.profile_fileName), Context.MODE_PRIVATE);
                // This method sets up our custom logger, which will print all log messages to the device
                // screen, as well as to adb logcat.
                initializeLogging();
                titleText=(TextView)findViewById(R.id.title_text_view);
                timestamp=  new Timestamp(System.currentTimeMillis());
                FitnessOptions fitnessOptions =
                        FitnessOptions.builder()
                                .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                                .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                .build();
                if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
                        GoogleSignIn.requestPermissions(
                                this,
                                REQUEST_OAUTH_REQUEST_CODE,
                                GoogleSignIn.getLastSignedInAccount(this),
                                fitnessOptions);
                } else {
                        subscribe();
                }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                        if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                                subscribe();
                        }
                }
        }

        /** Records step data by requesting a subscription to background step data. */
        public void subscribe() {
                // To create a subscription, invoke the Recording API. As soon as the subscription is
                // active, fitness data will start recording.
                Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))
                        .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                        Toast.makeText(WalkActivity2.this,"Success",Toast.LENGTH_SHORT).show();
                                                      //  Log.i(TAG, "Successfully subscribed!");
                                                } else {
                                                        Toast.makeText(WalkActivity2.this,"Heavy Network Load, Try Again Later",Toast.LENGTH_SHORT).show();// Log.w(TAG, "There was a problem subscribing.", task.getException());
                                                }
                                        }
                                });
        }

        /**
         * Reads the current daily step total, computed from midnight of the current day on the device's
         * current timezone.
         */

        private void readData() {
                Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                        .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                        .addOnSuccessListener(
                                new OnSuccessListener<DataSet>() {
                                        @Override
                                        public void onSuccess(DataSet dataSet) {
                                                long total =
                                                        dataSet.isEmpty()
                                                                ? 0
                                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                                                Log.i(TAG, "STEPS:"+total+": "+timestamp+"\n");
                                               totalnew=(int)total;
                                               titleText.setText("TODAY:"+totalnew);
                                            SharedPreferences.Editor editor = preferences.edit();

                                            editor.putInt("CaloriesBurnt", totalnew);
                                            editor.commit();
                                            Toast.makeText(WalkActivity2.this,""+totalnew,Toast.LENGTH_SHORT).show();
                                        }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "There was a problem getting the step count.", e);
                                        }
                                });
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the main; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_read_data) {
                        readData();
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }

        /** Initializes a custom log class that outputs both to in-app targets and logcat. */
        private void initializeLogging() {
                // Wraps Android's native log framework.
                LogWrapper logWrapper = new LogWrapper();
                // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
                Log.setLogNode(logWrapper);
                // Filter strips out everything except the message text.
                MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
                logWrapper.setNext(msgFilter);
                // On screen logging via a customized TextView.
               LogView logView = (LogView) findViewById(R.id.sample_logview);
             //   LogView logView2 = (LogView) findViewById(R.id.sample_logview2);

                // Fixing this lint error adds logic without benefit.
                // noinspection AndroidLintDeprecation
               //logView.setTextAppearance(R.style.Log);

              logView.setBackgroundColor(getResources().getColor(R.color.colorSplash));
            //  String a=""+msgFilter.getNext();

               msgFilter.setNext(logView);

               //titleText.setText("Total:");
               // Log.i(TAG, "Ready");
        }
}