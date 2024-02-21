package com.majid2851.blog_kmm.pages.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.majid2851.blog_kmm.components.CategoryNavigationItems
import com.majid2851.blog_kmm.components.LoadingIndicator
import com.majid2851.blog_kmm.components.OverflowSidePanel
import com.majid2851.blog_kmm.models.ApiListResponse
import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.navigation.Screen
import com.majid2851.blog_kmm.sections.HeaderSection
import com.majid2851.blog_kmm.sections.PostSection
import com.majid2851.blog_kmm.util.ApiPath
import com.majid2851.blog_kmm.util.ApiPath.searchPostsByCategory
import com.majid2851.blog_kmm.util.ApiPath.searchPostsByTitle
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.Constants.POSTS_PER_PAGE
import com.majid2851.blog_kmm.util.Res
import com.majid2851.blog_kmm.util.searchPostByCategory
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.px
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.majid2851.blog_kmm.util.Constants.QUERY_PARAM
import com.majid2851.blog_kmm.util.IdUtils
import com.majid2851.blog_kmm.util.searchPostByTitle
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLInputElement

@Page(routeOverride = "query")
@Composable
fun searchPage()
{
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    val scope = rememberCoroutineScope()

    var apiResponse by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    var overflowOpened by remember { mutableStateOf(false) }
    val searchedPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var postsToSkip by remember { mutableStateOf(0) }
    var showMorePosts by remember { mutableStateOf(false) }


    val hasCategoryParam= remember(key1 = context.route) {
        context.route.params.containsKey(Constants.CATEGORY_PARAM)
    }
    val hasQueryParam = remember(key1 = context.route) {
        context.route.params.containsKey(Constants.QUERY_PARAM)
    }
    val value= remember(key1 = context.route) {
        if(hasCategoryParam){
            context.route.params.getValue(Constants.CATEGORY_PARAM)
        } else if (hasQueryParam) {
            context.route.params.getValue(QUERY_PARAM)
        }else{
            ""
        }
    }

    LaunchedEffect(key1 = context.route) {
        (document.getElementById(IdUtils.adminSearchBar) as HTMLInputElement).value = ""
        showMorePosts = false
        postsToSkip = 0
        if (hasCategoryParam) {
            searchPostByCategory(
                category = runCatching { Category.valueOf(value) }
                    .getOrElse { Category.Programming },
                skip = postsToSkip,
                onSuccess = { response ->
                    apiResponse = response
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += POSTS_PER_PAGE
                        if (response.data.size >= POSTS_PER_PAGE) showMorePosts = true
                    }
                },
                onError = {}
            )
        } else if (hasQueryParam) {
            (document.getElementById(IdUtils.adminSearchBar) as HTMLInputElement).value = value
            searchPostByTitle(
                query = value,
                skip = postsToSkip,
                onSuccess = { response ->
                    apiResponse = response
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += POSTS_PER_PAGE
                        if (response.data.size >= POSTS_PER_PAGE) showMorePosts = true
                    }
                },
                onError = {}
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowOpened) {
            OverflowSidePanel(
                onMenuClose = { overflowOpened = false },
                content = {
                    CategoryNavigationItems(
                        selectedCategory = if (hasCategoryParam) runCatching {
                            Category.valueOf(value)
                        }.getOrElse { Category.Programming } else null,
                        vertical = true
                    )
                }
            )
        }
        HeaderSection(
            breakpoint = breakpoint,
            selectedCategory = if (hasCategoryParam) runCatching {
                Category.valueOf(value)
            }.getOrElse { Category.Programming } else null,
            logo = Res.Image.logo,
            onMenuOpen = { overflowOpened = true }
        )
        if (apiResponse is ApiListResponse.Success) {
            if (hasCategoryParam) {
                SpanText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .textAlign(TextAlign.Center)
                        .margin(top = 100.px, bottom = 40.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(36.px),
                    text = value.ifEmpty { Category.Programming.name }
                )
            }
            PostSection(
                breakpoint = breakpoint,
                posts = searchedPosts,
                showMoreVisibility = showMorePosts,
                onShowMoreClick = {
                    scope.launch {
                        if (hasCategoryParam) {
                            searchPostByCategory(
                                category = runCatching { Category.valueOf(value) }
                                    .getOrElse { Category.Programming },
                                skip = postsToSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < POSTS_PER_PAGE) {
                                                showMorePosts = false
                                            }
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += POSTS_PER_PAGE
                                        } else {
                                            showMorePosts = false
                                        }
                                    }
                                },
                                onError = {}
                            )
                        } else if (hasQueryParam) {
                            searchPostByTitle(
                                query = value,
                                skip = postsToSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < POSTS_PER_PAGE) {
                                                showMorePosts = false
                                            }
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += POSTS_PER_PAGE
                                        } else {
                                            showMorePosts = false
                                        }
                                    }
                                },
                                onError = {
                                    println(it)
                                }
                            )
                        }
                    }
                },
                onClick = {
//                    context.router.navigateTo(Screen.PostPage.getPost(id = it))
                },
            )
        } else {
            LoadingIndicator()
        }
//        FooterSection()
    }


}