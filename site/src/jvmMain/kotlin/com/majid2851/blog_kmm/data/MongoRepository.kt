package com.majid2851.blog_kmm.data

import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.models.User

interface MongoRepository
{
    suspend fun addPost(post:Post):Boolean
    suspend fun checkUserExistence(user: User) : User ?
    suspend fun checkUserId(id:String):Boolean
    suspend fun readMyPost(skip:Int,author:String):List<PostWithoutDetails>

}