package core.handlers

import configuration.model.Rule
import core.EventHandler
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.entity.Message
import kotlinx.coroutines.reactor.awaitSingleOrNull
import java.util.regex.Pattern

class BlockNotMatchedMessagesHandler(rule: Rule.BlockNotMatchedMessageRule) : EventHandler<MessageCreateEvent> {

    private val blockedUsers: Set<String>
    private val notBlockedMessagesPatterns: List<Pattern>

    init {
        blockedUsers = rule.usersIds
        notBlockedMessagesPatterns = rule.matchers.map(Pattern::compile)
    }

    override suspend fun handle(event: MessageCreateEvent) {
        val message = event.message

        val author = message.author.orElse(null)
        if (author?.id?.asString() !in blockedUsers || !isContentBlocked(message)) {
            return
        }

        message.delete().awaitSingleOrNull()
    }

    private fun isContentBlocked(message: Message): Boolean =
        notBlockedMessagesPatterns.none { it.matcher(message.content).find() }
}
