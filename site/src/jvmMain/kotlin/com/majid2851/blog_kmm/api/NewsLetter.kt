package com.majid2851.blog_kmm.api

import com.majid2851.blog_kmm.data.MongoDB
import com.majid2851.blog_kmm.models.NewsLetter
import com.majid2851.blog_kmm.util.ApiPath
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue

@Api(routeOverride = ApiPath.subscribe)
suspend fun subscribeNewsLetter(context:ApiContext)
{
    try {
        val newsLetter=context.req.getBody<NewsLetter>()
        context.res.setBody(
            newsLetter?.let {
                context.data.getValue<MongoDB>().subscribe(
                    newsLetter=it
                )
            }
        )


    }catch (e:Exception){
        context.res.setBody(e.message)
        println(e.toString())
    }

}