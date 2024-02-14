package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.majid2851.blog_kmm.components.AdminPageLayout
import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.Constants.SIDE_PANEL_WIDTH
import com.majid2851.blog_kmm.util.isUserLoggedIn
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.breakpoint.BreakpointUnitValue
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

@Page
@Composable
fun Create()
{
    isUserLoggedIn {
        CreateScreen()

    }
}

@Composable
fun CreateScreen()
{

    val breakPoint= rememberBreakpoint()
    val popularSwitch= remember { mutableStateOf(false) }
    val mainSwitch= remember { mutableStateOf(false) }
    val sponsoredSwitch= remember { mutableStateOf(false) }
    val selectedCategory= remember { mutableStateOf(Category.Programming) }

    AdminPageLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .margin(topBottom = 50.px)
                .padding(left = if(breakPoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px
                    else 0.px
                ),
            contentAlignment = Alignment.Center,
        )
        {
            Column(
                modifier=Modifier
                    .fillMaxSize()
                    .maxWidth(700.px)
                ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                SimpleGrid(numColumns = numColumns(base = 1,sm=3)){

                    SwitchRowItem(
                        breakPoint=breakPoint,
                        title = "Popular",
                        switchValue =popularSwitch
                    )

                    SwitchRowItem(
                        breakPoint=breakPoint,
                        title = "Main",
                        switchValue = mainSwitch,
                    )

                    SwitchRowItem(
                        breakPoint=breakPoint,
                        title = "Sponsored",
                        switchValue = sponsoredSwitch,
                    )
                }

                CustomInput(
                    placeHolder = "Title",
                    marginTop = 12
                )

                CustomInput(
                    placeHolder = "Subtitle"
                )

                CategoryDropDown(
                    selectedCategory =selectedCategory.value,
                    onCategorySelected = {
                        selectedCategory.value=it
                    }
                )


            }

        }
    }
}

@Composable
private fun CategoryDropDown(
    selectedCategory: Category,
    onCategorySelected:(Category) ->Unit,
)
{
    Box(
        modifier = Modifier
            .margin(topBottom = 12.px)
            .classNames("dropdown")
            .fillMaxWidth()
            .height(54.px)
            .backgroundColor(Theme.LightGray.rgb)
            .cursor(Cursor.Pointer)
            .attrsModifier {
                attr("data-bs-toggle","dropdown")
            }
    )
    {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(leftRight = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ){
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .fontSize(16.px)
                    .fontFamily(FONT_FAMILY),
                text = selectedCategory.name
            )
            Box(
                modifier = Modifier
                    .classNames("dropdown-toggle")

            )

        }

        Ul(
            attrs = Modifier
                .fillMaxWidth()
                .classNames("dropdown-menu")
                .toAttrs()
        ) {
            Category.entries.forEach{ category->
                Li{
                    A(
                        attrs=Modifier
                            .classNames("dropdown-item")
                            .color(Colors.Black)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(16.px)
                            .onClick { onCategorySelected(category) }
                            .toAttrs()
                    ){
                        Text(
                            value = category.name
                        )
                    }
                }
            }

        }

    }



}

@Composable
private fun CustomInput(placeHolder:String,marginTop:Int=0)
{
    Input(
        type = InputType.Text,
        attrs = Modifier
            .fillMaxWidth()
            .height(54.px)
            .margin(top = marginTop.px, bottom = 12.px)
            .backgroundColor(Theme.LightGray.rgb)
            .borderRadius(r = 4.px)
            .border(
                width = 0.px,
                style = LineStyle.None,
                color = Colors.Transparent
            )
            .outline(
                width = 0.px,
                style = LineStyle.None,
                color = Colors.Transparent
            )
            .fontFamily(FONT_FAMILY)
            .fontSize(16.px)
            .toAttrs {
                attr("placeholder", placeHolder)
            }

    )
}

@Composable
private fun SwitchRowItem(
    breakPoint: Breakpoint,
    title: String,
    switchValue: MutableState<Boolean>,
)
{
    Row(
        modifier = Modifier
            .margin(
                right = if (breakPoint < Breakpoint.SM) 0.px else 24.px,
                bottom = if (breakPoint < Breakpoint.SM) 12.px
                else 0.px
            ),
        verticalAlignment = Alignment.CenterVertically

    )
    {
        Switch(
            modifier = Modifier.margin(right = 8.px),
            checked = switchValue.value,
            onCheckedChange = {
                switchValue.value=it
            },
            size = SwitchSize.LG
        )

        SpanText(
            modifier = Modifier
                .fontSize(14.px)
                .fontFamily(FONT_FAMILY)
                .color(Theme.HalfBlack.rgb),
            text = title
        )

    }
}

