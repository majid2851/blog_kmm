package com.majid2851.blog_kmm.util

import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.RandomJoke
import com.majid2851.blog_kmm.models.User
import com.majid2851.blog_kmm.models.UserWithoutPassword
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.browser.http.http
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date
import kotlin.reflect.typeOf

suspend fun checkUserExistence(user:User)
    :UserWithoutPassword?
{
    return try {
        val result= window.api.tryPost(
            apiPath = ApiPath.userCheck,
            body = Json.encodeToString(user).encodeToByteArray()
        )
        result?.decodeToString()?.let {
            Json.decodeFromString<UserWithoutPassword>(it)
        }

    }catch (e:Exception){
        println(e.message)
        null
    }

}
suspend fun checkUserId(id:String):Boolean
{
    return try {
        val result= window.api.tryPost(
            apiPath = ApiPath.checkUserId,
            body = Json.encodeToString(id).encodeToByteArray()
        )
        result?.decodeToString()?.let {
            Json.decodeFromString<Boolean>(it)
        } ?:false


    }catch (e:Exception){
        println(e.message.toString())
        false
    }
}


suspend fun fetchRandomJoke(onCompleted:(RandomJoke) -> Unit)
{
    val date= localStorage[IdUtils.date]
    if(date!=null){
        val difference=(Date.now()-date.toDouble())
        val dayHasPassed = difference >= Constants.dayMiliSeconds
        if(dayHasPassed){
            try{
                val result= window.http.get(ApiAddress.HUMOR_API_URL)
                    .decodeToString()
                onCompleted(Json.decodeFromString<RandomJoke>(result))
                localStorage[IdUtils.date] = Date.now().toString()
                localStorage[IdUtils.joke]=result


            }
            catch (e:Exception)
            {
                onCompleted(RandomJoke(id=-1, joke = e.message.toString(),))

                println(e.message)
            }
        }else{
            try {
                localStorage[IdUtils.joke]?.let {
                    Json.decodeFromString<RandomJoke>(it)
                }?.let{onCompleted(it)}
            }catch (e:Exception){
                onCompleted(RandomJoke(id=-1, joke = e.message.toString(),))
                println(e.message)
            }

        }
    }else{
        try{
            val result= window.http.get(ApiAddress.HUMOR_API_URL)
                .decodeToString()
            onCompleted(Json.decodeFromString<RandomJoke>(result))
            localStorage[IdUtils.date] = Date.now().toString()
            localStorage[IdUtils.joke]=result


        }catch (e:Exception){
            onCompleted(RandomJoke(id=-1, joke = e.message.toString(),))
            println(e.message)
        }
    }
}

suspend fun addPost(post:Post):Boolean
{
    return try {
        window.api.tryPost(
            apiPath = ApiPath.addPost,
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()

    }catch (e:Exception) {
        println(e.message.toString())
        false
    }
}





