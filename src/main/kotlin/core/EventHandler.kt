package core

import discord4j.core.event.domain.Event

interface EventHandler<in T : Event> {

    suspend fun handle(event: T)
}
