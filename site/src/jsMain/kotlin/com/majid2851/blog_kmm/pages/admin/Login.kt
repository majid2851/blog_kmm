package com.majid2851.blog_kmm.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.models.User
import com.majid2851.blog_kmm.models.UserWithoutPassword
import com.majid2851.blog_kmm.navigation.Screen
import com.majid2851.blog_kmm.styles.LoginInputStyle
import com.majid2851.blog_kmm.util.Constants.FONT_FAMILY
import com.majid2851.blog_kmm.util.Res
import com.majid2851.blog_kmm.util.IdUtils
import com.majid2851.blog_kmm.util.checkUserExistence
import com.majid2851.blog_kmm.util.noBorder
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.set

@Page
@Composable
fun LoginScreen()
{
    val errorText = remember{ mutableStateOf("")}
    val context = rememberPageContext()
    val scope= rememberCoroutineScope()
    Box(
        modifier= Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier
                .padding(
                    leftRight = 50.px,
                    top = 80.px,
                    bottom = 24.px,
                )
                .background(Theme.LightGray.rgb),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Image(
                modifier = Modifier
                    .margin(50.px)
                    .width(100.px),
                src = Res.Image.logo,
                description = "Logo Image"
            )

            Input(
                type = InputType.Text,
                attrs =  LoginInputStyle.toModifier()
                    .id(IdUtils.userNameInput)
                    .margin(bottom = 12.px)
                    .width(350.px)
                    .height(54.px)
                    .fontSize(14.px)
                    .padding(leftRight = 20.px,)
                    .backgroundColor(Colors.White)
                    .fontFamily(FONT_FAMILY)
                    .outline(
                        width = 0.px,
                        style = LineStyle.None,
                        color=Colors.Transparent
                    )
                    .toAttrs{
                        attr("placeholder","Username")
                    },
            )

            Input(
                type = InputType.Password,
                attrs =  LoginInputStyle.toModifier()
                    .id(IdUtils.passwordInput)
                    .margin(bottom = 20.px)
                    .width(350.px)
                    .height(54.px)
                    .fontSize(14.px)
                    .padding(leftRight = 20.px,)
                    .backgroundColor(Colors.White)
                    .fontFamily(FONT_FAMILY)
                    .outline(
                        width = 0.px,
                        style = LineStyle.None,
                        color=Colors.Transparent
                    )
                    .toAttrs{
                        attr("placeholder","Password")
                    },
            )

            Button(
                attrs =Modifier
                    .margin(bottom = 24.px)
                    .width(350.px)
                    .height(54.px)
                    .backgroundColor(Theme.Primary.rgb)
                    .color(Colors.White)
                    .borderRadius(r = 4.px)
                    .fontFamily()
                    .fontWeight(FontWeight.Medium)
                    .fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .noBorder()
                    .cursor(Cursor.Pointer)
                    .onClick()
                    {
                        scope.launch {
                            val userName=(document.getElementById(IdUtils.userNameInput)
                                    as HTMLInputElement).value
                            val password=(document.getElementById(IdUtils.passwordInput)
                                    as HTMLInputElement).value
                            if (userName.isNotEmpty() && password.isNotEmpty())
                            {
                                val user= checkUserExistence(
                                    user = User(
                                        userName=userName,
                                        password = password
                                    )
                                )
                                if(user!=null)
                                {
                                    rememberLoggedIn(remember = true,user=user)
                                    context.router.
                                        navigateTo(Screen.AdminHome.route)

                                }else{
                                    errorText.value="The user doesn't exist"
                                    delay(3000)
                                    errorText.value=" "
                                }

                            }else{
                                errorText.value="Input fields are empty"
                                delay(3000)
                                errorText.value=" "
                            }
                        }


                    }
                    .toAttrs()
            ){
                SpanText(
                   text="Sign in"
                )
            }

            SpanText(
                modifier=Modifier
                  .width(350.px)
                  .color(Colors.Red)
                    .fontFamily(FONT_FAMILY)
                    .textAlign(TextAlign.Center),
                text=errorText.value
            )
        }
    }
}

private fun rememberLoggedIn(
    remember:Boolean,
    user: UserWithoutPassword?=null
)
{
    localStorage[IdUtils.localStorage] = remember.toString()
    if(user!=null){
        localStorage[IdUtils.userId]=user.id
        localStorage[IdUtils.userName]=user.userName
    }
}