package com.majid2851.blog_kmm.api

import com.majid2851.blog_kmm.data.MongoDB
import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.util.ApiPath
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.id.ObjectIdGenerator

@Api(routeOverride = ApiPath.addPost)
suspend fun addPost(context:ApiContext)
{
    try {
        val post = context.req.body
            ?.decodeToString()?.let {
                Json.decodeFromString<Post>(it)
            }
        val newPost=post?.copy(id = ObjectIdGenerator.newObjectId<String>().id
            .toHexString())

        val result=newPost?.let {
            context.data.getValue<MongoDB>().addPost(it)
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