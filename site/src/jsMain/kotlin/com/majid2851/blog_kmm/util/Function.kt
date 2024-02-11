package com.majid2851.blog_kmm.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.localStorage
import org.w3c.dom.get

@Composable
fun isUserLoggedIn(content:@Composable ()->Unit)
{

    val context = rememberPageContext()
    val remembered = remember{ localStorage[Utils.localStorage].toBoolean() }
    val userId= remember{ localStorage[Utils.userId] }
    val userIdExist= remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit){


        userIdExist.value = if(!userId.isNullOrEmpty()) checkUserId(id = userId)
            else false
        if(remembered==false || userIdExist.value==false){
            context.router.navigateTo("/admin/login")
        }
    }

    if (remembered && userIdExist.value){
        content()
    }else{
        println("Loading....")
    }



}