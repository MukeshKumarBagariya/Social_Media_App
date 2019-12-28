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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView signUp;
    private EditText loginUN,loginUP;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("LogIn");
        loginUN = findViewById(R.id.loginUN);
        loginUP = findViewById(R.id.loginUP);
        login = findViewById(R.id.login);
        loginUP.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                    onClick(login);
                return false;
            }
        });
        login.setOnClickListener(LogInActivity.this);
        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(LogInActivity.this);
        progressDialog.setMessage("Logging you in");
        progressDialog.show();
        if (isPasswordValid(loginUP.getText().toString())) {
            ParseUser.logInInBackground(loginUN.getText().toString(), loginUP.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null && user != null) {
                        Toast.makeText(LogInActivity.this, "LoggedIn Successfully.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LogInActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });

        } else {
            Toast.makeText(LogInActivity.this,"Please Enter Valid Password",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
    public boolean isPasswordValid(final String password){
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,12}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public void hideSoftKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
