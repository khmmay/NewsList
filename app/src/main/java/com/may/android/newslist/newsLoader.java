package com.may.android.newslist;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Henrik on 22.06.2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

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
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of newsItem.
        List<NewsItem> newsitems = QueryUtils.fetchnewsItemData(mUrl);
        return newsitems;
    }
}
