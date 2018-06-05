package com.example.user.newsapp;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
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

public final class QueryUtils
{

    private static final String LOG_TAG =QueryUtils.class.getName();


    public  static List<News> fetchNews(String requestURL)
    {
        URL url = createURL(requestURL);

        String jsonResponse = "";
        try
        {
            jsonResponse = makeHTTPrequest(url);
        } catch (IOException e)
        {
            Log.e(LOG_TAG, "Couldn't make the HTTP Connection", e);
        }
        ArrayList<News> newsArrayList = extractNewsItems(jsonResponse);


        return newsArrayList;
    }


    private static ArrayList<News> extractNewsItems(String jsonResponse)
    {

        if(TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }
        ArrayList<News> newsItems = new ArrayList<>();
        try
        {
            JSONObject baseJSONResponse = new JSONObject(jsonResponse);
            JSONObject response = baseJSONResponse.getJSONObject("response");
            JSONArray newsItemArray = response.getJSONArray("results");
            for (int i = 0; i < newsItemArray.length(); i++)
            {
                JSONObject currentNews = newsItemArray.getJSONObject(i);
                String sectionName = currentNews.getString("sectionName");
                String webTitle = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                String mUrl = currentNews.getString("webUrl");
                News newsItem = new News(sectionName, webTitle, date,mUrl);
                newsItems.add(newsItem);

            }


        } catch (JSONException e)
        {
            Log.e(LOG_TAG,"error parsing JSON",e);
        }
        return newsItems;

    }

    private static String makeHTTPrequest(URL url) throws IOException
    {
        String jsonResponse = "";
        if (url == null)
        {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else
            {
                Log.e(LOG_TAG, "Error Code:" + urlConnection.getResponseCode());

            }
        } catch (IOException e)
        {
            Log.e(LOG_TAG,"Internet Connection Issue",e);
        } finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (inputStream != null)
            {
                inputStream.close();
            }

        }

        return jsonResponse;
    }

        @NonNull
        private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null)
            {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();

    }

    private static URL createURL(String newsRequestUrl)
    {
        URL url = null;
        try
        {
            url = new URL(newsRequestUrl);
        } catch (MalformedURLException e)
        {
            Log.e(LOG_TAG,"URL could not be created",e);

        }
        return url;

    }


}










