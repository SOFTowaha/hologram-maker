package com.sergioloc.hologram.Interfaces

interface HomeInterface {

    interface View {
        fun navigateToActivity(code: String)
    }

    interface Presenter {
        fun buttonPressed()
        fun videoReady(code: String)
    }

    interface Interactor {
        fun getDemoFromFirebase()
    }

}