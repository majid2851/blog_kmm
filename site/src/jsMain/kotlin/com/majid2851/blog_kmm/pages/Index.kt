package com.majid2851.blog_kmm.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.majid2851.blog_kmm.components.CategoryNavigationItems
import com.majid2851.blog_kmm.components.NavigationItems
import com.majid2851.blog_kmm.components.OverflowSidePanel
import com.majid2851.blog_kmm.models.ApiListResponse
import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.navigation.Screen
import com.majid2851.blog_kmm.sections.FooterSection
import com.majid2851.blog_kmm.sections.HeaderSection
import com.majid2851.blog_kmm.sections.MainSection
import com.majid2851.blog_kmm.sections.NewsletterSection
import com.majid2851.blog_kmm.sections.PostSection
import com.majid2851.blog_kmm.sections.SponsoredPostsSection
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.fetchLatestPosts
import com.majid2851.blog_kmm.util.fetchMainPosts
import com.majid2851.blog_kmm.util.fetchPopularPosts
import com.majid2851.blog_kmm.util.fetchSponsoredPosts
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch

@Page
@Composable
fun HomePage()
{
    val context= rememberPageContext()
    val scope= rememberCoroutineScope()
    val breakpoint= rememberBreakpoint()
    val overflowOpened= remember { mutableStateOf(false) }
    val mainPosts=remember{ mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    val latestPosts= remember {
        mutableStateListOf<PostWithoutDetails>()
    }
    val sponsoredPosts= remember {
        mutableStateListOf<PostWithoutDetails>()
    }
    val popularPosts= remember { mutableStateListOf<PostWithoutDetails>() }
    val latestPostsToSkip= remember { mutableStateOf(0) }
    val showMoreLatest= remember { mutableStateOf(false) }
    val showMorePopular= remember { mutableStateOf(false) }
    val popularPostsToSkip= remember { mutableStateOf(0) }


    LaunchedEffect(Unit){
        fetchSponsoredPosts(
            onSuccess = {
                if(it is ApiListResponse.Success){
                    sponsoredPosts.addAll(it.data)
                }
                println(sponsoredPosts.toList().toString())

            },
            onError = {
                println(it.message)
            }
        )

        fetchMainPosts(
            onSuccess = {
                mainPosts.value=it
//                println(mainPosts.value)
            },
            onError = {
                println(it.message)
            }
        )

        fetchLatestPosts(
            skip = latestPostsToSkip.value,
            onSuccess = {
                println(it)
                if(it is ApiListResponse.Success){
                     latestPosts.addAll(it.data)
                    latestPostsToSkip.value  += Constants.POSTS_PER_PAGE
                    if(it.data.size >=Constants.POSTS_PER_PAGE){
                        showMoreLatest.value=true
                    }
                }
            },
            onError = {
                println(it)
            }
        )

        fetchPopularPosts(
            skip = popularPostsToSkip.value,
            onSuccess = {
                if(it is ApiListResponse.Success){
                    popularPosts.addAll(it.data)
                    popularPostsToSkip.value += Constants.POSTS_PER_PAGE
                    if(it.data.size >=Constants.POSTS_PER_PAGE){
                        showMorePopular.value=true
                    }
                }
            },
            onError = {

            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .backgroundColor(Theme.HalfWhite.rgb),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
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
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            }
        )

        PostSection(
            breakpoint = breakpoint,
            posts = latestPosts,
            title = "Latest Posts",
            showMoreVisibility = showMoreLatest.value,
            onShowMoreClick ={
                scope.launch {
                    fetchLatestPosts(
                        skip = latestPostsToSkip.value,
                        onSuccess = {response->
                            if(response is ApiListResponse.Success){
                                if(response.data.size < Constants.POSTS_PER_PAGE){
                                    showMoreLatest.value=false
                                }
                                latestPosts.addAll(response.data)
                                latestPostsToSkip.value +=Constants.POSTS_PER_PAGE
                            }else{
                                showMoreLatest.value=false
                            }
                        },
                        onError = {
                            println(it.message)
                        },
                    )
                }



            },
            onClick={
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            }
        )

        SponsoredPostsSection(
            breakpoint=breakpoint,
            posts = sponsoredPosts,
            onClick = {
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            }
        )

        PostSection(
            breakpoint = breakpoint,
            posts = popularPosts,
            title = "Popular Posts",
            showMoreVisibility = showMorePopular.value,
            onShowMoreClick ={
                scope.launch {
                    fetchPopularPosts(
                        skip = popularPostsToSkip.value,
                        onSuccess = {response->
                            if(response is ApiListResponse.Success){
                                if(response.data.size < Constants.POSTS_PER_PAGE){
                                    showMorePopular.value=false
                                }
                                popularPosts.addAll(response.data)
                                popularPostsToSkip.value +=Constants.POSTS_PER_PAGE
                            }else{
                                showMorePopular.value=false
                            }
                        },
                        onError = {
                            println(it.message)
                        },
                    )
                }



            },
            onClick={
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            }
        )

        NewsletterSection(
            breakpoint=breakpoint,
        )

        FooterSection()
    }
}