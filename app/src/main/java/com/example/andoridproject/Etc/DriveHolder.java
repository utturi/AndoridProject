package com.example.andoridproject.Etc;

import android.view.View;
import android.widget.TextView;

import com.example.andoridproject.R;

public class DriveHolder {
    public TextView titleView;
    public TextView contentView;
    public TextView dateView;

    public DriveHolder(View root) {
        titleView = root.findViewById(R.id.foodname);
        contentView = root.findViewById(R.id.food_content);
        dateView = root.findViewById(R.id.date);
    }
}