package com.majid2851.blog_kmm.util

sealed class RequestState<out T>
{
    object Idle : RequestState<Nothing>()
    object Loading : RequestState<Nothing>()

    data class Success<T>(val data:T):RequestState<T>()
    data class Error(val error:Throwable):RequestState<Nothing>()






}