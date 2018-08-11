package com.sergioloc.hologram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sergioloc.hologram.Fragments.ListFragment;
import com.sergioloc.hologram.Models.VideoModel;
import com.sergioloc.hologram.R;
import com.sergioloc.hologram.Activities.PlayerActivity;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.ArrayList;

/**
 * Created by Sergio López on 14/09/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;

    private static final int VIEW_TYPE_BOX = 1;
    private static final int VIEW_TYPE_LIST = 2;
    private static final int VIEW_TYPE_LIST_FAV = 3;
    private static final int VIEW_TYPE_BOX_FAV = 4;


    public static boolean FAV_LIST = false;

    public static ArrayList<VideoModel> fav_list;
    public static ArrayList<Integer> fav_id;
    private SwipeRevealLayout lastSwipeLayout;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference userFav, userHide;
    private FirebaseUser user;


    private ArrayList<VideoModel> array;
    private GridLayoutManager mLayoutManager;
    private Context context;
    private Boolean guest;

    public RecyclerAdapter(ArrayList<VideoModel> array, GridLayoutManager layoutManager, Boolean guest){
        this.array=array;
        this.mLayoutManager=layoutManager;
        this.guest = guest;
    }

    @Override
    public int getItemViewType(int position) {
        int spanCount = mLayoutManager.getSpanCount();
        if (spanCount == SPAN_COUNT_ONE) {
            if(FAV_LIST){
                return VIEW_TYPE_LIST_FAV;
            }else{
                return VIEW_TYPE_LIST;
            }
        } else {
            if (FAV_LIST){
                return VIEW_TYPE_BOX_FAV;
            }else{
                return VIEW_TYPE_BOX;
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (viewType == VIEW_TYPE_LIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video, parent, false);
        } else if(viewType == VIEW_TYPE_LIST_FAV){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video_fav, parent, false);
        } else if(viewType == VIEW_TYPE_BOX){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video_box, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video_box_fav, parent, false);
        }
        context = view.getContext();
        return new MyViewHolder(view, viewType);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        int id = context.getResources().getIdentifier(array.get(position).getImage(), "drawable", context.getPackageName());
        int name = context.getResources().getIdentifier(array.get(position).getName(), "string", context.getPackageName());
        holder.image.setImageResource(id);
        holder.text.setText(context.getResources().getString(name));

        if(guest){

            holder.shieldFav.setVisibility(View.VISIBLE);
            holder.shieldHide.setVisibility(View.VISIBLE);

            holder.shieldFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Debes estar registrado para añadir a favoritos",Toast.LENGTH_SHORT).show();
                }
            });
            holder.shieldHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Debes estar registrado para ocultar",Toast.LENGTH_SHORT).show();
                }
            });

            if (holder.type == 1 || holder.type == 2){
                holder.swipe_layout.close(true);
                colorTags(holder,position);
            }

        }else{
            if(holder.type == 1 || holder.type == 2)
                colorTags(holder,position);

            initFav();

            if (holder.type == 1 || holder.type == 3){ // Lista general
                loadInfo(holder,position);
            }else { // Lista favoritos
                loadInfoFav(holder,position);
            }

            if(holder.type == 1 || holder.type == 2)
                holder.swipe_layout.close(true);

            holder.swipe_layout.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
                @Override
                public void onClosed(SwipeRevealLayout view) {

                }

                @Override
                public void onOpened(SwipeRevealLayout view) {
                    if (lastSwipeLayout == null){
                        lastSwipeLayout = holder.swipe_layout;
                    }else {
                        lastSwipeLayout.close(true);
                        lastSwipeLayout = holder.swipe_layout;
                    }
                }

                @Override
                public void onSlide(SwipeRevealLayout view, float slideOffset) {
                }
            });


        }

        holder.button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),PlayerActivity.class);
                i.putExtra("id",ListFragment.actualList.get(position).getCode());
                context.startActivity(i);
            }
        });



    }

    /**Firebase**/

    public void initFav(){
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userFav = database.getReference("users").child(user.getUid()).child("fav");
        userHide = database.getReference("users").child(user.getUid()).child("hide");
    }
    public void addFav(int position){
        String name;
        int id = ListFragment.actualList.get(position).getId();
        if(id<10)
            name="video00"+id;
        else if(id<100)
            name="video0"+id;
        else
            name="video"+id;
        userFav.child(name).setValue(ListFragment.actualList.get(position));
    }
    public void removeFav(int position){
        String name;
        int id = ListFragment.actualList.get(position).getId();
        if(id<10)
            name="video00"+id;
        else if(id<100)
            name="video0"+id;
        else
            name="video"+id;
        userFav.child(name).removeValue();
    }
    public void addHide(int position){
        String name;
        int id = ListFragment.actualList.get(position).getId();
        if(id<10)
            name="video00"+id;
        else if(id<100)
            name="video0"+id;
        else
            name="video"+id;
        int nameSnackbar = context.getResources().getIdentifier(ListFragment.actualList.get(position).getName(), "string", context.getPackageName());
        showSnackbar(context.getResources().getString(nameSnackbar),id,name);
        userHide.child(name).setValue(ListFragment.actualList.get(position));
    }
    public void removeHide(int position){
        String name;
        int id = ListFragment.actualList.get(position).getId();
        if(id<10)
            name="video00"+id;
        else if(id<100)
            name="video0"+id;
        else
            name="video"+id;
        userHide.child(name).removeValue();
    }


    /**Views**/

    private void loadInfo(final MyViewHolder holder, final int position){
        holder.bFav.setChecked(false);
        userFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VideoModel video = snapshot.getValue(VideoModel.class);

                    int name = context.getResources().getIdentifier(video.getName(), "string", context.getPackageName());
                    if(holder.text.getText().toString().equals(context.getResources().getString(name))){
                        holder.bFav.setChecked(true);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Favorito
        holder.bFav.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState){ //active
                    Toast.makeText(context,"Añadido a favoritos",Toast.LENGTH_SHORT).show();
                    addFav(position);
                }else{ //inactive
                    Toast.makeText(context,"Eliminado de favoritos",Toast.LENGTH_SHORT).show();
                    removeFav(position);
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        // Ocultar
        holder.bHide.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState){ //active
                    Toast.makeText(context,"Añadido a ocultos",Toast.LENGTH_SHORT).show();
                    addHide(position);
                }else{ //inactive
                    Toast.makeText(context,"Eliminado de ocultos",Toast.LENGTH_SHORT).show();
                    removeHide(position);
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

    }
    private void loadInfoFav(final MyViewHolder holder, final int position){
        holder.bFav.setChecked(true);
        holder.bFav.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState){ //active
                    Toast.makeText(context,"Añadido a favoritos",Toast.LENGTH_SHORT).show();
                    addFav(position);
                }else{ //inactive
                    Toast.makeText(context,"Eliminado de favoritos",Toast.LENGTH_SHORT).show();
                    removeFav(position);
                }
            }
            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
    }



    /**Functions**/

    private void colorTags(final MyViewHolder holder, final int position){
        if(array.get(position).getTag().equals("Animals")){
            holder.tag.setBackground(context.getResources().getDrawable(R.drawable.circle_orange));
        }else if(array.get(position).getTag().equals("Films")){
            holder.tag.setBackground(context.getResources().getDrawable(R.drawable.circle_blue));
        }else if(array.get(position).getTag().equals("Space")){
            holder.tag.setBackground(context.getResources().getDrawable(R.drawable.circle_pink));
        }else if(array.get(position).getTag().equals("Natur")){
            holder.tag.setBackground(context.getResources().getDrawable(R.drawable.circle_green));
        }else if(array.get(position).getTag().equals("Music")){
            holder.tag.setBackground(context.getResources().getDrawable(R.drawable.circle_cyan));
        }else if(array.get(position).getTag().equals("Figures")){
            holder.tag.setBackground(context.getResources().getDrawable(R.drawable.circle_yellow));
        }else if(array.get(position).getTag().equals("Others")){
            holder.tag.setBackground(context.getResources().getDrawable(R.drawable.circle_purple));
        }
    }
    private void showSnackbar(String text, final int id, final String name){
        Snackbar snackbar = Snackbar
                .make(ListFragment.recyclerView, text+" hide", 3000)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userHide.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    VideoModel video = snapshot.getValue(VideoModel.class);
                                    if(video.getId()==id)
                                        userHide.child(name).removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

        snackbar.show();
    }
    private void animOut(final MyViewHolder holder){
        float translationX;
        translationX = holder.swipe_layout.getTranslationX() == 0 ? holder.swipe_layout.getWidth() : 0;
        holder.swipe_layout.animate().translationX(-translationX).start();
    }


    public void setFilter(ArrayList<VideoModel> newList){
        array= new ArrayList<>();
        array.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    //----------------------------------------------------------------------------------------------
    // Internal class

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image, tag, shieldFav, shieldHide;
        TextView text;
        Button button;
        int type;
        SwipeRevealLayout swipe_layout;
        SparkButton bFav,bHide;

        public MyViewHolder(final View itemView, int viewType) {
            super(itemView);
            fav_list = new ArrayList();
            fav_id = new ArrayList<>();
            if (viewType == VIEW_TYPE_LIST) {
                image = (ImageView) itemView.findViewById(R.id.image_big);
                text = (TextView) itemView.findViewById(R.id.title_big);
                button = (Button) itemView.findViewById(R.id.button_big);
                bFav = itemView.findViewById(R.id.button_fav_big);
                bHide = itemView.findViewById(R.id.button_hide_big);
                shieldFav = (ImageView) itemView.findViewById(R.id.button_fav_big_copy);
                shieldHide = (ImageView) itemView.findViewById(R.id.button_hide_big_copy);
                tag = (ImageView) itemView.findViewById(R.id.iv_tag);
                swipe_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout_big);
                type=1;
            }else if (viewType == VIEW_TYPE_LIST_FAV){
                image = (ImageView) itemView.findViewById(R.id.image_list_fav);
                text = (TextView) itemView.findViewById(R.id.title_list_fav);
                button = (Button) itemView.findViewById(R.id.button_list_fav);
                bFav = itemView.findViewById(R.id.button_fav_list);
                tag = (ImageView) itemView.findViewById(R.id.iv_tag_fav);
                swipe_layout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout_list_fav);
                type=2;
            }else if (viewType == VIEW_TYPE_BOX){
                image = (ImageView) itemView.findViewById(R.id.image_small);
                text = (TextView) itemView.findViewById(R.id.title_small);
                button = (Button) itemView.findViewById(R.id.button_small);
                bFav = itemView.findViewById(R.id.button_fav_small);
                bHide = itemView.findViewById(R.id.button_hide_small);
                shieldFav = (ImageView) itemView.findViewById(R.id.ivShieldFavBox);
                shieldHide = (ImageView) itemView.findViewById(R.id.ivShieldHideBox);
                type=3;
            }else {
                image = (ImageView) itemView.findViewById(R.id.image_small);
                text = (TextView) itemView.findViewById(R.id.title_small);
                button = (Button) itemView.findViewById(R.id.button_small);
                bFav = itemView.findViewById(R.id.button_fav_small);
                type=4;
            }
        }
    }

}

