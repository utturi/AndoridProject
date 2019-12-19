package com.example.andoridproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.andoridproject.Item.Recipe;
import com.example.andoridproject.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Recipe> recipes;
    private Context context;

    public RecyclerAdapter(ArrayList<Recipe> recipes,Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recipe_item,parent,false);
        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        final Recipe item = recipes.get(position);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        int index = position+1;

        StorageReference spaceRef = storageRef.child("recipes/"+index+".png");

        Glide.with(context)
                .load(spaceRef)
                .into(holder.picture);

        holder.intro.setText(item.getIntroduction());
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView picture;
        TextView intro;

        ViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.recipe_item);
            intro = itemView.findViewById(R.id.recipe_intro);
        }
    }
}
