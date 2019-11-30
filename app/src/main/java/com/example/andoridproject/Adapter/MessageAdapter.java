package com.example.andoridproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andoridproject.R;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
    ArrayList<String> messages;
    LayoutInflater inflater = null;
    public MessageAdapter(ArrayList<String> items)
    {
         messages = items;
    }
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.message_item, parent, false);
            TextView oText = (TextView) convertView.findViewById(R.id.message_item_title);
            TextView oName = (TextView)convertView.findViewById(R.id.username);
            ImageView oProfile = (ImageView)convertView.findViewById(R.id.message_item_profile);
            oProfile.setVisibility(View.GONE);
            String[] message = messages.get(position).split("#");
            oText.setText(message[1]);
            oName.setText(message[0]);
        }
        return convertView;
    }
}
