package com.example.courtneyvu.electme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class HomeScreen extends Activity {

    private Button getLocFromZip;
    private Button getLocDirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getLocFromZip = (Button) findViewById(R.id.zip_button);
        getLocDirect = (Button) findViewById(R.id.loc_button);

        getLocFromZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                EditText editText = (EditText) findViewById(R.id.zip_entry);
                String zipCode = editText.getText().toString();
                sendIntent.putExtra("ZIP_CODE", zipCode);
                findReps(v, zipCode);
                startService(sendIntent);
            }
        });

        getLocDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                //random location, replace with API
                Integer randomLoc = (int) (Math.random()*99999);
                sendIntent.putExtra("ZIP_CODE", randomLoc.toString());
                findReps(v, randomLoc.toString());
                startService(sendIntent);
            }
        });
    }

    /** Called when the user clicks the GO! button */
    public void findReps(View view, String loc) {
        // Do something in response to button
        Intent intent = new Intent(this, CongressionalOverview.class);
        intent.putExtra("ZIP_CODE", loc);
        startActivity(intent);
    }
}
