package com.example.user.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.support.annotation.Nullable;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader
{
    private static final String LOG_TAG = NewsLoader.class.getName();
    private  static String mURL;

    public NewsLoader(Context context, String newsRequestUrl)
    {
        super(context);
        mURL = newsRequestUrl;

    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground()
    {
        if(mURL==null)
        {
            return null;
        }

        List<News> newsList = QueryUtils.fetchNews(mURL);
        return  newsList;

    }
}
