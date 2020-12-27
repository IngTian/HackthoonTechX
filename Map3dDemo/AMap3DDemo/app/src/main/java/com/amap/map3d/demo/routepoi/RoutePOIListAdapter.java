package com.amap.map3d.demo.routepoi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.amap.map3d.demo.R;
import com.amap.map3d.demo.offlinemap.OfflineListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RoutePOIListAdapter extends BaseAdapter {
    private Context mContext;
    private List<RoutePOIListItem> itemList = new ArrayList<>();

    public RoutePOIListAdapter(Context context, List<String[]> input) {
        mContext = context;
        for (String[] location : input)
            itemList.add(new RoutePOIListItem(location[0], location[1]));

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_query_result, null);
            holder.info = (TextView) view.findViewById(R.id.inf);
            view.setTag(holder);
        } else holder = (ViewHolder) view.getTag();
        RoutePOIListItem item = itemList.get(i);
        String inf = item.getName() + " 位于 " + item.getAddress();
        holder.info.setText(inf);
        return view;
    }

    private class ViewHolder {
        TextView info;
    }
}
