package com.example.tweet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signUN,signEID,signCP;
    private TextView login;
    private Button signUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sign up");
        setContentView(R.layout.activity_sign_up);
        signUN = findViewById(R.id.signUN);
        signEID = findViewById(R.id.signEID);
        signCP = findViewById(R.id.signCP);
        signUP = findViewById(R.id.SignUp);
        signCP.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                    onClick(signUP);
                return false;
            }
        });
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
        signUP.setOnClickListener(SignUpActivity.this);
        if (ParseUser.getCurrentUser() != null){
            transitionActivity();
        }

    }

    @Override
    public void onClick(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Uploading.....");
        progressDialog.show();
        ParseUser tweetUser = new ParseUser();
        tweetUser.setUsername(signUN.getText().toString());
        tweetUser.setEmail(signEID.getText().toString());
        tweetUser.setPassword(signCP.getText().toString());
        if (isValidPassword(signCP.getText().toString())){
            tweetUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Intent intent = new Intent(SignUpActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignUpActivity.this,"Sign up Successfully",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this,"....Unsuccessfully....",Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });
    } else {
            Toast.makeText(SignUpActivity.this,"Please Enter valid password",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,12}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public void hideKeyboard(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    private void transitionActivity(){
        Intent intent = new Intent(SignUpActivity.this,WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
