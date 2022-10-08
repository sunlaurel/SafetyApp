package com.example.safetyapp.FetchData;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUrl {

    public String retrieveUrl(String url) throws IOException {
        String urlData ="";
        HttpURLConnection http = null;
        InputStream inputStream = null;
        try {
            URL getUrl = new URL(url);
            http = (HttpURLConnection) getUrl.openConnection();
            http.connect();

            inputStream = http.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            urlData = sb.toString();
            Log.d("downloadURL", urlData);
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            inputStream.close();
            http.disconnect();
        }
        return urlData;
    }
}

