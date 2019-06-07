package com.websarva.wings.android.mytwitterapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterCore;

public class MainActivity extends AppCompatActivity implements TwitterAPI.TwitterAPIListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterAPI twitterAPI = new TwitterAPI();
        twitterAPI.TwitterAPIListener(MainActivity.this);


        if(TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
            Toast toast = Toast.makeText(MainActivity.this, "ログイン画面に移動します。", Toast.LENGTH_LONG);
            toast.show();
            Intent intemt = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intemt);
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "ログイン中", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPostExecuted() {

    }
}
