package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.components.AdminPageLayout
import com.majid2851.blog_kmm.components.SearchBar
import com.majid2851.blog_kmm.components.SidePanel
import com.majid2851.blog_kmm.components.SidePanelInternal
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.isUserLoggedIn
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun MyPosts()
{
    isUserLoggedIn {
        PostScreen()
    }

}
@Composable
fun PostScreen()
{
    val breakpoint = rememberBreakpoint()
    AdminPageLayout()
    {
        Column(
            modifier = Modifier
                .margin(topBottom = 50.px)
                .fillMaxSize()
                .padding(
                    left = if(breakpoint>Breakpoint.MD)
                        Constants.SIDE_PANEL_WIDTH.px
                    else 0.px,
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        if (breakpoint >Breakpoint.MD)
                            30.percent
                        else 60.percent
                    ),
                contentAlignment = Alignment.Center,
            )
            {
                SearchBar(
                    onEnterClick = {

                    }
                )
            }


        }
    }
}