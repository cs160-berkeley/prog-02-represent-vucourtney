package com.example.courtneyvu.electme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Shake code sourced from http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it
 * Including SensorManager, SensorEventListener, onResume(), onPause()
 */


public class MainActivity extends Activity {

    private ArrayList<ClickableRepCardFragment> fragments;
    private String county;
    private String state;

    private SensorManager mSensorManager;
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        // two possibilites: the intent sends JSON from the phone, OR the intent sends lat/long/accel from shake

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        DotsPageIndicator dots = (DotsPageIndicator) findViewById(R.id.indicator);

        fragments = new ArrayList<>();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        if (extras == null) {
            return;
        } else if (extras.keySet().size() == 1) {
            String value = extras.getString("ALL_INFO");
            if (value != null) {
                String[] vals = value.split("%");
                String repsInfo = vals[0];
                county = vals[1];
                state = vals[2];

                parseRepsInfo(repsInfo);
            }
        }

        RepsGridPagerAdapter adapter = new RepsGridPagerAdapter(this, getFragmentManager(), fragments);

        pager.setAdapter(adapter);
        dots.setPager(pager);

    }

    /** Called when the user clicks the 2012 View button */
    public void get2012Screen(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, PreviousElectionScreen.class);
        intent.putExtra("COUNTY", county);
        intent.putExtra("STATE", state);
        startActivity(intent);
    }

    public void parseRepsInfo(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray reps = obj.getJSONArray("results");
            Log.d("TEST", ((Integer) reps.length()).toString());
            for (int i = 0; i < reps.length(); i++) {
                JSONObject current = reps.getJSONObject(i);

                String name = current.getString("first_name") + " " + current.getString("last_name");
                String title = current.getString("title");
                name = title + ". " + name;
                String party = current.getString("party");
                if (party.equals("D")) {
                    name += " (Democrat)";
                } else if (party.equals("R")) {
                    name += " (Republican)";
                } else if (party.equals("I")) {
                    name += " (Independent)";
                }

                String bioguide_id = current.getString("bioguide_id");
                String term_ends = current.getString("term_end");
                Bundle info = new Bundle();
                info.putString("name", name);
                info.putInt("photo", R.drawable.flag);
                info.putString("bioguide", bioguide_id);
                info.putString("term_ends", term_ends);

                ClickableRepCardFragment fragment = new ClickableRepCardFragment();
                fragment.setArguments(info);
                fragment.setCardGravity(Gravity.BOTTOM);
                fragment.setExpansionEnabled(true);
                fragments.add(fragment);
            }

        } catch (JSONException e) {
            Log.d("TEST", "exception");
            e.printStackTrace();
        }
    }


    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;

            if (delta > 50) {
                Toast toast = Toast.makeText(getApplicationContext(), "Shaken", Toast.LENGTH_LONG);
                toast.show();

                mAccelLast = mAccelCurrent;

                // send location to home screen to fetch legislators
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneShakeService.class);
                Log.d("test", "makes service");
                sendIntent.putExtra("SIGNAL", "Watch shaken");
                Log.d("test", "starts service");
                startService(sendIntent);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
