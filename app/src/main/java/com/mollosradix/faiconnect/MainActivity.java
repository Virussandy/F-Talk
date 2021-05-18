package com.mollosradix.faiconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relativeLayout1,relativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout1 = findViewById(R.id.relative1);
        relativeLayout2 = findViewById(R.id.relative2);

        Intent intent = getIntent();
        String intentUrl = intent.getDataString();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayout1.setVisibility(View.GONE);
                relativeLayout2.setVisibility(View.VISIBLE);
            }
        },3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent newIntent = new Intent(getApplicationContext(),Login.class);
                newIntent.putExtra("url",intentUrl);
                startActivity(newIntent);
                finish();
            }
        },5000);
    }
}