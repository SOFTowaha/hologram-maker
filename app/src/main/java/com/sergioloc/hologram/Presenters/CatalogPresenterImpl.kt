package com.sergioloc.hologram.Presenters

import android.content.Context
import android.view.View
import com.sergioloc.hologram.Adapter.RecyclerAdapter
import com.sergioloc.hologram.Interactor.CatalogInteractorImpl
import com.sergioloc.hologram.Interfaces.CatalogInterface

class CatalogPresenterImpl(var view: CatalogInterface.View, var myView: View, var guest: Boolean, var context: Context): CatalogInterface.Presenter {

    private var interactor: CatalogInteractorImpl? = null

    init {
        interactor = CatalogInteractorImpl(this, guest, myView, context)
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

    override fun startLoadingFavList() {
        view.showLoading()
    }

    override fun finishLoadingFavList() {
        view.hideLoading()
    }

    override fun listLoaded(size: Int) {
        view.hideConnectionError()
        view.changeCountText(size)
        view.hideLoading()
    }

    override fun errorLoadingList(message: String) {
        view.showFirebaseError(message)
    }

    override fun errorConnection() {
        view.showConnectionError()
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

    override fun callFullList() {
        interactor?.updateActualList()
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