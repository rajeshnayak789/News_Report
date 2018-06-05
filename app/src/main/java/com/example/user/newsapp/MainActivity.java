package com.example.user.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>>
{
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String NEWS_REQUEST_URL =
            "http://www.json-generator.com/api/json/get/bOuMCxrkEi?indent=2";

    private NewsAdapter customNewsAdapter;
    private TextView emptyView;
    SwipeRefreshLayout mSwipeRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsListView = (ListView) findViewById(R.id.list);

        customNewsAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(customNewsAdapter);
        emptyView = findViewById(R.id.empty_text);
        newsListView.setEmptyView(emptyView);

        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        mSwipeRefreshView = findViewById(R.id.swipe);


        if (networkInfo != null && networkInfo.isConnected())
        {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, MainActivity.this);
            mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    mSwipeRefreshView.setRefreshing(false);
                }
            });
        } else
        {
            emptyView.setText("Internet Connection Not Found");
            mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    mSwipeRefreshView.setRefreshing(true);
                    if (networkInfo != null && networkInfo.isConnected())
                    {
                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.initLoader(1, null, MainActivity.this);

                    } else
                    {
                        mSwipeRefreshView.setRefreshing(false);
                        customNewsAdapter.clear();
                        emptyView.setText("Internet Connection Not Found");
                    }
                }
            });

        }


        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                News currentNews = (News) customNewsAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentNews.getUrl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(browserIntent);
            }
        });


    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle)
    {
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data)
    {
        emptyView.setText("Looks like you gotta read newspaper today!");
        customNewsAdapter.clear();
        if (data != null && !data.isEmpty())
        {
            customNewsAdapter.addAll(data);
            customNewsAdapter.notifyDataSetChanged();
            mSwipeRefreshView.setRefreshing(false);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader)
    {
        customNewsAdapter.clear();

    }
}




