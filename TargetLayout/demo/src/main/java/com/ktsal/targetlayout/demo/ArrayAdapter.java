package com.ktsal.targetlayout.demo;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class ArrayAdapter<T> extends BaseAdapter {

    private List<T> items;

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return items != null ? items.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void updateItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}
