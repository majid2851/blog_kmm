package com.majid2851.blog_kmm.components

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.navigation.Screen
import com.majid2851.blog_kmm.pages.styles.NavigationItemStyle
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.Res
import com.majid2851.blog_kmm.util.IdUtils
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SidePanel()
{
    val context= rememberPageContext()
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
            onClick = {},
            selected =context.route.path==(Screen.AdminHome.route)
        )

        NavigationItem(
            title = "Create Post",
            icon = Res.PathIcon.create,
            onClick = {},
            selected = context.route.path==(Screen.AdminCreatePost.route)
        )

        NavigationItem(
            modifier = Modifier.margin(bottom = 24.px),
            title = "My Posts",
            icon = Res.PathIcon.posts,
            selected = context.route.path==(Screen.AdminMyPosts.route),
            onClick = {}
        )


        NavigationItem(
            title = "Logout",
            icon = Res.PathIcon.logout,
            onClick = {}
        )

    }
}

@Composable
fun NavigationItem(
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
fun VectorIcon(
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