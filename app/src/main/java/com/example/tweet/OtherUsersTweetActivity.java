package com.example.tweet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherUsersTweetActivity extends AppCompatActivity {

    private ListView viewTweetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users_tweet);
        viewTweetList = findViewById(R.id.viewTweetList);
        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(OtherUsersTweetActivity.this,tweetList,android.R.layout.simple_list_item_2,new String[]{"tweetUserName","tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});
        try{
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("UsersTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("following"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseObject tweeetObject : objects){
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName",tweeetObject.getString("user"));
                            userTweet.put("tweetValue",tweeetObject.getString("tweet"));
                            tweetList.add(userTweet);
                        }
                        viewTweetList.setAdapter(adapter);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
