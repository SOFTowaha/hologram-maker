package com.sergioloc.hologram.utils

import java.lang.Exception

class Safe {

    companion object {

        fun getString(map: Map<String, Any>, value: String): String {
            return try {
                map[value] as String
            } catch (e: Exception) {
                ""
            }
        }

        fun getInt(map: Map<String, Any>, value: String): Int {
            return try {
                (map[value] as Long).toInt()
            } catch (e: Exception) {
                0
            }
        }

        fun getBoolean(map: Map<String, Any>, value: String, default: Boolean): Boolean {
            return try {
                (map[value] as Boolean)
            } catch (e: Exception) {
                default
            }
        }

        fun getImage(images: ArrayList<String>): String {
            return if (images.isNotEmpty())
                images[0]
            else
                ""
        }

    }
}