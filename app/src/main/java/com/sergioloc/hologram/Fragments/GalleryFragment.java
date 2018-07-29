package com.sergioloc.hologram.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sergioloc.hologram.R;
import com.sergioloc.hologram.Viewers.ShowLoadedImage;


public class GalleryFragment extends Fragment {

    private Button button;
    private View view;
    private boolean guest;
    private ImageView iv_image;
    //Firebase
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference images;
    private StorageReference mStorage;

    @SuppressLint("ValidFragment")
    public GalleryFragment(Boolean guest) {
        this.guest = guest;
    }

    public GalleryFragment() {
    }


    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        init();
        buttons();
        if(!guest){
            initForUsers();
        }

        return view;
    }

    private void init(){
        button = view.findViewById(R.id.addImage_button);
        iv_image = view.findViewById(R.id.iv_test);
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://hologram-2.appspot.com/imagenes/user1/Brofist.jpg");
    }

    private void initForUsers(){
        //Firebase
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        images = database.getReference("users").child(user.getUid()).child("images");
    }

    private void buttons(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        toast("Success");
                        Intent i = new Intent(getContext(),ShowLoadedImage.class);
                        i.putExtra("imageUri",uri.toString());
                        startActivity(i);
                        /*Glide.with(view)
                                .load(uri)
                                .into(iv_image);*/
                    }
                });

            }
        });
    }


    private void toast(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }
    private void snackbar(String text){
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }




}
