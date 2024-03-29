package com.majid2851.blog_kmm.data

import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.models.NewsLetter
import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.models.User

interface MongoRepository
{
    suspend fun addPost(post:Post):Boolean

    suspend fun checkUserExistence(user: User) : User ?

    suspend fun checkUserId(id:String):Boolean

    suspend fun readMyPost(skip:Int,author:String):
            List<PostWithoutDetails>

    suspend fun deleteSelectedPost(posts: List<String>):Boolean

    suspend fun searchPostByTitle(
        query:String,
        skip: Int,
    ):List<PostWithoutDetails>

    suspend fun readSelectedPost(id:String):Post

    suspend fun updatePost(post:Post):Boolean


    suspend fun readLatestPosts(skip: Int):
            List<PostWithoutDetails>

    suspend fun readMainPosts():
            List<PostWithoutDetails>

    suspend fun readSponsoredPosts():
            List<PostWithoutDetails>

    suspend fun readPopularPosts(skip: Int):
            List<PostWithoutDetails>

    suspend fun subscribe(newsLetter: NewsLetter):String

    suspend fun searchPostsByCategory(category:Category,skip: Int)
        :List<PostWithoutDetails>


}