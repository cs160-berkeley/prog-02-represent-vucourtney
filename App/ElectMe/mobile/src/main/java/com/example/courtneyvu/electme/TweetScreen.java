package com.example.courtneyvu.electme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TweetScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_screen);

        Intent intent = getIntent();
        String message = intent.getStringExtra("REP_NAME");
        TextView textView = (TextView) findViewById(R.id.rep_name);
        textView.setText(message);

    }

    /** Called when the user clicks the Back button */
    public void returnToCongressionalOverview(View view) {
        Intent intent = new Intent(this, CongressionalOverview.class);
        startActivity(intent);
    }

}
