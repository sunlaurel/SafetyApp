package com.example.safetyapp.FetchData;

import android.os.AsyncTask;

import com.example.safetyapp.LocationService;
import com.example.safetyapp.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchDataNearby extends AsyncTask<Object, String, String> {

    String googleNearByPlacesData;
    GoogleMap gMap;
    String url;

    @Override
    protected String doInBackground(Object...objects) {
        try {
            gMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googleNearByPlacesData = downloadUrl.retrieveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleNearByPlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object1 = jsonArray.getJSONObject(i);

                String place_id = object1.getString("place_id");

                JSONObject getLocation = object1.getJSONObject("geometry")
                        .getJSONObject("location");

                String lat = getLocation.getString("lat");
                String lng = getLocation.getString("lng");

                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                Marker options = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(object1.getString("name")));
                options.setTag(place_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void getNextPage(JSONObject obj) {
        try {
            String nextPage = obj.getString("next_page_token");
            Object[] dataFetch = {MapFragment.getGMap(), new LocationService().
                    getUrlPlacesNear(LocationService.getLatLng()) + "&pageToken=" + nextPage};
            FetchDataNearby fetchData = new FetchDataNearby();
            fetchData.execute(dataFetch);
        } catch (Exception ignored) {

        }
    }
}

