package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.majid2851.blog_kmm.components.AdminPageLayout
import com.majid2851.blog_kmm.components.Posts
import com.majid2851.blog_kmm.components.SearchBar
import com.majid2851.blog_kmm.models.ApiListResponse
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.deleteSelectedPosts
import com.majid2851.blog_kmm.util.fetchMyPosts
import com.majid2851.blog_kmm.util.isUserLoggedIn
import com.majid2851.blog_kmm.util.noBorder
import com.majid2851.blog_kmm.util.parseSwitchText
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button

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
    val selectable= remember { mutableStateOf(false) }
    val postsToSkip= remember { mutableStateOf(0) }
    val showMoreVisibility = remember{ mutableStateOf(false) }
    val switchText= remember{ mutableStateOf("Select") }
    val myPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val selectedPosts= remember { mutableStateListOf<String>() }
    val scope= rememberCoroutineScope()


    LaunchedEffect(Unit){
        fetchMyPosts(
            skip = postsToSkip.value,
            onSuccess = {
                if (it is ApiListResponse.Success)
                {
                    myPosts.clear()
                    myPosts.addAll(it.data)
                    postsToSkip.value += Constants.POSTS_PER_PAGE
                    showMoreVisibility.value =it.data.size >= Constants.POSTS_PER_PAGE
                }
            },
            onError = {
                println(it)
            }
        )
    }



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

            Row(
                modifier = Modifier
                    .fillMaxWidth(
                        if (breakpoint > Breakpoint.MD) 80.percent
                        else 90.percent
                    )
                    .margin(bottom = 24.px),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        modifier = Modifier.margin(right = 8.px),
                        size = SwitchSize.LG,
                        checked = selectable.value,
                        onCheckedChange = {
                            selectable.value=it
                            if(!selectable.value){
                                switchText.value="Select"
                                selectedPosts.clear()
                            }else {
                                switchText.value="0 Posts Selected."
                            }

                        }
                    )
                    SpanText(
                        modifier = Modifier.color(Theme.HalfBlack.rgb),
                        text = switchText.value
                    )
                }
                Button(
                    attrs = Modifier
                        .margin(right = 20.px)
                        .height(54.px)
                        .padding(leftRight = 24.px)
                        .backgroundColor(Theme.Red.rgb)
                        .color(Colors.White)
                        .noBorder()
                        .borderRadius(r = 4.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .fontWeight(FontWeight.Medium)
                        .visibility(
                            if(selectedPosts.isNotEmpty())
                                Visibility.Visible
                            else Visibility.Hidden
                        )
                        .onClick {
                            scope.launch {
                                val result= deleteSelectedPosts(ids=selectedPosts)
                                if(result==true){
                                    selectable.value=false
                                    switchText.value="Select"
                                    postsToSkip.value -=selectedPosts.size

                                    selectedPosts.forEach {deletedPostId->
                                        myPosts.removeAll{
                                            it.id==deletedPostId
                                        }
                                    }
                                    selectedPosts.clear()

                                }
                            }

                        }
                        .toAttrs()
                ) {
                    SpanText(text = "Delete")
                }
            }

            Posts(
                posts = myPosts,
                breakpoint = breakpoint,
                showMoreVisibility = true,
                selectable = selectable.value,
                onSelect = {
                    selectedPosts.add(it)
                    switchText.value = parseSwitchText(selectedPosts.toList())
                },
                onDeselect = {
                    selectedPosts.remove(it)
                    switchText.value = parseSwitchText(selectedPosts.toList())
                },
                onShowMore = {
                    scope.launch {
                        fetchMyPosts(
                            skip = postsToSkip.value,
                            onSuccess = {
                                if (it is ApiListResponse.Success)
                                {
                                    if(it.data.isEmpty()){
                                        myPosts.addAll(it.data)
                                        postsToSkip.value += Constants.POSTS_PER_PAGE
                                        showMoreVisibility.value =
                                            it.data.size >= Constants.POSTS_PER_PAGE
                                        if(it.data.size <Constants.POSTS_PER_PAGE){
                                            showMoreVisibility.value=false
                                        }
                                    }else{
                                        showMoreVisibility.value=false
                                    }

                                }
                            },
                            onError = {
                                println(it)
                            }
                        )
                    }

                }
            )


        }
    }
}