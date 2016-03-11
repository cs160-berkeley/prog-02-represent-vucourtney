package com.example.courtneyvu.electme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;

public class TweetScreen extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "ToEj9AHqiq4cHAPnplkJheP9i";
    private static final String TWITTER_SECRET = "8AwrB6K5LEDswrQWJeRJAR0JYaS3V3QMHIChSQ3KIYVrRigzA7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_screen);

        Intent intent = getIntent();
        String name = intent.getStringExtra("REP_NAME");
        String twitter_username = intent.getStringExtra("TWITTER_USERNAME");

        ListView tweetView = (ListView) findViewById(R.id.tweet_display);
        TextView textView = (TextView) findViewById(R.id.rep_name);

        textView.setText(name);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(twitter_username)
                .maxItemsPerRequest(1)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        tweetView.setAdapter(adapter);

    }

}
