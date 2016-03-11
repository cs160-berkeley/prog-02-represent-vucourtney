package com.example.courtneyvu.electme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CongressionalOverview extends AppCompatActivity {

    // Add twitter_id, bioguide_id, and term_ends in that order
    HashMap<Integer, ArrayList<String>> repsInfoForLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_overview);

        repsInfoForLater = new HashMap<>();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String repsInfo = extras.getString("ALL_REPS_INFO");
            parseRepsInfo(repsInfo);

            ImageView imgView1 = (ImageView) findViewById(R.id.pic1);
            ImageView imgView2 = (ImageView) findViewById(R.id.pic2);
            ImageView imgView3 = (ImageView) findViewById(R.id.pic3);
            ImageView imgView4 = (ImageView) findViewById(R.id.pic4);
            ImageView imgView5 = (ImageView) findViewById(R.id.pic5);
            ImageView imgView6 = (ImageView) findViewById(R.id.pic6);
            ImageView imgView7 = (ImageView) findViewById(R.id.pic7);

            new ImageLoadTask(imgView1, repsInfoForLater.get(1).get(1)).execute();
            new ImageLoadTask(imgView2, repsInfoForLater.get(2).get(1)).execute();
            new ImageLoadTask(imgView3, repsInfoForLater.get(3).get(1)).execute();
            if (repsInfoForLater.size() > 3) {
                new ImageLoadTask(imgView4, repsInfoForLater.get(4).get(1)).execute();
            }
            if (repsInfoForLater.size() > 4) {
                new ImageLoadTask(imgView5, repsInfoForLater.get(5).get(1)).execute();
            }
            if (repsInfoForLater.size() > 5) {
                new ImageLoadTask(imgView6, repsInfoForLater.get(6).get(1)).execute();
            }
            if (repsInfoForLater.size() > 6) {
                new ImageLoadTask(imgView7, repsInfoForLater.get(7).get(1)).execute();
            }

            if (repsInfoForLater.size() < 7) {
                RelativeLayout block7 = (RelativeLayout) findViewById(R.id.rep7);
                block7.setVisibility(View.INVISIBLE);
            }
            if (repsInfoForLater.size() < 6) {
                RelativeLayout block6 = (RelativeLayout) findViewById(R.id.rep6);
                block6.setVisibility(View.INVISIBLE);
            }
            if (repsInfoForLater.size() < 5) {
                RelativeLayout block5 = (RelativeLayout) findViewById(R.id.rep5);
                block5.setVisibility(View.INVISIBLE);
            }
            if (repsInfoForLater.size() < 4) {
                RelativeLayout block4 = (RelativeLayout) findViewById(R.id.rep4);
                block4.setVisibility(View.INVISIBLE);
            }


//            if (extras.keySet().size() > 1) {
//                TextView name1 = (TextView) findViewById(R.id.name1);
//                TextView name2 = (TextView) findViewById(R.id.name2);
//                TextView name3 = (TextView) findViewById(R.id.name3);
//
//                name1.setText(intent.getStringExtra("REP0_NAME"));
//                name2.setText(intent.getStringExtra("REP1_NAME"));
//                name3.setText(intent.getStringExtra("REP2_NAME"));
//
//                if (extras.keySet().size() > 3) {
//                    RelativeLayout block4 = (RelativeLayout) findViewById(R.id.rep4);
//                    TextView name4 = (TextView) findViewById(R.id.name4);
//
//                    block4.setVisibility(View.VISIBLE);
//                    name4.setText(intent.getStringExtra("REP3_NAME"));
//                } else {
//
//                }
//            }
        }

    }

    public void parseRepsInfo(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray reps = obj.getJSONArray("results");
            Log.d("TEST", ((Integer) reps.length()).toString());
            for (int i = 0; i < reps.length(); i++) {
                JSONObject current = reps.getJSONObject(i);
                String bioguide_id = current.getString("bioguide_id");
                String name = current.getString("first_name") + " " + current.getString("last_name");
                String title = current.getString("title");
                name = title + ". " + name;

                String party = current.getString("party");
                if (party.equals("D")) {
                    name += " (D)";
                } else if (party.equals("R")) {
                    name += " (R)";
                } else if (party.equals("I")) {
                    name += " (I)";
                }

                String twitter_id = current.getString("twitter_id");
                String term_ends = current.getString("term_end");
                String email = "Email: " + current.getString("oc_email");
                String website = "Website: " + current.getString("website");

                ArrayList<String> infoForLater = new ArrayList<>();

                if (!current.isNull("twitter_id")) {
                    infoForLater.add(twitter_id);
                } else {
                    infoForLater.add("");
                }
                infoForLater.add(bioguide_id);
                infoForLater.add(term_ends);

                repsInfoForLater.put(i+1, infoForLater);

                Log.d("TEST", name);

                if (i == 0) {
                    Log.d("TEST", name);
                    RelativeLayout current_block = (RelativeLayout) findViewById(R.id.rep1);
                    TextView current_name = (TextView) findViewById(R.id.name1);
                    TextView current_email = (TextView) findViewById(R.id.email1);
                    TextView current_website = (TextView) findViewById(R.id.website1);

                    if (party.equals("D")) {
                        current_block.setBackgroundResource(R.drawable.border_democrat);
                    } else if (party.equals("R")) {
                        current_block.setBackgroundResource(R.drawable.border_republican);
                    } else {
                        current_block.setBackgroundResource(R.drawable.border_no_fill);
                    }
                    current_name.setText(name);
                    if (!current.isNull("oc_email")) {
                        current_email.setText(email);
                    } else {
                        current_email.setText("No email found.");
                    }
                    if (!current.isNull("website")) {
                        current_website.setText(website);
                    } else {
                        current_website.setText("No website found.");
                    }
                } else if (i == 1) {
                    RelativeLayout current_block = (RelativeLayout) findViewById(R.id.rep2);
                    TextView current_name = (TextView) findViewById(R.id.name2);
                    TextView current_email = (TextView) findViewById(R.id.email2);
                    TextView current_website = (TextView) findViewById(R.id.website2);

                    if (party.equals("D")) {
                        current_block.setBackgroundResource(R.drawable.border_democrat);
                    } else if (party.equals("R")) {
                        current_block.setBackgroundResource(R.drawable.border_republican);
                    } else {
                        current_block.setBackgroundResource(R.drawable.border_no_fill);
                    }
                    current_name.setText(name);
                    if (!current.isNull("oc_email")) {
                        current_email.setText(email);
                    } else {
                        current_email.setText("No email found.");
                    }
                    if (!current.isNull("website")) {
                        current_website.setText(website);
                    } else {
                        current_website.setText("No website found.");
                    }
                } else if (i == 2) {
                    RelativeLayout current_block = (RelativeLayout) findViewById(R.id.rep3);
                    TextView current_name = (TextView) findViewById(R.id.name3);
                    TextView current_email = (TextView) findViewById(R.id.email3);
                    TextView current_website = (TextView) findViewById(R.id.website3);

                    if (party.equals("D")) {
                        current_block.setBackgroundResource(R.drawable.border_democrat);
                    } else if (party.equals("R")) {
                        current_block.setBackgroundResource(R.drawable.border_republican);
                    } else {
                        current_block.setBackgroundResource(R.drawable.border_no_fill);
                    }
                    current_name.setText(name);
                    if (!current.isNull("oc_email")) {
                        current_email.setText(email);
                    } else {
                        current_email.setText("No email found.");
                    }
                    if (!current.isNull("website")) {
                        current_website.setText(website);
                    } else {
                        current_website.setText("No website found.");
                    }
                } else if (i == 3) {
                    RelativeLayout current_block = (RelativeLayout) findViewById(R.id.rep4);
                    TextView current_name = (TextView) findViewById(R.id.name4);
                    TextView current_email = (TextView) findViewById(R.id.email4);
                    TextView current_website = (TextView) findViewById(R.id.website4);

                    current_block.setVisibility(View.VISIBLE);
                    if (party.equals("D")) {
                        current_block.setBackgroundResource(R.drawable.border_democrat);
                    } else if (party.equals("R")) {
                        current_block.setBackgroundResource(R.drawable.border_republican);
                    } else {
                        current_block.setBackgroundResource(R.drawable.border_no_fill);
                    }
                    current_name.setText(name);
                    if (!current.isNull("oc_email")) {
                        current_email.setText(email);
                    } else {
                        current_email.setText("No email found.");
                    }
                    if (!current.isNull("website")) {
                        current_website.setText(website);
                    } else {
                        current_website.setText("No website found.");
                    }
                } else if (i == 4) {
                    RelativeLayout current_block = (RelativeLayout) findViewById(R.id.rep5);
                    TextView current_name = (TextView) findViewById(R.id.name5);
                    TextView current_email = (TextView) findViewById(R.id.email5);
                    TextView current_website = (TextView) findViewById(R.id.website5);

                    current_block.setVisibility(View.VISIBLE);
                    if (party.equals("D")) {
                        current_block.setBackgroundResource(R.drawable.border_democrat);
                    } else if (party.equals("R")) {
                        current_block.setBackgroundResource(R.drawable.border_republican);
                    } else {
                        current_block.setBackgroundResource(R.drawable.border_no_fill);
                    }
                    current_name.setText(name);
                    if (!current.isNull("oc_email")) {
                        current_email.setText(email);
                    } else {
                        current_email.setText("No email found.");
                    }
                    if (!current.isNull("website")) {
                        current_website.setText(website);
                    } else {
                        current_website.setText("No website found.");
                    }
                } else if (i == 5) {
                    RelativeLayout current_block = (RelativeLayout) findViewById(R.id.rep6);
                    TextView current_name = (TextView) findViewById(R.id.name6);
                    TextView current_email = (TextView) findViewById(R.id.email6);
                    TextView current_website = (TextView) findViewById(R.id.website6);

                    current_block.setVisibility(View.VISIBLE);
                    if (party.equals("D")) {
                        current_block.setBackgroundResource(R.drawable.border_democrat);
                    } else if (party.equals("R")) {
                        current_block.setBackgroundResource(R.drawable.border_republican);
                    } else {
                        current_block.setBackgroundResource(R.drawable.border_no_fill);
                    }
                    current_name.setText(name);
                    if (!current.isNull("oc_email")) {
                        current_email.setText(email);
                    } else {
                        current_email.setText("No email found.");
                    }
                    if (!current.isNull("website")) {
                        current_website.setText(website);
                    } else {
                        current_website.setText("No website found.");
                    }
                } else if (i == 6) {
                    RelativeLayout current_block = (RelativeLayout) findViewById(R.id.rep7);
                    TextView current_name = (TextView) findViewById(R.id.name7);
                    TextView current_email = (TextView) findViewById(R.id.email7);
                    TextView current_website = (TextView) findViewById(R.id.website7);

                    current_block.setVisibility(View.VISIBLE);
                    if (party.equals("D")) {
                        current_block.setBackgroundResource(R.drawable.border_democrat);
                    } else if (party.equals("R")) {
                        current_block.setBackgroundResource(R.drawable.border_republican);
                    } else {
                        current_block.setBackgroundResource(R.drawable.border_no_fill);
                    }
                    current_name.setText(name);
                    if (!current.isNull("oc_email")) {
                        current_email.setText(email);
                    } else {
                        current_email.setText("No email found.");
                    }
                    if (!current.isNull("website")) {
                        current_website.setText(website);
                    } else {
                        current_website.setText("No website found.");
                    }
                }

            }

        } catch (JSONException e) {
            Log.d("TEST", "exception");
            e.printStackTrace();
        }
    }


    /** Called when the user clicks the View Tweet button */
    public void getTweetRep1(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TweetScreen.class);
        TextView editText = (TextView) findViewById(R.id.name1);
        String message = editText.getText().toString();
        String twitter_username = repsInfoForLater.get(1).get(0);
        intent.putExtra("REP_NAME", message);
        intent.putExtra("TWITTER_USERNAME", twitter_username);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getTweetRep2(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TweetScreen.class);
        TextView editText = (TextView) findViewById(R.id.name2);
        String message = editText.getText().toString();
        String twitter_username = repsInfoForLater.get(2).get(0);
        intent.putExtra("REP_NAME", message);
        intent.putExtra("TWITTER_USERNAME", twitter_username);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getTweetRep3(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TweetScreen.class);
        TextView editText = (TextView) findViewById(R.id.name3);
        String message = editText.getText().toString();
        String twitter_username = repsInfoForLater.get(3).get(0);
        intent.putExtra("REP_NAME", message);
        intent.putExtra("TWITTER_USERNAME", twitter_username);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getTweetRep4(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TweetScreen.class);
        TextView editText = (TextView) findViewById(R.id.name4);
        String message = editText.getText().toString();
        String twitter_username = repsInfoForLater.get(4).get(0);
        intent.putExtra("REP_NAME", message);
        intent.putExtra("TWITTER_USERNAME", twitter_username);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getDetailedScreen1(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailRep.class);

        TextView editText = (TextView) findViewById(R.id.name1);
        String message = editText.getText().toString();
        String term_ends = repsInfoForLater.get(1).get(2);
        String bioguide_id = repsInfoForLater.get(1).get(1);

        intent.putExtra("REP_NAME", message);
        intent.putExtra("TERM_END", term_ends);
        intent.putExtra("BIOGUIDE", bioguide_id);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getDetailedScreen2(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailRep.class);

        TextView editText = (TextView) findViewById(R.id.name2);
        String message = editText.getText().toString();
        String term_ends = repsInfoForLater.get(2).get(2);
        String bioguide_id = repsInfoForLater.get(2).get(1);

        intent.putExtra("REP_NAME", message);
        intent.putExtra("TERM_END", term_ends);
        intent.putExtra("BIOGUIDE", bioguide_id);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getDetailedScreen3(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailRep.class);

        TextView editText = (TextView) findViewById(R.id.name3);
        String message = editText.getText().toString();
        String term_ends = repsInfoForLater.get(3).get(2);
        String bioguide_id = repsInfoForLater.get(3).get(1);

        intent.putExtra("REP_NAME", message);
        intent.putExtra("TERM_END", term_ends);
        intent.putExtra("BIOGUIDE", bioguide_id);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getDetailedScreen4(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailRep.class);

        TextView editText = (TextView) findViewById(R.id.name4);
        String message = editText.getText().toString();
        String term_ends = repsInfoForLater.get(4).get(2);
        String bioguide_id = repsInfoForLater.get(4).get(1);

        intent.putExtra("REP_NAME", message);
        intent.putExtra("TERM_END", term_ends);
        intent.putExtra("BIOGUIDE", bioguide_id);
        startActivity(intent);
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
