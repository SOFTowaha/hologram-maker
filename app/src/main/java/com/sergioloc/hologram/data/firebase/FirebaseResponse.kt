package com.sergioloc.hologram.data.firebase

sealed class FirebaseResponse<out T> {
    class Loading<out T>: FirebaseResponse<T>()

    data class Success<out T>(
        val data: T
    ): FirebaseResponse<T>()

    data class Failure<out T>(
        val errorMessage: String
    ): FirebaseResponse<T>()
}