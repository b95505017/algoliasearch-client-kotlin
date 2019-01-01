package com.algolia.search.saas.serialize

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray


internal interface Serializer<T> {

    fun serialize(input: T): JsonElement
    fun serializeList(input: List<T>): JsonArray {
        return jsonArray { input.forEach { +serialize(it) } }
    }
}