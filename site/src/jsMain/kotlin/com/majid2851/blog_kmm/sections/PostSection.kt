package com.majid2851.blog_kmm.sections

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.components.PostsView
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.util.Constants
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.px

@Composable
fun PostSection(
    breakpoint: Breakpoint,
    posts:List<PostWithoutDetails>,
    title:String="",
    showMoreVisibility:Boolean,
    onShowMoreClick:()->Unit,
    onClick:(String)->Unit,
)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .maxWidth(Constants.PAGE_WIDTH.px),
        contentAlignment =Alignment.TopCenter,
    ){
        PostsView(
            breakpoint = breakpoint,
            posts=posts,
            title = title,
            showMoreVisibility = showMoreVisibility,
            onShowMore = {
                onShowMoreClick()
            },
            onClick = {
                onClick(it)
            }

        )


    }

}