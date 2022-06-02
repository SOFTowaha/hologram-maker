package com.sergioloc.hologram.utils

import java.net.InetAddress

class Connection {
    companion object {
        fun isInternetAvailable(): Boolean {
            return try {
                val address: InetAddress = InetAddress.getByName("google.com")
                !address.equals("")
            } catch (e: Exception) {
                false
            }
        }
    }
}