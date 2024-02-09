package com.majid2851.blog_kmm.util

import com.majid2851.blog_kmm.models.User
import com.majid2851.blog_kmm.models.UserWithoutPassword
import com.varabyte.kobweb.browser.api
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

suspend fun checkUserExistence(user:User)
    :UserWithoutPassword?
{
    return try {
        val result= window.api.tryPost(
            apiPath = ApiPath.userCheck,
            body = Json.encodeToString(user).encodeToByteArray()
        )
        Json.decodeFromString<UserWithoutPassword>(result.toString())

    }catch (e:Exception){
        println(e.message)
        null
    }

}