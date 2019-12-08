package com.example.andoridproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andoridproject.Activity.ReadMessageActivity;
import com.example.andoridproject.R;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
    ArrayList<String> messages;
    LayoutInflater inflater = null;
    Context context;
    int identifier;
    public MessageAdapter(ArrayList<String> items,Context context,int identifier)
    {
         messages = items;
         this.context = context;
         this.identifier = identifier;
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
            final int index = messages.size()-(position+1);
            convertView = inflater.inflate(R.layout.message_item, parent, false);
            TextView oText = (TextView) convertView.findViewById(R.id.message_item_title);
            TextView oName = (TextView)convertView.findViewById(R.id.username);
            ImageView oProfile = (ImageView)convertView.findViewById(R.id.message_item_profile);
            TextView oDate = convertView.findViewById(R.id.message_item_date);
            oProfile.setVisibility(View.GONE);
            final String[] message = messages.get(index).split("#");
            oText.setText(message[1]);
            oName.setText(message[0]);
            oDate.setText(message[3]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReadMessageActivity.class);
                    intent.putExtra("UID",message[2]);
                    intent.putExtra("text",message[1]);
                    intent.putExtra("name",message[0]);
                    if(identifier ==0)
                        intent.putExtra("Identifier","sender");
                    else
                        intent.putExtra("Identifier","receiver");
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }
}
