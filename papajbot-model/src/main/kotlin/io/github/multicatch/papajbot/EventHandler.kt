package io.github.multicatch.papajbot

import io.github.multicatch.papajbot.model.ApiCall
import io.github.multicatch.papajbot.model.Event

interface EventHandler : (Event) -> ApiCall?