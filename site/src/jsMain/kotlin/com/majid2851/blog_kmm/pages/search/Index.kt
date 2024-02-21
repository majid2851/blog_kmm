package com.majid2851.blog_kmm.pages.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.searchPostByCategory
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext

@Page(routeOverride = "query")
@Composable
fun searchPage()
{
    val context= rememberPageContext()
    val postsToSkip= remember { mutableStateOf(0) }
    val hasCategoryParam= remember(key1 = context.route) {
        context.route.params.containsKey(Constants.CATEGORY_PARAM)
    }
    val value= remember(key1 = context.route) {
        if(hasCategoryParam){
            context.route.params.getValue(Constants.CATEGORY_PARAM)
        }else{
            ""
        }
    }

    LaunchedEffect(key1 = context.route){
        if(hasCategoryParam){
            searchPostByCategory(
                category =Category.valueOf(value = value),
                skip = postsToSkip.value,
                onSuccess = {

                },
                onError = {

                }
            )
        }
    }


}