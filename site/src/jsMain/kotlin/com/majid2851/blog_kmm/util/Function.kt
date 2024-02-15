package com.majid2851.blog_kmm.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.majid2851.blog_kmm.models.RandomJoke
import com.majid2851.blog_kmm.navigation.Screen
import com.varabyte.kobweb.browser.http.http
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
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


fun logout()
{
    localStorage[IdUtils.localStorage] = "false"
    localStorage[IdUtils.userName] = ""
    localStorage[IdUtils.userId] = ""
}


fun Modifier.noBorder():Modifier{
    return this
        .border(
            width =0.px,
            style = LineStyle.None,
            color = Colors.Transparent,
        )
        .outline(
            width =0.px,
            style = LineStyle.None,
            color = Colors.Transparent,
        )
}
