package com.sergioloc.hologram.Presenters

import android.content.Context
import android.view.View
import com.sergioloc.hologram.Adapter.RecyclerAdapter
import com.sergioloc.hologram.Interactor.ListInteractorImpl
import com.sergioloc.hologram.Interfaces.ListInterface
import com.sergioloc.hologram.Models.VideoModel

class ListPresenterImpl(var view: ListInterface.View, var myView: View, var guest: Boolean, var context: Context): ListInterface.Presenter {

    private var interactor: ListInteractorImpl? = null

    init {
        interactor = ListInteractorImpl(this, guest, myView, context)
    }

    override fun callInitFirebaseList() {
        interactor?.initFirebaseList()
    }

    override fun callFavList() {
        interactor?.loadFavList()
    }

    override fun callCloseSwipe() {
        interactor?.closeSwipe()
    }

    override fun listLoaded(size: Int) {
        //view.stopConnectionError()
        view.changeCountText(size)
    }

    fun callAdapter(): RecyclerAdapter{
        return interactor?.adapter!!
    }

    override fun callTagList(tag1: Boolean, tag2: Boolean, tag3: Boolean, tag4: Boolean, tag5: Boolean, tag6: Boolean, tag7: Boolean) {
        interactor?.mergeTags = ArrayList()
        if (tag1)
            interactor?.addTagToList(1)
        if (tag2)
            interactor?.addTagToList(2)
        if (tag3)
            interactor?.addTagToList(3)
        if (tag4)
            interactor?.addTagToList(4)
        if (tag5)
            interactor?.addTagToList(5)
        if (tag6)
            interactor?.addTagToList(6)
        if (tag7)
            interactor?.addTagToList(7)
        interactor?.orderListByName(interactor?.mergeTags!!)
        interactor?.updateMergeList()
        listLoaded(interactor?.mergeTags?.size!!)
    }

    override fun callListUpdate(fav: Boolean) {
        if (!fav){
            interactor?.updateActualList()
        }
    }

    override fun callSearchInFav(text: String) {
        interactor?.searchInFav(text)

    }

    override fun callSearchInFull(text: String) {
        interactor?.searchInFull(text)
    }

    override fun callSearchInMerge(text: String) {
        interactor?.searchInMerge(text)
    }
}