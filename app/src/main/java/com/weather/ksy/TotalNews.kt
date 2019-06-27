package com.weather.ksy

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TotalNews(
    @SerializedName("articles")
    var articleList: ArrayList<Articles>
) : Serializable {
   data class Articles(
        var title: String? = null,
        var urlToImage: String? = null,
        var content: String? = null,
        var description: String? = null,
        var url: String? = null
    ) : Serializable
}

//class TotalNews(
//    var title:String? = null,
//    var urlToImage:String? = null,
//    var content:String? = null,
//    var description:String? = null,
//    var url:String? = null
//):Serializable