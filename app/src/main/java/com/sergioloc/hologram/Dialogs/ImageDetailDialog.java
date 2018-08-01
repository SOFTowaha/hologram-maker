package com.sergioloc.hologram.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sergioloc.hologram.R;
import com.sergioloc.hologram.Viewers.ShowLoadedImage;

public class ImageDetailDialog extends Dialog {

    private ImageView ivDetail, ivPlay, ivClose;
    private Uri image;


    public ImageDetailDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_image_details);

        ivDetail = findViewById(R.id.ivImage);
        ivPlay = findViewById(R.id.ivPlay);
        ivClose = findViewById(R.id.ivCose);

        buttons();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void startDialog(Uri uri){
        image = uri;
        Glide.with(getContext())
                .load(uri)
                .into(ivDetail);
        show();
    }

    private void buttons(){
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ShowLoadedImage.class);
                i.putExtra("imageUri",image.toString());
                getContext().startActivity(i);
                dismiss();
            }
        });
    }
}
