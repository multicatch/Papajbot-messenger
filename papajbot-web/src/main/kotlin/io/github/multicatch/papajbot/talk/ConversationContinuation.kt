package io.github.multicatch.papajbot.talk

import kotlin.random.Random

class ConversationContinuation  : (String, Talk) -> ResponseStrategy? {

    private val responses = mapOf(
            Topics.POPE to listOf(
                    "Ja Panu Bogu dziękuję codziennie, że jest w Polsce takie radio, że się nazywa Radio Maryja. ",
                    "Jestem radosny, wy też bądźcie! ",
                    "Pozwólcie mi iść do domu Ojca. ",
                    "Kapłan jest człowiekiem żyjącym samotnie po to, aby inni nie byli samotni. "
            ),
            Topics.POLAND to listOf(
                    "Nie ma pokoju bez sprawiedliwości, nie ma sprawiedliwości bez przebaczenia.",
                    "Naród jest przede wszystkim bogaty ludźmi. Bogaty człowiekiem. Bogaty młodzieżą. Bogaty każdym. ",
                    "Rodzina Bogiem silna, staje się siłą człowieka i całego narodu.",
                    "Nie ma większego bogactwa w narodzie nad światłych obywateli."
            ),
            Topics.PERSONAL to listOf(
                    "W każdym mieszka dobro, i zło i tylko to drugie jest aktywne.",
                    "Przyszłość zaczyna się dzisiaj, nie jutro. ",
                    "Modlę się za brata, który mnie zranił, a któremu szczerze przebaczyłem. ",
                    "Musicie od siebie wymagać, nawet gdyby inni od was nie wymagali"
            ),
            Topics.ECONOMY to listOf(
                    "Bogatym nie jest ten, kto posiada, ale ten, kto „rozdaje”, kto zdolny jest dawać. ",
                    "Droga do celu to nie połowa przyjemności, to cała przyjemność.",
                    "Dobrze, dobrze, ale uwierz w swoje zdolności nabywcze.",
                    "A skąd na to pieniądze?"
            ),
            Topics.OTHER to listOf(
                    "A po maturze chodziliśmy na kremówki. ",
                    "Powiedz mi, jaka jest Twoja miłość, a powiem Ci, kim jesteś.",
                    "Służyć Chrystusowi to wolność.",
                    "Każde życie, nawet najmniej znaczące dla ludzi, ma wieczną wartość przed oczami Boga.",
                    "Bogatym nie jest ten kto posiada, lecz ten kto daje.",
                    "A po co?",
                    "Tak?",
                    "Nie wystarczy przekroczyć próg, trzeba iść w głąb.",
                    "Dobrze się z Tobą rozmawia."
            )
    )

    override fun invoke(message: String, talk: Talk): ResponseStrategy? {
        if (Random.nextBoolean()) {
            return responseStrategyOf(talk.topic, responses[talk.topic] ?: defaultResponses)
        }

        return null
    }

}