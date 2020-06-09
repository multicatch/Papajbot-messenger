package io.github.multicatch.papajbot.talk

class Greetings : (String, Talk) -> ResponseStrategy? {
    private val greetingKeyWords = listOf(
            "hej", "witam", "witaj", "cześć", "czesc", "elo", "siema", "szczęść", "szczesc"
    )

    private val greetingPhrases = listOf(
            "dzień dobry",
            "dzien dobry",
            "dobry wieczór",
            "dobry wieczor"
    )

    private val greetings = listOf(
            "Szczęść Boże!",
            "Co u Ciebie, drogie dziecko?"
    )

    override fun invoke(message: String, talk: Talk): ResponseStrategy? {
        if (keyWordsOf(message).intersect(greetingKeyWords).isNotEmpty()
                || greetingPhrases.any { message.contains(it, true) }
        ) {
            return responseStrategyOf(Topics.PERSONAL.from(talk), greetings)
        }

        return null
    }

}