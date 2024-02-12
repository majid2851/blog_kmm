package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.components.AdminPageLayout
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
fun Create()
{
    isUserLoggedIn {

        AdminPageLayout {

        }
    }
}

