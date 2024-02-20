package com.majid2851.blog_kmm.sections

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.components.PostPreview
import com.majid2851.blog_kmm.models.ApiListResponse
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.util.Constants.PAGE_WIDTH
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun MainSection(
    breakpoint: Breakpoint,
    posts: ApiListResponse,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(PAGE_WIDTH.px),
            contentAlignment = Alignment.Center
        ) {
            when (posts) {
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> {
                    MainPosts(
                        breakpoint = breakpoint,
                        posts = posts.data,
                        onClick = onClick,
                    )
                }

                is ApiListResponse.Error -> {}
            }
        }
    }
}

@Composable
fun MainPosts(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(
                if (breakpoint > Breakpoint.MD) 80.percent
                else 90.percent
            )
            .margin(topBottom = 50.px)
    ) {
        if (breakpoint == Breakpoint.XL) {
            PostPreview(
                post = posts.first(),
                darkTheme = true,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(80.percent)
                    .margin(left = 20.px)
            ) {
                posts.drop(1).forEach { postWithoutDetails ->
                    PostPreview(
//                        modifier = Modifier.margin(bottom = 20.px),
                        post = postWithoutDetails,
                        darkTheme = true,
                        vertical = false ,
                        thumbnailHeight = 200.px,
//                        titleMaxLines = 1,
//                        onClick = { onClick(postWithoutDetails._id) }
                    )
                }
            }
        } else if (breakpoint >= Breakpoint.LG) {
            Box(modifier = Modifier.margin(right = 10.px)) {
                PostPreview(
                    post = posts.first(),
                    darkTheme = true,
//                    onClick = { onClick(posts.first()._id) }
                )
            }
            Box(modifier = Modifier.margin(left = 10.px)) {
                PostPreview(
                    post = posts[1],
                    darkTheme = true,
//                    onClick = { onClick(posts[1]._id) }
                )
            }
        } else {
            PostPreview(
                post = posts.first(),
                darkTheme = true,
                thumbnailHeight = 640.px,
//                onClick = { onClick(posts.first()._id) }
            )
        }
    }
}