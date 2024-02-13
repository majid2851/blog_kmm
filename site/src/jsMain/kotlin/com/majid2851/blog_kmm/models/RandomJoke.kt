package com.majid2851.blog_kmm.models

import kotlinx.serialization.Serializable

@Serializable
data class RandomJoke(
    val id:Int,
    val joke:String,
)