package com.sergioloc.hologram;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * Created by Sergio López Ceballos on 17/01/2018.
 */

public class Auth extends AppCompatActivity {

    private TextView guest, title;
    private EditText signIn_email,signIn_password,signUp_email,signUp_password,signUp_password2;
    private CircularProgressButton signIn_button, signUp_button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private int viewBox;
    private FloatingActionButton change;
    private MaterialTextField signIn_fieldEmail,signIn_fieldPass, signUp_fieldEmail, signUp_fieldPass, signUp_fieldPass2;
    private RelativeLayout signInLayout, signUpLayout;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        view = findViewById(R.id.activity_main);
        init();
        buttons();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("SESION", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent i = new Intent(Auth.this,Navigation.class);
                    i.putExtra("guest",false);
                    startActivity(i);
                } else {
                    // User is signed out
                    Log.d("SESION", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };



    }

    private void init(){
        viewBox = 1;
        mAuth = FirebaseAuth.getInstance();
        title = (TextView) findViewById(R.id.title);
        change = (FloatingActionButton) findViewById(R.id.change);

        // Sign In
        signIn_email = (EditText) findViewById(R.id.email);
        signIn_password = (EditText) findViewById(R.id.password);
        signIn_button = (CircularProgressButton) findViewById(R.id.enter);
        signIn_fieldEmail = (MaterialTextField) findViewById(R.id.email_box);
        signIn_fieldPass = (MaterialTextField) findViewById(R.id.password_box);
        signInLayout = (RelativeLayout) findViewById(R.id.signInLayout);
        guest = (TextView) findViewById(R.id.guest);

        // Sign Up
        signUp_email = (EditText) findViewById(R.id.email2);
        signUp_password = (EditText) findViewById(R.id.password2);
        signUp_password2 = (EditText) findViewById(R.id.password2R);
        signUp_button = (CircularProgressButton) findViewById(R.id.register);
        signUp_fieldEmail = (MaterialTextField) findViewById(R.id.email_box_2);
        signUp_fieldPass = (MaterialTextField) findViewById(R.id.password_box_2);
        signUp_fieldPass2 = (MaterialTextField) findViewById(R.id.password_box_2_R);
        signUpLayout = (RelativeLayout) findViewById(R.id.signUpLayout);

        signIn_button.setBackgroundColor(getResources().getColor(R.color.turquoise));
        signUp_button.setBackgroundColor(getResources().getColor(R.color.turquoise));
    }

    private void buttons(){

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Auth.this,Navigation.class);
                i.putExtra("guest",true);
                startActivity(i);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });


        signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn_button.startAnimation();
                try{
                    final String emailS = signIn_email.getText().toString().trim();
                    final String passwordS = signIn_password.getText().toString().trim();

                    if(emailS.equals("") && passwordS.equals("")){ //Email y contraseña en blanco
                        failToEnter(1);
                    }else if(emailS.equals("") && !passwordS.equals("")){ //Email en blanco
                        failToEnter(2);
                    }else if(!emailS.equals("") && passwordS.equals("")){ //Contraseña en blanco
                        failToEnter(3);
                    }else{ // Campos rellenados
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                signIn(emailS,passwordS);
                            }
                        }, 2500);
                    }

                }catch (Exception e){
                    snackbar("Error al iniciar sesión");
                    signIn_button.revertAnimation();
                }
            }
        });

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    final String emailS = signUp_email.getText().toString().trim();
                    final String passwordS = signUp_password.getText().toString().trim();
                    String password2S = signUp_password2.getText().toString().trim();

                    if(emailS.equals("") || passwordS.equals("") || password2S.equals("")) {
                        snackbar("Debes rellenar todos los campos");
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_error);
                        signUp_button.doneLoadingAnimation(getResources().getColor(R.color.turquoise), icon);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                signUp_button.revertAnimation();
                            }
                        }, 2000);
                    }else{
                        if(passwordS.equals(password2S)){
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    signUp(emailS,passwordS);
                                }
                            }, 2500);
                        }else{
                            snackbar("La contrasena no coincide");
                            signUp_password.setText("");
                            signUp_password2.setText("");
                            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_error);
                            signUp_button.doneLoadingAnimation(getResources().getColor(R.color.turquoise),icon);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    signUp_button.revertAnimation();
                                }
                            }, 2000);
                        }

                    }

                }catch (Exception e){
                    snackbar("Error");
                    signUp_button.revertAnimation();
                }
            }
        });



    }

    private void changeView(){
        if(viewBox==1){
            int x = signInLayout.getRight();
            int y = signInLayout.getBottom();
            int startRadius = 0;
            int endRadius = (int) Math.hypot(signInLayout.getWidth(),signInLayout.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(signUpLayout,x,y,startRadius,endRadius);
            signUpLayout.setVisibility(View.VISIBLE);
            anim.start();
            title.setText("SIGN UP");

            viewBox=2;

        }else{
            int x = signUpLayout.getRight();
            int y = signUpLayout.getBottom();
            int startRadius = (int) Math.hypot(signInLayout.getWidth(),signInLayout.getHeight());
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(signUpLayout, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    signUpLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.start();
            title.setText("SIGN IN");
            viewBox=1;

        }
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SESION", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w("SESION", "signInWithEmail:failed", task.getException());
                            Toast.makeText(Auth.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Bitmap icon2 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_error);
                            signIn_button.doneLoadingAnimation(getResources().getColor(R.color.turquoise),icon2);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    signIn_button.revertAnimation();
                                }
                            }, 2000);

                        }else{
                            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_done);
                            signIn_button.doneLoadingAnimation(getResources().getColor(R.color.turquoise),icon);
                        }

                    }
                });

    }

    public void signUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SESION", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(Auth.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Bitmap icon2 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_error);
                            signUp_button.doneLoadingAnimation(getResources().getColor(R.color.turquoise),icon2);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    signUp_button.revertAnimation();
                                }
                            }, 2000);
                        }else{
                            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_done);
                            signUp_button.doneLoadingAnimation(getResources().getColor(R.color.turquoise),icon);
                        }

                    }
                });
    }

    private void failToEnter(int op){
        if(op==1){
            snackbar("Debes rellenar todos los campos");
            signIn_fieldEmail.setBackgroundColor(getResources().getColor(R.color.red));
            signIn_fieldPass.setBackgroundColor(getResources().getColor(R.color.red));
        }else if(op ==2){
            snackbar("Debes introducir el email");
            signIn_fieldEmail.setBackgroundColor(getResources().getColor(R.color.red));
        }else if(op ==3){
            snackbar("Debes introducir la contraseña");
            signIn_fieldPass.setBackgroundColor(getResources().getColor(R.color.red));
        }
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_error);
        signIn_button.doneLoadingAnimation(getResources().getColor(R.color.turquoise),icon);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                signIn_button.revertAnimation();
            }
        }, 2000);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void snackbar(String text){
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

}