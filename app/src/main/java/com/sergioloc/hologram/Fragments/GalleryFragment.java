package com.sergioloc.hologram.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sergioloc.hologram.Adapter.RecyclerAdapterImageCloud;
import com.sergioloc.hologram.Adapter.RecyclerAdapterImageLocal;
import com.sergioloc.hologram.Dialogs.ImageUploadDialog;
import com.sergioloc.hologram.R;
import com.sergioloc.hologram.Utils.ImageSaver;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment {

    //Basics
    private View view;
    private Context context;
    //Recycler
    private RecyclerView rvImages;
    private GridLayoutManager gridLayoutManager;
    private RecyclerAdapterImageCloud adapterCloud;
    private RecyclerAdapterImageLocal adapterLocal;
    //View
    private TextView tvCloud, tvLocal;
    private ImageButton button;
    private Switch swType;
    private MKLoader loader;
    //Firebase
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference images;
    private StorageReference mStorage;
    //Variables
    private boolean guest, firstLoad;
    private Uri filePath;
    private Boolean cloudView;
    private ImageUploadDialog dialog;
    private final int PICK_IMAGE_REQUEST = 71;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private ArrayList<Bitmap> localList;
    private int localListSize;


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
        firstLoad = true;
        loader = view.findViewById(R.id.loader);
        localList = new ArrayList();
        rvImages = view.findViewById(R.id.rvImages);
        button = view.findViewById(R.id.bAddImage);
        tvCloud = view.findViewById(R.id.tvCloud);
        tvLocal = view.findViewById(R.id.tvLocal);
        swType = view.findViewById(R.id.swType);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        localListSize = prefs.getInt("localListSize",0);
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
                    if (guest){
                        if (!firstLoad)
                            Toast.makeText(context,"Debes iniciar sesión para subir fotos a la nube", Toast.LENGTH_SHORT).show();
                        swType.setChecked(true);
                    }else {
                        if (cloudView){
                            switchLocal();
                        }else {
                            switchCloud();
                        }
                        cloudView = !cloudView;
                        editor.putBoolean("cloudView",cloudView);
                        editor.apply();
                        initView();
                    }
            }
        });
        firstLoad = false;
    }

    private void initView(){
        if(guest || (!guest && !cloudView)){
            initForLocal();
            switchLocal();
            swType.setChecked(true);
        }else {
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
                adapterCloud = new RecyclerAdapterImageCloud(listImages);
                rvImages.setAdapter(adapterCloud);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        loader.setVisibility(View.INVISIBLE);
    }


    /** Local **/

    private void initForLocal(){
        loadFromInteralStorage();
        adapterLocal = new RecyclerAdapterImageLocal(localList);
        rvImages.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(context, 3);
        rvImages.setLayoutManager(gridLayoutManager);
        rvImages.setAdapter(adapterLocal);
    }

    private void saveToInternalStorage(Bitmap bitmap){
        ImageSaver imageSaver = new ImageSaver(context)
                                    .setFileName(""+localListSize+".png")
                                    .setDirectoryName("images").save(bitmap);
        localListSize++;
        editor.putInt("localListSize",localListSize);
        editor.apply();
    }

    private void loadFromInteralStorage(){
        localList = new ArrayList<>();
        for (int i = 0; i < localListSize; i++){
            Bitmap bitmap = new ImageSaver(context).
                    setFileName(""+i+".png").
                    setDirectoryName("images").
                    load();
            localList.add(bitmap);
        }
    }



    /** Functions **/

    private void switchLocal(){
        tvLocal.setTypeface(null, Typeface.BOLD);
        tvCloud.setTypeface(null, Typeface.NORMAL);
        tvLocal.setTextColor(getResources().getColor(R.color.colorWhite));
        tvCloud.setTextColor(getResources().getColor(R.color.colorGrayT));
    }

    private void switchCloud(){
        tvCloud.setTypeface(null, Typeface.BOLD);
        tvLocal.setTypeface(null, Typeface.NORMAL);
        tvCloud.setTextColor(getResources().getColor(R.color.colorWhite));
        tvLocal.setTextColor(getResources().getColor(R.color.colorGrayT));
    }

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
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                    dialog = new ImageUploadDialog(getActivity());
                    dialog.show();
                    dialog.ivImageLoaded.setImageBitmap(bitmap);
                    if (!cloudView)
                        dialog.etImageName.setVisibility(View.INVISIBLE);
                    dialog.bUploadImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (cloudView){
                                if (dialog.getImageName().length() > 0){
                                    uploadImageToDatabase(dialog.getImageName());
                                    uploadImageToStorage(dialog.getImageName());
                                }else {
                                    Toast.makeText(context, "Debes escribir un nombre", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                saveToInternalStorage(bitmap);
                                loadFromInteralStorage();
                                rvImages.setAdapter(adapterLocal);
                            }
                            dialog.close();
                        }
                    });
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
