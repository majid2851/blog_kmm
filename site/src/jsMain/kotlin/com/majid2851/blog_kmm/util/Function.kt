package com.majid2851.blog_kmm.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.majid2851.blog_kmm.models.ControlStyle
import com.majid2851.blog_kmm.models.EditorControl
import com.majid2851.blog_kmm.navigation.Screen
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

@Composable
fun isUserLoggedIn(content:@Composable ()->Unit)
{

    val context = rememberPageContext()
    val remembered = remember{ localStorage[IdUtils.localStorage].toBoolean() }
    val userId= remember{ localStorage[IdUtils.userId] }
    val userIdExist= remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit){


        userIdExist.value = if(!userId.isNullOrEmpty()) checkUserId(id = userId)
            else false
        if(remembered==false || userIdExist.value==false){
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    }

    if (remembered && userIdExist.value){
        content()
    }else{
        println("Loading....")
    }

}


fun logout()
{
    localStorage[IdUtils.localStorage] = "false"
    localStorage[IdUtils.userName] = ""
    localStorage[IdUtils.userId] = ""
}


fun Modifier.noBorder():Modifier{
    return this
        .border(
            width =0.px,
            style = LineStyle.None,
            color = Colors.Transparent,
        )
        .outline(
            width =0.px,
            style = LineStyle.None,
            color = Colors.Transparent,
        )
}
 fun getValueBasedOnId(id:String): String {
    return (document.getElementById(id) as HTMLInputElement).value
}

fun getEditor()= document.getElementById(IdUtils.editor)
    as HTMLTextAreaElement

fun getSelectedIntRange():IntRange ? {
    val editor = getEditor()
    val start=editor.selectionStart
    val end=editor.selectionEnd

    return if(start!=null && end!=null){
        IntRange(start,end-1)
    }else null
}

fun getSelectedText():String ?{
    val range= getSelectedIntRange()

    return if(range!=null){
        getEditor().value.substring(range)
    }else null

}

fun applyStyle(controlStyle: ControlStyle)
{
    val selectedText= getSelectedText()
    val selectedIntRange= getSelectedIntRange()
    if(selectedIntRange !=null && selectedText !=null){
        getEditor().value = getEditor().value.replaceRange(
            range = selectedIntRange,
            replacement = controlStyle.style,
        )
        document.getElementById(IdUtils.editorPreview)
            ?.innerHTML = getEditor().value
    }
}


fun applyControlStyle(
    editorControl: EditorControl,
    onLinkClick: () -> Unit,
    onImgClick:()->Unit,
)
{
    when(editorControl)
    {
        EditorControl.Bold->{
            applyStyle(
                controlStyle = ControlStyle.Bold(
                    selectedText = getSelectedText()
                )
            )}
        EditorControl.Italic->{
            applyStyle(
                controlStyle = ControlStyle.Italic(
                    selectedText = getSelectedText()
                )
            )}
        EditorControl.Link->{
            onLinkClick()
        }
        EditorControl.Title->{
            applyStyle(
                controlStyle = ControlStyle.Title(
                    selectedText = getSelectedText()
                )
            )}
        EditorControl.Subtitle->{
            applyStyle(
                controlStyle = ControlStyle.Subtitle(
                    selectedText = getSelectedText()
                )
            )}
        EditorControl.Quote->{
            applyStyle(
                controlStyle = ControlStyle.Quote(
                    selectedText = getSelectedText()
                )
            )}
        EditorControl.Code->{
            applyStyle(
                controlStyle = ControlStyle.Code(
                    selectedText = getSelectedText()
                )
            )}
        EditorControl.Image->{
            onImgClick()
        }
        else -> {}
    }

}

fun Long.parseDateString()= Date(this).toLocaleDateString()

 fun parseSwitchText(posts:List<String>):String{
    return if(posts.size==1) "1 Post Selected."
        else "${posts.size} Posts Selected."
}
fun validateEmail(email: String): Boolean {
    val regex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    return regex.toRegex().matches(email)
}

