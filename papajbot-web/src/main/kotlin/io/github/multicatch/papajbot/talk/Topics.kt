package io.github.multicatch.papajbot.talk

enum class Topics(
        val topicsKeyWords: List<String>,
        override val mood: TalkMood = talkMoodOf(0f)
) : TalkTopic {
    PERSONAL(listOf("dzieje", "myślę", "myśle", "uważam", "mnie")),
    POPE(listOf("jesteś", "papież", "papiez", "jestes", "paweł", "pawel")),
    POLAND(listOf("polska", "kraj", "państwo", "ojczyzna", "panstwo")),
    ECONOMY(listOf("kosztuje", "praca", "pracuje", "kosztować", "kosztowac", "drogo", "drogi")),
    OTHER(listOf())
    ;

    fun with(mood: TalkMood) = talkTopicOf(this.name, mood)
    fun from(talk: Talk) = talk.knownTopics[this.name] ?: with(this.mood)
}
