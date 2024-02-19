package com.majid2851.blog_kmm.navigation

import com.majid2851.blog_kmm.util.Constants

sealed class Screen(val route:String)
{
    object AdminHome: Screen(route = "/admin/")
    object AdminLogin:Screen(route = "/admin/login")
    object AdminCreatePost:Screen(route = "/admin/create")
    object AdminMyPosts:Screen(route = "/admin/posts"){
        fun searchByTitle(query:String)=
             "/admin/posts?${Constants.QUERY_PARAM}=$query"

    }
    object AdminLogout:Screen(route = "/admin/logout")
    object AdminSuccess:Screen(route = "/admin/success")




}