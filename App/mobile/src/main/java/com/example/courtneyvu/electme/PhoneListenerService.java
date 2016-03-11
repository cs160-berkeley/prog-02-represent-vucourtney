package com.example.courtneyvu.electme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());

        // Value contains either name/bioguide/term end from requesting detailed view, or just a signal that the watch shook
        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

        // Make a toast with the String
        Context context = getApplicationContext();

        String[] vals = value.split("%");
        Log.d("values passed: ", ((Integer) vals.length).toString());
        Log.d("value:", vals[0]);

        if (vals.length == 3) {
            Intent intent = new Intent(this, DetailRep.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("REP_NAME", vals[0]);
            intent.putExtra("BIOGUIDE", vals[1]);
            intent.putExtra("TERM_END", vals[2]);

            Log.d("T", "about to start phone DetailView");
            startActivity(intent);
        } else if (vals.length == 1 && !vals[0].equals("")) {
            Intent intent = new Intent(this, HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("SIGNAL", vals[0]);

            Log.d("T", "about to start phone HomeScreen to retrieve new location");
            startActivity(intent);
        }

    }
}
