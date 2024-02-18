package com.majid2851.blog_kmm.models


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

@Serializable(ApiListResponseSerializer::class)
actual sealed class ApiListResponse
{

    @Serializable
    @SerialName("idle")
    actual object Idle:ApiListResponse()

    @Serializable
    @SerialName("success")
    actual data class Success(val data:List<PostWithoutDetails>)
        :ApiListResponse()

    @Serializable
    @SerialName("error")
    actual data class Error(val message:String)
        :ApiListResponse()
}


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

//object ApiListResponseSerializer:
//        JsonContentPolymorphicSerializer
//            <ApiListResponse>(ApiListResponse::class)
//{
//    override fun selectDeserializer(element: JsonElement) = when {
//        "data" in element.jsonObject -> ApiListResponse.Success.serializer()
//        "message" in element.jsonObject -> ApiListResponse.Error.serializer()
//        else -> ApiListResponse.Idle.serializer()
//    }
//
//}