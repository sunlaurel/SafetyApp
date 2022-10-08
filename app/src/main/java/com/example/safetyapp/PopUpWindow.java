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
import com.example.safetyapp.ui.map.MapFragment;
import com.google.android.gms.maps.model.Marker;

public class PopUpWindow extends Activity {

    static TextView tvName;
    static TextView tvOpenHour;
    static TextView tvAddress;
    static TextView tvPhone;
    Button startDirection;
    Marker m;
    Context context;

    public static TextView getTvName() {
        return tvName;
    }

    public static TextView getTvOpenHour() {
        return tvOpenHour;
    }

    public static TextView getTvAddress() {
        return tvAddress;
    }

    public static TextView getTvPhone() {
        return tvPhone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window);

        tvName = (TextView) findViewById(R.id.tvName);
        tvOpenHour = (TextView) findViewById(R.id.tvOpenHour);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        startDirection = (Button) findViewById(R.id.btStartDirection);
        m = MapFragment.getClicked();

        startDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //draw the polyline and zoom out
                //Or zoom in and start directions
                String[] d = {getDirectionsUrl()};
                System.out.println("Directions url: " + d[0]);
                FetchDirections fetchDirection = new FetchDirections(MapFragment.getFragmentContext());
                System.out.println("starting directions");
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
