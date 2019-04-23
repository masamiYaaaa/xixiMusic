package com.example.xixixi.musicplayer

import java.io.Serializable

data class MusicInfoBean(
    val album_pic_url: String,
    val artist_name: String,
    val song_name: String,
    val song_url: String,
    val time: String,
    val time_format: String
):Serializable{
    constructor():this("","","","","","")

}