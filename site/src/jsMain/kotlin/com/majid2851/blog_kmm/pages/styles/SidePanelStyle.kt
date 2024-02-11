package com.majid2851.blog_kmm.pages.styles

import com.majid2851.blog_kmm.models.Theme
import com.majid2851.blog_kmm.util.IdUtils
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import org.jetbrains.compose.web.css.Transition
import org.jetbrains.compose.web.css.ms

val NavigationItemStyle by ComponentStyle {

    cssRule(":hover > #${IdUtils.svgParent} > #${IdUtils.vectorIcon}")
    {
        Modifier
            .transition(CSSTransition(
                property = TransitionProperty.All,
                duration = 300.ms
            ))
            .styleModifier {
                property("stroke", Theme.Primary.hex)
            }

    }

    cssRule(":hover > #${IdUtils.svgParent} > #${IdUtils.vectorIcon}")
    {
        Modifier
            .transition(CSSTransition(
                property = TransitionProperty.All,
                duration = 300.ms
            ))
            .styleModifier {
            property("stroke", Theme.Primary.hex)
        }

    }
    cssRule(":hover > #${IdUtils.navigationText}"){
        Modifier
            .transition(CSSTransition(
                property = TransitionProperty.All,
                duration = 300.ms
            ))
            .color(Theme.Primary.rgb)
    }


    cssRule(" > #${IdUtils.navigationText}"){
        Modifier.color(Theme.White.rgb)
    }



}