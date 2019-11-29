package com.example.andoridproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter {
    ArrayList<Board> data;
    LayoutInflater inflater = null;
    Context mcontext;

    BoardAdapter(ArrayList<Board> items, Context context)
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

            convertView = inflater.inflate(R.layout.item, parent, false);
            String temp = data.get(position).getTitle();
            final int index = data.size()-(position+1);
            TextView oTextTitle = (TextView) convertView.findViewById(R.id.item_title);
            TextView oName = (TextView)convertView.findViewById(R.id.username);
            oTextTitle.setText(data.get(index).getTitle());
            Log.e("Board","msg = "+data.get(index).getTitle());
            oName.setText(data.get(index).getUserName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, PostDetailActivity.class);
                    intent.putExtra("post", data.get(index));
                    mcontext.startActivity(intent);
                }
            });
        }
        return convertView;
    }
}
