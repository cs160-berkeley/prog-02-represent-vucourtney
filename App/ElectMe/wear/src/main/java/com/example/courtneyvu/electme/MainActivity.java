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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
 * Shake code sourced from http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it
 * Including SensorManager, SensorEventListener, onResume(), onPause()
 */


public class MainActivity extends Activity {

    private TextView mTextView;
    private SensorManager mSensorManager;
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private int zipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        // two possibilites: the intent sends zip code from the phone, OR the intent sends lat/long/accel from shake

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        DotsPageIndicator dots = (DotsPageIndicator) findViewById(R.id.indicator);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        if (extras == null) {
            zipCode = 0;
        } else if (extras.keySet().size() == 1) {
            String zip = extras.getString("ZIP_CODE");
            zipCode = Integer.parseInt(zip);
        } else if (extras.keySet().size() > 1) {
            Double randLatitude = extras.getDouble("LATITUDE");
            Double randLongitude = extras.getDouble("LONGITUDE");
            Float accel = extras.getFloat("ACCEL");
            mAccelLast = accel;
            mAccelCurrent = accel;

            /* The following is to generate a random zip code, will be replaced with API code */
            zipCode = randLatitude.intValue() * 1000 + randLongitude.intValue() * 10 + (int) (Math.random() * 10);

            String textNewLoc = "New location: " + randLatitude.toString().substring(0, 5) + "N, " + randLongitude.toString().substring(0, 5) + "W";
            Toast toast = Toast.makeText(getApplicationContext(), textNewLoc, Toast.LENGTH_LONG);
            toast.show();
        }

        // When I get data, pass it in and change the constructor for RepsGridPagerAdapter
        String[] repNames;
        if (zipCode % 4 == 0) {
            repNames = new String[]{"Sen. Serena Walters (Republican)", "Sen. Sean Johnson (Independent)", "Rep. George Hamilton (Democrat)", "Those are your Congressional representatives!"};
        } else if (zipCode % 4 == 1) {
            repNames = new String[]{"Sen. Beatrice Parker (Democrat)", "Sen. Alexander Smith (Democrat)", "Rep. Robert Roberts (Republican)", "Those are your Congressional representatives!"};
        } else if (zipCode % 4 == 2) {
            repNames = new String[]{"Sen. Joanna Smith (Independent)", "Sen. Alvin Woodward (Independent)", "Rep. Xavier Parks (Democrat)", "Those are your Congressional representatives!"};
        } else {
            repNames = new String[]{"Sen. Annie Jameson (Republican)", "Sen. Hamilton Lee (Republican)", "Rep. Jon Hoover (Democrat)", "Those are your Congressional representatives!"};
        }
        Integer[] repPhotos = new Integer[]{R.drawable.woman1, R.drawable.man1, R.drawable.man2, 0};
        ArrayList<ClickableRepCardFragment> fragments = new ArrayList<ClickableRepCardFragment>();

        for (int i = 0; i < repNames.length; i++) {
            Bundle info = new Bundle();
            info.putCharSequence("name", repNames[i]);
            info.putInt("photo", repPhotos[i]);

            ClickableRepCardFragment fragment = new ClickableRepCardFragment();
            fragment.setArguments(info);
            fragment.setCardGravity(Gravity.BOTTOM);
            fragment.setExpansionEnabled(true);
            fragments.add(fragment);
        }

        RepsGridPagerAdapter adapter = new RepsGridPagerAdapter(this, getFragmentManager(), fragments);

        pager.setAdapter(adapter);
        dots.setPager(pager);

        if (extras != null && extras.keySet().size() > 1) {
            Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneShakeService.class);
            Log.d("test", "makes service");
            String concatNames = "";
            for (int i = 0; i < 3; i++) {
                concatNames += repNames[i] + ";";
            }
            sendIntent.putExtra("NAMES", concatNames);
            Log.d("test", "starts service");
            startService(sendIntent);
        }

    }

    /** Called when the user clicks the 2012 View button */
    public void get2012Screen(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, PreviousElectionScreen.class);
        intent.putExtra("ZIP_CODE", zipCode);
        startActivity(intent);
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
                Double randLatitude = Math.random()*25+24;
                Double randLongitude = Math.random()*58+66;

                Intent intent = new Intent(getBaseContext(), MainActivity.class);

                intent.putExtra("LATITUDE", randLatitude);
                intent.putExtra("LONGITUDE", randLongitude);
                intent.putExtra("ACCEL", mAccelCurrent);
                startActivity(intent);
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
