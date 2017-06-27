package com.may.android.newslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Henrik on 22.06.2017.
 */

public class newsAdapter extends ArrayAdapter<news_item> {

    public newsAdapter(Context context, List<news_item> newsitems) {
        super(context, 0, newsitems);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        news_item currentNewsitem = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNewsitem.getTitle());
        TextView authorView = (TextView) listItemView.findViewById(R.id.type);
        authorView.setText(currentNewsitem.getType());
        TextView priceView = (TextView) listItemView.findViewById(R.id.date);
        priceView.setText(currentNewsitem.getDate());
        TextView catView = (TextView) listItemView.findViewById(R.id.category);
        catView.setText(currentNewsitem.getCategory());

        // Return the list item view that is now showing the appropriate data
        return listItemView;


    }
}
