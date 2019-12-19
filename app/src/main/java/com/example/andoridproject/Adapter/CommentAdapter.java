package com.example.andoridproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.andoridproject.Item.Comment;
import com.example.andoridproject.R;
import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    ArrayList<Comment> data;
    LayoutInflater inflater = null;
    public CommentAdapter(ArrayList<Comment> items)
    {
        data = items;
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
            convertView = inflater.inflate(R.layout.comment, parent, false);
            TextView oTextText = (TextView) convertView.findViewById(R.id.comment_text);
            TextView oName = (TextView)convertView.findViewById(R.id.comment_username);
            oTextText.setText(data.get(position).getComment());
            oName.setText(data.get(position).getUserName());
        }
        return convertView;
    }
}
