package com.example.safetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LaughActivity extends AppCompatActivity {

    Button manLaughStart;
    Button womanLaughStart;
    Button manLaughEnd;
    Button womanLaughEnd;
    Button returnLaugh;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laugh);

        final MediaPlayer manLaughMP = MediaPlayer.create(this, R.raw.man_laugh);
        final MediaPlayer womanLaughMP = MediaPlayer.create(this, R.raw.woman_laugh);

        manLaughStart = (Button) findViewById(R.id.manLaughStartBtn);
        womanLaughStart = (Button) findViewById(R.id.womanLaughStartBtn);
        manLaughEnd = (Button) findViewById(R.id.manLaughEndBtn);
        womanLaughEnd = (Button) findViewById(R.id.womanLaughEndBtn);
        returnLaugh = (Button) findViewById(R.id.laughReturnBtn);
        note = (TextView) findViewById(R.id.tvFakeLaughterNote);

        manLaughStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manLaughMP.start();
            }
        });

        manLaughEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manLaughMP.stop();
            }
        });

        womanLaughStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                womanLaughMP.start();
            }
        });

        womanLaughEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                womanLaughMP.stop();
            }
        });

        returnLaugh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSounds();
            }
        });
    }
    public void openSounds ()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SoundsFragment()).commit();
        manLaughStart.setVisibility(View.GONE);
        womanLaughStart.setVisibility(View.GONE);
        manLaughEnd.setVisibility(View.GONE);
        womanLaughEnd.setVisibility(View.GONE);
        returnLaugh.setVisibility(View.GONE);
        note.setVisibility(View.GONE);
    }
}