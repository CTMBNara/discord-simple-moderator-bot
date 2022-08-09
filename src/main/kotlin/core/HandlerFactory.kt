package core

import configuration.model.Rule
import core.handlers.BlockGifsHandler
import core.handlers.BlockMatchedMessagesHandler
import core.handlers.BlockNotMatchedMessagesHandler
import discord4j.core.event.domain.Event

@Suppress("UNCHECKED_CAST")
fun <T : Event> handlerFor(rule: Rule): EventHandler<T> = when (rule) {
    is Rule.BlockMatchedMessageRule -> BlockMatchedMessagesHandler(rule)
    is Rule.BlockNotMatchedMessageRule -> BlockNotMatchedMessagesHandler(rule)
    is Rule.BlockGifsRule -> BlockGifsHandler(rule)
} as EventHandler<T>
