package io.github.multicatch.papajbot.talk

interface Responder {
    fun respond(message: String, talk: Talk): Pair<String, Talk>
}

class SimpleResponder(
        private val responseStrategyAdapters: List<(String, Talk) -> ResponseStrategy?>,
        private val defaultResponses: List<String>
) : Responder {
    override fun respond(message: String, talk: Talk): Pair<String, Talk> {
        val responseStrategy = strategyOf(message, talk)

        return if (responseStrategy == null) {
            defaultResponses.random() to talk
        } else {
            val knownTopics = talk.knownTopics.toMutableMap()
            knownTopics[responseStrategy.topic.name] = responseStrategy.topic
            responseStrategy.responses.random() to talkOf(responseStrategy.topic, knownTopics)
        }
    }

    private fun strategyOf(message: String, talk: Talk): ResponseStrategy? {
        for (responseAdapter in responseStrategyAdapters) {
            val responseStrategy = responseAdapter(message, talk)
            if (responseStrategy != null) {
                return responseStrategy
            }
        }
        return null
    }
}

interface ResponseStrategy {
    val topic: TalkTopic
    val responses: List<String>
}

data class SimpleResponseStrategy(
        override val topic: TalkTopic,
        override val responses: List<String>
) : ResponseStrategy

fun responseStrategyOf(topic: TalkTopic, responses: List<String>): ResponseStrategy = SimpleResponseStrategy(topic, responses)