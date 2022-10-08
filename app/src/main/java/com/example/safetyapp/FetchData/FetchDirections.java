package com.example.safetyapp.FetchData;

import android.content.Context;
import android.os.AsyncTask;

import com.example.safetyapp.DirectionHelper.PointsParser;

public class FetchDirections extends AsyncTask<String, Void, String> {

    String directions;
    Context context;

    public FetchDirections (Context c) {
        this.context = c;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            // Fetching the data from web service
            DownloadUrl downloadUrl = new DownloadUrl();
            directions = downloadUrl.retrieveUrl(strings[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directions;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            super.onPostExecute(s);
            PointsParser parserTask = new PointsParser(context);
            // Invokes the thread for parsing the JSON data
            parserTask.execute(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
