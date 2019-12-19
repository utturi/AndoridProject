package com.example.andoridproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.andoridproject.Item.Board;
import com.example.andoridproject.Activity.PostDetailActivity;
import com.example.andoridproject.R;
import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter {
    ArrayList<Board> data;
    LayoutInflater inflater = null;
    int comments;
    Context mcontext;

    public BoardAdapter(ArrayList<Board> items, Context context)
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
            final int index = data.size()-(position+1);
            convertView = inflater.inflate(R.layout.item, parent, false);
            String temp = data.get(position).getTitle();
            TextView oTextTitle = (TextView) convertView.findViewById(R.id.item_title);
            TextView oText = (TextView)convertView.findViewById(R.id.item_text);
            TextView oDate = convertView.findViewById(R.id.item_date);
            TextView commentNum = convertView.findViewById(R.id.item_chat);
            TextView starNum = convertView.findViewById(R.id.item_star);

            oTextTitle.setText(data.get(index).getTitle());
            oText.setText(data.get(index).getContent());
            oDate.setText(data.get(index).getDate());
            commentNum.setText(""+data.get(index).getComments());
            starNum.setText(""+data.get(index).getStars());
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
