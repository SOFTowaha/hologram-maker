package com.sergioloc.hologram.usecases.catalog

import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.sergioloc.hologram.adapter.RecyclerAdapter
import com.sergioloc.hologram.models.VideoModel
import java.util.*
import kotlin.collections.ArrayList

class CatalogInteractorImpl(var presenter: CatalogPresenterImpl, var guest: Boolean, var view: View, var context: Context): CatalogInterface.Interactor {

    //Firebase
    private var database: FirebaseDatabase? = null
    private var user: FirebaseUser? = null
    private var ref: DatabaseReference? = null
    private var userFav:DatabaseReference? = null

    //Tag List
    private var tags1: ArrayList<VideoModel>? = null
    private var tags2: ArrayList<VideoModel>? = null
    private var tags3: ArrayList<VideoModel>? = null
    private var tags4: ArrayList<VideoModel>? = null
    private var tags5: ArrayList<VideoModel>? = null
    private var tags6: ArrayList<VideoModel>? = null
    private var tags7: ArrayList<VideoModel>? = null

    var adapter: RecyclerAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var videosList: ArrayList<VideoModel>? = null
    private var actualList: ArrayList<VideoModel>? = null
    var mergeTags: ArrayList<VideoModel>? = null


    init {
        database = FirebaseDatabase.getInstance()
        actualList = ArrayList()
        gridLayoutManager =
            GridLayoutManager(context, 1)
        adapter = RecyclerAdapter(actualList!!, gridLayoutManager!!, guest, this, presenter)
        Handler().postDelayed({
            if (actualList?.size == 0)
                presenter?.errorConnection()

        }, 5000)
    }

    override fun initFirebaseList() {
        videosList = ArrayList()
        actualList = ArrayList()
        ref = database?.getReference("videos")
        ref?.keepSynced(true)
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val video = snapshot.getValue(VideoModel::class.java)
                    videosList?.add(video!!)
                }
                orderListByName(videosList!!)
                actualList?.addAll(videosList!!)
                adapter?.setFilter(actualList!!)
                presenter.listLoaded(actualList!!.size)
                setTagsList()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                presenter?.errorLoadingList(databaseError.message)
            }
        })
    }

    override fun loadFavList() {
        videosList = ArrayList()
        actualList = ArrayList()
        user = FirebaseAuth.getInstance().currentUser
        userFav = user?.uid?.let { database?.getReference("users")?.child(it)?.child("fav") }
        userFav?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val video = snapshot.getValue(VideoModel::class.java)
                    videosList?.add(video!!)
                }
                orderListByName(videosList!!)
                actualList?.addAll(videosList!!)
                adapter?.setFilter(actualList!!)
                adapter?.notifyDataSetChanged()
                presenter.listLoaded(actualList!!.size)
                setTagsList()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                presenter?.errorLoadingList(databaseError.message)
            }
        })
    }

    private fun setTagsList() {

        tags1 = ArrayList()
        tags2 = ArrayList()
        tags3 = ArrayList()
        tags4 = ArrayList()
        tags5 = ArrayList()
        tags6 = ArrayList()
        tags7 = ArrayList()

        actualList?.let {
            for (i in it.indices) {
                if (it[i].tag == "Animals") {
                    tags1?.add(it[i])
                }
            }
            for (i in it.indices) {
                if (it[i].tag == "Films") {
                    tags2?.add(it[i])
                }
            }
            for (i in it.indices) {
                if (it[i].tag == "Space") {
                    tags3?.add(it[i])
                }
            }
            for (i in it.indices) {
                if (it[i].tag == "Nature") {
                    tags4?.add(it[i])
                }
            }
            for (i in it.indices) {
                if (it[i].tag == "Music") {
                    tags5?.add(it[i])
                }
            }
            for (i in it.indices) {
                if (it[i].tag == "Figures") {
                    tags6?.add(it[i])
                }
            }
            for (i in it.indices) {
                if (it[i].tag == "Others") {
                    tags7?.add(it[i])
                }
            }
        }
    }

    override fun updateActualList() {
        actualList = ArrayList()
        actualList = videosList
        adapter?.setFilter(actualList!!)
        presenter?.listLoaded(actualList!!.size)
    }

    override fun orderListByName(list: ArrayList<VideoModel>) {
        list.sortWith(Comparator { item, t1 ->
            val s1: String = item.name!!.toString()
            val s2: String = t1.name!!.toString()
            s1.compareTo(s2)
        })
    }

    override fun addTagToList(i: Int) {
        when (i){
            1 -> mergeTags?.addAll(tags1!!)
            2 -> mergeTags?.addAll(tags2!!)
            3 -> mergeTags?.addAll(tags3!!)
            4 -> mergeTags?.addAll(tags4!!)
            5 -> mergeTags?.addAll(tags5!!)
            6 -> mergeTags?.addAll(tags6!!)
            7 -> mergeTags?.addAll(tags7!!)
        }
    }

    override fun searchInFav(text: String) {
        actualList = java.util.ArrayList()
    }

    override fun searchInFull(text: String) {
        actualList = java.util.ArrayList()
        searchInList(videosList!!, text)
        adapter?.setFilter(actualList!!)
        presenter?.listLoaded(actualList?.size!!)
    }

    override fun searchInMerge(text: String) {
        actualList = java.util.ArrayList()
        searchInList(mergeTags!!, text)
        adapter?.setFilter(actualList!!)
        presenter?.listLoaded(actualList?.size!!)
    }

    private fun searchInList(list: ArrayList<VideoModel>, text: String){
        for (videoModel in list) {
            val name = videoModel.name!!.toLowerCase()
            if (name.contains(text)) {
                actualList?.add(videoModel)
            }
        }
    }

    override fun closeSwipe() {
        adapter?.closeLastSwipeLayout()
    }

    override fun getVideo(position: Int): VideoModel? {
        return actualList?.let { it[position] }
    }

    override fun removeItem(position: Int) {
        adapter?.setFilter(actualList!!)
        adapter?.notifyDataSetChanged()
        presenter.listLoaded(actualList!!.size)
        setTagsList()
    }

    fun updateMergeList(){
        actualList = mergeTags
        adapter?.setFilter(actualList!!)
    }

}