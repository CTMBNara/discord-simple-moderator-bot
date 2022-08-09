import configuration.model.Configuration
import core.EventDispatcher
import core.YamlMapperProvider
import core.handlerFor
import discord4j.core.DiscordClient
import java.io.File

fun main(args: Array<String>) {
    val configuration = readConfiguration(args)
    val eventDispatcher = EventDispatcher {
        for (rule in configuration.rules) {
            handler { handlerFor(rule) }
        }
    }

    DiscordClient.create(configuration.token)
        .withGateway { it.on(eventDispatcher) }
        .block()
}

private fun readConfiguration(args: Array<String>): Configuration {
    val configurationPath = if (args.isNotEmpty()) args[0] else null
    val configurationFile = configurationPath?.let { File(it) }
        ?: Configuration::class.java.getResource("/application.yaml")?.toURI()?.let { File(it) }

    return YamlMapperProvider.mapper().readValue(configurationFile, Configuration::class.java)
}
