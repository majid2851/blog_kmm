package com.majid2851.blog_kmm.components

import androidx.compose.runtime.Composable
import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun CategoryChip(category: Category)
{
    Box(
        modifier = Modifier
            .height(32.px)
            .padding(leftRight = 14.px)
            .borderRadius(r = 100.px)
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = Theme.HalfBlack.rgb
            ),
        contentAlignment = Alignment.Center
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(12.px)
                .color(
                    Theme.HalfBlack.rgb
                ),
            text = category.name
        )
    }

}