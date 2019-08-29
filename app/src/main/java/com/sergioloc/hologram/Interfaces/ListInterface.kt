package com.sergioloc.hologram.Interfaces

import com.sergioloc.hologram.Models.VideoModel

interface ListInterface {

    interface View {
        fun setTitle()
        fun showConnectionError()
        fun stopConnectionError()
        fun changeCountText(number: Int)
        fun initFb()
        fun initRecyclerView()
        fun selectChip(i: Int)
        fun deselectChip(i: Int)
        fun areAllChipsSelected(): Boolean
        fun movePanel(open: Boolean)
        fun switchArrow()
    }

    interface Presenter {
        fun callInitFirebaseList()
        fun listLoaded(size: Int)
        fun callTagList(tag1: Boolean, tag2: Boolean, tag3: Boolean, tag4: Boolean, tag5: Boolean, tag6: Boolean, tag7: Boolean)
        fun callListUpdate(fav: Boolean)
        fun callSearchInFav(text: String)
        fun callSearchInFull(text: String)
        fun callSearchInMerge(text: String)
        fun callFavList()
        fun callCloseSwipe()
    }

    interface Interactor {
        fun initFirebaseList()
        fun addTagToList(i: Int)
        fun updateActualList()
        fun orderListByName(list: ArrayList<VideoModel>)
        fun searchInFav(text: String)
        fun searchInFull(text: String)
        fun searchInMerge(text: String)
        fun loadFavList()
        fun getVideo(position: Int): VideoModel?
        fun closeSwipe()
    }

}