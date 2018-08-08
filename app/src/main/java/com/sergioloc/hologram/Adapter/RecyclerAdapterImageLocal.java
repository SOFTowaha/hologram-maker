package com.sergioloc.hologram.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sergioloc.hologram.Dialogs.ImageDetailDialog;
import com.sergioloc.hologram.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Sergio López on 04/08/2018.
 */

public class RecyclerAdapterImageLocal extends RecyclerView.Adapter<RecyclerAdapterImageLocal.MyViewHolder> {


    private ArrayList<Bitmap> localList;
    private Context context;
    private ImageDetailDialog dialog;


    public RecyclerAdapterImageLocal(ArrayList<Bitmap> array){
        this.localList=array;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        context = view.getContext();
        dialog = new ImageDetailDialog(context);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (!localList.isEmpty()){
            holder.image.setImageBitmap(localList.get(position));
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.startDialog(getImageUri(localList.get(position)));
            }
        });
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public int getItemCount() {
        return localList.size();
    }


    //----------------------------------------------------------------

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public MyViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

}

