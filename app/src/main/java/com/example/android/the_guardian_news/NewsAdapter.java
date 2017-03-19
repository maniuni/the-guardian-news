package com.example.android.the_guardian_news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link NewsAdapter} is a type of {@link ArrayAdapter} that
 * takes care of making the layout for one list item in the ListView
 * based on a resource which is a list of {@link NewsItem} objects.
 */
public class NewsAdapter extends ArrayAdapter<NewsItem> {
    public NewsAdapter(Context context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Get the current news item
        NewsItem currentNewsItem = getItem(position);

        // Find a reference to the title TextView
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        // Set the text to be the current news item title
        title.setText(currentNewsItem.getTitle());

        // Find a reference to the section TextView
        TextView section = (TextView) listItemView.findViewById(R.id.section);
        // Set the text to be the current news item section
        section.setText(currentNewsItem.getSection());

        // Find a reference to the title TextView
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        // Set the text to be the current news item date using a helper function
        // to convert the Date object to a normal date
        date.setText(formatDate(currentNewsItem.getTimeString(), true));

        // Find a reference to the title TextView
        TextView time = (TextView) listItemView.findViewById(R.id.time);
        // Set the text to be the current news item time using a helper function
        // to convert the Date object to a normal time
        time.setText(formatDate(currentNewsItem.getTimeString(), false));

        return listItemView;
    }

    /**
     * Helper method to format the date into a more human readable format.
     * @param inputTime is the incoming date String that has to be formatted
     * @param isDate is true if we need a formatted date and false if we need time in the day.
     * @return the date/time as a formatted String.
     */
    private String formatDate(String inputTime, boolean isDate){
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        if(isDate){
            DateFormat toFormat = new SimpleDateFormat("LLL dd, y");
            Date date = null;
            try {
                date = fromFormat.parse(inputTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
             return toFormat.format(date);
        }
        else {
            DateFormat toFormat = new SimpleDateFormat("h:mm a");
            Date date = null;
            try {
                date = fromFormat.parse(inputTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return toFormat.format(date);
        }
    }
}
