package com.sergioloc.hologram.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.sergioloc.hologram.Models.FirebaseImage;
import com.sergioloc.hologram.R;
import java.util.ArrayList;

/**
 * Created by Sergio López on 31/07/2018.
 */

public class RecyclerAdapterImageCloud extends RecyclerView.Adapter<RecyclerAdapterImageCloud.MyViewHolder> {


    private ArrayList<String> imageList;
    private Context context;
    private ImageDetailDialog dialog;
    //Firebase
    private FirebaseUser user;
    private StorageReference mStorage;
    private String path;
    private ArrayList<FirebaseImage> firebaseImageList;


    public RecyclerAdapterImageCloud(ArrayList<String> array){
        this.imageList=array;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        context = view.getContext();
        dialog = new ImageDetailDialog(context);
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseImageList = new ArrayList<>();
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        path = "gs://hologram-2.appspot.com/images/users/"+user.getUid()+"/"+imageList.get(position);
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(path);
        mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                firebaseImageList.add(new FirebaseImage(position,uri));
                Log.i("IMG","Posicion -> "+position+" | Imagen: "+imageList.get(position));

                Glide.with(context)
                        .load(uri)
                        .into(holder.image);
            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("IMG","Buscando -> "+position);
                for (int i = 0; i < firebaseImageList.size(); i++){
                    if (position == firebaseImageList.get(i).getPosition())
                        dialog.startDialog(firebaseImageList.get(i).getImage());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return imageList.size();
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

