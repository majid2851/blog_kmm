package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import com.majid2851.blog_kmm.components.AdminPageLayout
import com.majid2851.blog_kmm.components.LoadingIndicator
import com.majid2851.blog_kmm.models.RandomJoke
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.navigation.Screen
import com.majid2851.blog_kmm.util.ApiAddress
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.IdUtils
import com.majid2851.blog_kmm.util.Res
import com.majid2851.blog_kmm.util.fetchRandomJoke
import com.majid2851.blog_kmm.util.isUserLoggedIn
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.browser.http.http
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

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
    var randomJoke:RandomJoke? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit)
    {
        fetchRandomJoke {
            randomJoke=it
        }

    }

    AdminPageLayout {
       HomeContent(joke = randomJoke)
       AddButton()
   }
}

@Composable
fun HomeContent(joke: RandomJoke?)
{
    val breakpoint = rememberBreakpoint()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(left = if(breakpoint > Breakpoint.MD)
                Constants.SIDE_PANEL_WIDTH.px else 0.px) ,
        contentAlignment = Alignment.Center
    )
    {

        if (joke!=null)
        {

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(topBottom = 50.px),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                if(joke.id != -1){
                    Image(
                        modifier = Modifier
                            .size(150.px)
                            .margin(bottom = 50.px)
                        ,
                        src= Res.Image.laugh,
                        description = joke.joke,
                    )
                }
                if(joke.joke.contains("Q:"))
                {
                    SpanText(
                        modifier = Modifier
                            .margin(bottom = 14.px)
                            .fillMaxWidth(60.percent)
                            .textAlign(TextAlign.Center)
                            .color(Theme.Secondary.rgb)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(28.px)

                        ,
                        text = joke.joke.split(":")[0]
                    )
                    SpanText(
                        modifier = Modifier
                            .fillMaxWidth(60.percent)
                            .textAlign(TextAlign.Center)
                            .color(Theme.HalfBlack.rgb)
                            .fontSize(20.px)
                            .fontFamily(FONT_FAMILY)
                            .fontWeight(FontWeight.Normal)
                        ,
                        text = joke.joke.split(":").last()
                    )
                }else{
                    SpanText(
                        modifier = Modifier
                            .margin(bottom = 14.px)
                            .fillMaxWidth(60.percent)
                            .textAlign(TextAlign.Center)
                            .color(Theme.Secondary.rgb)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(28.px)
                        ,
                        text = joke.joke
                    )
                }

            }
        }else{
            LoadingIndicator()
        }


    }


}

@Composable
fun AddButton()
{
    val breakPoint= rememberBreakpoint()
    val context= rememberPageContext()

    Box(
        modifier = Modifier
            .height(100.vh)
            .fillMaxWidth()
            .maxWidth(Constants.PAGE_WIDTH.px)
            .position(Position.Fixed)
            .styleModifier {
               property("pointer-events","none")
            }
        ,
        contentAlignment = Alignment.BottomEnd,
    )
    {
        Box(
            modifier = Modifier
                .margin(
                    right = if(breakPoint > Breakpoint.MD) 40.px else 20.px,
                    bottom =if(breakPoint > Breakpoint.MD) 40.px else 20.px,
                )
                .backgroundColor(Theme.Primary.rgb)
                .size(
                    if(breakPoint > Breakpoint.MD) 80.px
                    else 50.px
                )
                .borderRadius(r=14.px)
                .cursor(Cursor.Pointer)
                .onClick {
                    context.router.navigateTo(Screen.AdminCreatePost.route)
                }
                .styleModifier {
                    property("pointer-events","auto")
                }
            ,
            contentAlignment = Alignment.Center
        ){
            FaPlus(
                modifier = Modifier.color(Color.white),
                size = IconSize.LG
            )
        }

    }

}
