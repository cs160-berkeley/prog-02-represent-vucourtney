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

        // Value contains the String we sent over in WatchToPhoneService, "good job"
        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

        // Make a toast with the String
        Context context = getApplicationContext();

        String[] vals = value.split(";");
        Log.d("values passed: ", ((Integer) vals.length).toString());

        if (vals.length == 1) {
            Intent intent = new Intent(this, DetailRep.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("REP_NAME", vals[0]);

            Log.d("T", "about to start phone DetailView");
            startActivity(intent);
        } else if (vals.length > 1) {
            Intent intent = new Intent(this, CongressionalOverview.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("REP0_NAME", vals[0]);
            intent.putExtra("REP1_NAME", vals[1]);
            intent.putExtra("REP2_NAME", vals[2]);

            Log.d("T", "about to start phone CongressionalOverview");
            startActivity(intent);
        }

        // so you may notice this crashes the phone because it's
        //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
        // replace sending a toast with, like, starting a new activity or something.
        // who said skeleton code is untouchable? #breakCSconceptions

    }
}
