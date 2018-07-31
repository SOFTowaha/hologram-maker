package com.sergioloc.hologram.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sergioloc.hologram.R;
import java.util.ArrayList;

/**
 * Created by Sergio López on 14/09/2017.
 */

public class RecyclerAdapterImage extends RecyclerView.Adapter<RecyclerAdapterImage.MyViewHolder> {


    private ArrayList<String> array;
    private Context context;
    private Dialog dialog;
    private ImageView play, close, image;
    //Firebase
    private StorageReference mStorage;


    public RecyclerAdapterImage(ArrayList<String> array){
        this.array=array;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        context = view.getContext();
        dialog = new Dialog(context);
        //mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://hologram-2.appspot.com/imagenes/user1/Brofist.jpg");
        mStorage = FirebaseStorage.getInstance().getReference();

        return new MyViewHolder(view, viewType);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (position%2 == 0)
            holder.image.setImageResource(R.drawable.circle_green);
        else
            holder.image.setImageResource(R.drawable.circle_blue);


        final Drawable im = holder.image.getDrawable();
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setContentView(R.layout.dialog_image_details);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                image = dialog.findViewById(R.id.ivImage);
                image.setImageDrawable(im);
                play = dialog.findViewById(R.id.ivPlay);
                close = dialog.findViewById(R.id.ivCose);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Intent i = new Intent(context,ShowLoadedImage.class);
                                i.putExtra("imageUri",uri.toString());
                                context.startActivity(i);
                                Glide.with(view)
                                .load(uri)
                                .into(iv_image);
                            }
                        });*/
                    }
                });
                dialog.show();
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

        public MyViewHolder(final View itemView, int viewType) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

}

