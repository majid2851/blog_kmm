package com.majid2851.blog_kmm.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.majid2851.blog_kmm.models.PostWithoutDetails
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.navigation.Screen
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.parseDateString
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    post: PostWithoutDetails,
    selectableMode: Boolean = false,
    darkTheme: Boolean = false,
    thumbnailHeight:CSSSizeValue<CSSUnit.px> = 240.px,
    titleMaxLine:Int=2,
    vertical: Boolean = true,
    onSelect: (String) -> Unit = {},
    onDeselect: (String) -> Unit = {},
    onClick:(String)->Unit
) {
    val context = rememberPageContext()
    val checked = remember(selectableMode) { mutableStateOf(false) }
    if (vertical) {
        Column(
            modifier = Modifier
//                .thenIf(
//                    condition = post.main,
//                    other = MainPostPreviewStyle.toModifier()
//                )
//                .thenIf(
//                    condition = !post.main,
//                    other = PostPreviewStyle.toModifier()
//                )
//                .then(modifier)
                .fillMaxWidth(
                    if (darkTheme) 100.percent
//                    else if (titleColor == Theme.Sponsored.rgb) 100.percent
                    else 95.percent
                )
                .margin(bottom = 24.px)
                .padding(all = if (selectableMode) 10.px else 0.px)
                .borderRadius(r = 4.px)
                .border(
                    width = if (selectableMode) 4.px else 0.px,
                    style = if (selectableMode) LineStyle.Solid else LineStyle.None,
                    color = if (checked.value) Theme.Primary.rgb else Theme.LightGray.rgb
                )
                .onClick {
                    if (selectableMode) {
                        checked.value = !checked.value
                        if (checked.value) {
                            onSelect(post.id)
                        } else {
                            onDeselect(post.id)
                        }
                    } else {
                        onClick(post.id)
                    }
                }
                .transition(CSSTransition(property = TransitionProperty.All, duration = 200.ms))
                .cursor(Cursor.Pointer)
        )
        {
            PostContent(
                selectableMode = selectableMode,
                checked = checked,
                onSelect = onSelect,
                post = post,
                onDeselect=onDeselect,
                context=context,
                darkTheme=darkTheme,
                vertical=vertical,
                thumbnailHeight = thumbnailHeight,
                titleMaxLine = titleMaxLine,
            )
        }
    } else {
        Row(
            modifier = Modifier
                .onClick { onClick(post.id) }
                .cursor(Cursor.Pointer)
        ) {
            PostContent(
                selectableMode = selectableMode,
                checked = checked,
                onSelect = onSelect,
                post = post,
                onDeselect=onDeselect,
                context=context,
                darkTheme=darkTheme,
                vertical=vertical,
                thumbnailHeight=thumbnailHeight,
                titleMaxLine = titleMaxLine
            )

        }
    }


}

@Composable
private fun PostContent(
    selectableMode: Boolean,
    checked: MutableState<Boolean>,
    onSelect: (String) -> Unit,
    post: PostWithoutDetails,
    onDeselect: (String) -> Unit,
    context: PageContext,
    darkTheme: Boolean,
    vertical:Boolean,
    titleMaxLine: Int,
    thumbnailHeight:CSSSizeValue<CSSUnit.px>
) {
    Image(
        modifier = Modifier
            .margin(bottom = 16.px)
            .height(thumbnailHeight)
            .fillMaxWidth()
            .borderRadius(r = 4.px).border(
                width = if (selectableMode) 4.px else 0.px,
                style = if (selectableMode) LineStyle.Solid else LineStyle.None,
                color = if (checked.value) Theme.Primary.rgb else Theme.LightGray.rgb
            ).onClick {
                if (selectableMode) {
                    checked.value = !checked.value
                    if (checked.value) {
                        onSelect(post.id)
                    } else {
                        onDeselect(post.id)
                    }
                } else {

                    context.router.navigateTo(
                        Screen.AdminCreatePost.passPostId(id = post.id)
                    )
                }
            }.padding(all = if (selectableMode) 10.px else 0.px).cursor(Cursor.Pointer).transition(
                CSSTransition(
                    property = TransitionProperty.All, duration = 200.ms
                )
            ).objectFit(ObjectFit.Cover), src = post.thumbnail, description = "Post Thumbnail Image"
    )

    Column(
            modifier =Modifier
                .thenIf(
                    condition = !vertical,
                    other = Modifier.margin(8.px)
                )
    )
    {
        SpanText(
            modifier = Modifier.fontFamily(FONT_FAMILY).fontSize(10.px)
                .color(if (darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )

        SpanText(
            modifier = Modifier.margin(bottom = 20.px).fontFamily(FONT_FAMILY).fontSize(20.px)
                .fontWeight(FontWeight.Bold).color(if (darkTheme) Colors.White else Colors.Black)
                .textOverflow(TextOverflow.Ellipsis).overflow(Overflow.Hidden).styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "$titleMaxLine")
                    property("line-clamp", "$titleMaxLine")
                    property("-webkit-box-orient", "vertical")
                }, text = post.title
        )
        SpanText(
            modifier = Modifier.margin(bottom = 10.px).fontFamily(FONT_FAMILY).fontSize(16.px)
                .color(if (darkTheme) Colors.White else Colors.Black)
                .textOverflow(TextOverflow.Ellipsis).overflow(Overflow.Hidden).styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "2")
                    property("line-clamp", "2")
                    property("-webkit-box-orient", "vertical")
                }, text = post.subtitle
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CategoryChip(
                category = post.category, darkTheme = darkTheme
            )
            if (selectableMode) {
                CheckboxInput(
                    checked = checked.value, attrs = Modifier.size(20.px).toAttrs()

                )
            }
        }
    }


}

