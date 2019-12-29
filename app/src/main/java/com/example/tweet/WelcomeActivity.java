package com.example.tweet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView userDetails;
    private ArrayList<String> users;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        userDetails = findViewById(R.id.userDetails);
        users = new ArrayList<>();
        adapter = new ArrayAdapter(WelcomeActivity.this,android.R.layout.simple_list_item_checked,users);
        userDetails.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        userDetails.setOnItemClickListener(this);
        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseUser username : objects){
                            users.add(username.getUsername());
                        }
                        userDetails.setAdapter(adapter);

                        for (String followers : users){
                            if (ParseUser.getCurrentUser().getList("following") != null){
                                if (ParseUser.getCurrentUser().getList("following").contains(followers)){
                                    userDetails.setItemChecked(users.indexOf(followers),true);
                                }
                            }
                        }

                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logoutUser:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(WelcomeActivity.this,SignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            case R.id.sendTweetButton:
                Intent intent = new Intent(WelcomeActivity.this,SendTweetActivity.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            Toast.makeText(this,users.get(position)+" is now followed",Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().add("following",users.get(position));
        } else {
            Toast.makeText(this,users.get(position)+" is now Unfollowed",Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().getList("following").remove(users.get(position));
            List currentUsersFollowing = ParseUser.getCurrentUser().getList("following");
            ParseUser.getCurrentUser().remove("following");
            ParseUser.getCurrentUser().put("following",currentUsersFollowing);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(WelcomeActivity.this,"Is now followed",Toast.LENGTH_LONG);
                }
            }
        });
    }
}
