 package com.example.news_api;
import android.preference.ListPreference;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by moham on 10/2/2017.
 */

public class Query {

    public static List<news> fetchNewsData(String stringUrl) {
        if(stringUrl == null) {
            return null;
        }

        URL url = createUrl(stringUrl);
        String jsonResponse = makeHttpRequest(url);

        List<news> newsList = extractDataFromJson(jsonResponse);

        return newsList;
    }

    //Recieves a string and returns a url
    private static URL createUrl(String stringUrl) {
        URL url = null;
        if (stringUrl == null) {
            return null;
        }

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    private static String makeHttpRequest(URL url) {
        HttpURLConnection urlConnection = null;
        String jsonResponse = "";
        InputStream inputStream = null;

        if(url == null) {
            return null;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "makeHttpRequest: "+jsonResponse);
        return jsonResponse;
    }

    private static String readFromStream (InputStream inputStream) {
        InputStreamReader streamReader = null;
        StringBuilder result = new StringBuilder() ;
        BufferedReader bufferedReader = null;

        streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        bufferedReader = new BufferedReader(streamReader);

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                result.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private static List<news> extractDataFromJson (String jsonResponse) {

        List<news> newsList = new ArrayList<>();
        try {

            JSONObject json = new JSONObject(jsonResponse);
            JSONObject response = json.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < 10; i++) {

                JSONObject currentNews = results.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");
                String type = currentNews.getString("type");

                news newsObject = new news(title, date, url, title, section);
                newsList.add(newsObject);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}
