package com.developer.fabian.feedrss.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class MainListAdapter extends BaseAdapter {

    private ArrayList<?> enterList;
    private int idView;
    private Context context;

    protected MainListAdapter(ArrayList<?> enterList, int idView, Context context) {
        super();

        this.enterList = enterList;
        this.idView = idView;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(idView, null);
        }

        onEnter(enterList.get(position), convertView);

        return convertView;
    }

    @Override
    public int getCount() {
        return enterList.size();
    }

    @Override
    public Object getItem(int position) {
        return enterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract void onEnter(Object enter, View view);
}
