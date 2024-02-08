package com.majid2851.blog_kmm.data

import com.majid2851.blog_kmm.models.User

interface MongoRepository
{
    suspend fun checkUserExistence(user: User) : User ?


}