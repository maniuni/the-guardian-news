package com.example.android.the_guardian_news;

/**
 * Represents one news item with the information about it.
 */
public class NewsItem {

    // The title of the news item.
    private String mTitle;
    // The time as a string for the date and time that the news item was created.
    private String mTimeString;
    // The website address to the page with the content of the news item.
    private String mUrl;
    // The section parameter in the website address
    private String mSection;

    /** Create a new {@link NewsItem} object
     *
     * @param title is the title
     * @param time is the time in milliseconds
     * @param url is the url to the content
     * @param section is the section parameter of the website address
     */
    public NewsItem(String title, String time, String url, String section){
        mTitle = title;
        mTimeString = time;
        mUrl = url;
        mSection = section;
    }

    /** Get the time */
    public String getTimeString() {
        return mTimeString;
    }
    /** Get the title */
    public String getTitle() {
        return mTitle;
    }
    /** Get the url */
    public String getUrl() {
        return mUrl;
    }
    /** Get the section for the url */
    public String getSection() {
        return mSection;
    }
}
