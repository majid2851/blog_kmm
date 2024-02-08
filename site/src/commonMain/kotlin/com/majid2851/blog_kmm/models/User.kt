package com.majid2851.blog_kmm.models

expect class User {
    val id:String
    val userName:String
    val password:String
}

expect class UserWithoutPassword{
    val id:String
    val userName:String
}

