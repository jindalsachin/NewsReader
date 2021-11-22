package com.example.android.newsapp;

public class News {

    private String mHeadline;
    private String mSection;
    private String mAuthor;
    private String mTimeInMilliseconds;
    private String mUrl;
    private String mThumbnailUrl;

    public News(String headline, String section, String author, String timeInMilliseconds, String url, String thumbnailUrl) {
        mHeadline = headline;
        mSection = section;
        mAuthor = author;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
        mThumbnailUrl = thumbnailUrl;
    }

    public String headline() {
        return mHeadline;
    }
    public String section() {
        return mSection;
    }
    public String author() {
        return mAuthor;
    }
    public String timeInMilliseconds() {
        return mTimeInMilliseconds;
    }
    public String url() {
        return mUrl;
    }
    public String thumbnailUrl() {
        return mThumbnailUrl;
    }
}


