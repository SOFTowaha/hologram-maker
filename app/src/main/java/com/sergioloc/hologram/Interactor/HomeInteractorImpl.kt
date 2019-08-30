package com.sergioloc.hologram.Interactor

import com.google.firebase.database.*
import com.sergioloc.hologram.Interfaces.HomeInterface
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener



class HomeInteractorImpl(var presenter: HomeInterface.Presenter): HomeInterface.Interactor {

    private var database: FirebaseDatabase? = null
    private var demoRef: DatabaseReference? = null

    override fun getDemoFromFirebase() {
        database = FirebaseDatabase.getInstance()
        demoRef = database?.getReference("demo")
        demoRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(String::class.java)
                presenter?.videoReady(post!!)
            }
            override fun onCancelled(databaseError: DatabaseError) { }
        })

    }

}