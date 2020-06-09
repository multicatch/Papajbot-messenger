package io.github.multicatch.papajbot.talk

class TopicChange : (String, Talk) -> ResponseStrategy? {
    private val changeTopicKeyWords = listOf(
            "serio", "penis", "siusiak", "chuj", "gówno", "kurwa", "aaa", "aaaa", "ej", "chuja", "szmato", "bywa", "mogę"
    )

    private val phrases = listOf(
            "jednak jesteś",
            "to jesteś"
    )

    private val responses = listOf(
            "Co?",
            "Myślałem o tym prawdę mówiąc już jakiś czas temu.",
            "Po głowie chodziło mi coś innego.",
            "W sumie to nie.",
            "Nie lękajcie się.",
            "Możliwe."
    )

    override fun invoke(message: String, talk: Talk): ResponseStrategy? {
        if (keyWordsOf(message).intersect(changeTopicKeyWords).isNotEmpty() ||
                phrases.any { message.contains(it) }
        ) {
            return responseStrategyOf(talk.topic, responses)
        }

        return null
    }

}