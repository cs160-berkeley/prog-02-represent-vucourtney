package com.example.courtneyvu.electme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailRep extends AppCompatActivity {

    private String bioguide_id;

    static final String SUNLIGHT_API_KEY = "69875f0d316a48d29d2db7b76923ba61";
    static final String SUNLIGHT_API_URL = "https://congress.api.sunlightfoundation.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rep);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String name = intent.getStringExtra("REP_NAME");
            String term_ends = intent.getStringExtra("TERM_END");
            String bg = intent.getStringExtra("BIOGUIDE");
            bioguide_id = bg;

            ImageView photoView = (ImageView) findViewById(R.id.rep_photo);
            TextView nameView = (TextView) findViewById(R.id.rep_name);
            TextView termEndsView = (TextView) findViewById(R.id.term_ends_entry);

            nameView.setText(name);
            termEndsView.setText(term_ends);
            new ImageLoadTask(photoView, bioguide_id).execute();
            new RetrieveFeedTaskCommittees().execute();
            new RetrieveFeedTaskBills().execute();
        }

    }

    class RetrieveFeedTaskCommittees extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {

            try {
                URL url;
                if (bioguide_id != null) {
                    url = new URL(SUNLIGHT_API_URL + "committees?" + "member_ids=" + bioguide_id + "&apikey=" + SUNLIGHT_API_KEY);
                } else {
                    Log.e("ERROR", "Didn't transfer valid bioguide id");
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
                JSONArray reps = obj.getJSONArray("results");
                Integer count = Integer.parseInt(obj.getString("count"));
                String commsResult = "";

                for (int i = 0; i < count; i++) {
                    JSONObject current = reps.getJSONObject(i);

                    if (!current.getBoolean("subcommittee")) {
                        commsResult += " - " + current.getString("name");
                        if (i < count - 1) {
                            commsResult += "\n";
                        }
                    }
                }

                TextView committeesList = (TextView) findViewById(R.id.committees_string);
                committeesList.setText(commsResult);


            } catch (JSONException e) {
                Log.d("TEST", "exception");
                e.printStackTrace();
            }
        }
    }

    class RetrieveFeedTaskBills extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {

            try {
                URL url;
                if (bioguide_id != null) {
                    url = new URL(SUNLIGHT_API_URL + "bills?" + "sponsor_id=" + bioguide_id + "&apikey=" + SUNLIGHT_API_KEY);
                } else {
                    Log.e("ERROR", "Didn't transfer valid bioguide id");
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
                JSONArray reps = obj.getJSONArray("results");
                Integer count = Integer.parseInt(obj.getString("count"));
                String billsResult = "";

                for (int i = 0; i < count && i < 10; i++) {
                    JSONObject current = reps.getJSONObject(i);

                    if (!current.isNull("short_title")) {
                        billsResult += " - " + current.getString("introduced_on") + ": " + current.getString("short_title");
                        if (i < count - 1) {
                            billsResult += "\n";
                        }
                    }
                }

                TextView billsList = (TextView) findViewById(R.id.bills_list);
                billsList.setText(billsResult);


            } catch (JSONException e) {
                Log.d("TEST", "exception");
                e.printStackTrace();
            }
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
        private String bioguide;

        public ImageLoadTask(ImageView imageView, String bg) {
            this.imageView = imageView;
            this.bioguide = bg;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL photo_url = new URL("https://theunitedstates.io/images/congress/225x275/" + bioguide + ".jpg");
                HttpURLConnection connection = (HttpURLConnection) photo_url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

}
