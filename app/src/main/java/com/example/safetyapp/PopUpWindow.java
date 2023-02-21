package com.example.safetyapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.safetyapp.FetchData.FetchDirections;
import com.example.safetyapp.FetchData.FetchPlaceDetail;
import com.google.android.gms.maps.model.Marker;

public class PopUpWindow extends Activity {

    static TextView tvInfo;
    static TextView tvName;
    Button startDirection;
    Marker m;
    Context context;

    public static TextView getTvInfo() { return tvInfo; }
    public static TextView getTvName() { return tvName; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvName = (TextView) findViewById(R.id.tvName);
        startDirection = (Button) findViewById(R.id.btStartDirection);
        m = MapFragment.getClicked();

        startDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //draw the polyline and zoom out
                //Or zoom in and start directions
                String[] d = {getDirectionsUrl()};
                FetchDirections fetchDirection = new FetchDirections(MainActivity.getMapFragment());
                fetchDirection.execute(d);

                finish();
            }
        });

        String place_id = String.valueOf(m.getTag());
        Object[] placeDetail = {getPlaceDetailUrl(place_id)};
        FetchPlaceDetail fetchDetail = new FetchPlaceDetail();
        fetchDetail.execute(placeDetail);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.5), (int)(height * 0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }


    private String getPlaceDetailUrl(String placeID) {
        return "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeID +
                "&key=AIzaSyDDC-v9k517IDBrG_j1ajtr9rNBk8myG9U" +
                "&fields=name%2Cformatted_phone_number%2Copening_hours%2Cformatted_address";
    }

    private String getDirectionsUrl() {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=" + LocationService.getLatLng().latitude
                + "," + LocationService.getLatLng().longitude + "&destination=" + m.getPosition().latitude
                + "," + m.getPosition().longitude + "&key=AIzaSyDDC-v9k517IDBrG_j1ajtr9rNBk8myG9U"
                + "&mode=walking";
    }
}
