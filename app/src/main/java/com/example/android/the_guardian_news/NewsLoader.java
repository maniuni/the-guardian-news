package com.example.android.the_guardian_news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Here the work for downloading the data from the website
 * is done in a background thread.
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    /** The name of the class to use for error messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** The website url for the query */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if(mUrl == null){
            return null;
        }

        // Perform the network request, parse the response and return a list of news items.
        List<NewsItem> newsItems = QueryUtils.fetchNewsData(mUrl);

        return newsItems;
    }
}
