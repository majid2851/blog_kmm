package com.majid2851.blog_kmm.components

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.util.Constants
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun PostsView(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>,
    selectableMode: Boolean = false,
    title:String?=null,
    onSelect: (String) -> Unit={},
    onDeselect: (String) -> Unit={},
    onShowMore: () -> Unit,
    showMoreVisibility: Boolean,
    onClick:(String)->Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(
            if (breakpoint > Breakpoint.MD) 80.percent else 90.percent
        ),
        verticalArrangement = Arrangement.Center,
        ) {

        if(title!=null){
            SpanText(
                modifier = Modifier
                    .margin(bottom = 24.px)
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(18.px)
                    .fontWeight(FontWeight.Medium)
                ,
                text = title,
            )
        }

        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2, md = 3, lg = 4)
        ) {
            posts.forEach { post ->
                PostPreview(post = post,
                    selectableMode = selectableMode,
                    onSelect = { onSelect(post.id) },
                    onDeselect = { onDeselect(post.id) },
                    onClick = {
                        onClick(it)
                    }
                )
            }
        }


        SpanText(
            modifier = Modifier.fillMaxWidth().margin(topBottom = 50.px).textAlign(TextAlign.Center)
                .fontFamily(Constants.FONT_FAMILY).fontSize(16.px).fontWeight(FontWeight.Medium)
                .cursor(Cursor.Pointer).visibility(
                    if (showMoreVisibility) Visibility.Visible
                    else Visibility.Hidden
                ).onClick {
                    onShowMore()
                }, text = "Show More"
        )


    }


}