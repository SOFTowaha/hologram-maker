package com.sergioloc.hologram.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sergioloc.hologram.Dialogs.ImageDetailDialog;
import com.sergioloc.hologram.R;
import java.util.ArrayList;

/**
 * Created by Sergio López on 31/07/2018.
 */

public class RecyclerAdapterImage extends RecyclerView.Adapter<RecyclerAdapterImage.MyViewHolder> {


    private ArrayList<String> array;
    private Context context;
    private ImageDetailDialog dialog;
    //Firebase
    private FirebaseUser user;
    private StorageReference mStorage;
    private String path;


    public RecyclerAdapterImage(ArrayList<String> array){
        this.array=array;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        context = view.getContext();
        dialog = new ImageDetailDialog(context);
        user = FirebaseAuth.getInstance().getCurrentUser();
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        path = "gs://hologram-2.appspot.com/images/users/"+user.getUid()+"/"+array.get(position);
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(path);
        mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.image);
            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setImage();
            }
        });

    }


    @Override
    public int getItemCount() {
        return array.size();
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

