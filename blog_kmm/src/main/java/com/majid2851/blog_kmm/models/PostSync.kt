package com.majid2851.blog_kmm.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
open class PostSync: RealmObject {
    @PrimaryKey
    var _id: String = ""
    var author: String = ""
    var date: Long = 0L
    var title: String = ""
    var subtitle: String = ""
    var thumbnail: String = ""
    var category: String = Category.Programming.name
}
