package com.example.android.the_guardian_news;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods to use for obtaining news articles from The Guardian website.
 */
public abstract class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // The constructor is required but it will never be used
    // to make an instance of QueryUtils so it is private.
    private QueryUtils(){
    }

    private static List<NewsItem> fetchNewsData(String requestUrl){

        // Create an URL object.
        URL url = createUrl(requestUrl);

        // Perform HTTP request on the url and receive a JSON response back;
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        /** Extract relevant fields from the JSON response and
         * create an ArrayList<Earthquake> with the list of {@link NewsItem} objects */
        List<NewsItem> newsItems = extractNewsItemsFromJson(jsonResponse);

        return newsItems;
    }

    /** Create an {@link URL} object from a String url */
    private static URL createUrl(String urlString){
        URL url = null;

        try{
            url = new URL(urlString);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem with creating the URL", e);
        }

        return url;
    }

    /** Make a HTTP connection using the URL object and return a String as a response. */
    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /** milliseconds */);
            connection.setConnectTimeout(15000 /** milliseconds */);
            connection.setRequestMethod("GET");
            connection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if(connection.getResponseCode() == 200){
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem with parsing the JSON response", e);
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /** Convert the {@link InputStream} object into a String with the JSON response */
    private static String readFromStream(InputStream stream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(stream != null){
            InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while(line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link NewsItem} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<NewsItem> extractNewsItemsFromJson(String jsonString){
        // Check if the json is null or empty in which case return early
        if(TextUtils.isEmpty(jsonString)){
            return null;
        }
        // Make a new ArrayList that to populate with news items
        ArrayList<NewsItem> newsItems = new ArrayList<>();

        // If there is a problem with the way the JSON is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.l
        try{
            // Convert jsonString String into a JSONObject
            JSONObject reader = new JSONObject(jsonString);

            // First extract the object "response"
            JSONObject response = reader.getJSONObject("response");

            // Then extract the results array from it
            JSONArray results = response.getJSONArray("results");

            // Loop through each result in the array
            for(int i = 0; i < results.length(); i++){
                // Get the result at position i
                JSONObject result = results.getJSONObject(i);

                // Get the title of the result
                String title = result.getString("webTitle");

                // Get the time of publication
                String date = result.getString("webPublicationDate");

                // Get the url of the news item
                String url = result.getString("webUrl");

                // Get the section name for the news item
                String sectionName = result.getString("sectionName");

                // Add to the ArrayList of news items
                newsItems.add(new NewsItem(title, date, url, sectionName));
            }

        }
        catch (JSONException e){
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return newsItems;
    }
}
