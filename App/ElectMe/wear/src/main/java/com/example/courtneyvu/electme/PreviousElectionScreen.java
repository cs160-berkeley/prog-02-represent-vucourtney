package com.example.courtneyvu.electme;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class PreviousElectionScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_election_screen);
        TextView district = (TextView) findViewById(R.id.district_name);
        TextView obama_percent = (TextView) findViewById(R.id.obama_vote_percentage);
        TextView romney_percent = (TextView) findViewById(R.id.romney_vote_percentage);

        Intent intent = getIntent();
        int zipCode = intent.getIntExtra("ZIP_CODE", 0);

        /* the following is to randomly change the 2012 election data, will be replaced with API code */
        if (zipCode % 4 == 0) {
            district.setText(R.string.district_example);
            obama_percent.setText("51%");
            romney_percent.setText("48%");
        } else if (zipCode % 4 == 1) {
            district.setText(R.string.district_example1);
            obama_percent.setText("44%");
            romney_percent.setText("52%");
        } else if (zipCode % 4 == 2) {
            district.setText(R.string.district_example2);
            obama_percent.setText("61%");
            romney_percent.setText("30%");
        } else {
            district.setText(R.string.district_example3);
            obama_percent.setText("30%");
            romney_percent.setText("62%");
        }
    }

}
