package com.example.safetyapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safetyapp.ui.sounds.SoundsFragment;

public class DogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog);

        final MediaPlayer dogMp = MediaPlayer.create(this, R.raw.dogs_barking);

        Button startDogs = (Button) findViewById(R.id.dogStartBtn);
        Button pauseDogs = (Button) findViewById(R.id.dogPauseBtn);
        Button returnDogs = (Button) findViewById(R.id.dogReturnBtn);

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
        Intent intent = new Intent(this, SoundsFragment.class);
        startActivity(intent);
    }
}