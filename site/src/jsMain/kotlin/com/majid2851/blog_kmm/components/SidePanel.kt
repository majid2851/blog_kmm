package com.majid2851.blog_kmm.components

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.Res
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SidePanel()
{
   Column(
      modifier = Modifier
         .padding(leftRight = 40.px, topBottom = 50.px )
         .width(Constants.SIDE_PANEL_WIDTH.px)
         .height(100.vh)//view height
         .position(Position.Fixed)
         .backgroundColor(Theme.Secondary.rgb)
         .zIndex(9)
   )
   {
      Image(
         modifier = Modifier.margin(bottom = 60.px ),
         src= Res.Image.logo,
         description = "Logo Image"
      )

      SpanText(
         modifier = Modifier
            .margin(bottom = 30.px)
            .fontFamily(FONT_FAMILY)
            .fontSize(14.px)
            .color(Theme.HalfWhite.rgb)
         ,
         text = "Dashboard"
      )


   }
}