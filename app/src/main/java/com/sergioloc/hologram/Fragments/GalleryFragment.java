package com.sergioloc.hologram.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sergioloc.hologram.Adapter.RecyclerAdapterImage;
import com.sergioloc.hologram.Dialogs.ImageUploadDialog;
import com.sergioloc.hologram.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment {

    //Basics
    private View view;
    private Context context;
    //Recycler
    private RecyclerView rvImages;
    private GridLayoutManager gridLayoutManager;
    private RecyclerAdapterImage adapter;
    //View
    private TextView tvCloud, tvLocal;
    private ImageButton button;
    private Switch swType;
    //Firebase
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference images;
    private StorageReference mStorage;
    //Variables
    private boolean guest;
    private Uri filePath;
    private Boolean cloudView;
    private ImageUploadDialog dialog;
    private final int PICK_IMAGE_REQUEST = 71;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    @SuppressLint("ValidFragment")
    public GalleryFragment(Boolean guest) {
        this.guest = guest;
    }

    public GalleryFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        init();
        initButtons();
        initView();

        return view;
    }

    /** Init **/

    private void init(){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        context = activity.getApplicationContext();
        activity.setTitle("My holograms");
        rvImages = view.findViewById(R.id.rvImages);
        button = view.findViewById(R.id.bAddImage);
        tvCloud = view.findViewById(R.id.tvCloud);
        tvLocal = view.findViewById(R.id.tvLocal);
        swType = view.findViewById(R.id.swType);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        cloudView = prefs.getBoolean("cloudView", true);
        if (cloudView){
            swType.setChecked(false);
            tvCloud.setTypeface(null, Typeface.BOLD);
            tvLocal.setTypeface(null, Typeface.NORMAL);
            tvCloud.setTextColor(getResources().getColor(R.color.colorWhite));
            tvLocal.setTextColor(getResources().getColor(R.color.colorGrayT));
        }else {
            swType.setChecked(true);
            tvLocal.setTypeface(null, Typeface.BOLD);
            tvCloud.setTypeface(null, Typeface.NORMAL);
            tvLocal.setTextColor(getResources().getColor(R.color.colorWhite));
            tvCloud.setTextColor(getResources().getColor(R.color.colorGrayT));
        }
    }

    private void initButtons(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                chooseImage();
            }
        });
        swType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tvLocal.setTypeface(null, Typeface.BOLD);
                    tvCloud.setTypeface(null, Typeface.NORMAL);
                    tvLocal.setTextColor(getResources().getColor(R.color.colorWhite));
                    tvCloud.setTextColor(getResources().getColor(R.color.colorGrayT));
                }else {
                    tvCloud.setTypeface(null, Typeface.BOLD);
                    tvLocal.setTypeface(null, Typeface.NORMAL);
                    tvCloud.setTextColor(getResources().getColor(R.color.colorWhite));
                    tvLocal.setTextColor(getResources().getColor(R.color.colorGrayT));
                }
                cloudView = !cloudView;
                editor.putBoolean("cloudView",cloudView);
                editor.apply();
                initView();
            }
        });
    }

    private void initView(){
        if(guest || !cloudView){
            initForLocal();
        }else{
            initForFirebase();
        }
    }


    /** Cloud **/

    private void initForFirebase(){
        //Firebase
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        images = database.getReference("users").child(user.getUid()).child("images");
        mStorage = FirebaseStorage.getInstance().getReference("images");
        //Recyclerview
        rvImages.setHasFixedSize(true);
        initImageList();
        gridLayoutManager = new GridLayoutManager(context, 3);
        rvImages.setLayoutManager(gridLayoutManager);
    }

    private void initImageList(){
        images.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList listImages = new ArrayList();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listImages.add(snapshot.getValue().toString());
                }
                adapter = new RecyclerAdapterImage(listImages);
                rvImages.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /** Local **/

    private void initForLocal(){
        rvImages.setAdapter(new RecyclerAdapterImage(new ArrayList<String>()));

    }


    /** Functions **/

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImageToDatabase(String name){
        images.push().setValue(name);
    }

    private void uploadImageToStorage(String name) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                dialog = new ImageUploadDialog(getActivity());
                dialog.show();
                dialog.ivImageLoaded.setImageBitmap(bitmap);
                dialog.bUploadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog.getImageName().length() > 0){
                            uploadImageToDatabase(dialog.getImageName());
                            uploadImageToStorage(dialog.getImageName());
                            dialog.close();
                        }else {
                            Toast.makeText(context, "Debes escribir un nombre", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
