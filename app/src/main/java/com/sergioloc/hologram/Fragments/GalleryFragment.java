package com.sergioloc.hologram.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sergioloc.hologram.Adapter.RecyclerAdapterImage;
import com.sergioloc.hologram.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment {

    private ImageButton button;
    private View view;
    private boolean guest;
    private RecyclerView rvImages;
    private GridLayoutManager gridLayoutManager;
    private RecyclerAdapterImage adapter;
    private ArrayList listImages;
    private Context context;
    private RelativeLayout rlUploadImage;
    private Button bUploadImage, bCloseDialog;
    private EditText etNameImage;
    //Firebase
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference images;
    private StorageReference mStorage;
    //Image from gallery
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView ivImageLoaded;


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
        if(guest){
            initForGuest();
        }else{
            initForUsers();
        }

        return view;
    }

    private void init(){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        context = activity.getApplicationContext();
        activity.setTitle("My holograms");
        button = view.findViewById(R.id.bAddImage);

    }

    private void initForUsers(){
        //Firebase
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        images = database.getReference("users").child(user.getUid()).child("images");
        mStorage = FirebaseStorage.getInstance().getReference("images");
        //Recyclerview
        rvImages = view.findViewById(R.id.rvImages);
        rvImages.setHasFixedSize(true);
        listImages = new ArrayList();
        for (int i=0; i<12;i++){
            listImages.add("");

        }
        gridLayoutManager = new GridLayoutManager(context, 3);
        adapter = new RecyclerAdapterImage(listImages);
        rvImages.setAdapter(adapter);
        rvImages.setLayoutManager(gridLayoutManager);

        //Upload image
        rlUploadImage = view.findViewById(R.id.rlUploadImage);
        ivImageLoaded = view.findViewById(R.id.ivImageLoaded);
        bUploadImage = view.findViewById(R.id.bUploadImage);
        bCloseDialog = view.findViewById(R.id.bCloseDialog);
        etNameImage = view.findViewById(R.id.etImageName);
    }

    private void initForGuest(){
        rvImages.setVisibility(View.GONE);
    }

    private void buttons(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                chooseImage();
            }
        });
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                ivImageLoaded.setImageBitmap(bitmap);
                rlUploadImage.setVisibility(View.VISIBLE);
                bCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rlUploadImage.setVisibility(View.GONE);
                        etNameImage.getText().clear();
                    }
                });
                bUploadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etNameImage.getText().length() > 0){
                            uploadImage(etNameImage.getText().toString());
                            etNameImage.getText().clear();
                            rlUploadImage.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(context, "Debes escribir un nombre", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String name) {

        if(filePath != null)
        {
            StorageReference ref = mStorage.child("users/"+user.getUid()+"/"+name);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
