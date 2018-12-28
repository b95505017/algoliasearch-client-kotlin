package client.data

import client.serialize.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull


sealed class MultipleQueriesStrategy(override val raw: String) : Raw {

    object None : MultipleQueriesStrategy(KeyNone)

    object StopIfEnoughMatches : MultipleQueriesStrategy(KeyStopIfEnoughMatches)

    data class Unknown(override val raw: String) : MultipleQueriesStrategy(raw)

    override fun toString(): String {
        return raw
    }

    internal companion object : RawSerializer<MultipleQueriesStrategy>, Deserializer<MultipleQueriesStrategy> {

        override fun deserialize(element: JsonElement): MultipleQueriesStrategy? {
            return when (val content = element.contentOrNull) {
                KeyNone -> None
                KeyStopIfEnoughMatches -> StopIfEnoughMatches
                null -> null
                else -> Unknown(content)
            }
        }
    }
}