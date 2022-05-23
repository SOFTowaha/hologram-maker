package com.sergioloc.hologram.usecases.navigator

import com.google.firebase.database.*

class NavigateInteractorImpl(var presenter: NavigatePresenterImpl): NavigateInterface.Interactor {

    private var database: FirebaseDatabase? = null
    private var shareRef: DatabaseReference? = null

    override fun shareLink() {
        database = FirebaseDatabase.getInstance()
        shareRef = database?.getReference("share")
        shareRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var link = dataSnapshot.getValue(String::class.java)
                presenter?.sendShareLink(link!!)
            }
            override fun onCancelled(databaseError: DatabaseError) { }
        })
    }

}