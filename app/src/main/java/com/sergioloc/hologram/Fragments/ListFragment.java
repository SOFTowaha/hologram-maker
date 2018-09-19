package com.sergioloc.hologram.Fragments;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sergioloc.hologram.Adapter.RecyclerAdapter;
import com.sergioloc.hologram.Utils.DividerItemDecoration;
import com.sergioloc.hologram.Models.VideoModel;
import com.sergioloc.hologram.R;
import com.sergioloc.hologram.Activities.SettingsActivity;
import com.vpaliy.chips_lover.ChipView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInRightAnimationAdapter;


public class ListFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener{


    public static RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<VideoModel> videosList,favList,hideList,mergeTags,tags1,tags2,tags3,tags4,
            tags5,tags6,tags7;
    public static ArrayList<VideoModel> actualList;
    private Context context;
    private int count;
    public static boolean favSelected;
    private FloatingActionsMenu fab_menu;
    private FloatingActionButton fab_fav, fab_settings;
    private GridLayoutManager gridLayoutManager;
    private View view,controlsHandle;
    public static ArrayList<Integer> favId;
    //Firebase
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference ref, userFav, userHide;
    //Tags
    public static TextView text;
    private TextView blackSpace, tvNoConnection;
    private ChipView chip0,chip1,chip2,chip3,chip4,chip5,chip6,chip7,chipUp;
    private ImageView arrowTag, ivNoConnection;
    private boolean tagSelected,tagsOpen,arrowDown,chip0IsChecked,chip1IsChecked,chip2IsChecked,
            chip3IsChecked,chip4IsChecked,chip5IsChecked,chip6IsChecked,chip7IsChecked;
    private FlexboxLayout flexboxLayout;
    private Boolean guest;
    private DividerItemDecoration divider;

    private View toolbarShadow;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int typeView;


    @SuppressLint("ValidFragment")
    public ListFragment(Boolean guest) {
        this.guest = guest;
    }

    public ListFragment(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        view = inflater.inflate(R.layout.fragment_list, container, false);

        initView();
        initVariables();

        if(!guest){ // No invitado
            //Firebase user
            user = FirebaseAuth.getInstance().getCurrentUser();
            userFav = database.getReference("users").child(user.getUid()).child("fav");
            userHide = database.getReference("users").child(user.getUid()).child("hide");
            hideList = new ArrayList<>();
            initFirebase();
        }else{ // Invitado
            initFirebaseGuest();
        }

        initFab();
        initTags();

        checkList(3500);


        return view;
    }


    /**Inits**/

    public void initView(){
        ivNoConnection = (ImageView) view.findViewById(R.id.ivNoConnection);
        tvNoConnection = (TextView) view.findViewById(R.id.tvNoConnection);
    }

    public void initVariables() {

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        context = activity.getApplicationContext();
        activity.setTitle("Catalog");

        divider = new DividerItemDecoration(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(divider);

        count = 0;
        chipUp = (ChipView) view.findViewById(R.id.chipUp);
        text = (TextView) view.findViewById(R.id.textoCont);
        toolbarShadow = (View) view.findViewById(R.id.toolbarShadow);
        favSelected=false;
        tagSelected = false;
        tagsOpen = false;
        favId=new ArrayList<>();

        //Firebase
        database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);

        // Recycler
        videosList = new ArrayList<>();
        actualList = new ArrayList<>();

        prefs = getActivity().getSharedPreferences("prefView",Context.MODE_PRIVATE);
        editor = prefs.edit();
        typeView = prefs.getInt("typeView", 1);
        gridLayoutManager = new GridLayoutManager(context, typeView);
        adapter = new RecyclerAdapter(actualList, gridLayoutManager, guest);
        recyclerView.setAdapter(adapter);


        // Recycler Swipe
        //swipeController = new SwipeController();
        //itemTouchhelper = new ItemTouchHelper(swipeController);
        //itemTouchhelper.attachToRecyclerView(recyclerView);

        //loadAnimList(1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                adapter.closeLastSwipeLayout();
            }
        });

    }

    public void initFirebaseGuest() {
        videosList=new ArrayList<>();
        // Videos
        ref = database.getReference("videos");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final VideoModel video = snapshot.getValue(VideoModel.class);
                    videosList.add(video);
                }
                //actualList=videosList;
                actualList.addAll(videosList);
                text.setText(actualList.size() + " videos");
                adapter.setFilter(actualList);
                initTagsList();
                ivNoConnection.setVisibility(View.GONE);
                tvNoConnection.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initFirebase() {
        videosList=new ArrayList<>();
        // Videos
        ref = database.getReference("videos");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                hideList.removeAll(hideList);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final VideoModel video = snapshot.getValue(VideoModel.class);
                    Log.i("HIDE","==================");
                    if(false){ // No logeado
                        actualList.add(video);
                        count++;
                    }else{ // Si logeado
                        userHide.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean add = true;
                                Log.i("HIDE","----------------");
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    VideoModel hide = snapshot.getValue(VideoModel.class);
                                    Log.i("HIDE","video "+video.getId()+" hide "+hide.getId());
                                    //if(!repetido(actualList,video)){
                                        if(add){
                                            Log.i("HIDE","Comparo");
                                            if(video.getId()==hide.getId()){
                                                add=false;
                                                Log.i("HIDE","add = false");
                                                actualList.remove(video);
                                                adapter.notifyDataSetChanged();
                                                Log.i("HIDE","video borrado");
                                            }
                                        }else{
                                            Log.i("HIDE","es false");
                                        }
                                    //}else{
                                      //  Log.i("HIDE","repetido");
                                    //}

                                }
                                if(add && !repetido(actualList,video)){
                                    Log.i("HIDE","Añado "+video.getId());
                                    actualList.add(video);
                                    count++;
                                    Log.i("HIDE","Tamaño lista: "+actualList.size());
                                }
                                videosList=actualList;
                                text.setText(actualList.size() + " videos");
                                adapter.notifyDataSetChanged();
                                initTagsList();


                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
                if(false){
                    videosList=actualList;
                    //actualList.addAll(videosList);
                    text.setText(count + " videos");
                    adapter.notifyDataSetChanged();
                    initTagsList();
                    ivNoConnection.setVisibility(View.GONE);
                    tvNoConnection.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initFab() {
        fab_menu = (FloatingActionsMenu) view.findViewById(R.id.menu_fab);
        fab_fav = (FloatingActionButton) view.findViewById(R.id.fb_fav);
        fab_settings = (FloatingActionButton) view.findViewById(R.id.fb_settings);
        fab_menu.setScaleX(1);
        fab_menu.setScaleY(1);

        /**Hide FAB on Scroll**/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab_menu.getVisibility() == View.VISIBLE) {
                    animFab(1);
                    fab_menu.collapse();
                    fab_menu.setVisibility(View.INVISIBLE);
                } else if (dy < 0 && fab_menu.getVisibility() != View.VISIBLE) {
                    animFab(2);
                    fab_menu.setVisibility(View.VISIBLE);
                }
                /*
                if(recyclerView.getScrollState()<10)
                    chipUp.setVisibility(View.VISIBLE);
                else
                    chipUp.setVisibility(View.INVISIBLE);
                    */
            }
        });

        /**Favourite**/
        fab_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!guest){
                    favButton();
                    fab_menu.collapse();
                }else{
                    Toast.makeText(getContext(), "Debes estar registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SettingsActivity.class));
            }
        });


        /**Chip Up**/
        chipUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(0);
            }
        });
    }

    public void initTags(){
        flexboxLayout = (FlexboxLayout) view.findViewById(R.id.control_panel);
        text = (TextView) view.findViewById(R.id.textoCont);
        blackSpace = (TextView) view.findViewById(R.id.black_space);
        controlsHandle = view.findViewById(R.id.control_handle);
        arrowTag = (ImageView) view.findViewById(R.id.arrowTag);
        arrowDown = true;
        tagsOpen=false;

        // Chips
        chip0 = (ChipView) view.findViewById(R.id.chip0);
        chip1 = (ChipView) view.findViewById(R.id.chip1);
        chip2 = (ChipView) view.findViewById(R.id.chip2);
        chip3 = (ChipView) view.findViewById(R.id.chip3);
        chip4 = (ChipView) view.findViewById(R.id.chip4);
        chip5 = (ChipView) view.findViewById(R.id.chip5);
        chip6 = (ChipView) view.findViewById(R.id.chip6);
        chip7 = (ChipView) view.findViewById(R.id.chip7);
        chip0IsChecked = true;
        chip1IsChecked = false;
        chip2IsChecked = false;
        chip3IsChecked = false;
        chip4IsChecked = false;
        chip5IsChecked = false;
        chip6IsChecked = false;
        chip7IsChecked = false;
        chip0.setOnClickListener(this);
        chip1.setOnClickListener(this);
        chip2.setOnClickListener(this);
        chip3.setOnClickListener(this);
        chip4.setOnClickListener(this);
        chip5.setOnClickListener(this);
        chip6.setOnClickListener(this);
        chip7.setOnClickListener(this);
        select(0);

        controlsHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tagsOpen){
                    animation(1);
                }else{
                    animation(0);
                }
                switchArrow();
            }
        });
    }

    public void initTagsList() {

        tags1 = new ArrayList<>();
        tags2 = new ArrayList<>();
        tags3 = new ArrayList<>();
        tags4 = new ArrayList<>();
        tags5 = new ArrayList<>();
        tags6 = new ArrayList<>();
        tags7 = new ArrayList<>();

        for (int i = 0; i < actualList.size(); i++) {
            if (actualList.get(i).getTag().equals("Animals")) {
                tags1.add(actualList.get(i));
            }
        }
        for (int i = 0; i < actualList.size(); i++) {
            if (actualList.get(i).getTag().equals("Films")) {
                tags2.add(actualList.get(i));
            }
        }
        for (int i = 0; i < actualList.size(); i++) {
            if (actualList.get(i).getTag().equals("Space")) {
                tags3.add(actualList.get(i));
            }
        }
        for (int i = 0; i < actualList.size(); i++) {
            if (actualList.get(i).getTag().equals("Natur")) {
                tags4.add(actualList.get(i));
            }
        }
        for (int i = 0; i < actualList.size(); i++) {
            if (actualList.get(i).getTag().equals("Music")) {
                tags5.add(actualList.get(i));
            }
        }
        for (int i = 0; i < actualList.size(); i++) {
            if (actualList.get(i).getTag().equals("Figures")) {
                tags6.add(actualList.get(i));
            }
        }
        for (int i = 0; i < actualList.size(); i++) {
            if (actualList.get(i).getTag().equals("Others")) {
                tags7.add(actualList.get(i));
            }
        }
    }



    /**Animations**/

    private void animFab(int i) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Entrada lenta
            final Interpolator interpolador = AnimationUtils.loadInterpolator(getContext(),
                    android.R.interpolator.fast_out_slow_in);
            //Botar
            final Interpolator interpolador2 = AnimationUtils.loadInterpolator(getContext(),
                    android.R.interpolator.bounce);

            if (i == 1) { // Desaparece
                fab_menu.animate()
                        .scaleX(0)
                        .scaleY(0)
                        .setInterpolator(interpolador)
                        .setDuration(600)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
            } else { // Aparece
                fab_menu.animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setInterpolator(interpolador)
                        .setDuration(600)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
            }

        }
    }

    private void switchLayout() {
        if (gridLayoutManager.getSpanCount() == RecyclerAdapter.SPAN_COUNT_ONE) {
            gridLayoutManager.setSpanCount(RecyclerAdapter.SPAN_COUNT_THREE);
            recyclerView.removeItemDecoration(divider);
        } else {
            gridLayoutManager.setSpanCount(RecyclerAdapter.SPAN_COUNT_ONE);
            recyclerView.addItemDecoration(divider);
        }
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());

    }

    public void switchArrow() {
        if (arrowDown) {
            arrowTag.setImageResource(R.drawable.ic_action_arrow_up);
            arrowDown = false;
        } else {
            arrowTag.setImageResource(R.drawable.ic_action_arrow_down);
            arrowDown = true;
        }
    }

    private void switchIcon(MenuItem item) {
        if (gridLayoutManager.getSpanCount() == RecyclerAdapter.SPAN_COUNT_THREE) {
            item.setIcon(R.drawable.ic_action_view_big);
        } else {
            item.setIcon(R.drawable.ic_action_view_small);
        }
    }

    public void animList(int i) {
        // Anim --> AlphaInAnimationAdapter, ScaleInAnimationAdapter, SlideInLeftAnimationAdapter
        if (i == 1) {
            ScaleInAnimationAdapter animScale = new ScaleInAnimationAdapter(adapter);
            animScale.setInterpolator(new OvershootInterpolator());
            animScale.setFirstOnly(false);
            recyclerView.setAdapter(animScale);
        } else if (i == 2) {
            SlideInRightAnimationAdapter animSlide = new SlideInRightAnimationAdapter(adapter);
            animSlide.setInterpolator(new OvershootInterpolator());
            animSlide.setFirstOnly(false);
            recyclerView.setAdapter(animSlide);
        }

        //recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter)); //Multiple Anims
    }

    public void animation(int i) {
        float translationY;
        if(i==0){
            translationY = flexboxLayout.getTranslationY() == 0 ? flexboxLayout.getHeight() : 0;
        }else{
            translationY = flexboxLayout.getTranslationY() == 0 ? 0 : flexboxLayout.getHeight();
        }
        controlsHandle.animate().translationY(translationY).start();
        recyclerView.animate().translationY(translationY).start();
        arrowTag.animate().translationY(translationY).start();
        text.animate().translationY(translationY).start();
        blackSpace.animate().translationY(translationY).start();
        toolbarShadow.animate().translationY(translationY).start();
        if (tagsOpen) tagsOpen = false;
        else tagsOpen = true;
    }


    /**Buttons**/

    private void favButton(){
        select(0);
        deselect(1);
        deselect(2);
        deselect(3);
        deselect(4);
        deselect(5);
        deselect(6);
        deselect(7);
        if(favSelected){ // Volver de favs
            favSelected=false;
            //actualList=new ArrayList<>();
            //initFirebase();
            actualList=new ArrayList<>();
            actualList.addAll(videosList);
            initTagsList();
            adapter.setFilter(actualList);

            //Cambiar vista
            fab_fav.setIcon(R.drawable.ic_action_fav);
            RecyclerAdapter.FAV_LIST = false;
            getActivity().setTitle("Catalog");
            text.setText(videosList.size() + " videos");
        }
        else{ //Ir a favs
            favSelected=true;
            favList=new ArrayList<VideoModel>();

            userFav.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    favList = new ArrayList<VideoModel>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        VideoModel video = snapshot.getValue(VideoModel.class);
                        favList.add(video);
                        favId.add(video.getId());
                    }
                    //orderByName(favList);
                    actualList = new ArrayList<VideoModel>();
                    actualList.addAll(favList);
                    adapter.setFilter(actualList);
                    initTagsList();

                    //Cambiar vista
                    fab_fav.setIcon(R.drawable.ic_action_search);
                    RecyclerAdapter.FAV_LIST = true;
                    try{
                        getActivity().setTitle("Favourites");
                    }catch (Exception e){
                        Toast.makeText(context,"Error al cambiar titulo",Toast.LENGTH_SHORT).show();
                    }
                    text.setText(actualList.size() + " videos");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



    /**Functions**/

    public void checkList(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Acciones que se ejecutan tras los milisegundos
                if (actualList.size() == 0) {
                    ivNoConnection.setVisibility(View.VISIBLE);
                    tvNoConnection.setVisibility(View.VISIBLE);
                    /*
                    Toast.makeText(context, "Internet connexion is required",
                            Toast.LENGTH_LONG).show();
                            */
                    text.setText(videosList.size() + " videos");
                }
            }
        }, milisegundos);
    }

    public void orderByName(ArrayList<VideoModel> list) {
        Collections.sort(list, new Comparator<VideoModel>() {
            @Override
            public int compare(VideoModel item, VideoModel t1) {
                String s1, s2;
                s1 = item.getName().toString();
                s2 = t1.getName().toString();
                return s1.compareTo(s2);
            }
        });
    }

    private boolean repetido(ArrayList<VideoModel> array, VideoModel video){
        for(int i=0;i<array.size();i++){
            if(array.get(i).getId()==video.getId())
                return true;
        }
        return false;
    }


    /**Tags**/

    public boolean checkAll() {
        if (chip1IsChecked && chip2IsChecked && chip3IsChecked && chip4IsChecked && chip5IsChecked
                && chip6IsChecked && chip7IsChecked) {
            select(0);
            deselect(1);
            deselect(2);
            deselect(3);
            deselect(4);
            deselect(5);
            deselect(6);
            deselect(7);
            chip0IsChecked = true;
            return true;
        }
        return false;
    }

    public void deselect(int i) {
        switch (i) {
            case 0:
                chip0IsChecked = false;
                chip0.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                chip0.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 1:
                chip1IsChecked = false;
                chip1.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                chip1.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 2:
                chip2IsChecked = false;
                chip2.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                chip2.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 3:
                chip3IsChecked = false;
                chip3.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                chip3.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 4:
                chip4IsChecked = false;
                chip4.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                chip4.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 5:
                chip5IsChecked = false;
                chip5.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                chip5.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 6:
                chip6IsChecked = false;
                chip6.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                chip6.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 7:
                chip7IsChecked = false;
                chip7.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                chip7.setTextColor(context.getResources().getColor(R.color.black));
                break;


        }
    }

    public void select(int i) {
        switch (i) {
            case 0:
                chip0IsChecked = true;
                chip0.setBackgroundColor(context.getResources().getColor(R.color.red));
                chip0.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 1:
                chip1IsChecked = true;
                chip1.setBackgroundColor(context.getResources().getColor(R.color.orange));
                chip1.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 2:
                chip2IsChecked = true;
                chip2.setBackgroundColor(context.getResources().getColor(R.color.blue));
                chip2.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 3:
                chip3IsChecked = true;
                chip3.setBackgroundColor(context.getResources().getColor(R.color.pink));
                chip3.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 4:
                chip4IsChecked = true;
                chip4.setBackgroundColor(context.getResources().getColor(R.color.green));
                chip4.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 5:
                chip5IsChecked = true;
                chip5.setBackgroundColor(context.getResources().getColor(R.color.cyan));
                chip5.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 6:
                chip6IsChecked = true;
                chip6.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                chip6.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case 7:
                chip7IsChecked = true;
                chip7.setBackgroundColor(context.getResources().getColor(R.color.purple));
                chip7.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;


        }
    }

    public void mergeAll() {
        mergeTags = new ArrayList<>();

        if (chip0IsChecked) {
            mergeTags.addAll(actualList);
        } else {
            if (chip1IsChecked)
                mergeTags.addAll(tags1);
            if (chip2IsChecked)
                mergeTags.addAll(tags2);
            if (chip3IsChecked)
                mergeTags.addAll(tags3);
            if (chip4IsChecked)
                mergeTags.addAll(tags4);
            if (chip5IsChecked)
                mergeTags.addAll(tags5);
            if (chip6IsChecked)
                mergeTags.addAll(tags6);
            if (chip7IsChecked)
                mergeTags.addAll(tags7);
        }

    }

    @Override
    public void onClick(View view) {
        tagSelected = true;
        boolean allSelected, allButtonSelected = false;
        switch (view.getId()) {
            case R.id.chip0:
                if (chip0IsChecked) {
                    deselect(0);
                } else {
                    select(0);
                    deselect(1);
                    deselect(2);
                    deselect(3);
                    deselect(4);
                    deselect(5);
                    deselect(6);
                    deselect(7);
                    allButtonSelected = true;
                }
                break;
            case R.id.chip1:
                if (chip1IsChecked) {
                    deselect(1);
                } else {
                    select(1);
                }
                break;
            case R.id.chip2:
                if (chip2IsChecked) {
                    deselect(2);
                } else {
                    select(2);
                }
                break;
            case R.id.chip3:
                if (chip3IsChecked) {
                    deselect(3);
                } else {
                    select(3);
                }
                break;
            case R.id.chip4:
                if (chip4IsChecked) {
                    deselect(4);
                } else {
                    select(4);
                }
                break;
            case R.id.chip5:
                if (chip5IsChecked) {
                    deselect(5);
                } else {
                    select(5);
                }
                break;
            case R.id.chip6:
                if (chip6IsChecked) {
                    deselect(6);
                } else {
                    select(6);
                }
                break;
            case R.id.chip7:
                if (chip7IsChecked) {
                    deselect(7);
                } else {
                    select(7);
                }
                break;
        }
        allSelected = checkAll();
        if (chip1IsChecked || chip2IsChecked || chip3IsChecked || chip4IsChecked || chip5IsChecked
                || chip6IsChecked || chip7IsChecked) {
            chip0IsChecked = false;
            deselect(0);
        }
        actualList = new ArrayList<>();

        if((allSelected || allButtonSelected) && favSelected){
            actualList = favList;
        }else if(allSelected || allButtonSelected) {
            actualList = videosList;
        }else{
            mergeAll();
            orderByName(mergeTags);
            actualList=mergeTags;
        }

        adapter.setFilter(actualList);

        text.setText(actualList.size() + " videos");

    }




    /**Menu**/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //startActivity(new Intent(ListFragment.this, NavigationActivity.class));
        } else if (item.getItemId() == R.id.action_search) {
            fab_menu.setVisibility(View.INVISIBLE);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(this);
        } else if (item.getItemId() == R.id.action_change) {
            if (typeView == 1) editor.putInt("typeView",3);
            else editor.putInt("typeView",1);
            editor.commit();
            switchLayout();
            switchIcon(item);
            //loadAnimList(2);
        }
        return super.onOptionsItemSelected(item);
    }


    /**SearchView**/

    @Override
    public boolean onQueryTextSubmit(String query) {
        fab_menu.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        actualList = new ArrayList<>();
        if(favSelected){
            for (VideoModel videoModel : favList) {
                String name = videoModel.getName().toLowerCase();
                if (name.contains(newText)) {
                    actualList.add(videoModel);
                }
            }
        } else if(!tagSelected || chip0IsChecked) {
            for (VideoModel videoModel : videosList) {
                String name = videoModel.getName().toLowerCase();
                if (name.contains(newText)) {
                    actualList.add(videoModel);
                }
            }
        } else{
            for (VideoModel videoModel : mergeTags) {
                String name = videoModel.getName().toLowerCase();
                if (name.contains(newText)) {
                    actualList.add(videoModel);
                }
            }
        }

        text.setText(actualList.size() + " videos");
        adapter.setFilter(actualList);

        return true;
    }


    public void onBackPressed() {
        if (tagsOpen) {
            animation(1);
        } else {
            DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                //super.onBackPressed();
            }
        }
    }

}
