package com.example.safetyapp.FetchData;

import android.os.AsyncTask;

import com.example.safetyapp.PopUpWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchPlaceDetail extends AsyncTask<Object, String, String> {

    String googlePlaceDetails;
    String url;
    String address;
    String phone;
    JSONArray openHours;

    @Override
    protected String doInBackground(Object...objects) {
        try {
            url = (String) objects[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlaceDetails = downloadUrl.retrieveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlaceDetails;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject result = jsonObject.getJSONObject("result");

            PopUpWindow.getTvName().setText(result.getString("name"));

            address = result.getString("formatted_address");
            phone = result.getString("formatted_phone_number");
            openHours = result.getJSONObject("opening_hours").getJSONArray("weekday_text");

            for (int i = 0; i < openHours.length(); i++) {
                try {
                    PopUpWindow.getTvInfo().append(openHours.get(i) + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            PopUpWindow.getTvInfo().append("\n\n");

            PopUpWindow.getTvInfo().append(address + "\n\n");
            PopUpWindow.getTvInfo().append(phone + "\n\n");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
