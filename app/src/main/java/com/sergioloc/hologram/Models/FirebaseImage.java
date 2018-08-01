package com.sergioloc.hologram.Models;

import android.net.Uri;

public class FirebaseImage {
    private int position;
    private Uri image;

    public FirebaseImage(){}

    public FirebaseImage(int position, Uri image) {
        this.position = position;
        this.image = image;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
