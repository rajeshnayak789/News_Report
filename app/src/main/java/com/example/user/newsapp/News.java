package com.example.user.newsapp;

import java.io.Serializable;

public class News implements Serializable
{
    private String sectionName;
    private String webTitle;
    private String date;
    String url;


    public News(String eSectionName, String eWebTitle, String eDate, String eURL)
    {
        sectionName = eSectionName;
        webTitle = eWebTitle;
        date = eDate;
        url = eURL;
    }

    public String getSectionName()
    {
        return sectionName;
    }

    public String getWebTitle()
    {
        return webTitle;
    }

    public String getDate()
    {
        return date;
    }

    public String getUrl()
    {
        return url;


}

}
