package com.example.safetyapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

public class DogActivity extends AppCompatActivity {

    TextView tvBark;
    Button startDogs;
    Button pauseDogs;
    Button returnDogs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog);

        final MediaPlayer dogMp = MediaPlayer.create(this, R.raw.dogs_barking);

        tvBark = (TextView) findViewById(R.id.tvDogBark);
        startDogs = (Button) findViewById(R.id.dogStartBtn);
        pauseDogs = (Button) findViewById(R.id.dogPauseBtn);
        returnDogs = (Button) findViewById(R.id.dogReturnBtn);

        startDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dogMp.start();
            }
        });
        pauseDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dogMp.pause();
            }
        });
        returnDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSounds();
            }
        });
    }

    public void openSounds()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SoundsFragment()).commit();
        tvBark.setVisibility(View.GONE);
        startDogs.setVisibility(View.GONE);
        pauseDogs.setVisibility(View.GONE);
        returnDogs.setVisibility(View.GONE);
    }
}