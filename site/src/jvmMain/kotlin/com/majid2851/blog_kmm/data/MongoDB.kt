package com.majid2851.blog_kmm.data

import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.models.User
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.DATABASE_NAME
import com.majid2851.blog_kmm.util.Constants.POST_PER_PAGE
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitLast
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.`in`
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.regex
import org.litote.kmongo.setValue


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

    override suspend fun readMyPost(skip: Int, author: String):
            List<PostWithoutDetails>
    {
        return postCollection
            //withDocumentClass allows us to get
            // List<PostWithoutDetails> data from List<Post>
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(PostWithoutDetails::author eq author)
            .sort(descending(PostWithoutDetails::date))
            .skip(skip)
            .limit(Constants.POST_PER_PAGE)
            .toList()
    }

    override suspend fun
            deleteSelectedPost(ids: List<String>): Boolean {
        return postCollection
            .deleteMany(Post::id `in` ids)
            .awaitLast()
            .wasAcknowledged()


    }

    override suspend fun searchPostByTitle(query: String, skip: Int):
            List<PostWithoutDetails> {
        val regexQuery=query.toRegex(RegexOption.IGNORE_CASE)
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(PostWithoutDetails::title regex regexQuery)
            .sort(descending(PostWithoutDetails::date))
            .skip(skip)
            .limit(POST_PER_PAGE)
            .toList()

    }

    override suspend fun readSelectedPost(id: String): Post {
        return postCollection.find(Post::id eq id)
            .toList().first()
    }

    override suspend fun updatePost(post: Post): Boolean {
        return postCollection
            .updateOne(
                Post::id eq post.id,
                mutableListOf(
                    setValue(Post::title,post.title),
                    setValue(Post::subtitle,post.subtitle),
                    setValue(Post::category,post.category),
                    setValue(Post::thumbnail,post.thumbnail),
                    setValue(Post::content,post.content),
                    setValue(Post::main,post.main),
                    setValue(Post::popular,post.popular),
                    setValue(Post::sponsored,post.sponsored),
                )
            )
            .awaitLast()
            .wasAcknowledged()
    }

    override suspend fun readMainPosts(): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(PostWithoutDetails::main eq true)
            .sort(descending(PostWithoutDetails::date))
            .limit(Constants.MAIN_POSTS_LIMIT)
            .toList()

    }


}