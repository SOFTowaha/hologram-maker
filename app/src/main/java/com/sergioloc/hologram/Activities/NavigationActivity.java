package com.sergioloc.hologram.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sergioloc.hologram.Fragments.GalleryFragment;
import com.sergioloc.hologram.Fragments.HomeFragment;
import com.sergioloc.hologram.Fragments.HowFragment;
import com.sergioloc.hologram.Fragments.ListFragment;
import com.sergioloc.hologram.R;
import com.sergioloc.hologram.Views.LoginActivity;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView image;
    private TextView name,email;
    private Button sesion;
    private Boolean guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        // Invitado
        guest = getIntent().getExtras().getBoolean("guest");

        // Cabecera
        image = (ImageView) header.findViewById(R.id.image);
        name = (TextView) header.findViewById(R.id.name);
        email = (TextView) header.findViewById(R.id.email);
        sesion = (Button) header.findViewById(R.id.sesion);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            sesion.setVisibility(View.INVISIBLE);
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            image.setImageURI(user.getPhotoUrl());
        }else{
            image.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);

            sesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment=null;

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        }else if (id == R.id.nav_gallery){
            fragment = new GalleryFragment(guest);
        }else if (id == R.id.nav_list) {
            fragment = new ListFragment(guest);
        }else if (id == R.id.nav_pyramid) {
            fragment = new HowFragment();
        }else if (id == R.id.nav_close) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
        }else if (id == R.id.nav_share) {

        }

        try{
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }



        return true;
    }
}
