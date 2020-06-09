package io.github.multicatch.papajbot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCall(
        val recipient: ConversationParticipant,
        val message: ResponseMessage
)

@Serializable
data class ResponseMessage(
        val text: String? = null,
        val attachment: Attachment? = null
)

@Serializable
data class Attachment(
        val type: AttachmentType,
        val payload: AttachmentPayload
)

@Serializable
enum class AttachmentType {
    @SerialName("template") TEMPLATE,
    @SerialName("video") VIDEO
}

@Serializable
data class AttachmentPayload(
        @SerialName("attachment_id")
        val id: String? = null,
        val url: String? = null,
        @SerialName("is_reusable")
        val isReusable: Boolean? = null,
        @SerialName("template_type")
        val templateType: TemplateType? = null,
        val elements: List<PayloadElement> = listOf()
)

@Serializable
enum class TemplateType {
    @SerialName("open_graph") OPEN_GRAPH,
    @SerialName("media") MEDIA
}

interface PayloadElement {

}

@Serializable
data class OpenGraphElement(
        val url: String
) : PayloadElement