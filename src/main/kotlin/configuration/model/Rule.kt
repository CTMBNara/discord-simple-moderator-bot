package configuration.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

private const val BLOCK_MATCHED_MESSAGE = "block-matched-message"
private const val BLOCK_NOT_MATCHED_MESSAGE = "block-not-matched-message"
private const val BLOCK_GIFS = "block-gifs"

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "rule-type")
@JsonSubTypes(
    JsonSubTypes.Type(value = Rule.BlockMatchedMessageRule::class, name = BLOCK_MATCHED_MESSAGE),
    JsonSubTypes.Type(value = Rule.BlockNotMatchedMessageRule::class, name = BLOCK_NOT_MATCHED_MESSAGE),
    JsonSubTypes.Type(value = Rule.BlockGifsRule::class, name = BLOCK_GIFS)
)
sealed class Rule {

    @JsonTypeName(value = BLOCK_MATCHED_MESSAGE)
    data class BlockMatchedMessageRule(
        val usersIds: Set<String>,
        val matchers: List<String>
    ) : Rule()

    @JsonTypeName(value = BLOCK_NOT_MATCHED_MESSAGE)
    data class BlockNotMatchedMessageRule(
        val usersIds: Set<String>,
        val matchers: List<String>
    ) : Rule()

    @JsonTypeName(value = BLOCK_GIFS)
    data class BlockGifsRule(val usersIds: Set<String>) : Rule()
}
