package com.majid2851.blog_kmm.api

import com.majid2851.blog_kmm.data.MongoDB
import com.majid2851.blog_kmm.models.User
import com.majid2851.blog_kmm.models.UserWithoutPassword
import com.majid2851.blog_kmm.util.ApiPath
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlinx.serialization.decodeFromString

@Api(routeOverride = ApiPath.userCheck)
suspend fun userCheck(context: ApiContext)
{
    try {
        val userRequest = context.req.body?.decodeToString()?.let {
            Json.decodeFromString<User>(it)
        }
        val user = userRequest?.let {
            context.data.getValue<MongoDB>().checkUserExistence(
                User(userName = it.userName, password = hashPassword(it.password))
            )

        }
        if (user != null) {
            context.res.setBodyText(
                Json.encodeToString(
                    UserWithoutPassword(
                        id = user.id, userName = user.userName
                    )
                )
            )
        } else {
            context.res.setBodyText(
                Json.encodeToString(
                    Exception("User doesn't exists.")
                )
            )
        }


    } catch (e: Exception) {
        context.res.setBodyText(
            Json.encodeToString(
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        )
    }
}

@Api(routeOverride = ApiPath.checkUserId)
suspend fun checkUserId(context: ApiContext)
{
    try {
        val isRequest = context.req.body
            ?.decodeToString()?.let {
                Json.decodeFromString<String>(it)
            }
        val result=isRequest?.let {
            context.data.getValue<MongoDB>().checkUserId(it)
        }
        if (result!=null){
            context.res.setBodyText(Json.encodeToString(result))
        }else{
            context.res.setBodyText(Json.encodeToString(false))
        }


    }catch (e:Exception)
    {
        context.res.setBodyText(Json.encodeToString<Boolean>(false))
    }

}

private fun hashPassword(password:String):String
{
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val hashBytes =messageDigest.digest(
        password.toByteArray(StandardCharsets.UTF_8)
    )
    val hexString = StringBuffer()
    for(byte in hashBytes){
        hexString.append(String.format("%02x",byte))
    }
    return hexString.toString()


}