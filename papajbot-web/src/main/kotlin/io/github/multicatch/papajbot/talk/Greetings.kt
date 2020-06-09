package io.github.multicatch.papajbot.talk

class Greetings : (String, Talk) -> ResponseStrategy? {
    private val greetingKeyWords = listOf(
            "hej", "witam", "witaj", "cześć", "czesc", "elo", "siema", "szczęść", "szczesc"
    )

    private val greetings = listOf(
            "Co u Ciebie, drogie dziecko?"
    )

    override fun invoke(message: String, talk: Talk): ResponseStrategy? {
        if (keyWordsOf(message).intersect(greetingKeyWords).isNotEmpty()) {
            return responseStrategyOf(talk.topic, greetings)
        }

        return null
    }

}