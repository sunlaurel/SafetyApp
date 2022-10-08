package com.example.safetyapp;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.safetyapp.databinding.ActivityMainBinding;
import com.example.safetyapp.ui.call.CallFragment;
import com.example.safetyapp.ui.map.MapFragment;
import com.example.safetyapp.ui.sounds.SoundsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_sound, R.id.navigation_call)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    MapFragment mapFragment = new MapFragment();
    CallFragment callFragment = new CallFragment();
    SoundsFragment soundFragment = new SoundsFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
                return true;

            case R.id.navigation_sound:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, soundFragment).commit();
                return true;

            case R.id.navigation_call:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, callFragment).commit();
                return true;
        }
        return false;
    }

}