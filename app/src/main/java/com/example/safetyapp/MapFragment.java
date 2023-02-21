package com.example.safetyapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.safetyapp.Constants;
import com.example.safetyapp.DirectionHelper.TaskLoadedCallback;
import com.example.safetyapp.LocationService;
import com.example.safetyapp.PopUpWindow;
import com.example.safetyapp.R;
import com.example.safetyapp.databinding.FragmentMapBinding;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, TaskLoadedCallback {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    SupportMapFragment mp;
    static GoogleMap gMap;
    GoogleMapOptions options;
    static Marker yourLocation;
    static Context context;
    private Polyline currentPolyline;
    Button btStartDirections;

    private FragmentMapBinding binding;

    MapFragment () {
        options = new GoogleMapOptions();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //View view = inflater.inflate(R.layout.activity_main, container, false);
        //View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        btStartDirections = (Button) root.getRootView().findViewById(R.id.btStartGoogleMapDirections);

        mp = SupportMapFragment.newInstance();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.googleMap, mp)
                .commit();

        context = getActivity().getApplicationContext();

        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .scrollGesturesEnabled(true);

        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }

        mp.getMapAsync(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static Context getFragmentContext() {
        return context;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            for (ActivityManager.RunningServiceInfo service :
                    am.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getActivity(), LocationService.class);

            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            getActivity().startService(intent);
        }
    }

    private void stopLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getActivity(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            getActivity().startService(intent);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
    }

    static Marker clicked;
    @Override
    public boolean onMarkerClick(final Marker marker) {

        try {
            if (!Objects.equals(marker.getTag(), 0)) {
                clicked = marker;
                Intent i = new Intent(context, PopUpWindow.class);
                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
    }

    public static Marker getClicked() {
        return clicked;
    }

    public static GoogleMap getGMap() {
        return gMap;
    }

    public static Marker getMarkerLoc() {
        return yourLocation;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        try {
            currentPolyline = gMap.addPolyline((PolylineOptions) values[0]);
            LatLngBounds b = new LatLngBounds(LocationService.getLatLng(), getClicked().getPosition());
            gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(b, 40));
        } catch (Exception ignored) {
        }

        btStartDirections.setVisibility(View.VISIBLE);
        btStartDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mp.getActivity());
                builder.setMessage("Open Google Maps?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                String latitude = String.valueOf(getClicked().getPosition().latitude);
                                String longitude = String.valueOf(getClicked().getPosition().longitude);
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");

                                try{
                                    if (mapIntent.resolveActivity(mp.getActivity().getPackageManager()) != null) {
                                        startActivity(mapIntent);
                                    }
                                } catch (NullPointerException e){
                                    Toast.makeText(mp.getActivity(), "Couldn't open map", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}