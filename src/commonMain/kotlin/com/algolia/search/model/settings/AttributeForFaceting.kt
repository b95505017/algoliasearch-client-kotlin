package com.algolia.search.model.settings

import com.algolia.search.helper.toAttribute
import com.algolia.search.model.Attribute
import com.algolia.search.serialize.KeyFilterOnly
import com.algolia.search.serialize.KeySearchable
import com.algolia.search.serialize.regexFilterOnly
import com.algolia.search.serialize.regexSearchable
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringSerializer


@Serializable(AttributeForFaceting.Companion::class)
public sealed class AttributeForFaceting {

    abstract val attribute: Attribute

    public data class Default(override val attribute: Attribute) : AttributeForFaceting()

    /**
     * Defines an [Attribute] as filterable only and not facetable.
     * If you only need the filtering feature, you can take advantage of filterOnly which will reduce the index size
     * and improve the speed of the search.
     * You cannot define an attribute as both [FilterOnly] and [Searchable].
     */
    public data class FilterOnly(override val attribute: Attribute) : AttributeForFaceting()

    /**
     * Defines an attribute as searchable.
     * If you want to search for values of a given facet (using the Search for facet values method
     * you need to specify [Searchable].
     * You cannot define an attribute as both [Searchable] and [FilterOnly].
     */
    public data class Searchable(override val attribute: Attribute) : AttributeForFaceting()

    @Serializer(AttributeForFaceting::class)
    companion object : KSerializer<AttributeForFaceting> {

        override fun serialize(encoder: Encoder, obj: AttributeForFaceting) {
            val string = when (obj) {
                is Default -> obj.attribute.raw
                is FilterOnly -> "$KeyFilterOnly(${obj.attribute.raw})"
                is Searchable -> "$KeySearchable(${obj.attribute.raw})"
            }
            StringSerializer.serialize(encoder, string)
        }

        override fun deserialize(decoder: Decoder): AttributeForFaceting {
            val string = StringSerializer.deserialize(decoder)
            val findFilterOnly = regexFilterOnly.find(string)
            val findSearchable = regexSearchable.find(string)

            return when {
                findFilterOnly != null -> FilterOnly(findFilterOnly.groupValues[1].toAttribute())
                findSearchable != null -> Searchable(findSearchable.groupValues[1].toAttribute())
                else -> Default(string.toAttribute())
            }
        }
    }
}