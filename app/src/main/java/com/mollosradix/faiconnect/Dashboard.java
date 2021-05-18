package com.mollosradix.faiconnect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class Dashboard extends AppCompatActivity {

    private EditText secret_code, full_name;
    private ImageView send;
    private Button start_meeting, join_meeting;

    String secret = "";

    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();

        secret_code = findViewById(R.id.secret_code);
        send = findViewById(R.id.share_code);
        start_meeting = findViewById(R.id.start_meeting);
        join_meeting = findViewById(R.id.join_meeting);

        Intent intent = getIntent();
        String intentUrl = intent.getStringExtra("url");

        if(intentUrl != null){
           secret_code.setText(intentUrl);
        }

        URL serverUrl;

        try {
            serverUrl = new URL("https://meet.jit.si");

            JitsiMeetConferenceOptions defaultoptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverUrl)
                    .setWelcomePageEnabled(false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultoptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                secret = secret_code.getText().toString();

                if(secret.equals("")){
                    Toast.makeText(Dashboard.this, "Secret Code is Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, secret);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
            }
        });



        join_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                secret = secret_code.getText().toString();

                if (secret.equals("") ) {
                    secret_code.setError("This fields are required");
                }
                else if(secret.startsWith("https://meet.jit.si/")){
                    secret = secret.substring(20);
                    startConference(secret);
                }else if(secret.startsWith("https://meet.jit.si/static/")) {
                    secret = secret.substring(48);
                    startConference(secret);
                } else if(!secret.equals("")){
                    startConference(secret);
                }
            }
        });

        start_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                secret = secret_code.getText().toString();

                if(secret.equals("")){
                    secret = UUID.randomUUID().toString();
                    secret_code.setText(secret);
                    startConference(secret);
                } else if(secret.startsWith("https://meet.jit.si/")){
                    secret = secret.substring(20);
                    startConference(secret);
                } else if(secret.startsWith("https://meet.jit.si/static/")) {
                    secret = secret.substring(48);
                    startConference(secret);
                }
                else if(!secret.equals("")){
                    startConference(secret);
                }
            }
        });
    }

    private void startConference(String secret_id) {
        JitsiMeetConferenceOptions options = null;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            options = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(secret_id)
                    .setWelcomePageEnabled(false)
                    .setFeatureFlag("pip.enabled",false)
                    .build();
        }
        JitsiMeetActivity.launch(Dashboard.this, options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            SharedPreferences.setLoggedIn(getApplicationContext(), false);
            finish();
            startActivity(new Intent(Dashboard.this, Login.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        start_meeting.setEnabled(false);
        join_meeting.setEnabled(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        start_meeting.setEnabled(true);
        join_meeting.setEnabled(true);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        start_meeting.setEnabled(true);
        join_meeting.setEnabled(true);
    }


}