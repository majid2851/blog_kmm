package com.majid2851.blog_kmm.util

import androidx.compose.runtime.clearCompositionErrors
import com.majid2851.blog_kmm.models.ApiListResponse
import com.majid2851.blog_kmm.models.ApiResponse
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

suspend fun updatePost(post: Post):Boolean{
    return try {
        window.api.tryPost(
            apiPath = ApiPath.updatePost,
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()

    }catch (e:Exception){
        println(e)
        false
    }

}

suspend fun fetchMyPosts(
    skip:Int,
    onSuccess:(ApiListResponse) ->Unit,
    onError:(Exception) ->Unit,
)
{
    try{
        try {
            val result= window.api.tryGet(
                apiPath = ApiPath.readMyPosts+"?"+"skip=$skip&author" +
                        "=${localStorage.getItem(IdUtils.userName)}"
            )?.decodeToString()
            onSuccess(result.parseData())


        } catch (e: Exception) {
            onError(e)
        }


    }catch (e:Exception){
        onError(e)
    }
}

suspend fun deleteSelectedPosts(ids:List<String>):Boolean
{
    try {
        val result= window.api.tryPost(
            apiPath = ApiPath.deleteSelectedPosts,
            body = Json.encodeToString(ids).encodeToByteArray()
        )?.decodeToString()
        return result.toBoolean()
    }catch (e:Exception){
        println(e.message)
        return false
    }
}

suspend fun searchPostByTitle(
    query:String,
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit,
){
    try {
        val result=window.api.tryGet(
            apiPath = ApiPath.searchPost+"?query=$query&skip=$skip",
        )?.decodeToString()
        onSuccess(result.parseData())
    }catch (e:Exception) {
        onError(e)
    }
}

suspend fun fetchSelectedPost(id: String):ApiResponse
{
    try {
        val result= window.api.tryGet(
            apiPath = ApiPath.selectedPost+"?${Constants.POST_ID_PARAM}=$id"
        )?.decodeToString()
        if(result!=null){
            return result.parseData<ApiResponse>()
//            return Json.decodeFromString<ApiResponse>(result)

        }else{
            return ApiResponse.Error(message = "Result is Null")
        }

    }catch (e:Exception){
        return ApiResponse.Error(message = e.message.toString())
    }


}

inline fun <reified T> String?.parseData():T {
    return Json.decodeFromString(this.toString())
}




