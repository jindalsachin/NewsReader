package com.example.android.newsapp.utils;

import android.text.TextUtils;
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

import com.example.android.newsapp.News;


public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> gardianNews = extractFeatureFromJson(jsonResponse);
        return gardianNews;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(Constants.READ_TIMEOUT);
            urlConnection.setConnectTimeout(Constants.CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(Constants.REQUEST_METHOD_GET);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == Constants.SUCCESS_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the The Gardian news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONObject responseJsonObject = baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE);
            JSONArray newsArray = responseJsonObject.getJSONArray(Constants.JSON_KEY_RESULTS);
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String webTitle = currentNews.getString(Constants.JSON_KEY_WEB_TITLE);
                String url = currentNews.getString(Constants.JSON_KEY_WEB_URL);
                String sectionName = currentNews.getString(Constants.JSON_KEY_SECTION_NAME);
                String webPublicationDate = currentNews.getString(Constants.JSON_KEY_WEB_PUBLICATION_DATE);
                JSONObject fields = currentNews.getJSONObject(Constants.JSON_KEY_FIELDS);
                String author = fields.optString(Constants.JSON_KEY_BYLINE);
                String thumbnail = fields.getString(Constants.JSON_KEY_THUMBNAIL);
                News news = new News(webTitle, sectionName, author, webPublicationDate, url, thumbnail);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return newsList;
    }
}