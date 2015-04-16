package com.chadgolden.sleeptrack.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chadgolden.sleeptrack.R;
import com.chadgolden.sleeptrack.web.WebServerConnection;

public class LoginActivity extends ActionBarActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewLoginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewLoginStatus = (TextView)findViewById(R.id.textViewLoginStatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void verifyLogin(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()) {
            if (!WebServerConnection.loginPost(email, password)) {
                textViewLoginStatus.setVisibility(View.VISIBLE);
                textViewLoginStatus.setText("Invalid username or password combination.");
                textViewLoginStatus.setTextColor(Color.RED);
                return;
            }
        } else {
            textViewLoginStatus.setVisibility(View.VISIBLE);
            textViewLoginStatus.setText("Both fields are required.");
            textViewLoginStatus.setTextColor(Color.RED);
            return;
        }
        textViewLoginStatus.setVisibility(View.VISIBLE);
        textViewLoginStatus.setText("Login Successful!");
        textViewLoginStatus.setTextColor(Color.GREEN);

    }
}
