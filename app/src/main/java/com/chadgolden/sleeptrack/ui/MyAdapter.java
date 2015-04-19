package com.chadgolden.sleeptrack.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chadgolden.sleeptrack.R;

import java.util.List;

/**
 * Created by Chad on 4/19/2015.
 */
public class MyAdapter extends ArrayAdapter<ContentItem> {

    public MyAdapter(Context context, List<ContentItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContentItem contentItem = getItem(position);

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(contentItem.getItemName());
        holder.tvDesc.setText(contentItem.getItemDescription());

        return convertView;
    }

    private class ViewHolder {

        TextView tvName, tvDesc;
    }

}
