package com.algolia.search.model.search

import com.algolia.search.serialize.KeyFrom
import com.algolia.search.serialize.KeyValue
import com.algolia.search.serialize.asJsonInput
import com.algolia.search.serialize.asJsonOutput
import kotlinx.serialization.*
import kotlinx.serialization.json.*


/**
 * Precision of geo search (in meters), to add grouping by geo location to the ranking formula.
 * When ranking hits, geo distances are grouped into ranges of aroundPrecision size.
 * All hits within the same range are considered equal with respect to the geo ranking parameter.
 * For example, if you set aroundPrecision to 100, any two objects lying in the range [0, 99m] from the searched
 * location will be considered equal; same for [100, 199], [200, 299], etc.
 */
@Serializable(AroundPrecision.Companion::class)
public sealed class AroundPrecision {

    public data class Int(val value: kotlin.Int) : AroundPrecision()

    public data class Ranges(val list: List<IntRange>) : AroundPrecision() {

        public constructor(vararg range: IntRange) : this(range.toList())
    }

    public data class Other(val raw: JsonElement) : AroundPrecision()

    @Serializer(AroundPrecision::class)
    companion object : KSerializer<AroundPrecision> {

        override fun serialize(encoder: Encoder, obj: AroundPrecision) {
            val json = when (obj) {
                is Int -> JsonLiteral(obj.value)
                is Ranges -> jsonArray {
                    obj.list.forEach {
                        +json {
                            KeyFrom to it.first
                            KeyValue to it.endInclusive
                        }
                    }
                }
                is Other -> obj.raw
            }
            encoder.asJsonOutput().encodeJson(json)
        }

        override fun deserialize(decoder: Decoder): AroundPrecision {
            return when (val json = decoder.asJsonInput()) {
                is JsonArray -> Ranges(json.map {
                    val pair = it.jsonObject

                    IntRange(
                        pair.getPrimitive(KeyFrom).int,
                        pair.getPrimitive(KeyValue).int
                    )
                })
                is JsonLiteral -> Int(json.int)
                else -> Other(json)
            }
        }
    }
}