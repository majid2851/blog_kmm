package com.majid2851.blog_kmm.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.majid2851.blog_kmm.components.CategoryNavigationItems
import com.majid2851.blog_kmm.components.NavigationItems
import com.majid2851.blog_kmm.components.OverflowSidePanel
import com.majid2851.blog_kmm.models.ApiListResponse
import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.sections.HeaderSection
import com.majid2851.blog_kmm.sections.MainSection
import com.majid2851.blog_kmm.util.fetchMainPosts
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint

@Page
@Composable
fun HomePage()
{
    val scope= rememberCoroutineScope()
    val breakpoint= rememberBreakpoint()
    val overflowOpened= remember { mutableStateOf(false) }
    val mainPosts=remember{ mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }

    LaunchedEffect(Unit){
        fetchMainPosts(
            onSuccess = {
                mainPosts.value=it
                println(mainPosts.value)
            },
            onError = {

            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(overflowOpened.value){
            OverflowSidePanel(
                onMenuClose = {
                    overflowOpened.value=false
                },
                content={
                    CategoryNavigationItems()
                }
            )
        }

        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpen = {
                overflowOpened.value=true
            }
        )

        MainSection(
            breakpoint=breakpoint,
            posts = mainPosts.value,
            onClick = {

            }
        )
    }
}