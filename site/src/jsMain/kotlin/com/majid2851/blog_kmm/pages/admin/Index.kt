package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.majid2851.blog_kmm.components.OverflowSidePanel
import com.majid2851.blog_kmm.components.SidePanel
import com.majid2851.blog_kmm.components.SidePanelInternal
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.isUserLoggedIn
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.css.px

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
    val overflowMenuOpened = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .maxWidth(Constants.PAGE_WIDTH.px),
            horizontalAlignment = Alignment.Start
        ) {
            SidePanel(
                onMenuClick = {
                    overflowMenuOpened.value=true
                }
            )
            if(overflowMenuOpened.value){
                OverflowSidePanel(onMenuClose = {
                    overflowMenuOpened.value=false
                })
            }

        }

    }

}