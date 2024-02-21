package com.majid2851.blog_kmm.navigation

import com.majid2851.blog_kmm.models.Category
import com.majid2851.blog_kmm.util.Constants
import com.majid2851.blog_kmm.util.Constants.POST_ID_PARAM

sealed class Screen(val route:String)
{
    object AdminHome: Screen(route = "/admin/")
    object AdminLogin:Screen(route = "/admin/login")
    object AdminCreatePost:Screen(route = "/admin/create"){
        fun passPostId(id:String) = "/admin/create?${Constants.POST_ID_PARAM}" +
                "=$id"
    }
    object AdminMyPosts:Screen(route = "/admin/posts"){
        fun searchByTitle(query:String)=
             "/admin/posts?${Constants.QUERY_PARAM}=$query"

    }
    object AdminLogout:Screen(route = "/admin/logout")
    object AdminSuccess:Screen(route = "/admin/success"){
        fun postUpdated() ="/admin/success?${Constants.UpdateParam}=true"
    }
    object HomePage:Screen(route = "/")

    object SearchPage:Screen(route = "/search/query"){
        fun searchByCategory(category: Category)="/search/query?" +
                "${Constants.CATEGORY_PARAM}=${category.name}"
        fun searchByTitle(query: String)
            ="/search/query?${Constants.QUERY_PARAM}=$query"
    }
    object PostPage:Screen(route = "/posts/post"){
        fun getPost(id:String)="/posts/post?${POST_ID_PARAM}=$id"
    }




}