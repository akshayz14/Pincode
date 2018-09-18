package com.apc.pincode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by akshay on 30/07/18
 */
public class PostOfficeListAdapter extends ArrayAdapter<PostOffice> {


    private Context mContext;
    private List<PostOffice> postOfficeList;
    private String pincode;


    public PostOfficeListAdapter(@NonNull Context context, int resource, @NonNull List<PostOffice> poList, String pincode) {
        super(context, resource, poList);
        this.mContext = context;
        this.postOfficeList = poList;
        this.pincode = pincode;
    }

    public PostOfficeListAdapter(@NonNull Context context, int resource, @NonNull List<PostOffice> poList) {
        super(context, resource, poList);
        this.mContext = context;
        this.postOfficeList = poList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        PostOfficeItemHolder holder;

        if (rowView == null) {
            holder = new PostOfficeItemHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.post_office_item, parent, false);
            holder.tvPincode = rowView.findViewById(R.id.tvPincode);
            holder.tvNameOfPO = rowView.findViewById(R.id.tvNameOfPO);
            holder.tvNameOfState = rowView.findViewById(R.id.tvNameOfState);
            holder.tvRegion = rowView.findViewById(R.id.tvRegion);
            rowView.setTag(holder);
        } else {
            holder = (PostOfficeItemHolder) rowView.getTag();
        }

        if (postOfficeList.get(position).getPincode() != null) {
            holder.tvPincode.setText(postOfficeList.get(position).getPincode());
        } else {
//            holder.tvPincode.setVisibility(View.GONE);
            holder.tvPincode.setText(this.pincode);
        }

        holder.tvNameOfPO.setText(postOfficeList.get(position).getName());
        holder.tvNameOfState.setText(postOfficeList.get(position).getState());
        holder.tvRegion.setText(postOfficeList.get(position).getRegion());
        return rowView;
    }

    private static class PostOfficeItemHolder {
        TextView tvPincode, tvNameOfPO;
        TextView tvNameOfState, tvRegion;
    }
}
