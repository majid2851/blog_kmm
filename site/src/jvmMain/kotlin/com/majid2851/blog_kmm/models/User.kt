package com.majid2851.blog_kmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.id.ObjectIdGenerator

@Serializable
actual data class User(
    @SerialName(value="_id")
    actual val id:String= ObjectIdGenerator.newObjectId<String>()
        .id.toHexString(),
    actual val userName:String="",
    actual val password:String="",
)


@Serializable
data class UserWithoutPassword(
    @SerialName(value="_id")
    val id:String= ObjectIdGenerator.newObjectId<String>()
        .id.toHexString(),
    val userName:String="",
)