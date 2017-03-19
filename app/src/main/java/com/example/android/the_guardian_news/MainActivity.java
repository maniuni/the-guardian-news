package com.example.android.the_guardian_news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<NewsItem>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** Adapter for the list of news */
    private NewsAdapter mAdapter;

    /** TextView to display if the list is empty */
    private TextView mEmptyView;

    // The website url for news from The Guardian
    private static final String THE_GUARDIAN_URL = "https://content.guardianapis.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make a new {@link NewsAdapter} object using the ArrayList with the news items
        mAdapter = new NewsAdapter(getApplicationContext(), new ArrayList<NewsItem>());

        // Find a reference to the ListView in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        // Get a reference to the TextView for an empty list of news.
        mEmptyView = (TextView) findViewById(R.id.empty_view);

        // Set the empty state TextView onto the newsListView
        newsListView.setEmptyView(mEmptyView);

        // Set the adapter on the ListView
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set a listener so that when the user clicks
        // on a news item the browser opens on the relevant article in the website.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsItem currentNewsItem = mAdapter.getItem(i);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentNewsItem.getUrl()));
                startActivity(intent);
            }
        });

        // Get a reference to the connectivity manager
        // to check the state of network connectivity.
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details for the currently active default data network.
        NetworkInfo activeNetwork =
                connectivityManager.getActiveNetworkInfo();

        // If there is network connection, fetch data
        if(activeNetwork != null && activeNetwork.isConnected()){
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
        }
        else {
            // Display error. First hide the loading spinner
            // so the message will be visible.
            View loadingSpinner = findViewById(R.id.loading_spinner);
            loadingSpinner.setVisibility(View.GONE);
            // Display the no connection message.
            mEmptyView.setText(R.string.no_connection);
        }

    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        // Get a reference to the SharedPreferences object
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Check the user's preferred section of use the default;
        String section = preferences.getString(getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));
        // Use the base url and then add the necessary search
        // queries using some of the user's preferences
        Uri baseUri = Uri.parse(THE_GUARDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(section);
        uriBuilder.appendQueryParameter("api-key", "567a1934-9331-4ce1-8f51-6b65348b315d");
        // Create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        // Clear the adapter from the previous news data
        mAdapter.clear();
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {
        // Find the loading spinner in the layout.
        ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        // Hide the spinner if there are news to be shown.
        loadingSpinner.setVisibility(View.GONE);
        // Clear the adapter from the previous news data
        mAdapter.clear();
        // If the list of news items is not null or empty we can add it to
        // the adapter which will trigger the ListView to update.
        if(newsItems != null && !newsItems.isEmpty()){
            mAdapter.addAll(newsItems);
        }
        // Display message that no news has been found.
        mEmptyView.setText(R.string.empty_list);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
