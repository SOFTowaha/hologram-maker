package com.sergioloc.hologram.Viewers;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.Display;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.sergioloc.hologram.Navigation;
import com.sergioloc.hologram.R;

import java.io.IOException;
import java.io.InputStream;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ImageViewer extends AppCompatActivity {

    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100, PHOTO_CODE = 200, SELECT_PICTURE = 300;

    private ImageView picture1, picture2, picture3, picture4;
    private String mPath;
    private Button button;
    private Uri uri;
    //Firebase
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference images;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        init();

    }

    private void init(){
        button = (Button) findViewById(R.id.buttonX);
        picture1 = (ImageView) findViewById(R.id.imageView1);
        picture2 = (ImageView) findViewById(R.id.imageView2);
        picture3 = (ImageView) findViewById(R.id.imageView3);
        picture4 = (ImageView) findViewById(R.id.imageView4);
        /*uri = Uri.parse(getIntent().getExtras().getString("imagUri"));
        Glide.with(this)
                .load(uri)
                .into(picture1);
        Glide.with(this)
                .load(uri)
                .into(picture2);
        Glide.with(this)
                .load(uri)
                .into(picture3);
        Glide.with(this)
                .load(uri)
                .into(picture4);*/
    }

    private void buttons(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void onBackPressed(){
        Intent i = new Intent(ImageViewer.this, Navigation.class);
        startActivity(i);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path",mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE: // TOMAR FOTO
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                   Log.i("ExternalStorage", "Scanned " + path + ":");
                                   Log.i("ExternalStorege", "-> Uri = " + uri);
                                }
                            });

                    // PONER IMAGEN EN IMAGEVIEW
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    picture1.setImageBitmap(bitmap);
                    break;

                case SELECT_PICTURE: // COGER DE GALERIA
                    try {
                        Uri path = data.getData();
                        picture1.setImageURI(path);
                        picture2.setImageURI(path);
                        picture3.setImageURI(path);
                        picture4.setImageURI(path);

                        // ROTAR IMAGENES
                        BitmapDrawable drawable = (BitmapDrawable) picture1.getDrawable();
                        Bitmap bitmap2 = drawable.getBitmap();
                        Matrix matrix1 = new Matrix();
                        Matrix matrix3 = new Matrix();
                        Matrix matrix4 = new Matrix();
                        matrix1.postRotate(180);
                        matrix3.postRotate(90);
                        matrix4.postRotate(-90);
                        Bitmap rotated1 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix1, true);
                        Bitmap rotated3 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix3, true);
                        Bitmap rotated4 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix4, true);
                        picture1.setImageBitmap(rotated1);
                        picture3.setImageBitmap(rotated3);
                        picture4.setImageBitmap(rotated4);


                    }catch (Exception e){Toast.makeText(getApplicationContext(), "La imagen es demasiado grande", Toast.LENGTH_SHORT).show();}
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(ImageViewer.this, "Permisos Aceptados", Toast.LENGTH_LONG).show();
                //gallery.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageViewer.this);
        builder.setTitle("Permisos Denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

}


