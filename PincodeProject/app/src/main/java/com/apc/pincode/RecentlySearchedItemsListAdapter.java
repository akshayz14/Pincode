package com.apc.pincode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 25/09/18
 */
public class RecentlySearchedItemsListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List recentItemsSearched;

    public RecentlySearchedItemsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> recentItemList) {
        super(context, resource, recentItemList);
        this.mContext = context;
        this.recentItemsSearched = recentItemList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        RecentlySearchedItemsHolder holder;

        if (rowView == null) {
            holder = new RecentlySearchedItemsHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.recently_searched_item, parent, false);
            holder.tvRecentlySearched = rowView.findViewById(R.id.tvRecentlySearched);
            rowView.setTag(holder);
        } else {
            holder = (RecentlySearchedItemsHolder) rowView.getTag();
        }

        holder.tvRecentlySearched.setText(recentItemsSearched.get(position).toString());
        return rowView;
    }

    private static class RecentlySearchedItemsHolder {
        TextView tvRecentlySearched;
    }

}
