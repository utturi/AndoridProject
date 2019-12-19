package com.example.andoridproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.andoridproject.Etc.DriveHolder;
import com.example.andoridproject.Item.Alert;

import java.util.ArrayList;

public class AlertAdapter extends ArrayAdapter<Alert> {
    Context context;
    int resId;
    ArrayList<Alert> data;

    public AlertAdapter(Context context, int resId, ArrayList<Alert> data) {
        super(context, resId);
        this.context = context;
        this.resId = resId;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            DriveHolder holder = new DriveHolder(convertView);
            convertView.setTag(holder);
        }
        DriveHolder holder = (DriveHolder) convertView.getTag();

        TextView titleView = holder.titleView;
        TextView dateView = holder.dateView;
        TextView contentView = holder.contentView;

        final Alert vo = data.get(position);

        titleView.setText(vo.title);
        contentView.setText(vo.content);
        dateView.setText(vo.date);

        return convertView;
    }
}