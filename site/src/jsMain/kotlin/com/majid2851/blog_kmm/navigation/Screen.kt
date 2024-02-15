package com.majid2851.blog_kmm.navigation

sealed class Screen(val route:String)
{
    object AdminHome: Screen(route = "/admin/")
    object AdminLogin:Screen(route = "/admin/login")
    object AdminCreatePost:Screen(route = "/admin/create")
    object AdminMyPosts:Screen(route = "/admin/posts")
    object AdminLogout:Screen(route = "/admin/logout")
    object AdminSuccess:Screen(route = "/admin/success")




}