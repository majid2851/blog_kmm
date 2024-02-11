package com.majid2851.blog_kmm.util

import com.majid2851.blog_kmm.models.User
import com.majid2851.blog_kmm.models.UserWithoutPassword
import com.varabyte.kobweb.browser.api
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

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