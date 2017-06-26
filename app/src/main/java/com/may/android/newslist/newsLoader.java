package com.may.android.newslist;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Henrik on 22.06.2017.
 */

public class newsLoader extends AsyncTaskLoader<List<news_item>> {

    private String mUrl;

    public newsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<news_item> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of newsItem.
        List<news_item> newsitems = QueryUtils.fetchnewsItemData(mUrl);
        return newsitems;
    }
}
