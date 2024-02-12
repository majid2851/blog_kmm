package com.majid2851.blog_kmm.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.majid2851.blog_kmm.util.Constants
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import org.jetbrains.compose.web.css.px

@Composable
fun AdminPageLayout(content:@Composable ()->Unit)
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

    content()

}