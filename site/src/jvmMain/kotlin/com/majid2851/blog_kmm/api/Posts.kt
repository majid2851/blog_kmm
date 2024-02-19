package com.majid2851.blog_kmm.api

import com.majid2851.blog_kmm.data.MongoDB
import com.majid2851.blog_kmm.models.ApiListResponse
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

@Api(routeOverride = ApiPath.readMyPosts)
suspend fun readMyPosts(context: ApiContext)
{
    try {
        val skip=context.req.params["skip"]?.toInt() ?:0
        val author = context.req.params["author"]?:""
        val myPosts=context.data.getValue<MongoDB>()
            .readMyPost(skip=skip,author=author)
        context.res.setBodyText(
            Json.encodeToString(ApiListResponse.Success(data = myPosts))
        )

    }catch (e:Exception){
        context.res.setBodyText(
            Json.encodeToString(ApiListResponse.Error(
                message = e.message.toString()
            ))
        )
    }

}

@Api(routeOverride = ApiPath.deleteSelectedPosts)
suspend fun deleteSelectedPosts(context: ApiContext)
{
    try {
        val request=context.req.body?.decodeToString()?.let{
            Json.decodeFromString<List<String>>(it)
        }
        context.res.setBodyText(
            request?.let {
                context.data.getValue<MongoDB>()
                    .deleteSelectedPost(ids = it).toString()
            } ?:"false"
        )
    }catch (e:Exception){
        context.res.setBodyText(Json.encodeToString(e.message))
    }


}
