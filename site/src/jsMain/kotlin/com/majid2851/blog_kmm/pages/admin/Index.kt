package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.util.isUserLoggedIn
import com.varabyte.kobweb.core.Page

@Page
@Composable
fun HomeScreen()
{
    isUserLoggedIn {
        HomePage()
    }

}

@Composable
fun HomePage()
{
    println("Admin Home Page")
}