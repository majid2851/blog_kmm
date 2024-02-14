package com.majid2851.blog_kmm.models

import com.majid2851.blog_kmm.util.Res

enum class EditorKey(
    val icon:String,

)
{
    Bold(icon= Res.Icon.bold),
    Italic(icon = Res.Icon.italic),
    Link(icon= Res.Icon.link),
    Title(icon=Res.Icon.title),
    Subtitle(icon=Res.Icon.subtitle),
    Code(icon=Res.Icon.code),
    Quote(icon=Res.Icon.quote),
    Image(icon = Res.Icon.image)

}