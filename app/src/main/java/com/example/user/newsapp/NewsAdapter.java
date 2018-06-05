package com.example.user.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter
{
    public NewsAdapter(@NonNull Context context, ArrayList<News> newsArrayList)
    {
        super(context, 0, newsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        View newsListView = convertView;
        if(newsListView==null)
        {
            newsListView=LayoutInflater.from(getContext()).inflate(R.layout.news_view,parent,false);
        }
        News currentNews = (News) getItem(position);

        TextView sectionNameView = (TextView) newsListView.findViewById(R.id.sectionName);
        String SectionAd = currentNews.getSectionName();
        sectionNameView.setText(SectionAd);

        TextView webTitleView = (TextView) newsListView.findViewById(R.id.webTitle);
        String TitleAd =currentNews.getWebTitle();
        webTitleView.setText(TitleAd);

        /*Date dateObject = new Date(currentNews.getDate());*/
        String serverDate = currentNews.getDate().replace("T", "  ").replace("Z","");
        String serverTime = new String();
        String serverDateOnly = new String();


        try
        {
            serverDateOnly = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).
                    format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(serverDate));
            serverTime = new SimpleDateFormat("hh:mm a",Locale.getDefault()).
                    format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).parse(serverDate));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }


        TextView dateView = newsListView.findViewById(R.id.date);
        dateView.setText(serverDateOnly);

        TextView timeView = newsListView.findViewById(R.id.time);
        timeView.setText(serverTime);


        return newsListView;
    }
}
