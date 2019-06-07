package com.websarva.wings.android.mytwitterapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class TimelineActivity extends AppCompatActivity {

    ListView listView;
    List<Tweet> tweetList = new ArrayList<>();
    TweetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView = (ListView)findViewById(R.id.my_list_view);
        adapter = new TweetAdapter(this, tweetList);

        listView.setAdapter(adapter);

        getHomeTimeline();

    }

    private void getHomeTimeline() {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

        StatusesService statusesService = twitterApiClient.getStatusesService();

        Call<List<Tweet>> call = statusesService.homeTimeline(20,null,null,false,false,false,false);
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                // ListViewのListに取得したツイートリストを追加
                tweetList.addAll(result.data);
                // ListViewの表示を更新
                adapter.notifyDataSetChanged();

                Toast toast = Toast.makeText(TimelineActivity.this, "タイムライン取得成功", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast toast = Toast.makeText(TimelineActivity.this, "タイムライン取得失敗", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

}
