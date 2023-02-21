package com.example.safetyapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.safetyapp.FetchData.FetchDataNearby;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationService extends Service {
    private static LatLng latLng = new LatLng(0, 0);
    private static GoogleMap googleMap;
    private static Marker yourLocation = MapFragment.getMarkerLoc();
    private static int counter = 75;

    protected LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                latLng = new LatLng(locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude());

                googleMap = MapFragment.getGMap();
                if (counter >= 75) {
                    googleMap.clear();
                    Object[] dataFetch = {googleMap, getUrlPlacesNear(LocationService.getLatLng())};
                    FetchDataNearby fetchData = new FetchDataNearby();
                    fetchData.execute(dataFetch);
                    counter = 0;
                }

                if (googleMap != null) {
                    if (yourLocation == null) {
                        MarkerOptions options = new MarkerOptions().position(LocationService.latLng)
                                .title("You're here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .snippet(LocationService.latLng.latitude + ", " + LocationService.latLng.longitude);
                        yourLocation = googleMap.addMarker(options);
                        yourLocation.setTag(0);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationService.latLng, 13));
                    } else {
                        yourLocation.setPosition(latLng);
                    }
                }
            }
            counter++;
        }
    };

    public static LatLng getLatLng() {
        return latLng;
    }


    public String getUrlPlacesNear(LatLng loc) {
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "keyword=restaurant|police|hospital|store|gas_station|lodging&" +
                "open_now=true&radius=6400&rank_by=distance&location=" +
                loc.latitude + "," + loc.longitude +
                "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), channelId);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("Location service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (nM != null && nM.getNotificationChannel(channelId) == null) {
                NotificationChannel nC = new NotificationChannel(
                        channelId, "Location Service",
                        NotificationManager.IMPORTANCE_HIGH);
                nC.setDescription("This channel is used by location service");
                nM.createNotificationChannel(nC);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback,
                        Looper.getMainLooper());
        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                } else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                }

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
