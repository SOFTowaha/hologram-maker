package com.sergioloc.hologram.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sergioloc.hologram.Interfaces.CatalogInterface
import com.sergioloc.hologram.Models.VideoModel
import com.sergioloc.hologram.Views.PlayerActivity
import com.varunest.sparkbutton.SparkButton
import com.varunest.sparkbutton.SparkEventListener
import java.util.ArrayList
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sergioloc.hologram.Models.FirebaseImage
import com.sergioloc.hologram.R


class RecyclerAdapter(var array: ArrayList<VideoModel>, var layoutManager: GridLayoutManager, var guest: Boolean,
                      var interactor: CatalogInterface.Interactor, var presenter: CatalogInterface.Presenter):
        RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    var fav_list: ArrayList<VideoModel>? = null
    var fav_id: ArrayList<Int>? = null

    var FAV_LIST = false
    var lastSwipeLayout: SwipeRevealLayout? = null

    // Firebase
    private var database: FirebaseDatabase? = null
    private var mStorage: StorageReference? = null
    private var userFav: DatabaseReference? = null
    private var user: FirebaseUser? = null

    // View types
    private val VIEW_TYPE_LIST = 1
    private val VIEW_TYPE_LIST_FAV = 3

    private var context: Context? = null
    private var lastPosition = -1


    override fun getItemViewType(position: Int): Int {
        return if (FAV_LIST) {
                VIEW_TYPE_LIST_FAV
            } else {
                VIEW_TYPE_LIST
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if (!guest)
            initFirebase()
        val view: View = when (viewType) {
            VIEW_TYPE_LIST -> LayoutInflater.from(parent.context).inflate(R.layout.card_video, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.card_video_fav, parent, false)
        }
        context = view.context
        fav_list = ArrayList()
        fav_id = ArrayList()
        return MyViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val id = context?.resources?.getIdentifier(array[position].image, "drawable", context?.packageName)

        holder.text?.text = array[position].name

        var path = "gs://hologram-2.appspot.com/images/catalog/" + array[position].name + ".png"
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(path)
        mStorage?.downloadUrl?.addOnSuccessListener { uri ->
            Glide.with(context!!)
                    .load(uri)
                    .into(holder.image!!)
        }

        val animation = AnimationUtils.loadAnimation(context,
                if (position > lastPosition)
                    R.anim.up_from_bottom
                else
                    R.anim.down_from_top)
        holder.itemView.startAnimation(animation)
        lastPosition = position


        if (guest){
            holder.shieldFav?.visibility = View.VISIBLE
            holder.shieldHide?.visibility = View.VISIBLE
            holder.shieldFav?.setOnClickListener { Toast.makeText(context, R.string.registered_fav, Toast.LENGTH_SHORT).show() }
        }
        else{ //User
            if (holder.type == VIEW_TYPE_LIST) {
                loadList(holder, position)
            } else {
                loadFavList(holder, position)
            }
        }

        setColorTags(holder, position)
        holder.swipeLayout?.close(true)
        holder.swipeLayout?.setSwipeListener(object : SwipeRevealLayout.SwipeListener {
            override fun onClosed(view: SwipeRevealLayout) {}

            override fun onOpened(view: SwipeRevealLayout) {
                if (lastSwipeLayout == null) {
                    lastSwipeLayout = holder.swipeLayout
                } else {
                    lastSwipeLayout?.close(true)
                    lastSwipeLayout = holder.swipeLayout
                }
            }

            override fun onSlide(view: SwipeRevealLayout, slideOffset: Float) {}
        })

        holder.button?.setOnClickListener { v ->
            val i = Intent(v.context, PlayerActivity::class.java)
            i.putExtra("id", interactor.getVideo(position)?.code)
            context?.startActivity(i)
        }
    }

    override fun getItemCount(): Int { return array.size }


    /** Firebase **/

    private fun initFirebase(){
        database = FirebaseDatabase.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        userFav = database?.getReference("users")?.child(user?.uid)?.child("fav")
    }

    private fun addVideoToFav(position: Int){
        userFav?.child(getVideoName(position))?.setValue(interactor.getVideo(position))
    }

    private fun removeVideoFromFav(position: Int){
        presenter?.startLoadingFavList()
        userFav?.child(getVideoName(position))?.removeValue()
        Handler().postDelayed(
                {
                    interactor?.loadFavList()
                    Handler().postDelayed({ presenter?.finishLoadingFavList() }, 20)
                },
                20
        )
    }

    private fun getVideoName(position: Int): String{
        val name: String
        val id = interactor.getVideo(position)!!.id
        name = when {
            id < 10 -> "video00$id"
            id < 100 -> "video0$id"
            else -> "video$id"
        }
        return name
    }

    /** Views **/

    private fun loadList(holder: MyViewHolder, position: Int){
        holder.bFav?.isChecked = false
        userFav?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val video = snapshot.getValue(VideoModel::class.java)
                    if (holder.text?.text.toString() == video!!.name) {
                        holder.bFav?.isChecked = true
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })

        // Fav button
        holder.bFav?.setEventListener(object : SparkEventListener {
            override fun onEvent(button: ImageView, buttonState: Boolean) {
                if (buttonState) { //active
                    Toast.makeText(context, R.string.add_favourites, Toast.LENGTH_SHORT).show()
                    addVideoToFav(position)
                } else { //inactive
                    Toast.makeText(context, R.string.delete_favourites, Toast.LENGTH_SHORT).show()
                    removeVideoFromFav(position)
                }
            }
            override fun onEventAnimationEnd(button: ImageView, buttonState: Boolean) {}
            override fun onEventAnimationStart(button: ImageView, buttonState: Boolean) {}
        })
    }

    private fun loadFavList(holder: MyViewHolder, position: Int){
        holder.bFav?.isChecked = true
        holder.bFav?.setEventListener(object : SparkEventListener {
            override fun onEvent(button: ImageView, buttonState: Boolean) {
                if (buttonState) { //active
                    Toast.makeText(context, R.string.add_favourites, Toast.LENGTH_SHORT).show()
                    addVideoToFav(position)
                } else { //inactive
                    Toast.makeText(context, R.string.delete_favourites, Toast.LENGTH_SHORT).show()
                    removeVideoFromFav(position)
                }
            }
            override fun onEventAnimationEnd(button: ImageView, buttonState: Boolean) {}
            override fun onEventAnimationStart(button: ImageView, buttonState: Boolean) {}
        })
    }


    /** Functions **/

    private fun setColorTags(holder: MyViewHolder, position: Int) {
        when {
            array[position].tag == "Animals" -> holder.tag?.background = context?.resources?.getDrawable(R.drawable.circle_orange)
            array[position].tag == "Films" -> holder.tag?.background = context?.resources?.getDrawable(R.drawable.circle_blue)
            array[position].tag == "Space" -> holder.tag?.background = context?.resources?.getDrawable(R.drawable.circle_pink)
            array[position].tag == "Nature" -> holder.tag?.background = context?.resources?.getDrawable(R.drawable.circle_green)
            array[position].tag == "Music" -> holder.tag?.background = context?.resources?.getDrawable(R.drawable.circle_cyan)
            array[position].tag == "Figures" -> holder.tag?.background = context?.resources?.getDrawable(R.drawable.circle_yellow)
            array[position].tag == "Others" -> holder.tag?.background = context?.resources?.getDrawable(R.drawable.circle_purple)
        }
    }

    fun setFilter(newList: ArrayList<VideoModel>) {
        array = ArrayList()
        array.addAll(newList)
        notifyDataSetChanged()
    }

    fun closeLastSwipeLayout(){
        lastSwipeLayout?.close(true)
    }

    /** Holder Class **/

    class MyViewHolder(itemView: View, viewType: Int): RecyclerView.ViewHolder(itemView) {

        // View types
        val VIEW_TYPE_LIST = 1
        val VIEW_TYPE_LIST_FAV = 3

        var image: ImageView? = null
        var tag: ImageView? = null
        var shieldFav: ImageView? = null
        var shieldHide: ImageView? = null
        var text: TextView? = null
        var button: Button? = null
        var type: Int = 0
        var swipeLayout: SwipeRevealLayout? = null
        var bFav: SparkButton? = null

        init {
            when (viewType) {
                VIEW_TYPE_LIST -> {
                    showListView()
                }
                VIEW_TYPE_LIST_FAV -> {
                    showListFavView()
                }
            }
        }

        private fun showListView(){
            image = itemView.findViewById(R.id.image_big) as ImageView
            text = itemView.findViewById(R.id.title_big) as TextView
            button = itemView.findViewById(R.id.button_big) as Button
            bFav = itemView.findViewById(R.id.button_fav_big)
            shieldFav = itemView.findViewById(R.id.button_fav_big_copy) as ImageView
            tag = itemView.findViewById(R.id.iv_tag) as ImageView
            swipeLayout = itemView.findViewById(R.id.swipe_layout_big) as SwipeRevealLayout
            type = 1
        }
        private fun showListFavView(){
            image = itemView.findViewById(R.id.image_list_fav) as ImageView
            text = itemView.findViewById(R.id.title_list_fav) as TextView
            button = itemView.findViewById(R.id.button_list_fav) as Button
            bFav = itemView.findViewById(R.id.button_fav_list)
            tag = itemView.findViewById(R.id.iv_tag_fav) as ImageView
            swipeLayout = itemView.findViewById(R.id.swipe_layout_list_fav) as SwipeRevealLayout
            type = 3
        }
    }
}