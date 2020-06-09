package io.github.multicatch.papajbot.model

import com.fasterxml.jackson.annotation.JsonProperty

fun MessagingItem.respondWith(text: String? = null, attachment: Attachment? = null) = ApiCall(
        recipient = sender,
        message = ResponseMessage(text, attachment)
)

data class ApiCall(
        val recipient: ConversationParticipant,
        val message: ResponseMessage? = null,
        @JsonProperty("sender_action")
        val senderAction: SenderAction? = null
)

enum class SenderAction {
    @JsonProperty("typing_on") TYPING_ON,
    @JsonProperty("typing_off") TYPING_OFF,
    @JsonProperty("mark_seen") MARK_SEEN
}

data class ResponseMessage(
        val text: String? = null,
        val attachment: Attachment? = null
)

data class Attachment(
        val type: AttachmentType,
        val payload: AttachmentPayload
)

enum class AttachmentType {
    @JsonProperty("template") TEMPLATE,
    @JsonProperty("video") VIDEO
}

data class AttachmentPayload(
        @JsonProperty("attachment_id")
        val id: String? = null,
        val url: String? = null,
        @JsonProperty("is_reusable")
        val isReusable: Boolean? = null,
        @JsonProperty("template_type")
        val templateType: TemplateType? = null,
        val elements: List<PayloadElement>? = null
)

enum class TemplateType {
    @JsonProperty("open_graph") OPEN_GRAPH,
    @JsonProperty("media") MEDIA
}

data class PayloadElement(
    @JsonProperty("media_type")
    val mediaType: PayloadElementType? = null,
    val url: String? = null,
    val buttons: List<PayloadButton>? = null
)

enum class PayloadElementType {
    @JsonProperty("video") VIDEO
}

data class PayloadButton(
        val type: PayloadButtonType,
        val title: String,
        val payload: String? = null,
        val url: String? = null
)

enum class PayloadButtonType {
    @JsonProperty("web_url") WEB_URL,
    @JsonProperty("payload") PAYLOAD
}