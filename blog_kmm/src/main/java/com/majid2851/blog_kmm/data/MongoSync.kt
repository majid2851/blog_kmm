package com.majid2851.blog_kmm.data

import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.PostSync
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object MongoSync :MongoSyncRepository
{
    private val app= App.create(Constants.APP_ID)
    private val user=app.currentUser
    private lateinit var realm:Realm


    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if(user!=null){
            val config =SyncConfiguration
                .Builder(user, setOf(PostSync::class))
                .initialSubscriptions{
                    add(
                        query=it.query(PostSync::class),
                        name ="Blog Posts",
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm=Realm.open(config)

        }
    }

    override fun readAllPosts(): Flow<RequestState<List<PostSync>>> {
        return if(user !=null){
            try {
                realm.query(PostSync::class)
                    .asFlow()
                    .map {result->
                        RequestState.Success(data = result.list)
                    }
            }catch (e:Exception){
                flow{
                    emit(RequestState.Error(Exception(e.message)))
                }
            }
        }else{
            flow{
                emit(RequestState.Error(Exception("User not authenticated.")))
            }
        }
    }

}