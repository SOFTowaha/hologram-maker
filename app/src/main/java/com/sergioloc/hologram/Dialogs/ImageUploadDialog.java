package com.sergioloc.hologram.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.sergioloc.hologram.R;

public class ImageUploadDialog extends Dialog {

    public ImageView ivImageLoaded;
    public EditText etImageName;
    public Button bUploadImage, bCloseDialog;

    public ImageUploadDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_image_upload);

        ivImageLoaded = findViewById(R.id.ivImageLoaded);
        etImageName = findViewById(R.id.etImageName);
        bUploadImage = findViewById(R.id.bUploadImage);
        bCloseDialog = findViewById(R.id.bCloseDialog);
        buttons();
    }

    public String getImageName(){
        return etImageName.getText().toString();
    }

    private void buttons(){
        bCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void close(){
        dismiss();
    }
}
