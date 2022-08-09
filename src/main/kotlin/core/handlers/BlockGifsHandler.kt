package core.handlers

import configuration.model.Rule
import core.EventHandler
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.entity.channel.MessageChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.awaitSingleOrNull
import reactor.core.publisher.Mono

class BlockGifsHandler(rule: Rule.BlockGifsRule) : EventHandler<MessageCreateEvent> {

    private val blockedUsers: Set<String> = rule.usersIds

    override suspend fun handle(event: MessageCreateEvent) {
        val message = event.message

        val author = message.author.orElse(null)
        if (author?.id?.asString() !in blockedUsers || !isGif(message)) {
            return
        }

        message.delete().awaitSingleOrNull()
        mentionUser(message.channel, author)
    }

    private fun isGif(message: Message): Boolean =
        message.embeds.any { embed ->
            val type = embed.data.type()
            !type.isAbsent && type.get().contains("gif")
        } || message.content.contains("gif")

    private suspend fun mentionUser(channel: Mono<MessageChannel>, user: User) {
        channel.awaitSingleOrNull()
            ?.createMessage("Nice try, " + user.mention)
            ?.awaitSingleOrNull()
            ?.also {
                delay(1000L)
                it.delete().awaitSingleOrNull()
            }
    }
}
