package io.github.multicatch.papajbot.talk

class OpenQuestions : (String, Talk) -> ResponseStrategy?  {
    private val questionKeyWords = listOf(
            "jak",
            "ile",
            "cóż",
            "co",
            "kim",
            "jaki"
    )

    private val responses = listOf(
            "A jak tam chcesz",
            "Wiesz co jest lepszym pomysłem?",
            "A wiesz co?",
            "Okej..."
    )

    override fun invoke(message: String, talk: Talk): ResponseStrategy? {
        val keyWords = keyWordsOf(message)
        if (keyWords.intersect(questionKeyWords).isNotEmpty()) {
            val topic = Topics.values().firstOrNull { keyWords.intersect(it.topicsKeyWords).isNotEmpty() }
                    ?: Topics.OTHER
            return responseStrategyOf(topic.from(talk), responses)
        }

        return null
    }
}