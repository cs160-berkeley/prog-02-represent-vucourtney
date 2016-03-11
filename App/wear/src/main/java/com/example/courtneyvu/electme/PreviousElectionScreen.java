package com.example.courtneyvu.electme;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class PreviousElectionScreen extends Activity {

    private String county;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_election_screen);

        Intent intent = getIntent();
        county = intent.getStringExtra("COUNTY");
        state = intent.getStringExtra("STATE");
        String loc = county + ", " + state;

        TextView countyView = (TextView) findViewById(R.id.county_state);
        TextView obamaPercentView = (TextView) findViewById(R.id.obama_vote_percentage);
        TextView romneyPercentView = (TextView) findViewById(R.id.romney_vote_percentage);

        countyView.setText(loc);

        try {
            String response = loadJSONFromAsset();
            JSONArray results = new JSONArray(response);
            for (int i = 0; i < results.length(); i++) {
                JSONObject current = results.getJSONObject(i);
                String curr_state = current.getString("state-postal");
                String curr_county = current.getString("county-name") + " County";

                if (curr_state.equals(state) && curr_county.equals(county)) {
                    String obamaPercent = current.getString("obama-percentage") + "%";
                    String romneyPercent = current.getString("romney-percentage") + "%";
                    obamaPercentView.setText(obamaPercent);
                    romneyPercentView.setText(romneyPercent);
                    break;
                }
            }
        } catch (JSONException e) {
            Log.d("TEST", "exception");
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("election-county-2012.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
