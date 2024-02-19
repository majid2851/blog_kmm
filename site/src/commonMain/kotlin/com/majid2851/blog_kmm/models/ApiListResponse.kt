package com.majid2851.blog_kmm.models

expect sealed class ApiListResponse {
    object Idle
    class Success
    class Error
}


expect sealed class ApiResponse {
    object Idle
    class Success
    class Error
}