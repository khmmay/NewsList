package com.may.android.newslist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.may.android.newslist.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private boolean searched = false;
    boolean queryChanged = false;

    // Get a reference to the ConnectivityManager to check state of network connectivity
    ConnectivityManager connMgr = null;

    private static final String REQUEST_URL_BASE =
            "http://content.guardianapis.com/search"/*+"?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test"*/;

    private static final int newsItem_LOADER_ID = 1;

    /**
     * Adapter for the list of news_items
     */
    private NewsAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        uploadList();

        binding.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetQuery();
            }
        });
        binding.reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoaderManager loaderManager = getLoaderManager();
                if (!(loaderManager.getLoader(newsItem_LOADER_ID) == null)) {
                    loaderManager.restartLoader(newsItem_LOADER_ID, null, MainActivity.this);
                }
                uploadList();
            }
        });


    }

    public void uploadList() {
        View view = MainActivity.super.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        searched = true;
        binding.loadingIndicator.setVisibility(View.VISIBLE);


        // Find a reference to the {@link ListView} in the layout

        mEmptyStateTextView = binding.emptyView;
        binding.emptyView.setText("");
        binding.list.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(getBaseContext(), new ArrayList<NewsItem>());

        binding.list.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected newsItem.
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current newsItem that was clicked on
                NewsItem currentNewsitem = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)

                assert currentNewsitem != null;
                if (!(currentNewsitem.getLink() == null)) {
                    Uri newsItemUri = Uri.parse(currentNewsitem.getLink());
                    // Create a new intent to view the newsItem URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);

                    // Send the intent to launch a new activity
                    if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(websiteIntent);
                    }
                }
            }
        });

        connMgr=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(newsItem_LOADER_ID, null, MainActivity.this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            binding.loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(REQUEST_URL_BASE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String query = binding.searchView.getText().toString();


        uriBuilder.appendQueryParameter("q", query);
        uriBuilder.appendQueryParameter("api-key", "test");

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        NewsLoader loader=null;
        if (networkInfo != null && networkInfo.isConnected()) {
            loader=new NewsLoader(this, uriBuilder.toString());
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            binding.loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }
        return loader;

    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        // Hide loading indicator because the data has been loaded
        binding.loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous newsItem data
        mAdapter.clear();

        // If there is a valid list of {@link NewsItem}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        mAdapter.clear();
    }

    void resetQuery() {
        if (!queryChanged) {
            binding.searchView.setText("");
            queryChanged = true;
        }
    }

}
