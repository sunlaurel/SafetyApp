package com.example.safetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallActivity extends AppCompatActivity {

    TextView title;
    Button startGirl;
    Button startGuy;
    Button returnCall;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);


        startGirl = (Button) findViewById(R.id.girlButton);
        startGuy = (Button) findViewById(R.id.guyButton);
        returnCall = (Button) findViewById(R.id.callReturnBtn);
        title = (TextView) findViewById(R.id.tvFakeCall);
        note = (TextView) findViewById(R.id.callTV);

        startGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CallActivity.this, FullscreenCallGirl.class);
                startActivity(in);
            }
        });

        startGuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CallActivity.this, FullscreenCallGuy.class);
                startActivity(in);
            }
        });

        returnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSounds();
            }
        });
    }

    public void openSounds()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SoundsFragment()).commit();
        title.setVisibility(View.GONE);
        startGirl.setVisibility(View.GONE);
        startGuy.setVisibility(View.GONE);
        returnCall.setVisibility(View.GONE);
        note.setVisibility(View.GONE);
        //Intent i = new Intent(CallActivity.this, SoundsFragment.class);
        //startActivity(i);
    }


}