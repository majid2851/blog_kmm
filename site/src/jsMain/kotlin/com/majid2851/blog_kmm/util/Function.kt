package com.majid2851.blog_kmm.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.majid2851.blog_kmm.models.RandomJoke
import com.majid2851.blog_kmm.navigation.Screen
import com.varabyte.kobweb.browser.http.http
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

@Composable
fun isUserLoggedIn(content:@Composable ()->Unit)
{

    val context = rememberPageContext()
    val remembered = remember{ localStorage[IdUtils.localStorage].toBoolean() }
    val userId= remember{ localStorage[IdUtils.userId] }
    val userIdExist= remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit){


        userIdExist.value = if(!userId.isNullOrEmpty()) checkUserId(id = userId)
            else false
        if(remembered==false || userIdExist.value==false){
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    }

    if (remembered && userIdExist.value){
        content()
    }else{
        println("Loading....")
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

fun logout()
{
    localStorage[IdUtils.localStorage] = "false"
    localStorage[IdUtils.userName] = ""
    localStorage[IdUtils.userId] = ""
}