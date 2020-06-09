package io.github.multicatch.papajbot.talk

class Confirmation : (String, Talk) -> ResponseStrategy? {
    private val confirmationKeyWords = listOf(
            "hm", "hmm", "hmmm", "mhm", "słabe", "mocne", "zajebiste", "wut", "XD", "wow"
    )

    private val positiveResponses = listOf(
            "A czemu nie?",
            "Służyć Chrystusowi to wolność.",
            "Papież, którego nigdy nie znałaś osobiście i ciebie on też nigdy osobiście nie poznał, po prostu jest za młody, ten papież, który mówi tym samym językiem, jakim i ty mówiłaś, ten papież liczy na twoje wsparcie.",
            "Jestem radosny, wy też bądźcie!",
            "Obawiałem się przyjęcia tego wyboru, przyjąłem go jednak w duchu posłuszeństwa Panu i pełnej wiary w Jego matkę, Najświętszą Pannę Marię. "
    )

    private val negativeResponses = listOf(
            "Oszczędźmy słów, niech pozostanie wielkie milczenie. ",
            "No co?",
            "Po co mam wybierać?",
            "Człowieka trzeba mierzyć miarą serca – sercem.",
            "Tak?"
    )

    override fun invoke(message: String, talk: Talk): ResponseStrategy? {
        if (keyWordsOf(message, 1).intersect(confirmationKeyWords).isNotEmpty()) {
            val mood = talk.topic.mood
            return responseStrategyOf(talk.topic, if (mood.attitude == Attitude.POSITIVE) {
                positiveResponses
            } else {
                negativeResponses
            })
        }

        return null
    }
}