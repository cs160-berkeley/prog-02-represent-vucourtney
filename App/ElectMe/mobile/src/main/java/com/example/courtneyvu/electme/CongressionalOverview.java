package com.example.courtneyvu.electme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CongressionalOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_overview);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String zipCode = intent.getStringExtra("ZIP_CODE");

            if (extras.keySet().size() > 1) {
                TextView name1 = (TextView) findViewById(R.id.name1);
                TextView name2 = (TextView) findViewById(R.id.name2);
                TextView name3 = (TextView) findViewById(R.id.name3);

                ImageButton pic1 = (ImageButton) findViewById(R.id.pic1);
                ImageButton pic2 = (ImageButton) findViewById(R.id.pic2);
                ImageButton pic3 = (ImageButton) findViewById(R.id.pic3);

                name1.setText(intent.getStringExtra("REP0_NAME"));
                name1.setText(intent.getStringExtra("REP1_NAME"));
                name1.setText(intent.getStringExtra("REP2_NAME"));

                pic1.setImageResource(Integer.parseInt(intent.getStringExtra("PHOTO0")));
                pic2.setImageResource(Integer.parseInt(intent.getStringExtra("PHOTO1")));
                pic3.setImageResource(Integer.parseInt(intent.getStringExtra("PHOTO2")));
            }
        }

    }

    /** Called when the user clicks the View Tweet button */
    public void getTweetRep1(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TweetScreen.class);
        TextView editText = (TextView) findViewById(R.id.name1);
        String message = editText.getText().toString();
        intent.putExtra("REP_NAME", message);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getTweetRep2(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TweetScreen.class);
        TextView editText = (TextView) findViewById(R.id.name2);
        String message = editText.getText().toString();
        intent.putExtra("REP_NAME", message);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getTweetRep3(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, TweetScreen.class);
        TextView editText = (TextView) findViewById(R.id.name3);
        String message = editText.getText().toString();
        intent.putExtra("REP_NAME", message);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getDetailedScreen1(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailRep.class);
        TextView editText = (TextView) findViewById(R.id.name1);
        String message = editText.getText().toString();
        intent.putExtra("REP_NAME", message);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getDetailedScreen2(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailRep.class);
        TextView editText = (TextView) findViewById(R.id.name2);
        String message = editText.getText().toString();
        intent.putExtra("REP_NAME", message);
        startActivity(intent);
    }

    /** Called when the user clicks the View Tweet button */
    public void getDetailedScreen3(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailRep.class);
        TextView editText = (TextView) findViewById(R.id.name3);
        String message = editText.getText().toString();
        intent.putExtra("REP_NAME", message);
        startActivity(intent);
    }

}
