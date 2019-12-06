package com.example.andoridproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.example.andoridproject.Etc.Dday;
import com.example.andoridproject.Item.ListViewItem;
import com.example.andoridproject.R;
import com.example.andoridproject.Activity.SubmainActivity;

public class MainPagerAdapter extends PagerAdapter {
        private Context mContext = null;
        private ListViewItem[] items;

        public MainPagerAdapter(Context context, ListViewItem[] item)
        {
            mContext = context;
            items = item;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null ;

            if (mContext != null) {
                // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.main_page, container, false);

                LinearLayout linearLayout = view.findViewById(R.id.layout);
                TextView main_text = (TextView) view.findViewById(R.id.main_text) ;
                TextView d_day = (TextView)view.findViewById(R.id.d_day);
                TextView date = (TextView)view.findViewById(R.id.date);
                main_text.setText(items[position].getName());
                date.setText(items[position].getDate());
                if(items[position].getDate()!="") {
                    String[] calcDate = items[position].getDate().split("-");
                    int Dday_result = Dday.caldate(Integer.parseInt(calcDate[0]), Integer.parseInt(calcDate[1]), Integer.parseInt(calcDate[2]));
                    if (Dday_result > 0) {
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.main_button));
                        d_day.setText("D + " + Dday_result);
                        d_day.setTextColor(Color.RED);
                    } else if (Dday_result == 0) {
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.main_button));
                        d_day.setText("D - DAY");
                        d_day.setTextColor(Color.RED);
                    } else {
                        Dday_result = -Dday_result;
                        d_day.setText("D - " + Dday_result);
                    }
                }
                main_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SubmainActivity.class);
                        mContext.startActivity(intent);
                    }
                });
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SubmainActivity.class);
                        mContext.startActivity(intent);
                    }
                });
                d_day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SubmainActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            }

            // 뷰페이저에 추가.
            container.addView(view);

            return view ;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 뷰페이저에서 삭제.
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            // 전체 페이지 수는 10개로 고정.
            return items.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == (View)object);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

}
