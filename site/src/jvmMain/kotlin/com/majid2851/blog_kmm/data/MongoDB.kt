package com.majid2851.blog_kmm.data

import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.User
import com.majid2851.blog_kmm.util.Constants.DATABASE_NAME
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirst
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection


//There was a very strange error here , if you want to run first call
//initMongoDB() and then run MongoDB()
@InitApi
fun initMongoDB(context:InitApiContext,)
{
    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )

    context.data.add(MongoDB(context))
}

class MongoDB(private val context: InitApiContext) : MongoRepository
{
    private val client=KMongo.createClient()
    private val database=client.getDatabase(DATABASE_NAME)
    private val userCollection=database.getCollection<User>()
    private val postCollection=database.getCollection<Post>()


    override suspend fun addPost(post: Post): Boolean {
       return postCollection.insertOne(post)
           .awaitFirst()
           .wasAcknowledged()


    }

    override suspend fun checkUserExistence(user: User): User?
    {
        return try {
            userCollection
                .find(
                    and(
                        User::userName eq user.userName,
                        User::password eq user.password,
                    )
                ).awaitFirst()

        }catch (e:Exception){
            context.logger.error(e.message.toString())
            null
        }

    }

    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val documentCount= userCollection
                .countDocuments(User::id eq id)
                .awaitFirst()
            documentCount > 0

        }catch (e:Exception){
            context.logger.error(e.message.toString())
            false

        }
    }

}