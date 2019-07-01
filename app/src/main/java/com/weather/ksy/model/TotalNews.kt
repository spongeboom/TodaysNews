package com.weather.ksy.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class TotalNews(
    @SerializedName("articles")
    var articleList: ArrayList<Articles>
) : Serializable {
   data class Articles(
        var title: String? = null,
        var urlToImage: String? = null,
        var content: String? = null,
        var description: String? = null,
        var url: String? = null,
        var publishedAt: Date? = null
    ) : Serializable
}
