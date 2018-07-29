package com.sergioloc.hologram.Adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sergioloc.hologram.Objects.VideoModel;
import com.sergioloc.hologram.R;

import java.util.ArrayList;

public class RecyclerAdapterImage extends RecyclerView.Adapter<RecyclerAdapterImage.MyViewHolder>{

    private static ArrayList<Uri> list_images;

    public RecyclerAdapterImage(ArrayList<Uri> list_images){
        this.list_images = list_images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.iv_image.setBackground(list_images.get(position));
    }

    @Override
    public int getItemCount() {
        return list_images.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_image;

        public MyViewHolder(View item){
            super(item);
            list_images = new ArrayList<>();
            iv_image = itemView.findViewById(R.id.iv_image);

        }
    }
}
