package com.majid2851.blog_kmm.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.navigation.Screen
import com.majid2851.blog_kmm.styles.NavigationItemStyle
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.Res
import com.majid2851.blog_kmm.util.IdUtils
import com.majid2851.blog_kmm.util.logout
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SidePanel(onMenuClick: () -> Unit)
{
    val breakPoint = rememberBreakpoint()
    if(breakPoint > Breakpoint.MD){
        SidePanelInternal()
    }else {
        CollapseSizePanel(
            onMenuClick = {
                 onMenuClick()
            }
        )
    }
}

@Composable
fun SidePanelInternal()
{
    val breakPoint= rememberBreakpoint()
    Column(
        modifier = Modifier
            .padding(leftRight = 40.px, topBottom = 50.px)
            .width(Constants.SIDE_PANEL_WIDTH.px)
            .height(100.vh)//view height
            .position(Position.Fixed)
            .backgroundColor(Theme.Secondary.rgb)
            .zIndex(9)
    )
    {
        Image(
            modifier = Modifier.margin(bottom = 60.px),
            src = Res.Image.logo,
            description = "Logo Image"
        )

        NavigationItems()
    }
}

@Composable
private fun NavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    title: String,
    icon: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            NavigationItemStyle.toModifier()
            .cursor(Cursor.Pointer)
            .margin(bottom = 24.px)
            .onClick()
            {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    )
    {
        VectorIcon(
            modifier=modifier.margin(right = 10.px),
            pathData =icon ,
            selected=selected,
            color = if (selected) Theme.Primary.hex
                else Theme.White.hex ,
        )
        SpanText(
            modifier=Modifier
                .id(IdUtils.navigationText)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(
                    condition = selected,
                    other = Modifier.color(Theme.Primary.rgb)
                )
            ,
            text = title,
        )

    }

}

@Composable
fun NavigationItems()
{
    val context= rememberPageContext()
    SpanText(
        modifier = Modifier
            .margin(bottom = 30.px)
            .fontFamily(FONT_FAMILY)
            .fontSize(14.px)
            .color(Theme.HalfWhite.rgb),
        text = "Dashboard"
    )

    NavigationItem(
        title = "Home",
        icon = Res.PathIcon.home,
        onClick = {
            context.router.navigateTo(Screen.AdminHome .route)
        },
        selected =context.route.path==(Screen.AdminHome.route)
    )

    NavigationItem(
        title = "Create Post",
        icon = Res.PathIcon.create,
        onClick = {
            context.router.navigateTo(Screen.AdminCreatePost.route)
        },
        selected = context.route.path==(Screen.AdminCreatePost.route)
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        title = "My Posts",
        icon = Res.PathIcon.posts,
        selected = context.route.path==(Screen.AdminMyPosts.route),
        onClick = {
            context.router.navigateTo(Screen.AdminMyPosts.route)
        }
    )


    NavigationItem(
        title = "Logout",
        icon = Res.PathIcon.logout,
        onClick = {
            logout()
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    )

}



@Composable
private fun VectorIcon(
    modifier:Modifier =Modifier,
    pathData: String,
    selected: Boolean,
    color: String,
) {
    Svg(
        attrs = modifier
            .id(IdUtils.svgParent)
            .width(24.px)
            .height(24.px)
            .toAttrs {
                attr("viewBox", "0 0 24 24")
                attr("fill", "none")
            }
    ) {
        Path(
            attrs = Modifier
                .id(IdUtils.vectorIcon)
                .toAttrs {
                attr("d", pathData)
                attr("stroke", color)
                attr("stroke-width", "2")
                attr("stroke-Linecap", "round")
                attr("stroke-Linejoin", "round")
            }
        )

    }
}

@Composable
fun CollapseSizePanel(
    onMenuClick:() -> Unit,
){
    Row(
        modifier=Modifier
            .fillMaxWidth()
            .height(Constants.COLLAPSED_PANEL_HEIGHT.px)
            .padding(leftRight = 24.px)
            .backgroundColor(Theme.Secondary.rgb),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        FaBars(
            modifier = Modifier
                .margin(right=24.px,)
                .color(Colors.White)
                .cursor(Cursor.Pointer)
                .onClick {
                     onMenuClick()
                }
            ,
            size= IconSize.XL
        )

        Image(
            modifier = Modifier
                .width(80.px)

            ,
            src = Res.Image.logo,
            description = "Logo Image"
        )

    }
}

@Composable
fun OverflowSidePanel(
    onMenuClose:()->Unit,
    content: @Composable () -> Unit,
)
{
    val scope= rememberCoroutineScope()
    val breakPoint= rememberBreakpoint()
    val translateX= remember { mutableStateOf((100).percent) }
    val opacity= remember{ mutableStateOf(0.percent) }

    LaunchedEffect(key1=breakPoint)
    {
        translateX.value=0.percent
        opacity.value=100.percent
        if(breakPoint > Breakpoint.MD)
        {
            scope.launch {
                translateX.value=(-100).percent
                opacity.value=0.percent
                delay(500)
                onMenuClose()
            }

        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.vh)
            .position(Position.Fixed)
            .zIndex(9)
            .opacity(opacity.value)
            .transition(CSSTransition(property = "opacity",duration =300.ms))
            .backgroundColor(Theme.HalfBlack.rgb)

    )
    {
        Column(
            modifier = Modifier
                .padding(all=24.px)
                .fillMaxHeight()
                .width(
                    if(breakPoint < Breakpoint.MD)
                        50.percent
                    else 18.percent
                )// smaller than desktop
                .translateX(translateX.value)
                .transition(CSSTransition(property = "opacity",duration =300.ms))
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .backgroundColor(Theme.Secondary.rgb)
        ) {
            Row(
                modifier = Modifier.margin(bottom = 60.px),
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                FaXmark(
                    modifier = Modifier
                        .margin(right=20.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick {
                            scope.launch {
                                translateX.value=(-100).percent
                                opacity.value=0.percent
                                delay(500)
                                onMenuClose()
                            }
                        }
                    ,
                    size = IconSize.LG
                )

                Image(
                    modifier = Modifier.width(80.px),
                    src = Res.Image.logo,
                    description = "Logo Image",
                )
            }

           content()



        }


    }
}