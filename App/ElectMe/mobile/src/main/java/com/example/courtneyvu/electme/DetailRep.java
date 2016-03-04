package com.example.courtneyvu.electme;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailRep extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rep);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String name = intent.getStringExtra("REP_NAME");
            TextView textView = (TextView) findViewById(R.id.rep_name);
            textView.setText(name);
        }

    }

}
