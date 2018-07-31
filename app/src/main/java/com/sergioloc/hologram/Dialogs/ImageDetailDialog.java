package com.sergioloc.hologram.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.sergioloc.hologram.R;

public class ImageDetailDialog extends Dialog {

    ImageView ivDetail, ivPlay, ivClose;


    public ImageDetailDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_image_details);

        ivDetail = findViewById(R.id.ivImage);
        ivPlay = findViewById(R.id.ivPlay);
        ivClose = findViewById(R.id.ivCose);

        buttons();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setImage(){
        //ivDetail.setImageDrawable(drawable);
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

            }
        });
    }
}
