package com.majid2851.blog_kmm.data

import android.graphics.PostProcessor
import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.PostSync
import com.majid2851.blog_kmm.util.RequestState
import kotlinx.coroutines.flow.Flow


interface MongoSyncRepository
{
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<PostSync>>>
}