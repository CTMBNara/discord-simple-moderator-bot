package core

import discord4j.core.event.ReactiveEventAdapter
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.reactor.mono
import org.reactivestreams.Publisher
import kotlin.reflect.KClass

class EventDispatcher(initializer: EventDispatcherBuilder.() -> Unit) : ReactiveEventAdapter() {

    private val handlers: List<Pair<KClass<*>, EventHandler<Event>>>

    init {
        EventDispatcherBuilder().apply(initializer).also {
            handlers = it.handlers.toList()
        }
    }

    override fun onMessageCreate(event: MessageCreateEvent): Publisher<*> = dispatch(event)

    private fun dispatch(event: Event): Publisher<*> = mono {
        handlers
            .filter { it.first.java.isAssignableFrom(event::class.java) }
            .forEach { it.second.handle(event) }
    }

    class EventDispatcherBuilder {

        val handlers: MutableList<Pair<KClass<*>, EventHandler<Event>>> = mutableListOf()

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T : Event, H : EventHandler<T>> handler(eventHandlerSupplier: () -> H) {
            handlers.add(T::class to eventHandlerSupplier() as EventHandler<Event>)
        }
    }
}
