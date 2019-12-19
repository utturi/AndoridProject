package com.example.andoridproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.andoridproject.Item.Alert;
import com.example.andoridproject.R;
import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>  {
    private ArrayList<Alert> list;
    private Context context;

    public AlarmAdapter(Context context, ArrayList<Alert> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_item,parent,false);
        AlarmAdapter.ViewHolder viewHolder = new AlarmAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.ViewHolder holder, int position) {

        final Alert item = list.get(position);
        holder.name.setText(list.get(position).title);
        holder.contents.setText(list.get(position).content);
        holder.date.setText(list.get(position).date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView date;
        TextView contents;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodname);
            contents = itemView.findViewById(R.id.food_content);
            date = itemView.findViewById(R.id.date);
        }
    }
}
