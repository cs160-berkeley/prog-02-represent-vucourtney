package com.example.courtneyvu.electme;

import android.app.Activity;
import android.content.Intent;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeScreen extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mGoogleApiClient;
    private ProgressBar progressBar;
    private Double latitude;
    private Double longitude;
    private String latForCounty;
    private String lngForCounty;
    private String zipCode;
    private String state;
    private String county;
    private String retrievedInfo;
    private boolean randomized;

    static final String SUNLIGHT_API_KEY = "69875f0d316a48d29d2db7b76923ba61";
    static final String SUNLIGHT_API_URL = "https://congress.api.sunlightfoundation.com/";
    static final String GOOGLE_API_KEY = "AIzaSyAsHCLsRIGop5rjIfKFE14fVKm64OzM4Hw";
    static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api/geocode/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null && extras.getString("SIGNAL") != null) {
            randomized = true;
            generateRandomLegislators();
        } else {
            setContentView(R.layout.activity_home_screen);

            randomized = false;
            Button getLocFromZip = (Button) findViewById(R.id.zip_button);
            Button getLocDirect = (Button) findViewById(R.id.loc_button);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            getLocFromZip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TEST", "sees zip code entry");
                    EditText editText = (EditText) findViewById(R.id.zip_entry);
                    zipCode = editText.getText().toString();

                    new RetrieveFeedTaskCoords().execute();
                }
            });

            getLocDirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (latitude != null && longitude != null) {
                        latForCounty = latitude.toString();
                        lngForCounty = longitude.toString();
                        new RetrieveFeedTaskCounty().execute();
                    }
                }
            });
        }

    }

    /** Called when the user clicks the GO! button */
    public void startCongressional() {
        // Do something in response to button
        Intent intent = new Intent(this, CongressionalOverview.class);
        intent.putExtra("ALL_REPS_INFO", retrievedInfo);
        startActivity(intent);
    }

    public void generateRandomLegislators() {
        //Double randLatitude = Math.random()*25+24;
        //Double randLongitude = Math.random()*58+66;
        int randomZip = (int) (Math.random()*99999);
        zipCode = ((Integer) randomZip).toString();

        new RetrieveFeedTaskCoords().execute();
    }

    class RetrieveFeedTaskLegislators extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url;
                if (zipCode != null && !randomized) {
                    url = new URL(SUNLIGHT_API_URL + "legislators/locate?" + "zip=" + zipCode + "&apikey=" + SUNLIGHT_API_KEY);
                } else if (latForCounty != null && lngForCounty != null) {
                    url = new URL(SUNLIGHT_API_URL + "legislators/locate?" + "latitude=" + latForCounty + "&longitude=" + lngForCounty + "&apikey=" + SUNLIGHT_API_KEY);
                } else {
                    Log.e("ERROR", "Didn't input valid zip code or location");
                    return null;
                }
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            Log.i("INFO", "Success");

            try {
                JSONObject obj = new JSONObject(response);
                String count = obj.getString("count");

                if (Integer.parseInt(count) > 0) {
                    retrievedInfo = response;
                    startCongressional();
                    Log.d("TEST", "service initializing");

                    Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    sendIntent.putExtra("ALL_REPS_INFO", retrievedInfo);
                    sendIntent.putExtra("STATE", state);
                    sendIntent.putExtra("COUNTY", county);
                    Log.d("TEST", state);
                    Log.d("TEST", county);
                    startService(sendIntent);
                }
            } catch (JSONException e) {
                Log.d("TEST", "exception");
                e.printStackTrace();
            }
        }
    }

    class RetrieveFeedTaskCoords extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {

            try {
                URL url;
                if (zipCode != null) {
                    url = new URL(GOOGLE_API_URL + "json?" + "address=" + zipCode + "&components=country:US&key=" + GOOGLE_API_KEY);
                } else {
                    Log.e("ERROR", "Didn't input valid zip code");
                    return null;
                }
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", "Success");

            try {
                JSONObject obj = new JSONObject(response);
                JSONArray results = obj.getJSONArray("results");
                Log.d("TEST", obj.getString("status"));
                if (obj.getString("status").equals("ZERO_RESULTS")) {
                    generateRandomLegislators();
                } else if (results.length() > 0) {
                    JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
                    JSONObject loc = geometry.getJSONObject("location");

                    latForCounty = loc.getString("lat");
                    lngForCounty = loc.getString("lng");
                    Log.d("TEST", latForCounty + "," + lngForCounty);

                    new RetrieveFeedTaskCounty().execute();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Zip Code", Toast.LENGTH_LONG);
                    toast.show();
                }

            } catch (JSONException e) {
                Log.d("TEST", "exception");
                e.printStackTrace();
            }
        }
    }

    class RetrieveFeedTaskCounty extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {

            try {
                URL url;
                if (latForCounty != null && lngForCounty != null) {
                    url = new URL(GOOGLE_API_URL + "json?" + "latlng=" + latForCounty + "," + lngForCounty + "&key=" + GOOGLE_API_KEY);
                } else {
                    Log.e("ERROR", "Didn't input valid location in coordinates");
                    return null;
                }
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", "Success");

            try {
                JSONObject obj = new JSONObject(response);
                JSONArray results = obj.getJSONArray("results");
                Log.d("TEST", obj.getString("status"));
                if (obj.getString("status").equals("ZERO_RESULTS")) {
                    generateRandomLegislators();
                } else if (results.length() > 0) {
                    JSONArray addr = results.getJSONObject(0).getJSONArray("address_components");

                    for (int i = 0; i < addr.length(); i++) {
                        JSONObject comp = addr.getJSONObject(i);
                        JSONArray types = comp.getJSONArray("types");
                        for (int j = 0; j < types.length(); j++) {
                            // Get the state (level 1) and the county (level 2)
                            if (types.getString(j).equals("administrative_area_level_1")) {
                                state = comp.getString("short_name");
                            } else if (types.getString(j).equals("administrative_area_level_2")) {
                                county = comp.getString("short_name");
                            }
                        }
                    }
                    new RetrieveFeedTaskLegislators().execute();
                }
            } catch (JSONException e) {
                Log.d("TEST", "exception");
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("TEST", "connected");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("TEST", "got coords");
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

}
