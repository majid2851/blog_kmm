package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.majid2851.blog_kmm.components.AdminPageLayout
import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.models.EditorKey
import com.majid2851.blog_kmm.models.Post
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.pages.styles.EditorKeyStyle
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.Constants.SIDE_PANEL_WIDTH
import com.majid2851.blog_kmm.util.IdUtils
import com.majid2851.blog_kmm.util.addPost
import com.majid2851.blog_kmm.util.getValueBasedOnId
import com.majid2851.blog_kmm.util.isUserLoggedIn
import com.majid2851.blog_kmm.util.noBorder
import com.varabyte.kobweb.browser.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLTextAreaElement
import kotlin.js.Date

data class CreatePageUiEvent(
    val id :String ="",
    val title:String ="",
    val subtitle:String="",
    val thumbnail: String="",
    val thumbnailName: String="",
    val content:String="",
    val category:Category=Category.Programming,
    val thumbnailInputDisabled: Boolean=false,
    val editorVisibility: Boolean=true,
    val main:Boolean=false,
    val popular:Boolean=false,
    val sponsor:Boolean=false,
)

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
    val scope= rememberCoroutineScope()
    val breakPoint= rememberBreakpoint()
    val uiState= remember {
        mutableStateOf(CreatePageUiEvent())
    }

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
                        switchValue =uiState.value.popular,
                        onSwitchChange = {
                            uiState.value=uiState.value.copy(popular = it)
                        }
                    )

                    SwitchRowItem(
                        breakPoint=breakPoint,
                        title = "Main",
                        switchValue = uiState.value.main,
                        onSwitchChange = {
                            uiState.value=uiState.value.copy(main = it)
                        }
                    )

                    SwitchRowItem(
                        breakPoint=breakPoint,
                        title = "Sponsored",
                        switchValue = uiState.value.sponsor,
                        onSwitchChange = {
                            uiState.value=uiState.value.copy(sponsor = it)
                        }
                    )
                }

                CustomInput(
                    placeHolder = "Title",
                    marginTop = 12,
                    inputId = IdUtils.titleInput
                )

                CustomInput(
                    placeHolder = "Subtitle",
                    inputId = IdUtils.subtitleInput,
                )

                CategoryDropDown(
                    selectedCategory =uiState.value.category,
                    onCategorySelected = {
                        uiState.value=uiState.value.copy(category = it)
                    }
                )

                SwitchRowItem(
                    breakPoint=breakPoint,
                    title = "Past an Image URL instead",
                    switchValue =uiState.value.thumbnailInputDisabled,
                    switchSize = SwitchSize.MD,
                    onSwitchChange = {
                        uiState.value=uiState.value.copy(
                            thumbnailInputDisabled = it
                        )
                    }
                )

                ThumbnailUploader(
                    thumbnailName =uiState.value.thumbnailName ,
                    thumbnailInputDisabled = !uiState.value.thumbnailInputDisabled,
                    onThumbnailSelect = { name,file ->
//                        (document.getElementById(IdUtils.thumbnailInput)
//                                as HTMLInputElement).value =name
                        uiState.value=uiState.value.copy(
                            thumbnail = file,
                            thumbnailName = name
                        )
                    }
                )

                EditorControls(
                    breakPoint=breakPoint,
                    editorVisibility=uiState.value.editorVisibility,
                    onEditorVisibilityChange = {
                        uiState.value=uiState.value.copy(
                            editorVisibility = !uiState.value.editorVisibility
                        )
                    }
                )

                Editor(editorVisibility =  uiState.value.editorVisibility)

                createButton(
                    onClick = {

                        uiState.value=uiState.value.copy(
                            title =getValueBasedOnId(IdUtils.titleInput) ,
                            subtitle = getValueBasedOnId(IdUtils.subtitleInput) ,
                            content = (document.getElementById(IdUtils.editor)
                                    as HTMLTextAreaElement).value,
                        )
                        if(uiState.value.thumbnailInputDisabled){
                            uiState.value=uiState.value.copy(
                                thumbnail = getValueBasedOnId(IdUtils.thumbnailInput)
                            )
                        }

                        val value=uiState.value
                        if(
                            value.title.isNotEmpty() &&
                            value.subtitle.isNotEmpty() &&
                            value.thumbnail.isNotEmpty() &&
                            value.content.isNotEmpty()
                        ){
                            scope.launch {
                                val result= addPost(
                                    Post(
                                        author = localStorage.
                                        getItem(IdUtils.userName).toString(),
                                        title = value.title,
                                        subtitle = value.subtitle,
                                        date = Date.now().toLong(),
                                        thumbnail = value.thumbnail,
                                        content = value.content,
                                        category = value.category.name,
                                        popular = value.popular,
                                        main = value.main,
                                        sponsored = value.sponsor,
                                    )
                                )
                                if(result){
                                    println("Successful")
                                }
                            }


                        }else{
                            println("Please fill out all fileds")
                        }
                    }
                )


            }

        }
    }
}



@Composable
private fun createButton(onClick: () -> Unit)
{
    Button(
        attrs = Modifier
            .onClick {onClick() }
            .fillMaxWidth()
            .height(54.px)
            .margin(top = 24.px)
            .backgroundColor(Theme.Primary.rgb)
            .color(Colors.White)
            .borderRadius(r = 4.px)
            .noBorder()
            .fontFamily(FONT_FAMILY)
            .fontSize(16.px)
            .toAttrs()
    ) {
        SpanText("Create")
    }
}

@Composable
fun EditorControls(
    breakPoint: Breakpoint,
    editorVisibility: Boolean,
    onEditorVisibilityChange: ()->Unit,
)
{
    Box(modifier = Modifier.fillMaxWidth())
    {
        SimpleGrid(numColumns(base = 1, sm = 2))
        {
            Row(
                modifier = Modifier
                    .backgroundColor(Theme.LightGray.rgb)
                    .borderRadius(r=4.px)
                    .height(54.px)
            )
            {
                EditorKey.entries.forEach {
                    EditorKeyView(key = it)
                }
            }

            Box(contentAlignment = Alignment.CenterEnd)
            {
                Button(
                    attrs = Modifier
                        .height(54.px)
                        .thenIf(
                            condition =breakPoint <Breakpoint.SM,
                            other = Modifier.fillMaxWidth()

                        )
                        .margin(topBottom=if (breakPoint < Breakpoint.SM) 10.px
                            else 0.px
                        )
                        .padding(leftRight = 24.px)
                        .borderRadius(r=4.px)
                        .backgroundColor(
                            if(editorVisibility) Theme.LightGray.rgb
                            else Theme.Primary.rgb
                        )
                        .color(
                           if(editorVisibility) Theme.DarkGray.rgb
                            else Colors.White
                        )
                        .noBorder()
                        .onClick {
                            onEditorVisibilityChange()
                        }
                        .toAttrs()
                )
                {
                    SpanText(
                        modifier=Modifier
                            .fontFamily(FONT_FAMILY)
                            .fontWeight(FontWeight.Medium)
                            .fontSize(14.px),

                        text = "Preview"
                    )

                }
            }





        }


    }

}
@Composable
fun EditorKeyView(key:EditorKey)
{
    Box(
        modifier = EditorKeyStyle
            .toModifier()
            .fillMaxHeight()
            .padding(leftRight = 12.px)
            .borderRadius(r=4.px)
            .cursor(Cursor.Pointer)
            .onClick {  },
        contentAlignment = Alignment.Center,

    )
    {
        Image(
            src= key.icon,
            description="${key.name} Icon"
        )
    }



}

@Composable
fun Editor(editorVisibility: Boolean)
{
   Box(modifier = Modifier.fillMaxWidth())
   {
       TextArea(
           attrs = Modifier
               .id(IdUtils.editor)
               .fillMaxWidth()
               .height(400.px)
               .resize(Resize.None)
               .maxHeight(400.px)
               .margin(top = 20.px)
               .padding(all = 20.px)
               .fontFamily(FONT_FAMILY)
               .fontSize(16.px)
               .backgroundColor(Theme.LightGray.rgb)
               .borderRadius(r=4.px)
               .noBorder()
               .visibility(
                   if(editorVisibility) Visibility.Visible
                   else Visibility.Hidden
               )
               .toAttrs{
                    attr("placeholder","Post Content")
               }
       )

       Div(
           attrs = Modifier
               .id(IdUtils.editorPreview)
               .fillMaxWidth()
               .height(400.px)
               .maxHeight(400.px)
               .margin(top = 20.px)
               .padding(all = 20.px)
               .fontFamily(FONT_FAMILY)
               .fontSize(16.px)
               .backgroundColor(Theme.LightGray.rgb)
               .borderRadius(r=4.px)
               .noBorder()
               .visibility(
                   if(editorVisibility)
                    Visibility.Hidden else Visibility.Visible
               )
               .overflow(Overflow.Auto)
               .scrollBehavior(ScrollBehavior.Smooth)
               .toAttrs()
       )
       {


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
fun ThumbnailUploader(
    thumbnailName:String,
    thumbnailInputDisabled:Boolean,
    onThumbnailSelect:(String,String) ->Unit
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(bottom = 20.px)
            .height(54.px)
    ) {
        Input(
            type = InputType.Text,
            attrs =Modifier
                .id(IdUtils.thumbnailInput)
                .fillMaxSize()
                .padding(leftRight = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .fontWeight(FontWeight.Medium)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(
                    condition =  thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .toAttrs{
                    attr("placeholder","Thumbnail")
                    attr("value",thumbnailName)
                }
        )
        Button(
            attrs = Modifier
                .onClick {
                    document.loadDataUrlFromDisk(
                        accept = "image/png, image/jpeg",
                        onLoad = {
                            onThumbnailSelect(filename,it)//it is the string of file
                        }
                    )
                }
                .fillMaxHeight()
                .padding(leftRight = 24.px)
                .backgroundColor(
                    if(!thumbnailInputDisabled)
                        Theme.LightGray.rgb
                    else Theme.Primary.rgb
                )
                .noBorder()
                .color(if(!thumbnailInputDisabled)
                    Theme.DarkGray.rgb else Colors.White
                )
                .fontWeight(FontWeight.Medium)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(
                    condition = !thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .borderRadius(r = 4.px)
                .toAttrs()
        ) {
            SpanText(
                text ="Upload"
            )
        }

    }


}

@Composable
private fun CustomInput(
    placeHolder:String,
    marginTop:Int=0,
    inputId:String,
)
{
    Input(
        type = InputType.Text,
        attrs = Modifier
            .id(inputId)
            .fillMaxWidth()
            .height(54.px)
            .margin(top = marginTop.px, bottom = 12.px)
            .backgroundColor(Theme.LightGray.rgb)
            .borderRadius(r = 4.px)
            .noBorder()
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
    switchValue:Boolean,
    switchSize:SwitchSize = SwitchSize.LG,
    onSwitchChange:(Boolean)->Unit
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(
                right = if (breakPoint < Breakpoint.SM) 0.px else 24.px,
                left =  if (breakPoint < Breakpoint.SM) 0.px else 24.px,
                bottom = if (breakPoint < Breakpoint.SM) 12.px
                else 0.px
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    )
    {
        Switch(
            modifier = Modifier.margin(right = 8.px),
            checked = switchValue,
            onCheckedChange = {
                onSwitchChange(it)
            },
            size = switchSize
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

