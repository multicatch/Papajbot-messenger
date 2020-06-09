package io.github.multicatch.papajbot.talk

class Listening : (String, Talk) -> ResponseStrategy? {
    private val emotionKeyWords: Map<String, Double> = mapOf(
            "zajebiste" to 1.0,
            "świetne" to 0.6,
            "swietne" to 0.6,
            "świetnie" to 0.6,
            "dobrze" to 0.5,
            "źle" to -0.5,
            "okropnie" to -0.6,
            "lepiej" to 0.6,
            "gorzej" to -0.3,
            "paskudnie" to -0.8,
            "średnio" to -0.2,
            "zle" to -0.5,
            "srednio" to -0.2,
            "chujnia" to -1.0,
            "chujowo" to -1.0,
            "okropne" to -0.6
    )

    private val responses = listOf(
            "opowiedz mi jeszcze",
            "kontynuuj",
            "słucham Cię",
            "posłucham chętnie"
    )

    override fun invoke(message: String, talk: Talk): ResponseStrategy? {
        val keyWords = keyWordsOf(message)
        val presentKeyWords = keyWords.intersect(emotionKeyWords.keys)
        if (presentKeyWords.isNotEmpty()) {
            val mood = talk.topic.mood + emotionKeyWords.filterKeys { it in presentKeyWords }.values.sum()
            return responseStrategyOf(talkTopicOf(talk.topic.name, mood), responses.map { "Proszę, $it" })
        }

        return null
    }

}