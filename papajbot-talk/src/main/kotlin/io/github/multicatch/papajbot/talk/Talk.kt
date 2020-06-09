package io.github.multicatch.papajbot.talk

interface Talk {
    val topic: TalkTopic
    val knownTopics: Map<String, TalkTopic>
}

data class SimpleTalk(
        override val topic: TalkTopic = SimpleTalkTopic(),
        override val knownTopics: Map<String, TalkTopic> = mapOf()
) : Talk

fun talkOf(topic: TalkTopic, knownTopics: Map<String, TalkTopic> = mapOf()): Talk = SimpleTalk(topic, knownTopics)

interface TalkTopic {
    val name: String
    val mood: TalkMood
}

data class SimpleTalkTopic(
        override val name: String = "none",
        override val mood: TalkMood = talkMoodOf(0f)
) : TalkTopic

fun talkTopicOf(name: String, mood: TalkMood): TalkTopic = SimpleTalkTopic(name, mood)

interface TalkMood {
    val value: Float
    val attitude: Attitude
        get() = when {
            value < 0 -> Attitude.NEGATIVE
            value > 0 -> Attitude.POSITIVE
            else -> Attitude.NEUTRAL
        }

    operator fun minus(mood: TalkMood): TalkMood = talkMoodOf(this.value - mood.value)
    operator fun plus(mood: TalkMood): TalkMood = talkMoodOf(this.value + mood.value)
}

data class SimpleTalkMood(override val value: Float) : TalkMood

fun talkMoodOf(value: Float): TalkMood = SimpleTalkMood(value)

enum class Attitude {
    POSITIVE,
    NEUTRAL,
    NEGATIVE
}