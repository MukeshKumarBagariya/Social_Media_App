package com.example.tweet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SendTweetActivity extends AppCompatActivity {

    private EditText tweetTextArea;
    private Button sendTweet;
    private TextView othersTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
        tweetTextArea = findViewById(R.id.tweetTextArea);
        sendTweet = findViewById(R.id.sendTweet);
        othersTweet = findViewById(R.id.othersTweet);
        othersTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendTweetActivity.this,OtherUsersTweetActivity.class);
                startActivity(intent);
            }
        });
    }
    public void sendTweets(View view){
        ParseObject parseObject = new ParseObject("UsersTweet");
        parseObject.put("tweet",tweetTextArea.getText().toString());
        parseObject.put("user",ParseUser.getCurrentUser().getUsername());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending.....");
        progressDialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(SendTweetActivity.this,ParseUser.getCurrentUser().getUsername()+"'s Tweet received successfully",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SendTweetActivity.this,"OOPs Something went wrong",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}
