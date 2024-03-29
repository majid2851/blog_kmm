@file:Suppress("EXTERNAL_SERIALIZER_USELESS")

package com.majid2851.blog_kmm.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable(ApiListResponseSerializer::class)
sealed class ApiListResponse
{

    @Serializable
    @SerialName("idle")
    object Idle:ApiListResponse()

    @Serializable
    @SerialName("success")
    data class Success(val data:List<PostWithoutDetails>)
        :ApiListResponse()

    @Serializable
    @SerialName("error")
    data class Error(val message:String)
        :ApiListResponse()
}

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable(ApiResponseSerializer::class)
sealed class ApiResponse
{

    @Serializable
    @SerialName("idle")
    object Idle:ApiResponse()

    @Serializable
    @SerialName("success")
    data class Success(val data:Post)
        :ApiResponse()

    @Serializable
    @SerialName("error")
    data class Error(val message:String)
        :ApiResponse()
}


@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ApiListResponse::class)
object ApiListResponseSerializer : KSerializer<ApiListResponse> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ApiListResponse")

    override fun serialize(encoder: Encoder, value: ApiListResponse) {
        // Serialization logic (not the focus here)
    }

    override fun deserialize(decoder: Decoder): ApiListResponse {
        val input = decoder as? JsonDecoder ?: throw
        SerializationException("This serializer can only be used with JSON")
        val element = input.decodeJsonElement()
        return when {
            "data" in element.jsonObject -> {
                val data = Json.
                decodeFromJsonElement<List<PostWithoutDetails>>(element.jsonObject["data"]!!)
                ApiListResponse.Success(data)
            }
            "message" in element.jsonObject -> {
                val message = element.jsonObject["message"]!!.jsonPrimitive.content
                ApiListResponse.Error(message)
            }
            else -> ApiListResponse.Idle
        }
    }
}



@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ApiResponse::class)
object ApiResponseSerializer : KSerializer<ApiResponse> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ApiResponse")

    override fun serialize(encoder: Encoder, value: ApiResponse) {
        // Serialization logic (not the focus here)
    }

    override fun deserialize(decoder: Decoder): ApiResponse {
        val input = decoder as? JsonDecoder ?: throw
        SerializationException("This serializer can only be used with JSON")
        val element = input.decodeJsonElement()
        return when {
            "data" in element.jsonObject -> {
                val data = Json.
                decodeFromJsonElement<Post>(element.jsonObject["data"]!!)
                ApiResponse.Success(data)
            }
            "message" in element.jsonObject -> {
                val message = element.jsonObject["message"]!!.jsonPrimitive.content
                ApiResponse.Error(message)
            }
            else -> ApiResponse.Idle
        }
    }
}