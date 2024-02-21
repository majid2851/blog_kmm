package com.majid2851.blog_kmm.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.majid2851.blog_kmm.components.MessagePopup
import com.majid2851.blog_kmm.models.NewsLetter
import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.styles.NewsletterInputStyle
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.PAGE_WIDTH
import com.majid2851.blog_kmm.util.IdUtils
import com.majid2851.blog_kmm.util.noBorder
import com.majid2851.blog_kmm.util.subscribeToNewsletter
import com.majid2851.blog_kmm.util.validateEmail
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.style.KobwebComposeStyleSheet.attr
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement

@Composable
fun NewsletterSection(breakpoint: Breakpoint)
{
    val scope = rememberCoroutineScope()
    val responseMessage= remember { mutableStateOf("") }
    val invalidEmailPopup= remember { mutableStateOf(false) }
    val subscribePopup= remember { mutableStateOf(false) }

    if(invalidEmailPopup.value){
        MessagePopup(
            message="Email Address is not valid.",
            onDialogDismiss = {
                invalidEmailPopup.value=false
            },
        )
    }
    if(subscribePopup.value){
        MessagePopup(
            message=responseMessage.value,
            onDialogDismiss = {
                subscribePopup.value=false
            },
        )
    }

    Box(
        modifier = Modifier
            .margin(topBottom = 250.px)
            .fillMaxWidth()
            .maxWidth(PAGE_WIDTH.px)
    ) {
        NewsletterContent(
            breakpoint = breakpoint,
            onSubscribed = {
                responseMessage.value=it
                scope.launch {
                    subscribePopup.value=true
                    delay(2000)
                    subscribePopup.value=false
                }
            },
            onInvalidEmail = {
                scope.launch {
                    invalidEmailPopup.value=true
                    delay(2000)
                    invalidEmailPopup.value=false
                }


            }
        )
    }
}

@Composable
fun NewsletterContent(
    breakpoint: Breakpoint,
    onSubscribed: (String) -> Unit,
    onInvalidEmail: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth())
    {
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(36.px)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center),
            text = "Don't miss any New Post."
        )
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(36.px)
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Center),
            text = "Sign up to our Newsletter!"
        )
        SpanText(
            modifier = Modifier
                .margin(top = 6.px)
                .fillMaxWidth()
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(18.px)
                .fontWeight(FontWeight.Normal)
                .color(Theme.HalfBlack.rgb)
                .textAlign(TextAlign.Center),
            text = "Keep up with the latest news and blogs."
        )
        if (breakpoint > Breakpoint.SM) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(top = 40.px),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubscriptionForm(
                    vertical = false,
                    onSubscribed ={
                        onSubscribed(it)
                    } ,
                    onInvalidEmail ={
                        onInvalidEmail()
                    }
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(top = 40.px),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SubscriptionForm(
                    vertical = true,
                    onSubscribed ={
                        onSubscribed(it)
                    },
                    onInvalidEmail = onInvalidEmail
                )
            }
        }
    }
}

@Composable
fun SubscriptionForm(
    vertical: Boolean,
    onSubscribed: (String) -> Unit,
    onInvalidEmail: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Input(
        type = InputType.Text,
        attrs = NewsletterInputStyle.toModifier()
            .id(IdUtils.emailInput)
            .width(320.px)
            .height(54.px)
            .color(Theme.DarkGray.rgb)
            .backgroundColor(Theme.LightGray.rgb)
            .padding(leftRight = 24.px)
            .margin(
                right = if (vertical) 0.px else 20.px,
                bottom = if (vertical) 20.px else 0.px
            )
            .fontFamily(Constants.FONT_FAMILY)
            .fontSize(16.px)
            .borderRadius(100.px)
            .toAttrs {
                attr("placeholder", "Your Email Address")
            }
    )
    Button(
        attrs = Modifier
            .onClick {
                val email = (document.getElementById(IdUtils.emailInput)
                        as HTMLInputElement).value

                if(validateEmail(email = email)){
                    scope.launch {
                        onSubscribed(subscribeToNewsletter(
                            newsLetter = NewsLetter(email = email),
                        ))
                    }
                }else{
                    onInvalidEmail()
                }

            }
            .height(54.px)
            .backgroundColor(Theme.Primary.rgb)
            .borderRadius(100.px)
            .padding(leftRight = 50.px)
            .noBorder()
            .cursor(Cursor.Pointer)
            .toAttrs()
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(18.px)
                .fontWeight(FontWeight.Medium)
                .color(Colors.White),
            text = "Subscribe"
        )
    }
}