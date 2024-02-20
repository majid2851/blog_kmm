package com.majid2851.blog_kmm.api

import com.majid2851.blog_kmm.data.MongoDB
import com.majid2851.blog_kmm.models.ApiListResponse
import com.majid2851.blog_kmm.models.ApiResponse
import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.util.ApiPath
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.Request
import com.varabyte.kobweb.api.http.Response
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.id.ObjectIdGenerator

@Api(routeOverride = ApiPath.addPost)
suspend fun addPost(context:ApiContext)
{
    try {
        val post=context.req.getBody<Post>()
//        val post = context.req.body
//            ?.decodeToString()?.let {
//                Json.decodeFromString<Post>(it)
//            }
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
        val request=context.req.getBody<List<String>>()

//        val request=context.req.body?.decodeToString()?.let{
//            Json.decodeFromString<List<String>>(it)
//        }
        context.res.setBody(
            request?.let {
                context.data.getValue<MongoDB>()
                    .deleteSelectedPost(ids = it)
            }

        )
    }catch (e:Exception){
        context.res.setBody(e.message)
    }
}

@Api(routeOverride = ApiPath.searchPost)
suspend fun searchPostsByTitle(context: ApiContext)
{
    try {
        val query=context.req.params["query"] ?:""
        val skip=context.req.params["skip"]?.toInt() ?: 0
        val request=context.data.getValue<MongoDB>()
            .searchPostByTitle(
                query=query,
                skip=skip
            )
        context.res.setBody(
            ApiListResponse.Success(data = request)
        )

    }catch (e:Exception){
        context.res.setBody(
            ApiListResponse.Error(message = e.message.toString())
        )

    }

}

@Api(routeOverride = ApiPath.updatePost)
suspend fun updatePost(context: ApiContext)
{
    try {
        val updatedPost=context.req.getBody<Post>()
        context.res.setBody(
            updatedPost?.let {
                context.data.getValue<MongoDB>()
                    .updatePost(it)
            }
        )


    }catch (e:Exception){
        context.res.setBody(e.message)
    }

}

@Api(routeOverride = ApiPath.selectedPost)
suspend fun selectedPost(context: ApiContext)
{
    val postId=context.req.params["postId"]
    if(!postId.isNullOrEmpty()){
        try {
            val selectedPost=context.data.getValue<MongoDB>()
                .readSelectedPost(id=postId)
            context.res.setBody(
                    ApiResponse.Success(
                        data = selectedPost
                    )
            )
        }catch (e:Exception){
            context.res.setBody(ApiResponse.Error(
                message = e.message.toString()
            ))
        }
    }else{
        context.res.setBody(ApiResponse.Error(
            message = "Selected Post doesn't exist."
        ))
    }

}

@Api(routeOverride = ApiPath.readMainPosts)
suspend fun readMainPosts(context: ApiContext)
{
    try {
        val mainPost=context.data.getValue<MongoDB>().readMainPosts()
        context.res.setBody(ApiListResponse.Success(mainPost))

    }catch (e:Exception){
        context.res.setBody(ApiListResponse.Error(
            message = e.message.toString()
        ))
    }

}


@Api(routeOverride = ApiPath.readLatestPosts)
suspend fun readLatestPosts(context: ApiContext)
{
    try {
        val skip=context.req.params["skip"]?.toInt() ?: 0
        val latestPosts=context.data.getValue<MongoDB>().readLatestPosts(skip=skip)
        context.res.setBody(ApiListResponse.Success(latestPosts))

    }catch (e:Exception){
        context.res.setBody(ApiListResponse.Error(
            message = e.message.toString()
        ))
    }

}

@Api(routeOverride = ApiPath.readSponsoredPosts)
suspend fun readSponsoredPosts(context: ApiContext)
{
    try {
        val sponsoredPosts=context.data.getValue<MongoDB>().readSponsoredPosts()
        context.res.setBody(ApiListResponse.Success(sponsoredPosts))

    }catch (e:Exception){
        context.res.setBody(ApiListResponse.Error(
            message = e.message.toString()
        ))
    }
}

@Api(routeOverride = ApiPath.readPopularPosts)
suspend fun readPopularPosts(context: ApiContext)
{
    try {
        val skip=context.req.params["skip"]?.toInt() ?: 0
        val popularPosts=context.data.getValue<MongoDB>().
            readPopularPosts(skip=skip)
        context.res.setBody(ApiListResponse.Success(popularPosts))

    }catch (e:Exception){
        context.res.setBody(ApiListResponse.Error(
            message = e.message.toString()
        ))
    }

}



//TODO(I have to read about reified and inline )
inline fun <reified T> Response.setBody(data:T)
{
    setBodyText(Json.encodeToString(data))
}


inline fun <reified T> Request.getBody():T?
{
    return body?.decodeToString()?.let{return Json.decodeFromString(it)}
}


