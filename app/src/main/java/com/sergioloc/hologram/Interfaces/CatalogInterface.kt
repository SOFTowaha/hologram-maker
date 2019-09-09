package com.sergioloc.hologram.Interfaces

import com.sergioloc.hologram.Models.VideoModel

interface CatalogInterface {

    interface View {
        fun initVariables()
        fun showConnectionError()
        fun hideConnectionError()
        fun changeCountText(number: Int)
        fun initFb()
        fun initRecyclerView()
        fun selectChip(i: Int)
        fun deselectChip(i: Int)
        fun areAllChipsSelected(): Boolean
        fun movePanel(open: Boolean)
        fun switchArrow()
        fun showLoading()
        fun hideLoading()
        fun showFirebaseError(message: String)
    }

    interface Presenter {
        fun callInitFirebaseList()
        fun listLoaded(size: Int)
        fun callTagList(tag1: Boolean, tag2: Boolean, tag3: Boolean, tag4: Boolean, tag5: Boolean, tag6: Boolean, tag7: Boolean)
        fun callFullList()
        fun callSearchInFav(text: String)
        fun callSearchInFull(text: String)
        fun callSearchInMerge(text: String)
        fun callFavList()
        fun callCloseSwipe()
        fun startLoadingFavList()
        fun finishLoadingFavList()
        fun errorLoadingList(message: String)
        fun errorConnection()
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
        fun removeItem(position: Int)
    }

}