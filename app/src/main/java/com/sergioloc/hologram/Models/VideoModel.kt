package com.sergioloc.hologram.Models

class VideoModel {

    var id: Int = 0
    var name: String? = null
    var code: String? = null
    var tag: String? = null
    var fav: Boolean = false

    constructor(id: Int, name: String, code: String, tag: String, fav: Boolean) {
        this.id = id
        this.name = name
        this.code = code
        this.tag = tag
        this.fav = fav
    }

    constructor() {
        this.name = ""
        this.code = ""
    }

}