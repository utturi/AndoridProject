package com.example.andoridproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter
{
    LayoutInflater inflater = null;
    private ArrayList<ListViewItem> m_oData = null;
    private boolean mClick = false;
    private CheckBox checkBoxs[];

    public ListViewAdapter(ArrayList<ListViewItem> _oData)
    {
        m_oData = _oData;
        checkBoxs = new CheckBox[m_oData.size()];
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return m_oData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return m_oData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
        }

        TextView oTextTitle = (TextView) convertView.findViewById(R.id.name);
        TextView oTextDate = (TextView) convertView.findViewById(R.id.date);
        checkBoxs[position] =  (CheckBox)convertView.findViewById(R.id.checkbox);
        if(mClick) {
            checkBoxs[position].setVisibility(View.VISIBLE);
        } else {
            checkBoxs[position].setVisibility(View.INVISIBLE);
        }
        oTextTitle.setText(m_oData.get(position).getName());
        oTextDate.setText(m_oData.get(position).getDate());

        //체크박스 작업
        checkBoxs[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxs[position].isChecked())
                {
                    ListViewItem item1 = new ListViewItem();
                    item1 = m_oData.get(position);
                    item1.setSelected(true);
                    m_oData.set(position,item1);
                }
                else
                {
                    ListViewItem item1 = new ListViewItem();
                    item1 = m_oData.get(position);
                    item1.setSelected(false);
                    m_oData.set(position,item1);
                }
            }
        });
        return convertView;
    }
    public void addItem(String name, String date)
    {
        ListViewItem item = new ListViewItem(name,date);
        m_oData.add(item);

    }
    public void toggleCheckBox(boolean bClick) {

        mClick = bClick;
        notifyDataSetChanged();
    }
    public void allCheckBox(boolean isAllCheked) {
        if (isAllCheked) {
            for (int i = 0; i < m_oData.size(); i++) {
                checkBoxs[i].setChecked(false);
                ListViewItem item1 = new ListViewItem();
                item1 = m_oData.get(i);
                item1.setSelected(false);
                m_oData.set(i, item1);
            }
        } else {
            for (int i = 0; i < m_oData.size(); i++) {
                checkBoxs[i].setChecked(true);
                ListViewItem item1 = new ListViewItem();
                item1 = m_oData.get(i);
                item1.setSelected(true);
                m_oData.set(i, item1);
            }
        }
    }
}

