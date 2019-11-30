package com.example.andoridproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.andoridproject.Item.Board;
import com.example.andoridproject.R;

import java.util.ArrayList;

public class Tab1BoardAdapter extends BaseAdapter {
    ArrayList<Board> data;
    LayoutInflater inflater = null;
    Context mcontext;

    public Tab1BoardAdapter(ArrayList<Board> items, Context context)
    {
        data = items;
        mcontext = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            convertView = inflater.inflate(R.layout.tab1_item, parent, false);
            final int index = data.size()-(position+1);
            String temp = data.get(index).getTitle();
            TextView oTextDate = (TextView)convertView.findViewById(R.id.item_date);
            TextView oTextTitle = (TextView) convertView.findViewById(R.id.item_text);
            oTextTitle.setText(temp);
            oTextDate.setText(data.get(index).getDate());

        }
        return convertView;
    }
}