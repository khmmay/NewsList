package com.may.android.newslist;

/**
 * Created by Henrik on 21.06.2017.
 */

public class news_item {
    private String title;
    private String type;
    private String category;
    private String date;
    private String link;

    public news_item(String mTitle, String mType, String mCategory, String mDate, String mLink) {
        title = mTitle;
        type = mType;
        category = mCategory;
        date = mDate;
        link = mLink;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }


}
