package com.majid2851.blog_kmm.models

expect class Post{
    val id:String
    val author:String
    val date:Long
    val title:String
    val subtitle:String
    val thumbnail:String
    val content:String
    val category:Category
    val popular:Boolean
    val main:Boolean
    val sponsored:Boolean
}

expect class PostWithoutDetails {
    val id: String
    val author: String
    val date: Double
    val title: String
    val subtitle: String
    val thumbnail: String
    val category: Category
}
